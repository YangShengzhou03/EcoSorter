const express = require('express');
const multer = require('multer');
const sharp = require('sharp');
const tf = require('@tensorflow/tfjs-node');
const fs = require('fs').promises;
const path = require('path');
const { body, validationResult } = require('express-validator');
const Classification = require('../models/Classification');
const WasteCategory = require('../models/WasteCategory');
const User = require('../models/User');
const { auth } = require('../middleware/auth');
const logger = require('../utils/logger');

const router = express.Router();

// Configure multer for file uploads
const storage = multer.diskStorage({
  destination: async (req, file, cb) => {
    const uploadDir = path.join(__dirname, '../../uploads/classifications');
    await fs.mkdir(uploadDir, { recursive: true });
    cb(null, uploadDir);
  },
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
    cb(null, `${req.user._id}-${uniqueSuffix}${path.extname(file.originalname)}`);
  }
});

const fileFilter = (req, file, cb) => {
  const allowedTypes = /jpeg|jpg|png|gif|bmp|webp/;
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
    fileSize: 10 * 1024 * 1024 // 10MB limit
  },
  fileFilter: fileFilter
});

// Load AI model (this would be loaded from a file or cloud storage)
let model;
const loadModel = async () => {
  try {
    // In a real implementation, load your trained model here
    // model = await tf.loadLayersModel('file://path/to/model.json');
    // For now, we'll create a mock model
    model = {
      predict: (image) => {
        // Mock prediction
        const categories = ['recyclable', 'hazardous', 'wet', 'dry'];
        const predictions = categories.map(category => ({
          category,
          confidence: Math.random() * 100,
          subcategory: `${category}_item`,
          description: `This appears to be ${category} waste`
        }));
        
        predictions.sort((a, b) => b.confidence - a.confidence);
        
        return {
          topPrediction: predictions[0],
          alternatives: predictions.slice(1, 4),
          confidence: predictions[0].confidence,
          processingTime: Math.random() * 1000 + 200
        };
      }
    };
    
    logger.ai('AI model loaded successfully');
  } catch (error) {
    logger.error('Failed to load AI model:', error);
    throw error;
  }
};

// Initialize model on startup
loadModel().catch(error => {
  logger.error('Failed to initialize AI model:', error);
});

// Preprocess image for AI model
const preprocessImage = async (imagePath) => {
  try {
    const image = sharp(imagePath);
    const metadata = await image.metadata();
    
    // Resize to 224x224 (standard for many models)
    const resized = await image
      .resize(224, 224, { fit: 'cover' })
      .toBuffer();
    
    // Convert to tensor
    const tensor = tf.node.decodeImage(resized, 3)
      .expandDims(0)
      .div(255.0); // Normalize to [0,1]
    
    return tensor;
  } catch (error) {
    logger.error('Image preprocessing error:', error);
    throw error;
  }
};

