const express = require('express');
const multer = require('multer');
const sharp = require('sharp');
const fs = require('fs').promises;
const path = require('path');
const { body, validationResult } = require('express-validator');
const { auth } = require('../middleware/auth');
const logger = require('../utils/logger');

const router = express.Router();

// Configure multer for file uploads
const storage = multer.diskStorage({
  destination: async (req, file, cb) => {
    const uploadDir = path.join(__dirname, '../../uploads/avatars');
    await fs.mkdir(uploadDir, { recursive: true });
    cb(null, uploadDir);
  },
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
    cb(null, `${req.user._id}-${uniqueSuffix}${path.extname(file.originalname)}`);
  }
});

const fileFilter = (req, file, cb) => {
  const allowedTypes = /jpeg|jpg|png|gif|webp/;
  const extname = allowedTypes.test(path.extname(file.originalname).toLowerCase());
  const mimetype = allowedTypes.test(file.mimetype);

  if (mimetype && extname) {
    return cb(null, true);
  } else {
    cb(new Error('Only image files are allowed'));
  }
};

const upload = multer({
  storage: storage,
  limits: {
    fileSize: 5 * 1024 * 1024 // 5MB limit for avatars
  },
  fileFilter: fileFilter
});

// Upload avatar endpoint
router.post('/avatar', [
  auth,
  upload.single('avatar')
], async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        error: 'Avatar file is required'
      });
    }

    const User = require('../models/User');
    const user = await User.findById(req.user._id);
    
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    // Process and resize avatar
    const avatarPath = req.file.path;
    const processedPath = avatarPath.replace(path.extname(avatarPath), '_processed.png');
    
    await sharp(avatarPath)
      .resize(200, 200, { 
        fit: 'cover',
        position: 'center'
      })
      .png()
      .toFile(processedPath);

    // Delete original file
    await fs.unlink(avatarPath);

    // Delete old avatar if exists
    if (user.profile.avatar) {
      const oldAvatarPath = path.join(__dirname, '../../uploads/avatars', path.basename(user.profile.avatar));
      try {
        await fs.unlink(oldAvatarPath);
      } catch (error) {
        logger.warn('Failed to delete old avatar:', error);
      }
    }

    // Update user avatar
    user.profile.avatar = `/api/uploads/avatars/${path.basename(processedPath)}`;
    await user.save();

    logger.info(`Avatar uploaded for user: ${user.username}`);

    res.json({
      success: true,
      message: 'Avatar uploaded successfully',
      data: {
        avatarUrl: user.profile.avatar
      }
    });

  } catch (error) {
    logger.error('Avatar upload error:', error);
    
    // Clean up uploaded file on error
    if (req.file) {
      try {
        await fs.unlink(req.file.path);
      } catch (unlinkError) {
        logger.error('Failed to delete uploaded file:', unlinkError);
      }
    }

    res.status(500).json({
      success: false,
      error: 'Failed to upload avatar'
    });
  }
});

// Upload classification image endpoint
router.post('/classification', [
  auth,
  upload.single('image')
], async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        error: 'Image file is required'
      });
    }

    // Create thumbnail
    const imagePath = req.file.path;
    const thumbnailPath = imagePath.replace(path.extname(imagePath), '_thumb.jpg');
    
    await sharp(imagePath)
      .resize(300, 300, { 
        fit: 'inside',
        withoutEnlargement: true
      })
      .jpeg({ quality: 80 })
      .toFile(thumbnailPath);

    logger.info(`Classification image uploaded by user: ${req.user.username}`);

    res.json({
      success: true,
      message: 'Image uploaded successfully',
      data: {
        originalUrl: `/api/uploads/classifications/${path.basename(imagePath)}`,
        thumbnailUrl: `/api/uploads/classifications/${path.basename(thumbnailPath)}`,
        fileName: req.file.filename,
        size: req.file.size,
        mimeType: req.file.mimetype
      }
    });

  } catch (error) {
    logger.error('Classification image upload error:', error);
    
    // Clean up uploaded file on error
    if (req.file) {
      try {
        await fs.unlink(req.file.path);
      } catch (unlinkError) {
        logger.error('Failed to delete uploaded file:', unlinkError);
      }
    }

    res.status(500).json({
      success: false,
      error: 'Failed to upload image'
    });
  }
});

// Get uploaded files endpoint
router.get('/list', auth, async (req, res) => {
  try {
    const { type = 'all' } = req.query;
    const uploadDir = path.join(__dirname, '../../uploads');
    
    let files = [];
    
    if (type === 'avatars' || type === 'all') {
      const avatarDir = path.join(uploadDir, 'avatars');
      try {
        const avatarFiles = await fs.readdir(avatarDir);
        files = files.concat(avatarFiles
          .filter(file => file.startsWith(req.user._id.toString()))
          .map(file => ({
            name: file,
            url: `/api/uploads/avatars/${file}`,
            type: 'avatar',
            size: (await fs.stat(path.join(avatarDir, file))).size,
            created: (await fs.stat(path.join(avatarDir, file))).birthtime
          }))
        );
      } catch (error) {
        // Directory might not exist
      }
    }

    res.json({
      success: true,
      data: {
        files: files.sort((a, b) => new Date(b.created) - new Date(a.created))
      }
    });

  } catch (error) {
    logger.error('File list error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch file list'
    });
  }
});

// Delete uploaded file endpoint
router.delete('/:filename', auth, async (req, res) => {
  try {
    const { filename } = req.params;
    const { type = 'avatar' } = req.query;
    
    // Validate filename to prevent directory traversal
    if (filename.includes('..') || filename.includes('/')) {
      return res.status(400).json({
        success: false,
        error: 'Invalid filename'
      });
    }

    // Check if file belongs to user
    if (!filename.startsWith(req.user._id.toString())) {
      return res.status(403).json({
        success: false,
        error: 'Access denied to this file'
      });
    }

    const uploadDir = path.join(__dirname, '../../uploads', type + 's');
    const filePath = path.join(uploadDir, filename);

    try {
      await fs.unlink(filePath);
      
      // Also delete thumbnail if it exists
      const thumbnailPath = filePath.replace(path.extname(filePath), '_thumb.jpg');
      try {
        await fs.unlink(thumbnailPath);
      } catch (thumbError) {
        // Thumbnail might not exist
      }

      // If it's an avatar, update user profile
      if (type === 'avatar') {
        const User = require('../models/User');
        await User.findByIdAndUpdate(req.user._id, {
          $unset: { 'profile.avatar': 1 }
        });
      }

      logger.info(`File deleted: ${filename} by user: ${req.user.username}`);

      res.json({
        success: true,
        message: 'File deleted successfully'
      });

    } catch (unlinkError) {
      if (unlinkError.code === 'ENOENT') {
        return res.status(404).json({
          success: false,
          error: 'File not found'
        });
      }
      throw unlinkError;
    }

  } catch (error) {
    logger.error('File deletion error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to delete file'
    });
  }
});

module.exports = router;