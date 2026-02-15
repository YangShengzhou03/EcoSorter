import request from '@/utils/request'

export const trashcanApi = {
  login(data) {
    return request({
      url: '/api/auth/login',
      method: 'post',
      data
    })
  },

  register(data) {
    return request({
      url: '/api/auth/register',
      method: 'post',
      data
    })
  },

  getTrashcanInfo() {
    return request({
      url: '/api/trashcan/me',
      method: 'get'
    })
  },

  updateStatus(data) {
    return request({
      url: '/api/trashcan/status',
      method: 'put',
      data
    })
  },

  submitClassification(data) {
    return request({
      url: '/api/recognition/classification',
      method: 'post',
      data,
      baseURL: 'http://localhost:9000'
    })
  },

  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/api/upload',
      method: 'post',
      data: formData,
      baseURL: 'http://localhost:9000',
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getClassification(imageUrl) {
    return request({
      url: '/api/recognition/recognize',
      method: 'post',
      data: { image_url: imageUrl },
      baseURL: 'http://localhost:9000'
    })
  },

  getBanners() {
    return request({
      url: '/api/banners',
      method: 'get',
      params: { target: 'trashcan' }
    })
  }
}
