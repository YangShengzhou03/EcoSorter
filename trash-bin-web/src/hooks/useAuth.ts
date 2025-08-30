import { useState, useCallback, useEffect } from 'react'

interface UserInfo {
  id: string
  name: string
  points: number
  avatar?: string
  level?: number
  lastCheckIn?: string
}

interface AuthState {
  isAuthenticated: boolean
  userInfo: UserInfo | null
  loading: boolean
  error: string | null
}

interface UseAuthReturn extends AuthState {
  login: (userId: string) => Promise<void>
  logout: () => void
  updatePoints: (points: number) => Promise<void>
  checkIn: () => Promise<void>
  refreshUserInfo: () => Promise<void>
}

// API基础URL（根据环境配置）
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000/api'

// 模拟用户数据（开发环境使用）
const MOCK_USERS: Record<string, UserInfo> = {
  'user001': {
    id: 'user001',
    name: '张三',
    points: 150,
    level: 2,
    lastCheckIn: new Date().toISOString()
  },
  'user002': {
    id: 'user002',
    name: '李四',
    points: 80,
    level: 1,
    lastCheckIn: new Date().toISOString()
  },
  'user003': {
    id: 'user003',
    name: '王五',
    points: 300,
    level: 3,
    lastCheckIn: new Date().toISOString()
  }
}

