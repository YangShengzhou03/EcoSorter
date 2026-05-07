import request from '@/utils/request'

export const collectorApi = {
  getDashboard() {
    return request({
      url: '/api/collector/dashboard',
      method: 'get'
    })
  },

  getTasks(params) {
    return request({
      url: '/api/collector/tasks',
      method: 'get',
      params
    })
  },

  getTaskDetail(taskId) {
    return request({
      url: `/api/collector/tasks/${taskId}`,
      method: 'get'
    })
  },

  startTask(taskId) {
    return request({
      url: `/api/collector/tasks/${taskId}/start`,
      method: 'post'
    })
  },

  completeTask(taskId) {
    return request({
      url: `/api/collector/tasks/${taskId}/complete`,
      method: 'post'
    })
  },

  reportException(taskId, data) {
    return request({
      url: `/api/collector/tasks/${taskId}/exception`,
      method: 'post',
      data
    })
  },

  getDevices() {
    return request({
      url: '/api/collector/devices',
      method: 'get'
    })
  },

  getStatistics() {
    return request({
      url: '/api/collector/statistics',
      method: 'get'
    })
  },

  getPointRecords(params) {
    return request({
      url: '/api/collector/point-records',
      method: 'get',
      params
    })
  },

  getOrders(params) {
    return request({
      url: '/api/collector/orders',
      method: 'get',
      params
    })
  },

  createOrder(data) {
    return request({
      url: '/api/collector/orders',
      method: 'post',
      data
    })
  },

  getOrderDetail(orderId) {
    return request({
      url: `/api/collector/orders/${orderId}`,
      method: 'get'
    })
  },

  cancelOrder(orderId) {
    return request({
      url: `/api/collector/orders/${orderId}/cancel`,
      method: 'post'
    })
  }
}
