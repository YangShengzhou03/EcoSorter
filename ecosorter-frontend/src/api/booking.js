import request from '@/utils/request'

export const bookingApi = {
  getList(params) {
    return request({
      url: '/api/bookings',
      method: 'get',
      params
    })
  },

  create(data) {
    return request({
      url: '/api/bookings',
      method: 'post',
      data
    })
  },

  cancel(id) {
    return request({
      url: `/api/bookings/${id}/cancel`,
      method: 'post'
    })
  },

  getDetail(id) {
    return request({
      url: `/api/bookings/${id}`,
      method: 'get'
    })
  }
}
