import javaRequest from '@/utils/request'
import { pythonRequest } from '@/utils/request'

export const trashcanApi = {
  login(data) {
    return javaRequest({
      url: '/api/auth/login',
      method: 'post',
      data
    })
  },

  register(data) {
    return javaRequest({
      url: '/api/auth/register',
      method: 'post',
      data
    })
  },

  getCurrentUser() {
    return javaRequest({
      url: '/api/auth/me',
      method: 'get'
    })
  },

  getTrashcanInfo() {
    return javaRequest({
      url: '/api/trashcan/me',
      method: 'get'
    })
  },

  updateStatus(data) {
    return javaRequest({
      url: '/api/trashcan/status',
      method: 'put',
      data
    })
  },

  submitClassification(data) {
    return javaRequest({
      url: '/api/trashcan/classification',
      method: 'post',
      data
    })
  },

  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return pythonRequest({
      url: '/api/upload',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getClassification(imageUrl) {
    return pythonRequest({
      url: '/api/recognition/recognize',
      method: 'post',
      params: { image_url: imageUrl }
    })
  },

  getBanners() {
    return javaRequest({
      url: '/api/banners',
      method: 'get',
      params: { target: 'trashcan' }
    })
  }
}
