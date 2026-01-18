import request from '@/utils/request'

export const binApi = {
  getBinStatus(deviceId) {
    return request({
      url: `/api/bin/status/${deviceId}`,
      method: 'get'
    })
  },

  updateBinStatus(deviceId, data) {
    return request({
      url: `/api/bin/status/${deviceId}`,
      method: 'put',
      data
    })
  }
}