// Create classification endpoint
router.post('/classify', [
  auth,
  upload.single('image'),
  body('location')
    .optional()
    .isJSON()
    .withMessage('Location must be valid JSON'),
  body('device')
    .optional()
    .isJSON()
    .withMessage('Device info must be valid JSON')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    if (!req.file) {
      return res.status(400).json({
        success: false,
        error: 'Image file is required'
      });
    }

    const startTime = Date.now();
    const { location, device } = req.body;

    // Preprocess image
    const tensor = await preprocessImage(req.file.path);
    
    // Make prediction
    const prediction = await model.predict(tensor);
    
    // Clean up tensor
    tensor.dispose();

    // Get waste category information
    const wasteCategory = await WasteCategory.findOne({ 
      category: prediction.topPrediction.category.toLowerCase() 
    });

    // Create classification record
    const classification = new Classification({
      user: req.user._id,
      image: {
        originalName: req.file.originalname,
        fileName: req.file.filename,
        mimeType: req.file.mimetype,
        size: req.file.size,
        path: req.file.path,
        url: `/api/uploads/classifications/${req.file.filename}`
      },
      prediction: {
        confidence: prediction.confidence,
        topPrediction: prediction.topPrediction,
        alternatives: prediction.alternatives,
        processingTime: Date.now() - startTime,
        modelVersion: '1.0.0',
        modelUsed: 'EcoSorter-ResNet50'
      },
      wasteInfo: {
        category: prediction.topPrediction.category,
        subcategory: prediction.topPrediction.subcategory,
        type: wasteCategory?.type || 'other',
        disposalMethod: wasteCategory?.disposalInstructions?.general || 'Please check local regulations',
        recyclingInfo: wasteCategory?.disposalInstructions?.preparation || 'No specific recycling information available',
        environmentalImpact: wasteCategory?.environmentalImpact?.decompositionTime || 'Unknown impact'
      },
      location: location ? JSON.parse(location) : undefined,
      device: device ? JSON.parse(device) : undefined,
      metadata: {
        ipAddress: req.ip,
        userAgent: req.get('User-Agent'),
        sessionId: req.sessionID
      }
    });

    await classification.save();

    // Update user statistics
    await User.findByIdAndUpdate(req.user._id, {
      $inc: { 'statistics.totalClassifications': 1 }
    });

    // Update category statistics
    if (wasteCategory) {
      await wasteCategory.incrementStats(false, prediction.confidence);
    }

    logger.ai(`Classification completed for user ${req.user.username}: ${prediction.topPrediction.category} (${prediction.confidence.toFixed(2)}% confidence)`);

    res.json({
      success: true,
      message: 'Classification completed successfully',
      data: {
        classificationId: classification._id,
        prediction: classification.prediction,
        wasteInfo: classification.wasteInfo,
        processingTime: classification.prediction.processingTime
      }
    });

  } catch (error) {
    logger.error('Classification error:', error);
    
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
      error: 'Classification failed. Please try again.'
    });
  }
});

// Get classification history endpoint
router.get('/history', [
  auth,
  body('page')
    .optional()
    .isInt({ min: 1 })
    .withMessage('Page must be a positive integer'),
  body('limit')
    .optional()
    .isInt({ min: 1, max: 100 })
    .withMessage('Limit must be between 1 and 100'),
  body('category')
    .optional()
    .isString()
    .withMessage('Category must be a string'),
  body('dateFrom')
    .optional()
    .isISO8601()
    .withMessage('Date from must be a valid date'),
  body('dateTo')
    .optional()
    .isISO8601()
    .withMessage('Date to must be a valid date')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 20;
    const skip = (page - 1) * limit;

    // Build query
    const query = { user: req.user._id, status: 'active' };
    
    if (req.query.category) {
      query['prediction.topPrediction.category'] = req.query.category;
    }
    
    if (req.query.dateFrom || req.query.dateTo) {
      query.createdAt = {};
      if (req.query.dateFrom) {
        query.createdAt.$gte = new Date(req.query.dateFrom);
      }
      if (req.query.dateTo) {
        query.createdAt.$lte = new Date(req.query.dateTo);
      }
    }

    // Get total count
    const total = await Classification.countDocuments(query);
    
    // Get classifications
    const classifications = await Classification.find(query)
      .sort({ createdAt: -1 })
      .skip(skip)
      .limit(limit)
      .populate('user', 'username profile.firstName profile.lastName');

    const totalPages = Math.ceil(total / limit);

    res.json({
      success: true,
      data: {
        classifications,
        pagination: {
          page,
          limit,
          total,
          totalPages,
          hasNext: page < totalPages,
          hasPrev: page > 1
        }
      }
    });

  } catch (error) {
    logger.error('History fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch classification history'
    });
  }
});

// Get single classification endpoint
router.get('/:id', auth, async (req, res) => {
  try {
    const classification = await Classification.findOne({
      _id: req.params.id,
      user: req.user._id,
      status: 'active'
    }).populate('user', 'username profile.firstName profile.lastName');

    if (!classification) {
      return res.status(404).json({
        success: false,
        error: 'Classification not found'
      });
    }

    res.json({
      success: true,
      data: classification
    });

  } catch (error) {
    logger.error('Classification fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch classification'
    });
  }
});

