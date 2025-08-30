import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器 - 添加认证token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理认证错误
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('auth_token')
      localStorage.removeItem('user')
      window.location.href = '/'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  // 用户登录
  login: async (username: string, password: string) => {
    const response = await apiClient.post('/auth/login', {
      username,
      password,
    })
    return response.data
  },

  // 验证token
  verifyToken: async (token: string) => {
    const response = await apiClient.get('/auth/verify', {
      headers: { Authorization: `Bearer ${token}` },
    })
    return response.data
  },

  // 获取用户信息
  getUserInfo: async (userId: string) => {
    const response = await apiClient.get(`/users/${userId}`)
    return response.data
  },

  // 注册新用户
  register: async (userData: any) => {
    const response = await apiClient.post('/auth/register', userData)
    return response.data
  },
}

export default apiClient