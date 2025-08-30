import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '@/store/auth';
import { UserRole } from '@/types/auth';

const RoleBasedRedirect: React.FC = () => {
  const navigate = useNavigate()
  const { currentUser: user } = useAuthStore()

  useEffect(() => {
    if (user) {
      switch (user.role as UserRole) {
        case 'admin':
          navigate('/admin', { replace: true })
          break
        case 'collector':
          navigate('/collector', { replace: true })
          break
        case 'resident':
          navigate('/resident', { replace: true })
          break
        default:
          navigate('/resident', { replace: true })
          break
      }
    }
  }, [user, navigate])

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
        <p className="text-gray-600">正在重定向到您的控制面板...</p>
      </div>
    </div>
  )
}

export default RoleBasedRedirect