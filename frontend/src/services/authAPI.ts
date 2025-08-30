import { api } from './api';
import { LoginCredentials, AuthResponse, User, RegisterData } from '@/types/auth';

class AuthAPI {
  /**
   * 用户登录
   */
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', credentials)
    return response.data
  }

  /**
   * 用户注册
   */
  async register(userData: RegisterData): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/register', userData)
    return response.data
  }

  /**
   * 用户登出
   */
  async logout(): Promise<void> {
    await api.post('/auth/logout')
  }

  /**
   * 获取当前用户信息
   */
  async getCurrentUser(): Promise<User | null> {
    try {
      const response = await api.get<AuthResponse>('/auth/me')
      return response.data.success && response.data.user ? response.data.user : null
    } catch (error) {
      console.warn('Failed to fetch current user:', error)
      return null
    }
  }

  /**
   * 刷新访问令牌
   */
  async refreshToken(): Promise<AuthResponse> {
    try {
      const response = await api.post<AuthResponse>('/auth/refresh')
      return response.data
    } catch (error) {
      console.warn('Token refresh failed:', error)
      return {
        success: false,
        error: 'Token refresh failed'
      }
    }
  }

  /**
   * 发送密码重置邮件
   */
  async forgotPassword(email: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/forgot-password', { email })
    return response.data
  }

  /**
   * 重置密码
   */
  async resetPassword(token: string, newPassword: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/reset-password', {
      token,
      newPassword
    })
    return response.data
  }

  /**
   * 验证邮箱
   */
  async verifyEmail(token: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/verify-email', { token })
    return response.data
  }

  /**
   * 重新发送验证邮件
   */
  async resendVerificationEmail(email: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/resend-verification', { email })
    return response.data
  }

  /**
   * 检查用户名是否可用
   */
  async checkUsernameAvailability(username: string): Promise<{ available: boolean }> {
    const response = await api.get<{ available: boolean }>(`/auth/check-username?username=${encodeURIComponent(username)}`)
    return response.data
  }

  /**
   * 检查邮箱是否可用
   */
  async checkEmailAvailability(email: string): Promise<{ available: boolean }> {
    const response = await api.get<{ available: boolean }>(`/auth/check-email?email=${encodeURIComponent(email)}`)
    return response.data
  }
}

export const authAPI = new AuthAPI()
export default authAPI