import request from '@/utils/request'

export const productApi = {
  getList(params) {
    return request({
      url: '/api/products',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/products/${id}`,
      method: 'get'
    })
  },

  create(data) {
    return request({
      url: '/api/products',
      method: 'post',
      data
    })
  },

  update(id, data) {
    return request({
      url: `/api/products/${id}`,
      method: 'put',
      data
    })
  },

  delete(id) {
    return request({
      url: `/api/products/${id}`,
      method: 'delete'
    })
  }
}
