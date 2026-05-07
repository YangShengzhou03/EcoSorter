import request from '@/utils/request'

export const pointApi = {
  getRecordsPage(page = 1, pageSize = 10) {
    return request({
      url: '/api/points/records/page',
      method: 'get',
      params: { page, pageSize }
    })
  }
}
