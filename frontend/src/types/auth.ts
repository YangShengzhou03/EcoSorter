export interface User {
  id: string
  username: string
  email: string
  role: UserRole
  avatar?: string
  phone?: string
  department?: string
  createdAt: string
  updatedAt: string
  lastLogin?: string
}

export interface LoginCredentials {
  username: string
  password: string
  rememberMe?: boolean
}

export interface AuthResponse {
  success: boolean
  user?: User
  token?: string
  refreshToken?: string
  error?: string
  message?: string
}

export interface AuthState {
  currentUser: User | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
}

export type UserRole = 'admin' | 'collector' | 'resident' | 'guest'

// 角色权限配置
export const ROLE_PERMISSIONS: Record<UserRole, string[]> = {
  admin: [
    'user:manage',
    'device:monitor', 
    'rule:configure',
    'data:analyze',
    'system:settings',
    'report:view',
    'waste:classify'
  ],
  collector: [
    'task:receive',
    'task:execute',
    'device:report',
    'route:plan',
    'statistics:view',
    'waste:classify'
  ],
  resident: [
    'waste:classify',
    'record:view',
    'points:manage',
    'appeal:create',
    'notification:receive',
    'profile:manage'
  ],
  guest: ['waste:classify']
}

// 角色显示名称
export const ROLE_DISPLAY_NAMES: Record<UserRole, string> = {
  admin: '系统管理员',
  collector: '垃圾收集员',
  resident: '居民用户',
  guest: '访客'
}

export interface Permission {
  resource: string
  action: ('create' | 'read' | 'update' | 'delete')[]
}

export interface UserProfile extends User {
  permissions: Permission[]
  preferences?: UserPreferences
}

export interface UserPreferences {
  theme: 'light' | 'dark' | 'auto'
  language: 'zh-CN' | 'en-US'
  notifications: {
    email: boolean
    push: boolean
    sms: boolean
  }
}

export interface RegisterData {
  username: string
  email: string
  password: string
  confirmPassword: string
  phone?: string
  department?: string
}

export interface PasswordResetData {
  email: string
}

export interface PasswordChangeData {
  currentPassword: string
  newPassword: string
  confirmPassword: string
}

export interface AuthContextType {
  user: User | null
  isLoading: boolean
  error: string | null
  login: (credentials: LoginCredentials) => Promise<boolean>
  logout: () => void
  register: (data: RegisterData) => Promise<boolean>
  resetPassword: (data: PasswordResetData) => Promise<boolean>
  changePassword: (data: PasswordChangeData) => Promise<boolean>
  clearError: () => void
}