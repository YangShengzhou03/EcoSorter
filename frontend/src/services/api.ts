import axios from 'axios';
import { toast } from 'react-hot-toast';

// Create axios instance with default config
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth-token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // Unauthorized - clear auth data and redirect to login
          localStorage.removeItem('auth-token')
          sessionStorage.removeItem('auth-session')
          delete api.defaults.headers.Authorization
          
          if (window.location.pathname !== '/login') {
            window.location.href = '/login'
            toast.error('会话已过期，请重新登录')
          }
          break
          
        case 403:
          toast.error('权限不足，无法访问该资源')
          break
          
        case 404:
          toast.error('请求的资源不存在')
          break
          
        case 429:
          toast.error('请求过于频繁，请稍后再试')
          break
          
        case 500:
          toast.error('服务器内部错误，请稍后再试')
          break
          
        default:
          if (data?.message) {
            toast.error(data.message)
          } else {
            toast.error('网络请求失败')
          }
      }
    } else if (error.request) {
      // Network error
      toast.error('网络连接错误，请检查网络连接')
    } else {
      // Other error
      toast.error('请求配置错误')
    }
    
    return Promise.reject(error)
  },
)

// API methods
export const apiMethods = {
  get: api.get,
  post: api.post,
  put: api.put,
  patch: api.patch,
  delete: api.delete,
}

// Helper functions
export const handleApiError = (error: any): string => {
  if (error.response?.data?.message) {
    return error.response.data.message
  }
  if (error.message) {
    return error.message
  }
  return '未知错误'
}

export const isNetworkError = (error: any): boolean => {
  return !error.response
}

export const isServerError = (error: any): boolean => {
  return error.response?.status >= 500
}

export const isClientError = (error: any): boolean => {
  return error.response?.status >= 400 && error.response?.status < 500
}

export { api }
export default api