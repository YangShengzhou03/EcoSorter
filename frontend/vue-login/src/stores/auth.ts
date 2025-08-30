import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { User } from '@/types/auth';
import { authAPI } from '@/services/api';

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null);
  const token = ref<string | null>(localStorage.getItem('auth_token'));
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  const isAuthenticated = computed(() => !!token.value);
  const currentUser = computed(() => user.value);

  const login = async (username: string, password: string) => {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await authAPI.login(username, password);
      
      if (response.success) {
        token.value = response.data.token;
        user.value = response.data.user;
        
        // 存储token到localStorage
        localStorage.setItem('auth_token', token.value || '');
        localStorage.setItem('user', JSON.stringify(user.value));
        
        return { success: true };
      } else {
        error.value = response.message;
        return { success: false, message: response.message };
      }
    } catch (err: any) {
      error.value = err.message || '登录失败';
      return { success: false, message: err.message };
    } finally {
      isLoading.value = false;
    }
  };

  const logout = () => {
    token.value = null;
    user.value = null;
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
  };

  const checkAuth = async () => {
    if (token.value) {
      try {
        const response = await authAPI.verifyToken(token.value);
        if (response.success) {
          user.value = response.data.user;
          return true;
        }
      } catch {
        logout();
      }
    }
    return false;
  };

  // 初始化时检查认证状态
  const init = async () => {
    if (token.value) {
      await checkAuth();
    }
  };

  return {
    user,
    token,
    isLoading,
    error,
    isAuthenticated,
    currentUser,
    login,
    logout,
    checkAuth,
    init
  }
})