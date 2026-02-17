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

  resetAdminPassword(deviceId) {
    return request({
      url: `/api/admin/devices/${deviceId}/reset-admin-password`,
      method: 'post'
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

  getReports() {
    return request({
      url: '/api/admin/reports',
      method: 'get'
    })
  }
}
