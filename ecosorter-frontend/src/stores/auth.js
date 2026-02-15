import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api/auth'

const USER_STORAGE_KEY = 'userInfo'
const TOKEN_STORAGE_KEY = 'token'

const normalizeUserData = (user) => ({
  id: user.id,
  username: user.username,
  email: user.email,
  role: user.role || 'RESIDENT',
  name: user.username,
  avatar: user.profile?.avatar || null
})

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem(TOKEN_STORAGE_KEY) || '')
  const userInfo = ref(JSON.parse(localStorage.getItem(USER_STORAGE_KEY) || '{}'))
  const isLoading = ref(false)

  const isAuthenticated = computed(() => !!token.value)
  const userRole = computed(() => userInfo.value.role || 'RESIDENT')
  const userName = computed(() => userInfo.value.name || userInfo.value.username || '')
  const userAvatar = computed(() => userInfo.value.avatar || '')

  const setAuthData = (authData) => {
    const userData = normalizeUserData(authData.user)
    
    token.value = authData.accessToken
    userInfo.value = userData
    
    localStorage.setItem(TOKEN_STORAGE_KEY, authData.accessToken)
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userData))
  }

  const clearAuthData = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem(TOKEN_STORAGE_KEY)
    localStorage.removeItem(USER_STORAGE_KEY)
  }

  const handleAuthSuccess = (response, action) => {
    setAuthData(response)
    ElMessage.success(`${action}成功`)
    return { success: true, role: userInfo.value.role }
  }

  const handleAuthError = (error, action) => {
    const message = error.response?.data?.message || `${action}失败`
    ElMessage.error(message)
    throw error
  }

  const login = async (credentials) => {
    isLoading.value = true
    try {
      const response = await authApi.login(credentials)
      return handleAuthSuccess(response, '登录')
    } catch (error) {
      return handleAuthError(error, '登录')
    } finally {
      isLoading.value = false
    }
  }

  const register = async (data) => {
    isLoading.value = true
    try {
      const response = await authApi.register(data)
      return handleAuthSuccess(response, '注册')
    } catch (error) {
      return handleAuthError(error, '注册')
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    try {
      await authApi.logout()
    } finally {
      clearAuthData()
      ElMessage.success('登出成功')
    }
  }

  const getCurrentUser = async () => {
    try {
      const response = await authApi.getCurrentUser()
      const userData = normalizeUserData(response)
      userInfo.value = userData
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userData))
      return userData
    } catch (error) {
      ElMessage.error('获取用户信息失败')
      throw error
    }
  }

  const updateUserInfo = (updates) => {
    userInfo.value = { ...userInfo.value, ...updates }
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userInfo.value))
  }

  const refreshToken = async () => {
    try {
      const response = await authApi.refreshToken(token.value)
      setAuthData(response)
      return response.accessToken
    } catch (error) {
      clearAuthData()
      throw error
    }
  }

  return {
    token,
    userInfo,
    isLoading,
    isAuthenticated,
    userRole,
    userName,
    userAvatar,
    login,
    register,
    logout,
    getCurrentUser,
    updateUserInfo,
    refreshToken
  }
})
