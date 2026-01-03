const express = require('express');
const { body, validationResult } = require('express-validator');
const User = require('../models/User');
const { auth, adminOnly } = require('../middleware/auth');
const logger = require('../utils/logger');

const router = express.Router();

router.get('/', [auth, adminOnly], async (req, res) => {
  try {
    const { page = 1, limit = 20, role, status, search } = req.query;
    const skip = (page - 1) * limit;

    const query = {};
    if (role) query.role = role;
    if (status) query.status = status;
    if (search) {
      query.$or = [
        { username: { $regex: search, $options: 'i' } },
        { email: { $regex: search, $options: 'i' } },
        { 'profile.firstName': { $regex: search, $options: 'i' } },
        { 'profile.lastName': { $regex: search, $options: 'i' } }
      ];
    }

    const [users, total] = await Promise.all([
      User.find(query)
        .select('-password')
        .sort({ createdAt: -1 })
        .skip(skip)
        .limit(parseInt(limit)),
      User.countDocuments(query)
    ]);

    const totalPages = Math.ceil(total / limit);

    res.json({
      success: true,
      data: {
        users,
        pagination: {
          page: parseInt(page),
          limit: parseInt(limit),
          total,
          totalPages,
          hasNext: page < totalPages,
          hasPrev: page > 1
        }
      }
    });

  } catch (error) {
    logger.error('Users fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch users'
    });
  }
});

router.get('/:id', [auth, adminOnly], async (req, res) => {
  try {
    const user = await User.findById(req.params.id).select('-password');
    
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    res.json({
      success: true,
      data: user
    });

  } catch (error) {
    logger.error('User fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch user'
    });
  }
});

