import request from '@/utils/request'

export const bookingApi = {
  getList(params) {
    return request({
      url: '/api/bookings',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/bookings/${id}`,
      method: 'get'
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
  }
}