// Submit feedback endpoint
router.post('/:id/feedback', [
  auth,
  body('isCorrect')
    .isBoolean()
    .withMessage('isCorrect must be a boolean'),
  body('userCategory')
    .optional()
    .isString()
    .withMessage('User category must be a string'),
  body('userSubcategory')
    .optional()
    .isString()
    .withMessage('User subcategory must be a string'),
  body('comments')
    .optional()
    .isLength({ max: 500 })
    .withMessage('Comments must be less than 500 characters'),
  body('rating')
    .optional()
    .isInt({ min: 1, max: 5 })
    .withMessage('Rating must be between 1 and 5')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { isCorrect, userCategory, userSubcategory, comments, rating } = req.body;

    const classification = await Classification.findOne({
      _id: req.params.id,
      user: req.user._id,
      status: 'active'
    });

    if (!classification) {
      return res.status(404).json({
        success: false,
        error: 'Classification not found'
      });
    }

    // Update feedback
    classification.feedback = {
      isCorrect,
      userCategory,
      userSubcategory,
      comments,
      rating,
      submittedAt: new Date()
    };

    await classification.save();

    // Update user statistics
    if (isCorrect) {
      await User.findByIdAndUpdate(req.user._id, {
        $inc: { 
          'statistics.correctClassifications': 1,
          'statistics.carbonSaved': classification.wasteInfo.carbonFootprint || 0
        }
      });
    }

    // Update category statistics
    const wasteCategory = await WasteCategory.findOne({ 
      category: classification.prediction.topPrediction.category.toLowerCase() 
    });
    
    if (wasteCategory) {
      await wasteCategory.incrementStats(isCorrect, classification.prediction.confidence, classification.wasteInfo.weight);
    }

    logger.ai(`Feedback submitted for classification ${req.params.id}: ${isCorrect ? 'correct' : 'incorrect'}`);

    res.json({
      success: true,
      message: 'Feedback submitted successfully',
      data: classification
    });

  } catch (error) {
    logger.error('Feedback submission error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to submit feedback'
    });
  }
});

// Get classification statistics endpoint
router.get('/statistics/summary', auth, async (req, res) => {
  try {
    const { dateFrom, dateTo } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const stats = await Classification.getStatistics(req.user._id, timeRange);
    const categoryStats = await Classification.getCategoryStats(req.user._id, timeRange);

    res.json({
      success: true,
      data: {
        summary: stats[0] || {
          totalClassifications: 0,
          correctClassifications: 0,
          totalWeight: 0,
          totalCarbonFootprint: 0,
          avgConfidence: 0,
          accuracy: 0
        },
        categories: categoryStats
      }
    });

  } catch (error) {
    logger.error('Statistics fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch statistics'
    });
  }
});

// Get timeline data endpoint
router.get('/statistics/timeline', auth, async (req, res) => {
  try {
    const { interval = 'day', dateFrom, dateTo } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const timeline = await Classification.getTimelineData(req.user._id, interval, timeRange);

    res.json({
      success: true,
      data: timeline
    });

  } catch (error) {
    logger.error('Timeline fetch error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch timeline data'
    });
  }
});

// Delete classification endpoint (soft delete)
router.delete('/:id', auth, async (req, res) => {
  try {
    const classification = await Classification.findOne({
      _id: req.params.id,
      user: req.user._id,
      status: 'active'
    });

    if (!classification) {
      return res.status(404).json({
        success: false,
        error: 'Classification not found'
      });
    }

    // Soft delete
    classification.status = 'deleted';
    await classification.save();

    logger.ai(`Classification deleted: ${req.params.id}`);

    res.json({
      success: true,
      message: 'Classification deleted successfully'
    });

  } catch (error) {
    logger.error('Classification deletion error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to delete classification'
    });
  }
});

module.exports = router;