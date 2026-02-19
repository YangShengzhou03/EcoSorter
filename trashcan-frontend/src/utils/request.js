import axios from 'axios'

const javaApiBaseURL = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081'
const pythonApiBaseURL = process.env.VUE_APP_PYTHON_API_URL || 'http://localhost:9000'

const createAxiosInstance = (baseURL) => axios.create({
  baseURL,
  timeout: parseInt(process.env.VUE_APP_REQUEST_TIMEOUT) || 30000,
  headers: {
    'Content-Type': 'application/json; charset=UTF-8',
    'Accept': 'application/json; charset=UTF-8'
  },
  responseType: 'json',
  responseEncoding: 'utf8'
})

const javaRequest = createAxiosInstance(javaApiBaseURL)
const pythonRequest = createAxiosInstance(pythonApiBaseURL)

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
      
      const deviceToken = localStorage.getItem('token')
      const userToken = sessionStorage.getItem('userToken')
      const token = userToken || deviceToken
      
      if (token && !config.url.includes('/auth/login') && !config.url.includes('/auth/register') && !config.url.includes('/auth/device/activate')) {
        config.headers.Authorization = `Bearer ${token}`
      }
      
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
        const isDeviceToken = localStorage.getItem('token') && !sessionStorage.getItem('userToken')
        
        switch (status) {
          case 401:
          case 403:
            if (isDeviceToken) {
              localStorage.removeItem('token')
              localStorage.removeItem('deviceInitialized')
              localStorage.removeItem('deviceInfo')
              window.location.href = '/init'
            } else {
              sessionStorage.removeItem('userToken')
              sessionStorage.removeItem('user')
            }
            break
        }
      }
      
      return Promise.reject(error)
    }
  )
}

setupInterceptors(javaRequest)
setupInterceptors(pythonRequest)

export default javaRequest
export { pythonRequest }
