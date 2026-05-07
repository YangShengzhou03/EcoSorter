import request from '@/utils/request'

export const bannerApi = {
  getList(target) {
    return request({
      url: '/api/banners',
      method: 'get',
      params: target ? { target } : {}
    })
  },

  getById(id) {
    return request({
      url: `/api/banners/${id}`,
      method: 'get'
    })
  },

  create(data) {
    return request({
      url: '/api/banners',
      method: 'post',
      data
    })
  },

  update(id, data) {
    return request({
      url: `/api/banners/${id}`,
      method: 'put',
      data
    })
  },

  delete(id) {
    return request({
      url: `/api/banners/${id}`,
      method: 'delete'
    })
  }
}
