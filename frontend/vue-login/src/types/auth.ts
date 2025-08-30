export interface User {
  id: string
  username: string
  email: string
  role: UserRole
  avatar?: string
  createdAt: string
  updatedAt: string
}

export enum UserRole {
  ADMIN = 'admin',
  USER = 'user',
  SUPERVISOR = 'supervisor',
  GUEST = 'guest'
}

export interface LoginCredentials {
  username: string
  password: string
}

export interface AuthResponse {
  success: boolean
  message?: string
  data?: {
    token: string
    user: User
  }
}

export interface RegisterData {
  username: string
  email: string
  password: string
  confirmPassword: string
  role?: UserRole
}

export interface AuthState {
  user: User | null
  token: string | null
  isLoading: boolean
  error: string | null
}