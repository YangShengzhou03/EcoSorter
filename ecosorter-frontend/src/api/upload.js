import request from '@/utils/request'

export const uploadApi = {
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
  }
}
