const express = require('express');
const { body, validationResult } = require('express-validator');
const Classification = require('../models/Classification');
const User = require('../models/User');
const WasteCategory = require('../models/WasteCategory');
const { auth, adminOnly } = require('../middleware/auth');
const logger = require('../utils/logger');

const router = express.Router();

// Get user statistics endpoint
router.get('/user', auth, async (req, res) => {
  try {
    const { dateFrom, dateTo } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const stats = await Classification.getStatistics(req.user._id, timeRange);
    const categoryStats = await Classification.getCategoryStats(req.user._id, timeRange);
    const timeline = await Classification.getTimelineData(req.user._id, 'day', timeRange);

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

// Get system-wide statistics (admin only)
router.get('/system', [auth, adminOnly], async (req, res) => {
  try {
    const { dateFrom, dateTo } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    // Get system-wide classification statistics
    const systemStats = await Classification.getStatistics(null, timeRange);
    const categoryStats = await Classification.getCategoryStats(null, timeRange);
    const timeline = await Classification.getTimelineData(null, 'day', timeRange);

    // Get user statistics
    const userStats = await User.getStatistics();

    // Get category statistics
    const wasteCategoryStats = await WasteCategory.getStatistics();

    res.json({
      success: true,
      data: {
        system: systemStats[0] || {
          totalClassifications: 0,
          correctClassifications: 0,
          totalWeight: 0,
          totalCarbonFootprint: 0,
          avgConfidence: 0,
          accuracy: 0
        },
        users: userStats[0] || {
          totalUsers: 0,
          activeUsers: 0,
          adminUsers: 0,
          avgAccuracy: 0,
          totalClassifications: 0,
          totalCarbonSaved: 0
        },
        categories: categoryStats,
        wasteCategories: wasteCategoryStats,
        timeline
      }
    });

  } catch (error) {
    logger.error('System statistics error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch system statistics'
    });
  }
});

// Get leaderboard endpoint
router.get('/leaderboard', auth, async (req, res) => {
  try {
    const { dateFrom, dateTo, limit = 10 } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const leaderboard = await Classification.getLeaderboard(timeRange, parseInt(limit));

    res.json({
      success: true,
      data: leaderboard
    });

  } catch (error) {
    logger.error('Leaderboard error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch leaderboard'
    });
  }
});

// Get detailed analytics endpoint
router.get('/analytics', [auth, adminOnly], async (req, res) => {
  try {
    const { dateFrom, dateTo, groupBy = 'day' } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    // Get timeline data for different metrics
    const [classificationsTimeline, accuracyTimeline, weightTimeline] = await Promise.all([
      Classification.getTimelineData(null, groupBy, timeRange),
      Classification.aggregate([
        {
          $match: {
            ...timeRange,
            status: 'active',
            'feedback.isCorrect': { $ne: null }
          }
        },
        {
          $group: {
            _id: {
              $dateToString: { 
                format: groupBy === 'day' ? '%Y-%m-%d' : 
                        groupBy === 'week' ? '%Y-%U' :
                        groupBy === 'month' ? '%Y-%m' : '%Y',
                date: '$createdAt' 
              }
            },
            total: { $sum: 1 },
            correct: {
              $sum: { $cond: ['$feedback.isCorrect', 1, 0] }
            }
          }
        },
        {
          $project: {
            date: '$_id',
            accuracy: { $multiply: [{ $divide: ['$correct', '$total'] }, 100] },
            total: 1,
            correct: 1
          }
        },
        { $sort: { date: 1 } }
      ]),
      Classification.aggregate([
        {
          $match: {
            ...timeRange,
            status: 'active'
          }
        },
        {
          $group: {
            _id: {
              $dateToString: { 
                format: groupBy === 'day' ? '%Y-%m-%d' : 
                        groupBy === 'week' ? '%Y-%U' :
                        groupBy === 'month' ? '%Y-%m' : '%Y',
                date: '$createdAt' 
              }
            },
            totalWeight: { $sum: '$wasteInfo.weight' },
            totalCarbonFootprint: { $sum: '$wasteInfo.carbonFootprint' }
          }
        },
        {
          $project: {
            date: '$_id',
            totalWeight: 1,
            totalCarbonFootprint: 1
          }
        },
        { $sort: { date: 1 } }
      ])
    ]);

    // Get user activity distribution
    const userActivity = await Classification.aggregate([
      {
        $match: {
          ...timeRange,
          status: 'active'
        }
      },
      {
        $group: {
          _id: '$user',
          classifications: { $sum: 1 },
          correct: {
            $sum: { $cond: ['$feedback.isCorrect', 1, 0] }
          },
          totalWeight: { $sum: '$wasteInfo.weight' }
        }
      },
      {
        $lookup: {
          from: 'users',
          localField: '_id',
          foreignField: '_id',
          as: 'user'
        }
      },
      { $unwind: '$user' },
      {
        $project: {
          userId: '$_id',
          username: '$user.username',
          fullName: { $concat: ['$user.profile.firstName', ' ', '$user.profile.lastName'] },
          classifications: 1,
          correct: 1,
          totalWeight: 1,
          accuracy: { $multiply: [{ $divide: ['$correct', '$classifications'] }, 100] }
        }
      },
      { $sort: { classifications: -1 } },
      { $limit: 50 }
    ]);

    // Get model performance metrics
    const modelPerformance = await Classification.aggregate([
      {
        $match: {
          ...timeRange,
          status: 'active',
          'feedback.isCorrect': { $ne: null }
        }
      },
      {
        $group: {
          _id: null,
          totalPredictions: { $sum: 1 },
          correctPredictions: {
            $sum: { $cond: ['$feedback.isCorrect', 1, 0] }
          },
          avgConfidence: { $avg: '$prediction.confidence' },
          avgProcessingTime: { $avg: '$prediction.processingTime' },
          highConfidencePredictions: {
            $sum: { $cond: [{ $gte: ['$prediction.confidence', 80] }, 1, 0] }
          }
        }
      },
      {
        $project: {
          accuracy: { $multiply: [{ $divide: ['$correctPredictions', '$totalPredictions'] }, 100] },
          avgConfidence: 1,
          avgProcessingTime: 1,
          highConfidenceRate: { $multiply: [{ $divide: ['$highConfidencePredictions', '$totalPredictions'] }, 100] }
        }
      }
    ]);

    res.json({
      success: true,
      data: {
        timeline: {
          classifications: classificationsTimeline,
          accuracy: accuracyTimeline,
          weight: weightTimeline
        },
        userActivity,
        modelPerformance: modelPerformance[0] || {
          accuracy: 0,
          avgConfidence: 0,
          avgProcessingTime: 0,
          highConfidenceRate: 0
        }
      }
    });

  } catch (error) {
    logger.error('Analytics error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch analytics data'
    });
  }
});

