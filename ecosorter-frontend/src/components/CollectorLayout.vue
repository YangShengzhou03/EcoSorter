<template>
  <el-container class="collector-layout">
    <el-aside width="200px" class="layout-aside">
      <div class="logo-container">
        <div class="logo-title">ECO-Sorter 收集端</div>
      </div>
      <el-menu :default-active="$route.path" class="el-menu-vertical" router>
        <el-menu-item index="/collector/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/collector/tasks">
          <el-icon><Memo /></el-icon>
          <span>我的任务</span>
        </el-menu-item>
        <el-menu-item index="/collector/device-status">
          <el-icon><Monitor /></el-icon>
          <span>设备状态</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="layout-container">
      <el-header class="layout-header">
        <el-breadcrumb class="breadcrumb" separator="/">
          <el-breadcrumb-item :to="{ path: '/collector/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentCategory">{{ currentCategory }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentPage">{{ currentPage }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="header-right">
          <el-badge :value="notificationCount" :hidden="notificationCount === 0" class="notification-badge">
            <el-button circle class="icon-button" @click="goToNotifications">
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown">
              <el-avatar :size="40" :src="userInfo.avatar || DEFAULT_AVATAR" />
              <span class="user-name">{{ userInfo.username || '收集员' }}</span>
              <el-icon class="dropdown-icon"><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  <span>个人资料</span>
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <component :is="Component" />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { DEFAULT_AVATAR, createBreadcrumbUpdater } from '@/utils/layout'

defineOptions({
  name: 'CollectorLayout'
})

import {
  User,
  ArrowDown,
  SwitchButton,
  DataLine,
  Memo,
  Monitor,
  Bell
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)
const notificationCount = ref(0)

const routeMap = {
  '/collector/dashboard': { category: '首页', page: '仪表盘' },
  '/collector/tasks': { category: '工作', page: '我的任务' },
  '/collector/device-status': { category: '工作', page: '设备状态' },
  '/collector/notifications': { category: '信息', page: '通知' },
  '/collector/profile': { category: '我的', page: '个人资料' }
}

const currentCategory = ref('')
const currentPage = ref('')

const updateBreadcrumb = createBreadcrumbUpdater(routeMap)

watch(() => route.path, () => {
  const result = updateBreadcrumb(route)
  currentCategory.value = result.category
  currentPage.value = result.page
}, { immediate: true })

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/collector/profile')
      break
    case 'logout':
      authStore.logout()
      router.push('/login')
      break
  }
}

const goToNotifications = () => {
  router.push('/collector/notifications')
}
</script>

<style scoped>
.collector-layout {
  height: 100vh;
  display: flex;
  background: #f0fdf4;
  overflow: hidden;
}

.layout-aside {
  background: #ffffff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.08);
  z-index: 10;
}

.logo-container {
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 16px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.logo-title {
  font-size: 16px;
  font-weight: 600;
  color: #059669;
  letter-spacing: 0.5px;
}

.layout-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.layout-header {
  height: 56px !important;
  background: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.08);
  flex-shrink: 0;
}

.breadcrumb {
  flex: 1;
  margin-right: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: auto;
}

.icon-button {
  width: 40px;
  height: 40px;
  border: none;
  background: transparent;
  color: #64748b;
  font-size: 18px;
  padding: 0;
}

.icon-button:hover {
  background: #ecfdf5;
  color: #059669;
}

.notification-badge :deep(.el-badge__content) {
  background: #ef4444;
  border: 2px solid #ffffff;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s;
}

.user-dropdown:hover {
  background: #ecfdf5;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

.dropdown-icon {
  color: #94a3b8;
  font-size: 14px;
}

.layout-main {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #f0fdf4;
}

.el-menu-vertical {
  border-right: none;
  background: transparent;
  padding: 4px 12px;
}

.el-menu-vertical :deep(.el-menu-item) {
  height: 44px;
  line-height: 44px;
  margin: 2px 0;
  border-radius: 4px;
  color: #4b5563;
  transition: all 0.2s;
}

.el-menu-vertical :deep(.el-menu-item:hover) {
  background: #ecfdf5;
  color: #059669;
}

.el-menu-vertical :deep(.el-menu-item.is-active) {
  background: #064e3b;
  color: #ffffff;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(6, 78, 59, 0.4);
}

.el-menu-vertical :deep(.el-menu-item .el-icon) {
  margin-right: 10px;
  font-size: 18px;
}
</style>
