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

  faceLoginWithFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return pythonRequest({
      url: '/api/face/verify-with-file',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': undefined
      }
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
        'Content-Type': undefined
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
  },

  getWasteCategories() {
    return javaRequest({
      url: '/api/classification/categories',
      method: 'get'
    })
  },

  createQRSession() {
    return javaRequest({
      url: '/api/qr-login/create',
      method: 'post'
    })
  },

  checkQRStatus(qrCode) {
    return javaRequest({
      url: `/api/qr-login/status/${qrCode}`,
      method: 'get'
    })
  },

  reportFault(data) {
    return javaRequest({
      url: '/api/trashcan/fault',
      method: 'post',
      data
    })
  }
}
