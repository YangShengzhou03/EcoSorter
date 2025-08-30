import React from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuthStore } from '@/store/auth'
import { LoadingSpinner } from '@/components/ui/LoadingSpinner'

interface PublicRouteProps {
  children: React.ReactNode
  isAuthenticated: boolean
  redirectPath?: string
}

export const PublicRoute: React.FC<PublicRouteProps> = ({
  children,
  isAuthenticated,
  redirectPath = '/dashboard',
}) => {
  const { isLoading } = useAuthStore()
  const location = useLocation()

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" text="加载中..." />
      </div>
    )
  }

  if (isAuthenticated) {
    // Redirect authenticated users away from public pages
    const from = location.state?.from?.pathname || redirectPath
    return <Navigate to={from} replace />
  }

  return <>{children}</>
}

// Component to restrict access during maintenance
export const MaintenanceRoute: React.FC<{
  children: React.ReactNode
  isUnderMaintenance: boolean
}> = ({ children, isUnderMaintenance }) => {
  if (isUnderMaintenance) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="max-w-md w-full space-y-8 p-8">
          <div className="text-center">
            <div className="mx-auto h-12 w-12 text-yellow-500">
              <svg className="w-full h-full" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                  clipRule="evenodd"
                />
              </svg>
            </div>
            <h2 className="mt-6 text-3xl font-bold text-gray-900">系统维护中</h2>
            <p className="mt-2 text-sm text-gray-600">
              我们正在对系统进行维护升级，请稍后再访问。
            </p>
            <p className="mt-1 text-xs text-gray-500">
              预计恢复时间: 2024-01-01 12:00
            </p>
          </div>
        </div>
      </div>
    )
  }

  return <>{children}</>
}

// Component for feature flag based routing
export const FeatureRoute: React.FC<{
  children: React.ReactNode
  feature: string
  enabled: boolean
  fallback?: React.ReactNode
}> = ({ children, feature, enabled, fallback }) => {
  if (!enabled) {
    return fallback ? (
      <>{fallback}</>
    ) : (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="text-6xl mb-4">🔒</div>
          <h1 className="text-2xl font-bold text-gray-900 mb-2">
            功能暂未开放
          </h1>
          <p className="text-gray-600">
            {feature} 功能正在开发中，敬请期待
          </p>
        </div>
      </div>
    )
  }

  return <>{children}</>
}

// Component for browser compatibility check
export const BrowserCompatibilityRoute: React.FC<{
  children: React.ReactNode
  supportedBrowsers?: string[]
}> = ({ children, supportedBrowsers = ['chrome', 'firefox', 'safari', 'edge'] }) => {
  const [isCompatible, setIsCompatible] = React.useState(true)

  React.useEffect(() => {
    // Simple browser detection
    const userAgent = navigator.userAgent.toLowerCase()
    const isSupported = supportedBrowsers.some(browser =>
      userAgent.includes(browser)
    )
    
    setIsCompatible(isSupported)
  }, [supportedBrowsers])

  if (!isCompatible) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="max-w-md w-full space-y-8 p-8">
          <div className="text-center">
            <div className="mx-auto h-12 w-12 text-red-500">
              <svg className="w-full h-full" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                  clipRule="evenodd"
                />
              </svg>
            </div>
            <h2 className="mt-6 text-3xl font-bold text-gray-900">浏览器不兼容</h2>
            <p className="mt-2 text-sm text-gray-600">
              您当前使用的浏览器可能不完全支持本系统的所有功能。
            </p>
            <div className="mt-4 space-y-2">
              <p className="text-xs text-gray-500">推荐使用以下浏览器：</p>
              <ul className="text-xs text-gray-500 space-y-1">
                <li>• Google Chrome (最新版本)</li>
                <li>• Mozilla Firefox (最新版本)</li>
                <li>• Microsoft Edge (最新版本)</li>
                <li>• Apple Safari (最新版本)</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    )
  }

  return <>{children}</>
}