const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const userSchema = new mongoose.Schema({
  username: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    minlength: 3,
    maxlength: 20,
    match: /^[a-zA-Z0-9_]+$/
  },
  email: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    lowercase: true,
    match: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
  },
  password: {
    type: String,
    required: true,
    minlength: 6
  },
  role: {
    type: String,
    enum: ['user', 'admin', 'operator'],
    default: 'user'
  },
  profile: {
    firstName: { type: String, trim: true, maxlength: 50 },
    lastName: { type: String, trim: true, maxlength: 50 },
    avatar: { type: String, default: '' },
    phone: { type: String, match: /^\+?[\d\s-()]+$/ },
    department: { type: String, trim: true, maxlength: 100 },
    position: { type: String, trim: true, maxlength: 100 }
  },
  preferences: {
    theme: { type: String, enum: ['light', 'dark'], default: 'light' },
    language: { type: String, enum: ['zh-CN', 'en-US'], default: 'zh-CN' },
    notifications: {
      email: { type: Boolean, default: true },
      push: { type: Boolean, default: true },
      classification: { type: Boolean, default: true }
    }
  },
  statistics: {
    totalClassifications: { type: Number, default: 0 },
    correctClassifications: { type: Number, default: 0 },
    accuracy: { type: Number, default: 0 },
    totalWasteWeight: { type: Number, default: 0 },
    carbonSaved: { type: Number, default: 0 }
  },
  status: {
    type: String,
    enum: ['active', 'inactive', 'suspended'],
    default: 'active'
  },
  lastLoginAt: { type: Date },
  loginAttempts: { type: Number, default: 0 },
  lockUntil: { type: Date },
  passwordResetToken: { type: String },
  passwordResetExpires: { type: Date },
  emailVerificationToken: { type: String },
  emailVerified: { type: Boolean, default: false },
  twoFactorSecret: { type: String },
  twoFactorEnabled: { type: Boolean, default: false }
}, {
  timestamps: true,
  toJSON: {
    transform: function(doc, ret) {
      delete ret.password;
      delete ret.passwordResetToken;
      delete ret.passwordResetExpires;
      delete ret.emailVerificationToken;
      delete ret.twoFactorSecret;
      return ret;
    }
  }
});

// Indexes
userSchema.index({ username: 1 });
userSchema.index({ email: 1 });
userSchema.index({ role: 1 });
userSchema.index({ status: 1 });
userSchema.index({ createdAt: 1 });

// Virtual for full name
userSchema.virtual('fullName').get(function() {
  return `${this.profile.firstName || ''} ${this.profile.lastName || ''}`.trim();
});

// Virtual for account lock status
userSchema.virtual('isLocked').get(function() {
  return !!(this.lockUntil && this.lockUntil > Date.now());
});

// Pre-save middleware for password hashing
userSchema.pre('save', async function(next) {
  if (!this.isModified('password')) return next();
  
  try {
    const salt = await bcrypt.genSalt(12);
    this.password = await bcrypt.hash(this.password, salt);
    next();
  } catch (error) {
    next(error);
  }
});

// Pre-save middleware for updating accuracy
userSchema.pre('save', function(next) {
  if (this.isModified('statistics.totalClassifications') || this.isModified('statistics.correctClassifications')) {
    if (this.statistics.totalClassifications > 0) {
      this.statistics.accuracy = (this.statistics.correctClassifications / this.statistics.totalClassifications) * 100;
    }
  }
  next();
});

// Instance methods
userSchema.methods.comparePassword = async function(candidatePassword) {
  return bcrypt.compare(candidatePassword, this.password);
};

userSchema.methods.incrementLoginAttempts = function() {
  if (this.lockUntil && this.lockUntil < Date.now()) {
    return this.updateOne({
      $unset: { loginAttempts: 1, lockUntil: 1 }
    });
  }
  
  const updates = { $inc: { loginAttempts: 1 } };
  
  if (this.loginAttempts + 1 >= 5 && !this.isLocked) {
    updates.$set = {
      lockUntil: Date.now() + 2 * 60 * 60 * 1000 // 2 hours
    };
  }
  
  return this.updateOne(updates);
};

userSchema.methods.resetLoginAttempts = function() {
  return this.updateOne({
    $unset: { loginAttempts: 1, lockUntil: 1 }
  });
};

userSchema.methods.generatePasswordResetToken = function() {
  const token = require('crypto').randomBytes(32).toString('hex');
  this.passwordResetToken = require('crypto').createHash('sha256').update(token).digest('hex');
  this.passwordResetExpires = Date.now() + 10 * 60 * 1000; // 10 minutes
  return token;
};

userSchema.methods.generateEmailVerificationToken = function() {
  const token = require('crypto').randomBytes(32).toString('hex');
  this.emailVerificationToken = require('crypto').createHash('sha256').update(token).digest('hex');
  return token;
};

// Static methods
userSchema.statics.findByCredentials = async function(identifier, password) {
  const user = await this.findOne({
    $or: [
      { username: identifier },
      { email: identifier }
    ]
  });
  
  if (!user) {
    throw new Error('Invalid credentials');
  }
  
  if (user.isLocked) {
    throw new Error('Account is locked due to too many failed login attempts');
  }
  
  const isPasswordMatch = await user.comparePassword(password);
  
  if (!isPasswordMatch) {
    await user.incrementLoginAttempts();
    throw new Error('Invalid credentials');
  }
  
  if (user.loginAttempts > 0) {
    await user.resetLoginAttempts();
  }
  
  user.lastLoginAt = new Date();
  await user.save();
  
  return user;
};

userSchema.statics.getStatistics = async function() {
  return this.aggregate([
    {
      $group: {
        _id: null,
        totalUsers: { $sum: 1 },
        activeUsers: {
          $sum: {
            $cond: [{ $eq: ['$status', 'active'] }, 1, 0]
          }
        },
        adminUsers: {
          $sum: {
            $cond: [{ $eq: ['$role', 'admin'] }, 1, 0]
          }
        },
        avgAccuracy: { $avg: '$statistics.accuracy' },
        totalClassifications: { $sum: '$statistics.totalClassifications' },
        totalCarbonSaved: { $sum: '$statistics.carbonSaved' }
      }
    }
  ]);
};

module.exports = mongoose.model('User', userSchema);