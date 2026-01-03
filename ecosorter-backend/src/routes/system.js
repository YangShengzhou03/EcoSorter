const express = require('express');
const { body, validationResult } = require('express-validator');
const User = require('../models/User');
const Classification = require('../models/Classification');
const WasteCategory = require('../models/WasteCategory');
const { auth, adminOnly } = require('../middleware/auth');
const logger = require('../utils/logger');

const router = express.Router();

// Get system status endpoint
router.get('/status', auth, async (req, res) => {
  try {
    const [
      totalUsers,
      activeUsers,
      totalClassifications,
      todayClassifications,
      totalCategories,
      activeCategories
    ] = await Promise.all([
      User.countDocuments(),
      User.countDocuments({ status: 'active' }),
      Classification.countDocuments({ status: 'active' }),
      Classification.countDocuments({
        status: 'active',
        createdAt: { $gte: new Date(new Date().setHours(0, 0, 0, 0)) }
      }),
      WasteCategory.countDocuments(),
      WasteCategory.countDocuments({ isActive: true })
    ]);

    const systemStatus = {
      users: {
        total: totalUsers,
        active: activeUsers,
        inactive: totalUsers - activeUsers
      },
      classifications: {
        total: totalClassifications,
        today: todayClassifications
      },
      categories: {
        total: totalCategories,
        active: activeCategories
      },
      system: {
        uptime: process.uptime(),
        memory: process.memoryUsage(),
        version: process.env.npm_package_version || '1.0.0',
        environment: process.env.NODE_ENV || 'development'
      }
    };

    res.json({
      success: true,
      data: systemStatus
    });

  } catch (error) {
    logger.error('System status error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch system status'
    });
  }
});

// Get system configuration endpoint (admin only)
router.get('/config', [auth, adminOnly], async (req, res) => {
  try {
    const config = {
      ai: {
        modelVersion: process.env.AI_MODEL_VERSION || '1.0.0',
        confidenceThreshold: parseFloat(process.env.AI_CONFIDENCE_THRESHOLD || '70'),
        maxFileSize: parseInt(process.env.MAX_FILE_SIZE || '10485760'), // 10MB
        supportedFormats: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
      },
      security: {
        jwtExpiresIn: process.env.JWT_EXPIRES_IN || '7d',
        jwtRefreshExpiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '30d',
        maxLoginAttempts: parseInt(process.env.MAX_LOGIN_ATTEMPTS || '5'),
        lockoutDuration: parseInt(process.env.LOCKOUT_DURATION || '7200000') // 2 hours
      },
      upload: {
        maxFileSize: parseInt(process.env.MAX_FILE_SIZE || '10485760'), // 10MB
        avatarMaxSize: parseInt(process.env.AVATAR_MAX_SIZE || '5242880'), // 5MB
        classificationMaxSize: parseInt(process.env.CLASSIFICATION_MAX_SIZE || '10485760'), // 10MB
        allowedTypes: ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/bmp', 'image/webp']
      },
      database: {
        connection: process.env.NODE_ENV === 'production' ? 'connected' : 'connected',
        lastBackup: new Date(), // This would come from actual backup service
        backupSchedule: process.env.BACKUP_SCHEDULE || '0 2 * * *' // Daily at 2 AM
      },
      features: {
        emailVerification: process.env.EMAIL_VERIFICATION_ENABLED === 'true',
        twoFactorAuth: process.env.TWO_FACTOR_AUTH_ENABLED === 'true',
        socialLogin: process.env.SOCIAL_LOGIN_ENABLED === 'true',
        notifications: process.env.NOTIFICATIONS_ENABLED === 'true',
        leaderboard: process.env.LEADERBOARD_ENABLED === 'true',
        exportData: process.env.EXPORT_DATA_ENABLED === 'true'
      }
    };

    res.json({
      success: true,
      data: config
    });

  } catch (error) {
    logger.error('System config error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch system configuration'
    });
  }
});

// Update system configuration endpoint (admin only)
router.put('/config', [
  auth, 
  adminOnly,
  body('ai.confidenceThreshold')
    .optional()
    .isFloat({ min: 0, max: 100 })
    .withMessage('Confidence threshold must be between 0 and 100'),
  body('security.maxLoginAttempts')
    .optional()
    .isInt({ min: 1, max: 10 })
    .withMessage('Max login attempts must be between 1 and 10'),
  body('features.*')
    .optional()
    .isBoolean()
    .withMessage('Feature flags must be boolean')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const configUpdates = req.body;

    // In a real implementation, you would update these in a configuration database
    // For now, we'll just log the changes
    logger.info(`System configuration updated by admin: ${req.user.username}`, configUpdates);

    res.json({
      success: true,
      message: 'System configuration updated successfully',
      data: configUpdates
    });

  } catch (error) {
    logger.error('System config update error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to update system configuration'
    });
  }
});

// Get system logs endpoint (admin only)
router.get('/logs', [auth, adminOnly], async (req, res) => {
  try {
    const { level = 'info', limit = 100, dateFrom, dateTo } = req.query;
    
    // This would typically read from a log aggregation service
    // For now, return mock data
    const mockLogs = [
      {
        timestamp: new Date(),
        level: 'info',
        message: 'System started successfully',
        service: 'ecosorter-backend',
        category: 'system'
      },
      {
        timestamp: new Date(Date.now() - 60000),
        level: 'info',
        message: 'User login successful',
        service: 'ecosorter-backend',
        category: 'authentication',
        username: 'admin'
      },
      {
        timestamp: new Date(Date.now() - 120000),
        level: 'warn',
        message: 'High memory usage detected',
        service: 'ecosorter-backend',
        category: 'performance',
        memoryUsage: '85%'
      }
    ];

    res.json({
      success: true,
      data: {
        logs: mockLogs.slice(0, limit),
        total: mockLogs.length,
        filters: { level, dateFrom, dateTo }
      }
    });

  } catch (error) {
    logger.error('System logs error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch system logs'
    });
  }
});

