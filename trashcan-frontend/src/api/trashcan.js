import javaRequest from '@/utils/request'
import { pythonRequest } from '@/utils/request'

export const trashcanApi = {
  activateDevice(data) {
    return javaRequest({
      url: '/api/auth/device/activate',
      method: 'post',
      data
    })
  },

  getTrashcanInfo() {
    return javaRequest({
      url: '/api/trashcan/me',
      method: 'get'
    })
  },

  updateTrashcanInfo(data) {
    return javaRequest({
      url: '/api/trashcan/me',
      method: 'put',
      data
    })
  },

  updateTrashcanStatus(data) {
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
    return javaRequest({
      url: '/api/upload/image',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  faceLoginWithFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return javaRequest({
      url: '/api/auth/face-login-with-file',
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

  getClassificationWithFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return pythonRequest({
      url: '/api/recognition/recognize-with-file',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getBanners() {
    return javaRequest({
      url: '/api/banners',
      method: 'get',
      params: { target: 'trashcan' }
    })
  },

  adminLogin(password) {
    return javaRequest({
      url: '/api/trashcan/admin-login',
      method: 'post',
      data: {
        password: password
      }
    })
  },

  resetAdminPassword(newPassword) {
    return javaRequest({
      url: '/api/trashcan/reset-password',
      method: 'post',
      data: {
        newPassword: newPassword
      }
    })
  },

  clearDeviceData() {
    return javaRequest({
      url: '/api/trashcan/clear-data',
      method: 'post'
    })
  },

  heartbeat() {
    return javaRequest({
      url: '/api/trashcan/heartbeat',
      method: 'post'
    })
  }
}
