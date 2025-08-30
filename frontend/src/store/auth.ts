import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { authService } from '@/services/auth'
import { User, AuthState, LoginCredentials } from '@/types/auth'

interface AuthStore extends AuthState {
  login: (credentials: LoginCredentials) => Promise<{ success: boolean; error?: string }>
  logout: () => void
  setLoading: (loading: boolean) => void
  setError: (error: string | null) => void
  clearError: () => void
}

export const useAuthStore = create<AuthStore>()(
  persist(
    (set, get) => ({
      currentUser: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,

      login: async (credentials) => {
        set({ isLoading: true, error: null })

        try {
          const response = await authService.login(credentials)
          
          if (response.success && response.user) {
            set({
              currentUser: response.user,
              isAuthenticated: true,
              isLoading: false,
              error: null,
            })
            return { success: true }
          } else {
            set({
              isLoading: false,
              error: response.error || '登录失败',
            })
            return { success: false, error: response.error }
          }
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : '登录请求失败'
          set({
            isLoading: false,
            error: errorMessage,
          })
          return { success: false, error: errorMessage }
        }
      },

      logout: () => {
        // Clear any stored tokens or credentials
        localStorage.removeItem('auth-token')
        sessionStorage.removeItem('auth-session')
        
        set({
          currentUser: null,
          isAuthenticated: false,
          error: null,
        })
      },

      setLoading: (loading: boolean) => set({ isLoading: loading }),
      setError: (error: string | null) => set({ error }),
      clearError: () => set({ error: null }),
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        currentUser: state.currentUser,
        isAuthenticated: state.isAuthenticated,
      }),
    },
  ),
)