// Get export data endpoint
router.get('/export', [auth, adminOnly], async (req, res) => {
  try {
    const { dateFrom, dateTo, format = 'json' } = req.query;
    const timeRange = {};
    
    if (dateFrom) timeRange.start = new Date(dateFrom);
    if (dateTo) timeRange.end = new Date(dateTo);

    const match = { status: 'active' };
    if (timeRange.start) {
      match.createdAt = { $gte: new Date(timeRange.start) };
    }
    if (timeRange.end) {
      match.createdAt = { ...match.createdAt, $lte: new Date(timeRange.end) };
    }

    const data = await Classification.find(match)
      .populate('user', 'username email profile.firstName profile.lastName')
      .populate('verification.verifiedBy', 'username')
      .sort({ createdAt: -1 })
      .limit(10000); // Limit to prevent memory issues

    if (format === 'csv') {
      // Convert to CSV format
      const csvData = data.map(item => ({
        id: item._id,
        date: item.createdAt,
        user: item.user?.username || 'Unknown',
        category: item.prediction.topPrediction.category,
        subcategory: item.prediction.topPrediction.subcategory,
        confidence: item.prediction.confidence,
        isCorrect: item.feedback.isCorrect,
        userCategory: item.feedback.userCategory,
        weight: item.wasteInfo.weight,
        carbonFootprint: item.wasteInfo.carbonFootprint,
        processingTime: item.prediction.processingTime,
        verificationStatus: item.verification.status
      }));

      // Convert to CSV string
      const headers = Object.keys(csvData[0] || {}).join(',');
      const rows = csvData.map(row => Object.values(row).join(','));
      const csv = [headers, ...rows].join('\n');

      res.setHeader('Content-Type', 'text/csv');
      res.setHeader('Content-Disposition', `attachment; filename="ecosorter-data-${Date.now()}.csv"`);
      res.send(csv);
    } else {
      // Return JSON
      res.json({
        success: true,
        data: {
          exportDate: new Date(),
          dateRange: timeRange,
          totalRecords: data.length,
          records: data
        }
      });
    }

  } catch (error) {
    logger.error('Export error:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to export data'
    });
  }
});

module.exports = router;