export const useAuth = (): UseAuthReturn => {
  const [state, setState] = useState<AuthState>({
    isAuthenticated: false,
    userInfo: null,
    loading: false,
    error: null
  })

  // API请求封装
  const apiRequest = useCallback(async <T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> => {
    try {
      const url = `${API_BASE_URL}${endpoint}`
      const response = await fetch(url, {
        headers: {
          'Content-Type': 'application/json',
          ...options.headers
        },
        ...options
      })

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('API请求失败:', error)
      throw error
    }
  }, [])

  // 用户登录
  const login = useCallback(async (userId: string): Promise<void> => {
    try {
      setState(prev => ({ ...prev, loading: true, error: null }))

      // 开发环境使用模拟数据
      if (import.meta.env.DEV) {
        const user = MOCK_USERS[userId] || {
          id: userId,
          name: `用户${userId}`,
          points: 0,
          level: 1
        }

        // 模拟网络延迟
        await new Promise(resolve => setTimeout(resolve, 500))

        setState({
          isAuthenticated: true,
          userInfo: user,
          loading: false,
          error: null
        })

        // 存储登录状态
        localStorage.setItem('auth_userId', userId)
        localStorage.setItem('auth_userInfo', JSON.stringify(user))
        return
      }

      // 生产环境调用真实API
      const userData = await apiRequest<UserInfo>(`/users/${userId}`)
      
      setState({
        isAuthenticated: true,
        userInfo: userData,
        loading: false,
        error: null
      })

      // 存储登录状态
      localStorage.setItem('auth_userId', userId)
      localStorage.setItem('auth_userInfo', JSON.stringify(userData))

    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '登录失败'
      setState(prev => ({
        ...prev,
        loading: false,
        error: errorMessage
      }))
      throw error
    }
  }, [apiRequest])

  // 用户登出
  const logout = useCallback((): void => {
    setState({
      isAuthenticated: false,
      userInfo: null,
      loading: false,
      error: null
    })

    // 清除本地存储
    localStorage.removeItem('auth_userId')
    localStorage.removeItem('auth_userInfo')
  }, [])

  // 更新用户积分
  const updatePoints = useCallback(async (points: number): Promise<void> => {
    if (!state.userInfo) {
      throw new Error('用户未登录')
    }

    try {
      // 开发环境模拟
      if (import.meta.env.DEV) {
        const updatedUser = {
          ...state.userInfo,
          points: state.userInfo.points + points
        }

        setState(prev => ({
          ...prev,
          userInfo: updatedUser
        }))

        localStorage.setItem('auth_userInfo', JSON.stringify(updatedUser))
        return
      }

      // 生产环境调用API
      const updatedUser = await apiRequest<UserInfo>(
        `/users/${state.userInfo.id}/points`,
        {
          method: 'POST',
          body: JSON.stringify({ points })
        }
      )

      setState(prev => ({
        ...prev,
        userInfo: updatedUser
      }))

      localStorage.setItem('auth_userInfo', JSON.stringify(updatedUser))

    } catch (error) {
      console.error('积分更新失败:', error)
      throw error
    }
  }, [state.userInfo, apiRequest])

  // 每日签到
  const checkIn = useCallback(async (): Promise<void> => {
    if (!state.userInfo) {
      throw new Error('用户未登录')
    }

    try {
      // 开发环境模拟
      if (import.meta.env.DEV) {
        const today = new Date().toISOString().split('T')[0]
        if (state.userInfo.lastCheckIn?.startsWith(today)) {
          throw new Error('今日已签到')
        }

        const updatedUser = {
          ...state.userInfo,
          points: state.userInfo.points + 10,
          lastCheckIn: new Date().toISOString()
        }

        setState(prev => ({
          ...prev,
          userInfo: updatedUser
        }))

        localStorage.setItem('auth_userInfo', JSON.stringify(updatedUser))
        return
      }

      // 生产环境调用API
      const updatedUser = await apiRequest<UserInfo>(
        `/users/${state.userInfo.id}/checkin`,
        {
          method: 'POST'
        }
      )

      setState(prev => ({
        ...prev,
        userInfo: updatedUser
      }))

      localStorage.setItem('auth_userInfo', JSON.stringify(updatedUser))

    } catch (error) {
      console.error('签到失败:', error)
      throw error
    }
  }, [state.userInfo, apiRequest])

  // 刷新用户信息
  const refreshUserInfo = useCallback(async (): Promise<void> => {
    if (!state.userInfo) {
      return
    }

    try {
      setState(prev => ({ ...prev, loading: true }))

      // 开发环境使用模拟数据
      if (import.meta.env.DEV) {
        await new Promise(resolve => setTimeout(resolve, 300))
        const storedInfo = localStorage.getItem('auth_userInfo')
        if (storedInfo) {
          setState(prev => ({
            ...prev,
            userInfo: JSON.parse(storedInfo),
            loading: false
          }))
        }
        return
      }

      // 生产环境调用API
      const userData = await apiRequest<UserInfo>(`/users/${state.userInfo.id}`)
      
      setState(prev => ({
        ...prev,
        userInfo: userData,
        loading: false
      }))

      localStorage.setItem('auth_userInfo', JSON.stringify(userData))

    } catch (error) {
      console.error('用户信息刷新失败:', error)
      setState(prev => ({ ...prev, loading: false }))
    }
  }, [state.userInfo, apiRequest])

  // 自动恢复登录状态
  useEffect(() => {
    const initializeAuth = async () => {
      const userId = localStorage.getItem('auth_userId')
      const userInfoStr = localStorage.getItem('auth_userInfo')

      if (userId && userInfoStr) {
        try {
          const userInfo = JSON.parse(userInfoStr)
          setState({
            isAuthenticated: true,
            userInfo,
            loading: false,
            error: null
          })

          // 刷新用户信息
          await refreshUserInfo()
        } catch (error) {
          console.error('登录状态恢复失败:', error)
          logout()
        }
      }
    }

    initializeAuth()
  }, [refreshUserInfo, logout])

  // 定期同步用户数据
  useEffect(() => {
    if (!state.isAuthenticated) return

    const syncInterval = setInterval(() => {
      refreshUserInfo()
    }, 30000) // 每30秒同步一次

    return () => clearInterval(syncInterval)
  }, [state.isAuthenticated, refreshUserInfo])

  return {
    ...state,
    login,
    logout,
    updatePoints,
    checkIn,
    refreshUserInfo
  }
}