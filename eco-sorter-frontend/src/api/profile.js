import request from '@/utils/request'

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
      data: formData
    })
  },

  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/api/upload/image',
      method: 'post',
      data: formData
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
    return request({
      url: `/python/api/face/register-with-file?userId=${userId}`,
      method: 'post',
      data: formData
    })
  }
}
