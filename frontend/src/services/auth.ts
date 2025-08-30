import { api } from './api'
import { LoginCredentials, AuthResponse, User } from '@/types/auth'

class AuthService {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      const response = await api.post<AuthResponse>('/auth/login', credentials)
      
      if (response.data.success) {
        // Store token if available
        if (response.data.token) {
          localStorage.setItem('auth-token', response.data.token)
          api.defaults.headers.Authorization = `Bearer ${response.data.token}`
        }
        
        // Store user session data
        if (response.data.user) {
          sessionStorage.setItem('auth-session', JSON.stringify(response.data.user))
        }
      }
      
      return response.data
    } catch (error: any) {
      if (error.response?.data) {
        return {
          success: false,
          error: error.response.data.message || '登录失败',
        }
      }
      
      return {
        success: false,
        error: error.message || '网络连接错误',
      }
    }
  }

  async logout(): Promise<void> {
    try {
      await api.post('/auth/logout')
    } catch (error) {
      console.warn('Logout API call failed:', error)
    } finally {
      // Always clear local storage regardless of API call success
      localStorage.removeItem('auth-token')
      sessionStorage.removeItem('auth-session')
      delete api.defaults.headers.Authorization
    }
  }

  async getCurrentUser(): Promise<User | null> {
    try {
      const response = await api.get<AuthResponse>('/auth/me')
      
      if (response.data.success && response.data.user) {
        return response.data.user
      }
      
      return null
    } catch (error) {
      console.warn('Failed to fetch current user:', error)
      return null
    }
  }

  async refreshToken(): Promise<string | null> {
    try {
      const response = await api.post<{ token: string }>('/auth/refresh')
      
      if (response.data.token) {
        localStorage.setItem('auth-token', response.data.token)
        api.defaults.headers.Authorization = `Bearer ${response.data.token}`
        return response.data.token
      }
      
      return null
    } catch (error) {
      console.warn('Token refresh failed:', error)
      return null
    }
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('auth-token')
    const session = sessionStorage.getItem('auth-session')
    return !!(token && session)
  }

  getStoredUser(): User | null {
    const session = sessionStorage.getItem('auth-session')
    if (session) {
      try {
        return JSON.parse(session)
      } catch (error) {
        console.warn('Failed to parse stored user session:', error)
        return null
      }
    }
    return null
  }
}

export const authService = new AuthService()