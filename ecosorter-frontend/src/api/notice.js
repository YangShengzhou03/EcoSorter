import request from '@/utils/request'

export const noticeApi = {
  getList(params) {
    return request({
      url: '/api/notices',
      method: 'get',
      params
    })
  },

  getPublished() {
    return request({
      url: '/api/notices/published',
      method: 'get'
    })
  },

  getUnreadCount() {
    return request({
      url: '/api/notices/unread/count',
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `/api/notices/${id}`,
      method: 'get'
    })
  },

  create(data) {
    return request({
      url: '/api/notices',
      method: 'post',
      data
    })
  },

  update(id, data) {
    return request({
      url: `/api/notices/${id}`,
      method: 'put',
      data
    })
  },

  delete(id) {
    return request({
      url: `/api/notices/${id}`,
      method: 'delete'
    })
  }
}