router.post('/', [
  auth,
  adminOnly,
  body('username')
    .isLength({ min: 3, max: 20 })
    .matches(/^[a-zA-Z0-9_]+$/)
    .withMessage('Username must be 3-20 characters, alphanumeric and underscore only'),
  body('email')
    .isEmail()
    .normalizeEmail()
    .withMessage('Please provide a valid email address'),
  body('password')
    .isLength({ min: 6 })
    .withMessage('Password must be at least 6 characters long'),
  body('role')
    .isIn(['user', 'admin', 'operator'])
    .withMessage('Role must be user, admin, or operator'),
  body('firstName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('First name must be less than 50 characters'),
  body('lastName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('Last name must be less than 50 characters'),
  body('status')
    .optional()
    .isIn(['active', 'inactive', 'suspended'])
    .withMessage('Status must be active, inactive, or suspended')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { username, email, password, role, firstName, lastName, status } = req.body;

    const existingUser = await User.findOne({
      $or: [{ username }, { email }]
    });

    if (existingUser) {
      return res.status(400).json({
        success: false,
        error: existingUser.username === username ? 'Username already exists' : 'Email already registered'
      });
    }

    const user = new User({
      username,
      email,
      password,
      role: role || 'user',
      status: status || 'active',
    .isLength({ min: 6 })
    .withMessage('Password must be at least 6 characters long'),
  body('role')
    .isIn(['user', 'admin', 'operator'])
    .withMessage('Role must be user, admin, or operator'),
  body('firstName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('First name must be less than 50 characters'),
  body('lastName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('Last name must be less than 50 characters'),
  body('status')
    .optional()
    .isIn(['active', 'inactive', 'suspended'])
    .withMessage('Status must be active, inactive, or suspended')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { username, email, password, role, firstName, lastName, status } = req.body;

    // Check if user already exists
    const existingUser = await User.findOne({
      $or: [{ username }, { email }]
    });

    if (existingUser) {
      return res.status(400).json({
        success: false,
        error: existingUser.username === username ? 'Username already exists' : 'Email already registered'
      });
    }

    // Create new user
    const user = new User({
      username,
      email,
      password,
      role: role || 'user',
      status: status || 'active',
      profile: {
        firstName: firstName || '',
        lastName: lastName || ''
      },
      emailVerified: true // Admin-created users are auto-verified
    });

    await user.save();

    logger.info(`User created by admin: ${req.user.username} -> ${username}`);

    res.status(201).json({
      success: true,
      message: 'User created successfully',
      data: user.toJSON()
    });

  } catch (error) {
    logger.error('User creation error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to create user'
    });
  }
});

// Update user (admin only)
router.put('/:id', [
  auth,
  adminOnly,
  body('email')
    .optional()
    .isEmail()
    .normalizeEmail()
    .withMessage('Please provide a valid email address'),
  body('role')
    .optional()
    .isIn(['user', 'admin', 'operator'])
    .withMessage('Role must be user, admin, or operator'),
  body('status')
    .optional()
    .isIn(['active', 'inactive', 'suspended'])
    .withMessage('Status must be active, inactive, or suspended'),
  body('firstName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('First name must be less than 50 characters'),
  body('lastName')
    .optional()
    .isLength({ max: 50 })
    .trim()
    .withMessage('Last name must be less than 50 characters')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { email, role, status, firstName, lastName } = req.body;

    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    // Check if email already exists
    if (email && email !== user.email) {
      const existingUser = await User.findOne({ email });
      if (existingUser) {
        return res.status(400).json({
          success: false,
          error: 'Email already registered'
        });
      }
    }

    // Update user fields
    if (email !== undefined) user.email = email;
    if (role !== undefined) user.role = role;
    if (status !== undefined) user.status = status;
    if (firstName !== undefined) user.profile.firstName = firstName;
    if (lastName !== undefined) user.profile.lastName = lastName;

    await user.save();

    logger.info(`User updated by admin: ${req.user.username} -> ${user.username}`);

    res.json({
      success: true,
      message: 'User updated successfully',
      data: user.toJSON()
    });

  } catch (error) {
    logger.error('User update error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to update user'
    });
  }
});

// Delete user (admin only - soft delete)
router.delete('/:id', [auth, adminOnly], async (req, res) => {
  try {
    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    // Prevent deleting yourself
    if (user._id.toString() === req.user._id.toString()) {
      return res.status(400).json({
        success: false,
        error: 'Cannot delete your own account'
      });
    }

    // Prevent deleting the last admin
    if (user.role === 'admin') {
      const adminCount = await User.countDocuments({ role: 'admin', status: 'active' });
      if (adminCount <= 1) {
        return res.status(400).json({
          success: false,
          error: 'Cannot delete the last admin user'
        });
      }
    }

    // Soft delete by setting status to suspended
    user.status = 'suspended';
    await user.save();

    logger.info(`User suspended by admin: ${req.user.username} -> ${user.username}`);

    res.json({
      success: true,
      message: 'User suspended successfully'
    });

  } catch (error) {
    logger.error('User deletion error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to delete user'
    });
  }
});

// Reset user password (admin only)
router.post('/:id/reset-password', [
  auth,
  adminOnly,
  body('newPassword')
    .isLength({ min: 6 })
    .withMessage('New password must be at least 6 characters long')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { newPassword } = req.body;

    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    // Update password
    user.password = newPassword;
    await user.save();

    logger.info(`Password reset by admin: ${req.user.username} -> ${user.username}`);

    res.json({
      success: true,
      message: 'Password reset successfully'
    });

  } catch (error) {
    logger.error('Password reset error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to reset password'
    });
  }
});

// Get user statistics endpoint
router.get('/:id/statistics', [auth, adminOnly], async (req, res) => {
  try {
    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({
        success: false,
        error: 'User not found'
      });
    }

    const { dateFrom, dateTo } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const stats = await Classification.getStatistics(user._id, timeRange);
    const categoryStats = await Classification.getCategoryStats(user._id, timeRange);
    const timeline = await Classification.getTimelineData(user._id, 'day', timeRange);

    res.json({
      success: true,
      data: {
        user: {
          id: user._id,
          username: user.username,
          email: user.email,
          role: user.role,
          status: user.status,
          profile: user.profile,
          statistics: user.statistics
        },
        classifications: stats[0] || {
          totalClassifications: 0,
          correctClassifications: 0,
          totalWeight: 0,
          totalCarbonFootprint: 0,
          avgConfidence: 0,
          accuracy: 0
        },
        categories: categoryStats,
        timeline
      }
    });

  } catch (error) {
    logger.error('User statistics error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch user statistics'
    });
  }
});

module.exports = router;