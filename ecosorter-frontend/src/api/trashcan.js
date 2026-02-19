import request from '@/utils/request'

export const trashcanApi = {
  getNearby(params) {
    return request({
      url: '/api/trashcans/nearby',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/trashcans/${id}`,
      method: 'get'
    })
  },

  getByDeviceId(deviceId) {
    return request({
      url: `/api/trashcans/device/${deviceId}`,
      method: 'get'
    })
  }
}
