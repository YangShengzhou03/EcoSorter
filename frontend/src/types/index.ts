export * from './auth'
export * from './waste'
export * from './common'

export interface ApiResponse<T = any> {
  success: boolean
  data?: T
  error?: string
  message?: string
  pagination?: PaginationMeta
}

export interface PaginationMeta {
  page: number
  limit: number
  total: number
  totalPages: number
  hasNext: boolean
  hasPrev: boolean
}

export interface ListResponse<T> {
  items: T[]
  meta: PaginationMeta
}

export interface ErrorResponse {
  code: string
  message: string
  details?: Record<string, string[]>
}

export type SortOrder = 'asc' | 'desc'

export interface SortOptions {
  field: string
  order: SortOrder
}

export interface FilterOptions {
  [key: string]: any
}

export interface QueryOptions {
  page?: number
  limit?: number
  sort?: SortOptions
  filter?: FilterOptions
  search?: string
}

export type ThemeMode = 'light' | 'dark' | 'auto'

export interface AppConfig {
  theme: ThemeMode
  language: string
  apiBaseUrl: string
  enableAnalytics: boolean
  enableNotifications: boolean
}