// Get system health endpoint
router.get('/health', async (req, res) => {
  try {
    const healthChecks = {
      database: await checkDatabase(),
      aiModel: await checkAIModel(),
      storage: await checkStorage(),
      memory: checkMemory(),
      uptime: process.uptime()
    };

    const overallHealth = Object.values(healthChecks).every(check => check.status === 'healthy');

    res.json({
      success: true,
      data: {
        status: overallHealth ? 'healthy' : 'degraded',
        checks: healthChecks,
        timestamp: new Date()
      }
    });

  } catch (error) {
    logger.error('Health check error:', error);
    res.status(503).json({
      success: false,
      error: 'Health check failed',
      data: {
        status: 'unhealthy',
        error: error.message,
        timestamp: new Date()
      }
    });
  }
});

// Helper functions for health checks
async function checkDatabase() {
  try {
    const mongoose = require('mongoose');
    const state = mongoose.connection.readyState;
    return {
      status: state === 1 ? 'healthy' : 'unhealthy',
      details: {
        state: state === 1 ? 'connected' : 'disconnected',
        host: mongoose.connection.host,
        port: mongoose.connection.port,
        name: mongoose.connection.name
      }
    };
  } catch (error) {
    return {
      status: 'unhealthy',
      details: { error: error.message }
    };
  }
}

async function checkAIModel() {
  try {
    // Check if AI model is loaded and functional
    // This would be implemented based on your AI model setup
    return {
      status: 'healthy',
      details: {
        modelVersion: process.env.AI_MODEL_VERSION || '1.0.0',
        loaded: true,
        lastPrediction: new Date()
      }
    };
  } catch (error) {
    return {
      status: 'unhealthy',
      details: { error: error.message }
    };
  }
}

async function checkStorage() {
  try {
    const fs = require('fs').promises;
    const uploadDir = path.join(__dirname, '../../uploads');
    
    await fs.access(uploadDir);
    const stats = await fs.stat(uploadDir);
    
    return {
      status: 'healthy',
      details: {
        accessible: true,
        writable: true,
        path: uploadDir
      }
    };
  } catch (error) {
    return {
      status: 'unhealthy',
      details: { error: error.message }
    };
  }
}

function checkMemory() {
  const memUsage = process.memoryUsage();
  const totalMB = Math.round(memUsage.heapTotal / 1024 / 1024);
  const usedMB = Math.round(memUsage.heapUsed / 1024 / 1024);
  const usagePercent = Math.round((usedMB / totalMB) * 100);
  
  return {
    status: usagePercent < 90 ? 'healthy' : 'warning',
    details: {
      total: `${totalMB}MB`,
      used: `${usedMB}MB`,
      usage: `${usagePercent}%`,
      external: `${Math.round(memUsage.external / 1024 / 1024)}MB`
    }
  };
}

// Get maintenance status endpoint (admin only)
router.get('/maintenance', [auth, adminOnly], async (req, res) => {
  try {
    const maintenanceStatus = {
      isMaintenanceMode: process.env.MAINTENANCE_MODE === 'true',
      scheduledMaintenance: {
        start: process.env.MAINTENANCE_START || null,
        end: process.env.MAINTENANCE_END || null,
        reason: process.env.MAINTENANCE_REASON || 'System maintenance'
      },
      lastMaintenance: new Date(), // This would come from maintenance logs
      nextMaintenance: null, // This would be scheduled
      affectedServices: ['api', 'classification', 'uploads']
    };

    res.json({
      success: true,
      data: maintenanceStatus
    });

  } catch (error) {
    logger.error('Maintenance status error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch maintenance status'
    });
  }
});

// Toggle maintenance mode endpoint (admin only)
router.post('/maintenance', [
  auth, 
  adminOnly,
  body('enabled')
    .isBoolean()
    .withMessage('Maintenance mode must be boolean'),
  body('reason')
    .optional()
    .isString()
    .withMessage('Reason must be a string'),
  body('start')
    .optional()
    .isISO8601()
    .withMessage('Start time must be a valid date'),
  body('end')
    .optional()
    .isISO8601()
    .withMessage('End time must be a valid date')
], async (req, res) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { enabled, reason, start, end } = req.body;

    // In a real implementation, this would update a configuration database
    process.env.MAINTENANCE_MODE = enabled.toString();
    if (reason) process.env.MAINTENANCE_REASON = reason;
    if (start) process.env.MAINTENANCE_START = start;
    if (end) process.env.MAINTENANCE_END = end;

    logger.info(`Maintenance mode ${enabled ? 'enabled' : 'disabled'} by admin: ${req.user.username}`, {
      reason,
      start,
      end
    });

    res.json({
      success: true,
      message: `Maintenance mode ${enabled ? 'enabled' : 'disabled'} successfully`,
      data: {
        enabled,
        reason,
        start,
        end
      }
    });

  } catch (error) {
    logger.error('Maintenance mode toggle error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to toggle maintenance mode'
    });
  }
});

module.exports = router;