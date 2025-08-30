<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 导航栏 -->
    <nav class="bg-white shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-semibold text-gray-900">智能垃圾分类督导系统</h1>
          </div>
          
          <div class="flex items-center space-x-4">
            <span class="text-sm text-gray-700">欢迎, {{ authStore.currentUser?.username }}</span>
            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
              {{ authStore.currentUser?.role }}
            </span>
            <button
              @click="handleLogout"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            >
              退出登录
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主内容 -->
    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div class="px-4 py-6 sm:px-0">
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <h2 class="text-lg font-medium text-gray-900 mb-4">用户信息</h2>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <h3 class="text-sm font-medium text-gray-500">用户名</h3>
                <p class="mt-1 text-sm text-gray-900">{{ authStore.currentUser?.username }}</p>
              </div>
              
              <div>
                <h3 class="text-sm font-medium text-gray-500">邮箱</h3>
                <p class="mt-1 text-sm text-gray-900">{{ authStore.currentUser?.email }}</p>
              </div>
              
              <div>
                <h3 class="text-sm font-medium text-gray-500">用户角色</h3>
                <p class="mt-1 text-sm text-gray-900">{{ authStore.currentUser?.role }}</p>
              </div>
              
              <div>
                <h3 class="text-sm font-medium text-gray-500">注册时间</h3>
                <p class="mt-1 text-sm text-gray-900">{{ formatDate(authStore.currentUser?.createdAt) }}</p>
              </div>
            </div>
            
            <div class="mt-6">
              <h3 class="text-sm font-medium text-gray-500">权限说明</h3>
              <p class="mt-1 text-sm text-gray-900">
                {{ getRoleDescription(authStore.currentUser?.role) }}
              </p>
            </div>
          </div>
        </div>
        
        <!-- 功能卡片 -->
        <div class="mt-6 grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          <div 
            v-for="feature in availableFeatures" 
            :key="feature.id"
            class="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow"
          >
            <div class="p-6">
              <div class="flex items-center">
                <div class="flex-shrink-0">
                  <component :is="feature.icon" class="h-6 w-6 text-blue-500" />
                </div>
                <div class="ml-5 w-0 flex-1">
                  <dl>
                    <dt class="text-sm font-medium text-gray-500 truncate">
                      {{ feature.title }}
                    </dt>
                    <dd class="text-sm text-gray-900">
                      {{ feature.description }}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  ChartBarIcon,
  CameraIcon,
  DocumentTextIcon,
  UsersIcon,
  CogIcon,
  BellIcon
} from '@heroicons/vue/24/outline'

const router = useRouter()
const authStore = useAuthStore()

const formatDate = (dateString?: string) => {
  if (!dateString) return '未知'
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getRoleDescription = (role?: string) => {
  const descriptions = {
    admin: '系统管理员，拥有所有权限',
    supervisor: '督导员，负责垃圾分类监督和指导',
    user: '普通用户，可以进行垃圾分类投放',
    guest: '访客用户，权限受限'
  }
  return descriptions[role as keyof typeof descriptions] || '未知角色'
}

const availableFeatures = computed(() => {
  const baseFeatures = [
    {
      id: 1,
      title: '数据统计',
      description: '查看垃圾分类数据统计',
      icon: ChartBarIcon,
      roles: ['admin', 'supervisor']
    },
    {
      id: 2,
      title: '图像识别',
      description: '使用AI识别垃圾类型',
      icon: CameraIcon,
      roles: ['admin', 'supervisor', 'user']
    },
    {
      id: 3,
      title: '报告生成',
      description: '生成垃圾分类报告',
      icon: DocumentTextIcon,
      roles: ['admin', 'supervisor']
    }
  ]
  
  return baseFeatures.filter(feature => 
    feature.roles.includes(authStore.currentUser?.role || 'guest')
  )
})

const handleLogout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
/* 自定义样式 */
</style>