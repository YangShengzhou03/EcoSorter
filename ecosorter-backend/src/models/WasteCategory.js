const mongoose = require('mongoose');

const wasteCategorySchema = new mongoose.Schema({
  category: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    maxlength: 50
  },
  displayName: {
    type: String,
    required: true,
    trim: true,
    maxlength: 100
  },
  description: {
    type: String,
    trim: true,
    maxlength: 500
  },
  type: {
    type: String,
    enum: ['recyclable', 'hazardous', 'wet', 'dry', 'other'],
    required: true
  },
  color: {
    type: String,
    required: true,
    match: /^#[0-9A-Fa-f]{6}$/
  },
  icon: {
    type: String,
    trim: true,
    maxlength: 100
  },
  subcategories: [{
    name: { type: String, required: true, trim: true, maxlength: 50 },
    displayName: { type: String, required: true, trim: true, maxlength: 100 },
    description: { type: String, trim: true, maxlength: 300 },
    examples: [{ type: String, trim: true, maxlength: 100 }],
    disposalMethod: { type: String, trim: true, maxlength: 500 },
    recyclingInfo: { type: String, trim: true, maxlength: 500 },
    environmentalImpact: { type: String, trim: true, maxlength: 300 }
  }],
  disposalInstructions: {
    general: { type: String, trim: true, maxlength: 1000 },
    preparation: { type: String, trim: true, maxlength: 1000 },
    collection: { type: String, trim: true, maxlength: 1000 },
    specialNotes: { type: String, trim: true, maxlength: 500 }
  },
  environmentalImpact: {
    carbonFactor: { type: Number, min: 0 },
    decompositionTime: { type: String, trim: true, maxlength: 100 },
    toxicityLevel: { 
      type: String, 
      enum: ['none', 'low', 'medium', 'high'], 
      default: 'none' 
    },
    soilImpact: { type: String, trim: true, maxlength: 300 },
    waterImpact: { type: String, trim: true, maxlength: 300 },
    airImpact: { type: String, trim: true, maxlength: 300 }
  },
  regulations: {
    local: [{ type: String, trim: true, maxlength: 200 }],
    national: [{ type: String, trim: true, maxlength: 200 }],
    international: [{ type: String, trim: true, maxlength: 200 }]
  },
  statistics: {
    totalClassifications: { type: Number, default: 0 },
    correctClassifications: { type: Number, default: 0 },
    avgConfidence: { type: Number, default: 0 },
    totalWeight: { type: Number, default: 0 },
    totalCarbonFootprint: { type: Number, default: 0 }
  },
  mlModel: {
    labels: [{ type: String, trim: true, maxlength: 100 }],
    confidenceThreshold: { type: Number, min: 0, max: 100, default: 70 },
    trainingAccuracy: { type: Number, min: 0, max: 100 },
    validationAccuracy: { type: Number, min: 0, max: 100 },
    lastTrained: { type: Date }
  },
  isActive: { type: Boolean, default: true },
  displayOrder: { type: Number, default: 0 },
  tags: [{ type: String, trim: true, maxlength: 50 }],
  translations: {
    zh_CN: {
      displayName: { type: String, trim: true, maxlength: 100 },
      description: { type: String, trim: true, maxlength: 500 },
      disposalInstructions: {
        general: { type: String, trim: true, maxlength: 1000 },
        preparation: { type: String, trim: true, maxlength: 1000 },
        collection: { type: String, trim: true, maxlength: 1000 },
        specialNotes: { type: String, trim: true, maxlength: 500 }
      }
    },
    en_US: {
      displayName: { type: String, trim: true, maxlength: 100 },
      description: { type: String, trim: true, maxlength: 500 },
      disposalInstructions: {
        general: { type: String, trim: true, maxlength: 1000 },
        preparation: { type: String, trim: true, maxlength: 1000 },
        collection: { type: String, trim: true, maxlength: 1000 },
        specialNotes: { type: String, trim: true, maxlength: 500 }
      }
    }
  },
  createdBy: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  },
  updatedBy: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  }
}, {
  timestamps: true
});

