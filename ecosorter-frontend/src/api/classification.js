import request from '@/utils/request'

export const classificationApi = {
  getClassificationHistory(params) {
    return request({
      url: '/api/classification/history',
      method: 'get',
      params
    })
  },

  getCategories() {
    return request({
      url: '/api/classification/categories',
      method: 'get'
    })
  },

  createCategory(data) {
    return request({
      url: '/api/classification/categories',
      method: 'post',
      data
    })
  },

  updateCategory(categoryId, data) {
    return request({
      url: `/api/classification/categories/${categoryId}`,
      method: 'put',
      data
    })
  },

  deleteCategory(categoryId) {
    return request({
      url: `/api/classification/categories/${categoryId}`,
      method: 'delete'
    })
  }
}
