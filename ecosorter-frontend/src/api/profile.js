import request from '@/utils/request'

const PYTHON_API_URL = 'http://localhost:9000'

export const profileApi = {
  getProfile() {
    return request({
      url: '/api/profile',
      method: 'get'
    })
  },

  updateProfile(data) {
    return request({
      url: '/api/profile',
      method: 'put',
      data
    })
  },

  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/api/upload/avatar',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/api/upload/image',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  updateAvatar(avatarUrl) {
    return request({
      url: '/api/profile/avatar',
      method: 'put',
      data: { avatar: avatarUrl }
    })
  },

  registerFaceFromFile(file, userId) {
    const formData = new FormData()
    formData.append('file', file)
    return fetch(`${PYTHON_API_URL}/api/face/register-with-file?userId=${userId}`, {
      method: 'POST',
      body: formData
    }).then(res => res.json())
  }
}