// Indexes
wasteCategorySchema.index({ category: 1 });
wasteCategorySchema.index({ type: 1 });
wasteCategorySchema.index({ isActive: 1 });
wasteCategorySchema.index({ displayOrder: 1 });
wasteCategorySchema.index({ 'subcategories.name': 1 });

// Pre-save middleware to update statistics
wasteCategorySchema.pre('save', function(next) {
  // Calculate accuracy if we have statistics
  if (this.statistics.totalClassifications > 0) {
    this.statistics.avgConfidence = this.statistics.correctClassifications / this.statistics.totalClassifications;
  }
  
  next();
});

// Instance methods
wasteCategorySchema.methods.getDisplayName = function(language = 'zh_CN') {
  return this.translations[language]?.displayName || this.displayName;
};

wasteCategorySchema.methods.getDescription = function(language = 'zh_CN') {
  return this.translations[language]?.description || this.description;
};

wasteCategorySchema.methods.getDisposalInstructions = function(language = 'zh_CN') {
  const translations = this.translations[language];
  if (translations?.disposalInstructions) {
    return translations.disposalInstructions;
  }
  return this.disposalInstructions;
};

wasteCategorySchema.methods.incrementStats = function(isCorrect, confidence, weight = 0) {
  this.statistics.totalClassifications += 1;
  
  if (isCorrect) {
    this.statistics.correctClassifications += 1;
  }
  
  if (confidence) {
    const total = this.statistics.totalClassifications;
    const currentAvg = this.statistics.avgConfidence;
    this.statistics.avgConfidence = ((currentAvg * (total - 1)) + confidence) / total;
  }
  
  if (weight) {
    this.statistics.totalWeight += weight;
    if (this.environmentalImpact.carbonFactor) {
      this.statistics.totalCarbonFootprint += weight * this.environmentalImpact.carbonFactor;
    }
  }
  
  return this.save();
};

// Static methods
wasteCategorySchema.statics.findByType = function(type) {
  return this.find({ type, isActive: true }).sort({ displayOrder: 1 });
};

wasteCategorySchema.statics.findByCategory = function(category) {
  return this.findOne({ category, isActive: true });
};

wasteCategorySchema.statics.getActiveCategories = function() {
  return this.find({ isActive: true }).sort({ displayOrder: 1, displayName: 1 });
};

wasteCategorySchema.statics.getStatistics = async function() {
  return this.aggregate([
    { $match: { isActive: true } },
    {
      $group: {
        _id: '$type',
        categories: { $sum: 1 },
        totalClassifications: { $sum: '$statistics.totalClassifications' },
        correctClassifications: { $sum: '$statistics.correctClassifications' },
        totalWeight: { $sum: '$statistics.totalWeight' },
        totalCarbonFootprint: { $sum: '$statistics.totalCarbonFootprint' },
        avgConfidence: { $avg: '$statistics.avgConfidence' }
      }
    },
    {
      $project: {
        type: '$_id',
        categories: 1,
        totalClassifications: 1,
        correctClassifications: 1,
        totalWeight: 1,
        totalCarbonFootprint: 1,
        avgConfidence: 1,
        accuracy: {
          $cond: [
            { $gt: ['$totalClassifications', 0] },
            { $multiply: [{ $divide: ['$correctClassifications', '$totalClassifications'] }, 100] },
            0
          ]
        }
      }
    },
    { $sort: { totalClassifications: -1 } }
  ]);
};

wasteCategorySchema.statics.searchCategories = function(searchTerm, language = 'zh_CN') {
  const regex = new RegExp(searchTerm, 'i');
  
  return this.find({
    $and: [
      { isActive: true },
      {
        $or: [
          { category: regex },
          { displayName: regex },
          { description: regex },
          { [`translations.${language}.displayName`]: regex },
          { [`translations.${language}.description`]: regex },
          { 'subcategories.name': regex },
          { 'subcategories.displayName': regex },
          { 'subcategories.examples': regex }
        ]
      }
    ]
  }).sort({ displayOrder: 1, displayName: 1 });
};

module.exports = mongoose.model('WasteCategory', wasteCategorySchema);