import request from '@/utils/request'

export const pointApi = {
  getRecords() {
    return request({
      url: '/api/points/records',
      method: 'get'
    })
  },

  getRecordsPage(page = 0, size = 10) {
    return request({
      url: '/api/points/records/page',
      method: 'get',
      params: { page, size }
    })
  },

  getTotalPoints() {
    return request({
      url: '/api/points/total',
      method: 'get'
    })
  }
}
