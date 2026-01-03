const mongoose = require('mongoose');

const classificationSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    index: true
  },
  image: {
    originalName: { type: String, required: true },
    fileName: { type: String, required: true },
    mimeType: { type: String, required: true },
    size: { type: Number, required: min: 0 },
    path: { type: String, required: true },
    url: { type: String },
    thumbnail: { type: String }
  },
  prediction: {
    confidence: { type: Number, required: true, min: 0, max: 100 },
    topPrediction: {
      category: { type: String, required: true },
      subcategory: { type: String },
      confidence: { type: Number, required: true, min: 0, max: 100 },
      description: { type: String }
    },
    alternatives: [{
      category: { type: String, required: true },
      subcategory: { type: String },
      confidence: { type: Number, required: true, min: 0, max: 100 },
      description: { type: String }
    }],
    processingTime: { type: Number, required: true, min: 0 },
    modelVersion: { type: String, required: true },
    modelUsed: { type: String, required: true }
  },
  wasteInfo: {
    category: { type: String, required: true },
    subcategory: { type: String },
    type: { 
      type: String, 
      enum: ['recyclable', 'hazardous', 'wet', 'dry', 'other'],
      required: true 
    },
    weight: { type: Number, min: 0 },
    volume: { type: Number, min: 0 },
    carbonFootprint: { type: Number, min: 0 },
    disposalMethod: { type: String },
    recyclingInfo: { type: String },
    environmentalImpact: { type: String }
  },
  location: {
    latitude: { type: Number, min: -90, max: 90 },
    longitude: { type: Number, min: -180, max: 180 },
    address: { type: String },
    city: { type: String },
    region: { type: String },
    country: { type: String },
    postalCode: { type: String }
  },
  device: {
    type: { type: String },
    model: { type: String },
    os: { type: String },
    appVersion: { type: String },
    deviceId: { type: String }
  },
  feedback: {
    isCorrect: { type: Boolean },
    userCategory: { type: String },
    userSubcategory: { type: String },
    comments: { type: String, maxlength: 500 },
    rating: { type: Number, min: 1, max: 5 },
    submittedAt: { type: Date }
  },
  verification: {
    status: { 
      type: String, 
      enum: ['pending', 'verified', 'rejected', 'corrected'],
      default: 'pending'
    },
    verifiedBy: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    verifiedAt: { type: Date },
    verificationNotes: { type: String, maxlength: 500 }
  },
  metadata: {
    ipAddress: { type: String },
    userAgent: { type: String },
    sessionId: { type: String },
    correlationId: { type: String },
    tags: [{ type: String }],
    notes: { type: String, maxlength: 500 }
  },
  status: {
    type: String,
    enum: ['active', 'archived', 'deleted'],
    default: 'active'
  },
  isPublic: { type: Boolean, default: false },
  shareToken: { type: String },
  shareExpires: { type: Date }
}, {
  timestamps: true,
  toJSON: {
    transform: function(doc, ret) {
      // Remove sensitive metadata for non-admin users
      if (ret.user && !ret.user.role || ret.user.role !== 'admin') {
        delete ret.metadata;
        delete ret.device;
        delete ret.location;
      }
      return ret;
    }
  }
});

// Indexes
classificationSchema.index({ user: 1, createdAt: -1 });
classificationSchema.index({ 'prediction.topPrediction.category': 1 });
classificationSchema.index({ 'wasteInfo.type': 1 });
classificationSchema.index({ 'verification.status': 1 });
classificationSchema.index({ createdAt: -1 });
classificationSchema.index({ 'feedback.isCorrect': 1 });
classificationSchema.index({ status: 1 });
classificationSchema.index({ shareToken: 1 });

// Compound indexes for statistics
classificationSchema.index({ user: 1, 'wasteInfo.type': 1, createdAt: -1 });
classificationSchema.index({ user: 1, 'feedback.isCorrect': 1, createdAt: -1 });

