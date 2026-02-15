import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081',
  timeout: parseInt(process.env.VUE_APP_REQUEST_TIMEOUT) || 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

const pendingRequests = new Map()

const generateRequestKey = (config) => {
  return `${config.method}-${config.url}-${JSON.stringify(config.params)}-${JSON.stringify(config.data)}`
}

request.interceptors.request.use(
  config => {
    const requestKey = generateRequestKey(config)
    
    if (pendingRequests.has(requestKey)) {
      const cancel = pendingRequests.get(requestKey)
      cancel('取消重复请求')
    }
    
    config.cancelToken = new axios.CancelToken((cancel) => {
      pendingRequests.set(requestKey, cancel)
    })
    
    const token = localStorage.getItem('token')
    if (token && !config.url.includes('/auth/login') && !config.url.includes('/auth/register')) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    config.metadata = { startTime: new Date() }
    
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const requestKey = generateRequestKey(response.config)
    pendingRequests.delete(requestKey)
    
    return response.data
  },
  async error => {
    const requestKey = generateRequestKey(error.config || {})
    pendingRequests.delete(requestKey)
    
    if (axios.isCancel(error)) {
      return Promise.reject(error)
    }
    
    if (error.response) {
      const { status } = error.response
      
      switch (status) {
        case 401:
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/login')
          break
      }
    }
    
    return Promise.reject(error)
  }
)

export default request
