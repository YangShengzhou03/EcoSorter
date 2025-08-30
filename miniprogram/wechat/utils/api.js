// utils/api.js
const BASE_URL = 'https://api.eco-sorter.com'

/**
 * 网络请求
 */
function request(url, method = 'GET', data = {}, header = {}) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: BASE_URL + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': wx.getStorageSync('token') || '',
        ...header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res.data)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

/**
 * 垃圾识别API
 */
function recognizeGarbage(imagePath) {
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: BASE_URL + '/api/garbage/recognize',
      filePath: imagePath,
      name: 'image',
      header: {
        'Authorization': wx.getStorageSync('token') || ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(JSON.parse(res.data))
        } else {
          reject(JSON.parse(res.data))
        }
      },
      fail: reject
    })
  })
}

/**
 * 用户登录
 */
function login(code) {
  return request('/api/auth/login', 'POST', { code })
}

/**
 * 获取用户信息
 */
function getUserInfo() {
  return request('/api/user/info')
}

/**
 * 获取用户统计
 */
function getUserStats() {
  return request('/api/user/stats')
}

/**
 * 获取识别历史
 */
function getRecognitionHistory(page = 1, limit = 20) {
  return request(`/api/recognition/history?page=${page}&limit=${limit}`)
}

/**
 * 获取积分奖励
 */
function getRewards() {
  return request('/api/rewards/list')
}

/**
 * 兑换奖励
 */
function exchangeReward(rewardId) {
  return request('/api/rewards/exchange', 'POST', { rewardId })
}

module.exports = {
  request,
  recognizeGarbage,
  login,
  getUserInfo,
  getUserStats,
  getRecognitionHistory,
  getRewards,
  exchangeReward
}