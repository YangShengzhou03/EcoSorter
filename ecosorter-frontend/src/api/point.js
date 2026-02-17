import request from '@/utils/request'

export const pointApi = {
  getRecordsPage(page = 0, size = 10) {
    return request({
      url: '/api/points/records/page',
      method: 'get',
      params: { page, size }
    })
  }
}
