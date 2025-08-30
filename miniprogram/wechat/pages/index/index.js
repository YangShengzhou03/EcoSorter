// pages/index/index.js
Page({
  data: {
    currentDate: '',
    stats: {
      todayCount: 0,
      totalCount: 0,
      points: 0
    }
  },

  onLoad() {
    this.setCurrentDate()
    this.loadStatistics()
  },

  onShow() {
    // 页面显示时刷新数据
    this.loadStatistics()
  },

  setCurrentDate() {
    const date = new Date()
    const year = date.getFullYear()
    const month = date.getMonth() + 1
    const day = date.getDate()
    this.setData({
      currentDate: `${year}年${month}月${day}日`
    })
  },

  loadStatistics() {
    // 模拟加载统计数据
    const stats = wx.getStorageSync('userStats') || {
      todayCount: 12,
      totalCount: 156,
      points: 480
    }
    this.setData({ stats })
  },

  navigateToRecognition() {
    wx.navigateTo({
      url: '/pages/recognition/recognition'
    })
  },

  navigateToReward() {
    wx.navigateTo({
      url: '/pages/reward/reward'
    })
  },

  navigateToGuide() {
    wx.showModal({
      title: '功能开发中',
      content: '分类指南功能即将上线，敬请期待！',
      showCancel: false
    })
  },

  navigateToStatistics() {
    wx.showModal({
      title: '功能开发中',
      content: '数据统计功能即将上线，敬请期待！',
      showCancel: false
    })
  },

  startQuickRecognition() {
    wx.navigateTo({
      url: '/pages/recognition/recognition'
    })
  },

  onShareAppMessage() {
    return {
      title: '智能垃圾分类，共建绿色家园',
      path: '/pages/index/index',
      imageUrl: '/images/share-banner.jpg'
    }
  },

  onShareTimeline() {
    return {
      title: '智能垃圾分类，共建绿色家园',
      imageUrl: '/images/share-banner.jpg'
    }
  }
})