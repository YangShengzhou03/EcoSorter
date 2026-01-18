import request from '@/utils/request'

export const authApi = {
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

  logout() {
    return request({
      url: '/api/auth/logout',
      method: 'post'
    })
  },

  getCurrentUser() {
    return request({
      url: '/api/auth/me',
      method: 'get'
    })
  },

  refreshToken(refreshToken) {
    return request({
      url: '/api/auth/refresh',
      method: 'post',
      params: { refreshToken }
    })
  }
}
