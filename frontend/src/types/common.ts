export interface Coordinates {
  latitude: number
  longitude: number
}

export interface Address {
  street: string
  city: string
  state: string
  country: string
  postalCode: string
  coordinates?: Coordinates
}

export interface FileUpload {
  id: string
  name: string
  url: string
  size: number
  type: string
  uploadedAt: string
}

export interface ImageMetadata {
  width: number
  height: number
  format: string
  size: number
  dominantColor?: string
}

export interface SelectOption {
  value: string
  label: string
  disabled?: boolean
  icon?: React.ReactNode
}

export interface BreadcrumbItem {
  label: string
  href?: string
  icon?: React.ReactNode
}

export interface TabItem {
  id: string
  label: string
  icon?: React.ReactNode
  disabled?: boolean
  badge?: number | string
}

export interface Notification {
  id: string
  type: 'info' | 'success' | 'warning' | 'error'
  title: string
  message: string
  timestamp: string
  read: boolean
  action?: {
    label: string
    onClick: () => void
  }
}

export interface StatsCard {
  title: string
  value: string | number
  icon: React.ReactNode
  trend?: {
    value: number
    isPositive: boolean
  }
  subtitle?: string
}

export interface ChartData {
  label: string
  value: number
  color?: string
}

export interface DateRange {
  start: Date
  end: Date
}

export interface TimeRange {
  start: string
  end: string
}

export interface PaginationParams {
  page: number
  limit: number
  total: number
}

export interface SearchParams {
  query: string
  filters?: Record<string, any>
  sort?: string
  order?: 'asc' | 'desc'
}

export interface ValidationError {
  field: string
  message: string
  code?: string
}

export interface FormState<T> {
  data: T
  errors: Record<string, string>
  isDirty: boolean
  isValid: boolean
  isSubmitting: boolean
}

export interface ModalProps {
  isOpen: boolean
  onClose: () => void
  title?: string
  size?: 'sm' | 'md' | 'lg' | 'xl'
}

export interface DrawerProps {
  isOpen: boolean
  onClose: () => void
  title?: string
  position?: 'left' | 'right' | 'top' | 'bottom'
}

export interface ToastOptions {
  id?: string
  duration?: number
  position?: 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right'
  icon?: React.ReactNode
  className?: string
}

export interface LoadingState {
  isLoading: boolean
  error?: string | null
}

export interface OperationResult {
  success: boolean
  error?: string
  data?: any
}