import request from '@/utils/request'

export const classificationApi = {
  classifyWasteFromImage(formData) {
    return request({
      url: '/api/classification/classify-image',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getClassificationHistory(params) {
    return request({
      url: '/api/classification/history',
      method: 'get',
      params
    })
  },

  getClassificationById(id) {
    return request({
      url: `/api/classification/${id}`,
      method: 'get'
    })
  },

  submitFeedback(id, feedback, comment) {
    return request({
      url: `/api/classification/${id}/feedback`,
      method: 'put',
      params: { feedback, comment }
    })
  },

  getWasteCategories() {
    return request({
      url: '/api/classification/categories',
      method: 'get'
    })
  }
}
