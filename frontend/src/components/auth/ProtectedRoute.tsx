import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '@/store/auth';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';

interface ProtectedRouteProps {
  children: React.ReactNode
  isAuthenticated: boolean
  requiredRole?: string[]
  fallbackPath?: string
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  children,
  isAuthenticated,
  requiredRole,
  fallbackPath = '/login',
}) => {
  const { isLoading } = useAuthStore()
  const location = useLocation()

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" text="验证中..." />
      </div>
    )
  }

  if (!isAuthenticated) {
    // Redirect to login page with return url
    return (
      <Navigate
        to={fallbackPath}
        state={{ from: location }}
        replace
      />
    )
  }

  // Check role-based access if required
  if (requiredRole) {
    const { currentUser } = useAuthStore()
    const hasRequiredRole = currentUser && requiredRole.includes(currentUser.role)
    
    if (!hasRequiredRole) {
      return (
        <div className="min-h-screen flex items-center justify-center">
          <div className="text-center">
            <div className="text-6xl mb-4">🚫</div>
            <h1 className="text-2xl font-bold text-gray-900 mb-2">
              权限不足
            </h1>
            <p className="text-gray-600 mb-6">
              您没有访问此页面的权限
            </p>
            <button
              onClick={() => window.history.back()}
              className="btn btn-primary"
            >
              返回上一页
            </button>
          </div>
        </div>
      )
    }
  }

  return <>{children}</>
}

// Higher-order component for role-based protection
export const withRole = (requiredRole: string[]) => {
  return function WithRoleComponent<P extends object>(
    WrappedComponent: React.ComponentType<P>,
  ) {
    return function WithRole(props: P) {
      const { currentUser } = useAuthStore()
      
      if (!currentUser || !requiredRole.includes(currentUser.role)) {
        return (
          <div className="min-h-screen flex items-center justify-center">
            <div className="text-center">
              <div className="text-6xl mb-4">🚫</div>
              <h1 className="text-2xl font-bold text-gray-900 mb-2">
                权限不足
              </h1>
              <p className="text-gray-600">
                您需要特定角色才能访问此功能
              </p>
            </div>
          </div>
        )
      }
      
      return <WrappedComponent {...props} />
    }
  }
}

// Component for checking multiple permissions
export const PermissionGuard: React.FC<{
  children: React.ReactNode
  permissions: string[]
  fallback?: React.ReactNode
}> = ({ children, fallback }) => {
  
  // Mock permission check - replace with actual permission logic
  const hasPermission = true // Implement actual permission check logic
  
  if (!hasPermission) {
    return fallback ? (
      <>{fallback}</>
    ) : (
      <div className="p-4 text-center text-gray-500">
        无权限访问此内容
      </div>
    )
  }
  
  return <>{children}</>
}