// Pre-save middleware
classificationSchema.pre('save', function(next) {
  // Calculate carbon footprint based on waste type and weight
  if (this.isModified('wasteInfo') && this.wasteInfo.weight) {
    const carbonFactors = {
      recyclable: 0.5,
      hazardous: 2.0,
      wet: 0.8,
      dry: 1.2,
      other: 1.0
    };
    
    this.wasteInfo.carbonFootprint = this.wasteInfo.weight * (carbonFactors[this.wasteInfo.type] || 1.0);
  }
  
  // Generate share token if needed
  if (this.isModified('isPublic') && this.isPublic && !this.shareToken) {
    this.shareToken = require('crypto').randomBytes(16).toString('hex');
    this.shareExpires = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000); // 7 days
  }
  
  next();
});

// Instance methods
classificationSchema.methods.getAccuracyScore = function() {
  if (!this.feedback.isCorrect) return 0;
  
  let score = this.prediction.confidence;
  
  // Bonus for high confidence correct predictions
  if (this.prediction.confidence >= 90) score += 10;
  else if (this.prediction.confidence >= 80) score += 5;
  
  // Penalty for requiring verification
  if (this.verification.status === 'corrected') score -= 20;
  
  return Math.max(0, Math.min(100, score));
};

classificationSchema.methods.canShare = function() {
  return this.isPublic && this.shareToken && this.shareExpires > new Date();
};

classificationSchema.methods.archive = function() {
  this.status = 'archived';
  return this.save();
};

classificationSchema.methods.verify = function(verifiedBy, status, notes) {
  this.verification = {
    status: status,
    verifiedBy: verifiedBy,
    verifiedAt: new Date(),
    verificationNotes: notes
  };
  return this.save();
};

// Static methods
classificationSchema.statics.getStatistics = async function(userId, timeRange = {}) {
  const match = { status: 'active' };
  
  if (userId) {
    match.user = userId;
  }
  
  if (timeRange.start) {
    match.createdAt = { $gte: new Date(timeRange.start) };
  }
  
  if (timeRange.end) {
    match.createdAt = { ...match.createdAt, $lte: new Date(timeRange.end) };
  }
  
  return this.aggregate([
    { $match: match },
    {
      $group: {
        _id: null,
        totalClassifications: { $sum: 1 },
        correctClassifications: {
          $sum: {
            $cond: [{ $eq: ['$feedback.isCorrect', true] }, 1, 0]
          }
        },
        totalWeight: { $sum: '$wasteInfo.weight' },
        totalCarbonFootprint: { $sum: '$wasteInfo.carbonFootprint' },
        avgConfidence: { $avg: '$prediction.confidence' },
        avgProcessingTime: { $avg: '$prediction.processingTime' }
      }
    },
    {
      $project: {
        totalClassifications: 1,
        correctClassifications: 1,
        totalWeight: 1,
        totalCarbonFootprint: 1,
        avgConfidence: 1,
        avgProcessingTime: 1,
        accuracy: {
          $cond: [
            { $gt: ['$totalClassifications', 0] },
            { $multiply: [{ $divide: ['$correctClassifications', '$totalClassifications'] }, 100] },
            0
          ]
        }
      }
    }
  ]);
};

classificationSchema.statics.getCategoryStats = async function(userId, timeRange = {}) {
  const match = { status: 'active' };
  
  if (userId) {
    match.user = userId;
  }
  
  if (timeRange.start) {
    match.createdAt = { $gte: new Date(timeRange.start) };
  }
  
  if (timeRange.end) {
    match.createdAt = { ...match.createdAt, $lte: new Date(timeRange.end) };
  }
  
  return this.aggregate([
    { $match: match },
    {
      $group: {
        _id: '$wasteInfo.type',
        count: { $sum: 1 },
        totalWeight: { $sum: '$wasteInfo.weight' },
        totalCarbonFootprint: { $sum: '$wasteInfo.carbonFootprint' },
        correctCount: {
          $sum: {
            $cond: [{ $eq: ['$feedback.isCorrect', true] }, 1, 0]
          }
        },
        avgConfidence: { $avg: '$prediction.confidence' }
      }
    },
    {
      $project: {
        category: '$_id',
        count: 1,
        totalWeight: 1,
        totalCarbonFootprint: 1,
        correctCount: 1,
        avgConfidence: 1,
        accuracy: {
          $cond: [
            { $gt: ['$count', 0] },
            { $multiply: [{ $divide: ['$correctCount', '$count'] }, 100] },
            0
          ]
        }
      }
    },
    { $sort: { count: -1 } }
  ]);
};

