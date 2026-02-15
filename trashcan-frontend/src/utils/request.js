import axios from 'axios'

const javaApiBaseURL = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081'
const pythonApiBaseURL = process.env.VUE_APP_PYTHON_API_URL || 'http://localhost:9000'

const javaRequest = axios.create({
  baseURL: javaApiBaseURL,
  timeout: parseInt(process.env.VUE_APP_REQUEST_TIMEOUT) || 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

const pythonRequest = axios.create({
  baseURL: pythonApiBaseURL,
  timeout: parseInt(process.env.VUE_APP_REQUEST_TIMEOUT) || 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

const pendingRequests = new Map()

const generateRequestKey = (config) => {
  return `${config.method}-${config.url}-${JSON.stringify(config.params)}-${JSON.stringify(config.data)}`
}

const setupInterceptors = (axiosInstance) => {
  axiosInstance.interceptors.request.use(
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

  axiosInstance.interceptors.response.use(
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
            localStorage.removeItem('deviceInitialized')
            localStorage.removeItem('deviceInfo')
            window.location.href = '/init'
            break
          case 403:
            console.error('权限不足:', error.response?.data?.message || 'Forbidden')
            break
          case 404:
            console.error('资源不存在:', error.response?.data?.message || 'Not Found')
            break
          case 500:
            console.error('服务器错误:', error.response?.data?.message || 'Internal Server Error')
            break
          default:
            console.error('请求错误:', error.response?.data?.message || error.message)
        }
      } else if (error.request) {
        console.error('网络错误: 请检查网络连接')
      } else {
        console.error('请求配置错误:', error.message)
      }
      
      return Promise.reject(error)
    }
  )
}

setupInterceptors(javaRequest)
setupInterceptors(pythonRequest)

export default javaRequest
export { pythonRequest }
