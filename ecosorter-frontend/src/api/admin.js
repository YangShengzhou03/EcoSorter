import request from '@/utils/request'

export const adminApi = {
  getDashboard() {
    return request({
      url: '/api/admin/dashboard',
      method: 'get'
    })
  },

  getDevices() {
    return request({
      url: '/api/admin/devices',
      method: 'get'
    })
  },

  createDevice(data) {
    return request({
      url: '/api/admin/devices',
      method: 'post',
      data
    })
  },

  updateDevice(deviceId, data) {
    return request({
      url: `/api/admin/devices/${deviceId}`,
      method: 'put',
      data
    })
  },

  deleteDevice(deviceId) {
    return request({
      url: `/api/admin/devices/${deviceId}`,
      method: 'delete'
    })
  },

  getBanners() {
    return request({
      url: '/api/admin/banners',
      method: 'get'
    })
  },

  createBanner(data) {
    return request({
      url: '/api/admin/banners',
      method: 'post',
      data
    })
  },

  updateBanner(id, data) {
    return request({
      url: `/api/admin/banners/${id}`,
      method: 'put',
      data
    })
  },

  deleteBanner(id) {
    return request({
      url: `/api/admin/banners/${id}`,
      method: 'delete'
    })
  },

  getCategories() {
    return request({
      url: '/api/admin/categories',
      method: 'get'
    })
  },

  createCategory(data) {
    return request({
      url: '/api/admin/categories',
      method: 'post',
      data
    })
  },

  updateCategory(id, data) {
    return request({
      url: `/api/admin/categories/${id}`,
      method: 'put',
      data
    })
  },

  deleteCategory(id) {
    return request({
      url: `/api/admin/categories/${id}`,
      method: 'delete'
    })
  },

  getDeviceStatus() {
    return request({
      url: '/api/admin/device-status',
      method: 'get'
    })
  },

  getActivities() {
    return request({
      url: '/api/admin/activities',
      method: 'get'
    })
  },

  getUsers() {
    return request({
      url: '/api/admin/users',
      method: 'get'
    })
  },

  createUser(data) {
    return request({
      url: '/api/admin/users',
      method: 'post',
      data
    })
  },

  updateUser(userId, data) {
    return request({
      url: `/api/admin/users/${userId}`,
      method: 'put',
      data
    })
  },

  deleteUser(userId) {
    return request({
      url: `/api/admin/users/${userId}`,
      method: 'delete'
    })
  },

  adjustUserPoints(userId, data) {
    return request({
      url: `/api/admin/users/${userId}/points`,
      method: 'put',
      data
    })
  },

  getLogs() {
    return request({
      url: '/api/admin/logs',
      method: 'get'
    })
  },

  getReports() {
    return request({
      url: '/api/admin/reports',
      method: 'get'
    })
  },

  getConfiguration() {
    return request({
      url: '/api/admin/configuration',
      method: 'get'
    })
  },

  updateConfiguration(data) {
    return request({
      url: '/api/admin/configuration',
      method: 'put',
      data
    })
  }
}
