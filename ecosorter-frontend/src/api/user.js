import request from '@/utils/request'

export const userApi = {
  getStatistics() {
    return request({
      url: '/api/user/statistics',
      method: 'get'
    })
  }
}
