import React, { Suspense } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from '@/store/auth';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';
import { ProtectedRoute } from '@/components/auth/ProtectedRoute';
import { PublicRoute } from '@/components/auth/PublicRoute';
import RoleBasedRedirect from '@/components/auth/RoleBasedRedirect';

// Lazy load pages for better performance
const LoginPage = React.lazy(() => import('@/pages/auth/LoginPage'));
const ResidentDashboard = React.lazy(() => import('@/pages/resident/ResidentDashboard'));
const CollectorDashboard = React.lazy(() => import('@/pages/collector/CollectorDashboard'));
const AdminDashboard = React.lazy(() => import('@/pages/admin/AdminDashboard'));
const WasteClassificationPage = React.lazy(() => import('@/pages/garbage/WasteClassificationPage'));
const StatisticsPage = React.lazy(() => import('@/pages/statistics/StatisticsPage'));
const SettingsPage = React.lazy(() => import('@/pages/profile/SettingsPage'));
const NotFoundPage = React.lazy(() => import('@/pages/error/NotFoundPage'));

function App() {
  const { isAuthenticated, isLoading } = useAuthStore()

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Suspense
        fallback={
          <div className="min-h-screen flex items-center justify-center">
            <LoadingSpinner size="lg" />
          </div>
        }
      >
        <Routes>
          {/* Public routes */}
          <Route
            path="/login"
            element={
              <PublicRoute isAuthenticated={isAuthenticated}>
                <LoginPage />
              </PublicRoute>
            }
          />

          {/* Protected routes with role-based redirection */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <RoleBasedRedirect />
              </ProtectedRoute>
            }
          />
          
          {/* Role-specific dashboards */}
          <Route
            path="/resident"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <ResidentDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/collector"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <CollectorDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/waste-classification"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <WasteClassificationPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/statistics"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <StatisticsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/settings"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated}>
                <SettingsPage />
              </ProtectedRoute>
            }
          />

          {/* Redirect root to dashboard */}
          <Route
            path="/"
            element={
              <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />
            }
          />

          {/* 404 page */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Suspense>
    </div>
  )
}

export default App