import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const isLoading = ref(false)

  const isAuthenticated = () => !!token.value

  const login = async (credentials) => {
    isLoading.value = true
    try {
      const response = await authApi.login(credentials)
      
      const userData = {
        id: response.user.id,
        username: response.user.username,
        email: response.user.email,
        role: response.user.role.toLowerCase(),
        name: response.user.username,
        avatar: response.user.profile?.avatar || null
      }
      
      token.value = response.accessToken
      userInfo.value = userData
      
      localStorage.setItem('token', response.accessToken)
      localStorage.setItem('userInfo', JSON.stringify(userData))
      
      ElMessage.success('登录成功')
      return { success: true, role: userData.role }
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '登录失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const register = async (data) => {
    isLoading.value = true
    try {
      const response = await authApi.register(data)
      
      const userData = {
        id: response.user.id,
        username: response.user.username,
        email: response.user.email,
        role: response.user.role.toLowerCase(),
        name: response.user.username,
        avatar: response.user.profile?.avatar || null
      }
      
      token.value = response.accessToken
      userInfo.value = userData
      
      localStorage.setItem('token', response.accessToken)
      localStorage.setItem('userInfo', JSON.stringify(userData))
      
      ElMessage.success('注册成功')
      return { success: true, role: userData.role }
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '注册失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      token.value = ''
      userInfo.value = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.success('登出成功')
    }
  }

  const getCurrentUser = async () => {
    try {
      const response = await authApi.getCurrentUser()
      const userData = {
        id: response.id,
        username: response.username,
        email: response.email,
        role: response.role.toLowerCase(),
        name: response.username,
        avatar: response.profile?.avatar || null
      }
      userInfo.value = userData
      localStorage.setItem('userInfo', JSON.stringify(userData))
      return userData
    } catch (error) {
      ElMessage.error('获取用户信息失败')
      throw error
    }
  }

  const updateUserInfo = (updates) => {
    userInfo.value = { ...userInfo.value, ...updates }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    token,
    userInfo,
    isLoading,
    isAuthenticated,
    login,
    register,
    logout,
    getCurrentUser,
    updateUserInfo
  }
})
