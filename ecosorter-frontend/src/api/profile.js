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

  changePassword(data) {
    return request({
      url: '/api/profile/change-password',
      method: 'post',
      data
    })
  }
}
