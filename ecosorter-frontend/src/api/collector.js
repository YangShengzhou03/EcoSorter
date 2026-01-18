import request from '@/utils/request'

export const collectorApi = {
  getDashboard() {
    return request({
      url: '/api/collector/dashboard',
      method: 'get'
    })
  },

  getTasks() {
    return request({
      url: '/api/collector/tasks',
      method: 'get'
    })
  },

  getCollectionRecords() {
    return request({
      url: '/api/collector/collection-records',
      method: 'get'
    })
  }
}