classificationSchema.statics.getTimelineData = async function(userId, interval = 'day', timeRange = {}) {
  const match = { status: 'active' };
  
  if (userId) {
    match.user = userId;
  }
  
  if (timeRange.start) {
    match.createdAt = { $gte: new Date(timeRange.start) };
  }
  
  if (timeRange.end) {
    match.createdAt = { ...match.createdAt, $lte: new Date(timeRange.end) };
  }
  
  const groupBy = {
    day: { $dateToString: { format: '%Y-%m-%d', date: '$createdAt' } },
    week: { $dateToString: { format: '%Y-%U', date: '$createdAt' } },
    month: { $dateToString: { format: '%Y-%m', date: '$createdAt' } },
    year: { $dateToString: { format: '%Y', date: '$createdAt' } }
  };
  
  return this.aggregate([
    { $match: match },
    {
      $group: {
        _id: groupBy[interval] || groupBy.day,
        count: { $sum: 1 },
        correctCount: {
          $sum: {
            $cond: [{ $eq: ['$feedback.isCorrect', true] }, 1, 0]
          }
        },
        totalWeight: { $sum: '$wasteInfo.weight' },
        totalCarbonFootprint: { $sum: '$wasteInfo.carbonFootprint' },
        avgConfidence: { $avg: '$prediction.confidence' }
      }
    },
    { $sort: { _id: 1 } },
    {
      $project: {
        date: '$_id',
        count: 1,
        correctCount: 1,
        totalWeight: 1,
        totalCarbonFootprint: 1,
        avgConfidence: 1,
        accuracy: {
          $cond: [
            { $gt: ['$count', 0] },
            { $multiply: [{ $divide: ['$correctCount', '$count'] }, 100] },
            0
          ]
        }
      }
    }
  ]);
};

classificationSchema.statics.getLeaderboard = async function(timeRange = {}, limit = 10) {
  const match = { status: 'active' };
  
  if (timeRange.start) {
    match.createdAt = { $gte: new Date(timeRange.start) };
  }
  
  if (timeRange.end) {
    match.createdAt = { ...match.createdAt, $lte: new Date(timeRange.end) };
  }
  
  return this.aggregate([
    { $match: match },
    {
      $group: {
        _id: '$user',
        totalClassifications: { $sum: 1 },
        correctClassifications: {
          $sum: {
            $cond: [{ $eq: ['$feedback.isCorrect', true] }, 1, 0]
          }
        },
        totalWeight: { $sum: '$wasteInfo.weight' },
        totalCarbonFootprint: { $sum: '$wasteInfo.carbonFootprint' },
        avgConfidence: { $avg: '$prediction.confidence' }
      }
    },
    {
      $match: {
        totalClassifications: { $gte: 10 } // Minimum 10 classifications to qualify
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
        avatar: '$user.profile.avatar',
        totalClassifications: 1,
        correctClassifications: 1,
        totalWeight: 1,
        totalCarbonFootprint: 1,
        avgConfidence: 1,
        accuracy: {
          $multiply: [{ $divide: ['$correctClassifications', '$totalClassifications'] }, 100]
        },
        score: {
          $add: [
            { $multiply: ['$accuracy', 0.6] },
            { $multiply: ['$avgConfidence', 0.3] },
            { $multiply: [{ $divide: ['$totalClassifications', 100] }, 10] }
          ]
        }
      }
    },
    { $sort: { score: -1 } },
    { $limit: limit }
  ]);
};

module.exports = mongoose.model('Classification', classificationSchema);