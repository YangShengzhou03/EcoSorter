import request from '@/utils/request'

export const orderApi = {
  getList(params) {
    return request({
      url: '/api/orders',
      method: 'get',
      params
    })
  },

  getAllList(params) {
    return request({
      url: '/api/orders/all',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/orders/${id}`,
      method: 'get'
    })
  },

  create(data) {
    return request({
      url: '/api/orders',
      method: 'post',
      data
    })
  },

  updateStatus(id, status) {
    return request({
      url: `/api/orders/${id}/status`,
      method: 'put',
      data: status
    })
  }
}
