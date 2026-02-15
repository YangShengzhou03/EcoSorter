<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="layout-aside">
      <div class="logo-container">
        <div class="logo-title">ECO-Sorter 管理后台</div>
      </div>
      <el-menu :default-active="$route.path" class="el-menu-vertical" router>
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>监控面板</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/task-management">
          <el-icon><Memo /></el-icon>
          <span>任务管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/devices">
          <el-icon><Setting /></el-icon>
          <span>设备管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><Tickets /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/notifications">
          <el-icon><Bell /></el-icon>
          <span>通知管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/banners">
          <el-icon><Picture /></el-icon>
          <span>轮播图管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><Grid /></el-icon>
          <span>垃圾分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/complaints">
          <el-icon><Warning /></el-icon>
          <span>申诉管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/reports">
          <el-icon><TrendCharts /></el-icon>
          <span>报表统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="layout-container">
      <el-header class="layout-header">
        <el-breadcrumb class="breadcrumb" separator="/">
          <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentCategory">{{ currentCategory }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentPage">{{ currentPage }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="header-right">
          <el-button circle class="icon-button" @click="goToNotifications">
            <el-icon><Bell /></el-icon>
          </el-button>
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown">
              <el-avatar :size="40" :src="userInfo.avatar || DEFAULT_AVATAR" />
              <span class="user-name">{{ userInfo.username || '管理员' }}</span>
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
  name: 'AdminLayout'
})

import {
  User,
  Bell,
  ArrowDown,
  Picture,
  Grid,
  TrendCharts,
  SwitchButton,
  DataLine,
  UserFilled,
  Memo,
  Goods,
  Tickets,
  Setting,
  Warning
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)

const routeMap = {
  '/admin/dashboard': { category: '数据监控', page: '监控面板' },
  '/admin/reports': { category: '数据监控', page: '报表统计' },
  '/admin/users': { category: '用户管理', page: '用户管理' },
  '/admin/task-management': { category: '任务管理', page: '任务管理' },
  '/admin/notifications': { category: '任务管理', page: '通知管理' },
  '/admin/banners': { category: '内容管理', page: '轮播图管理' },
  '/admin/categories': { category: '内容管理', page: '垃圾分类管理' },
  '/admin/complaints': { category: '内容管理', page: '申诉管理' },
  '/admin/products': { category: '商城管理', page: '商品管理' },
  '/admin/orders': { category: '商城管理', page: '订单管理' },
  '/admin/devices': { category: '设备管理', page: '设备管理' },
  '/admin/profile': { category: '个人中心', page: '个人资料' }
}

const currentCategory = ref('')
const currentPage = ref('')

const updateBreadcrumb = createBreadcrumbUpdater(routeMap)

watch(() => route.path, () => {
  const result = updateBreadcrumb(route)
  currentCategory.value = result.category
  currentPage.value = result.page
}, { immediate: true })

const goToNotifications = () => {
  router.push('/admin/notifications')
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/admin/profile')
      break
    case 'logout':
      authStore.logout()
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  background: #f8fafc;
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
  color: #1e3a8a;
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
  box-shadow: 0 2px 8px rgba(30, 58, 138, 0.08);
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
  background: #eff6ff;
  color: #3b82f6;
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
  background: #eff6ff;
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
  background: #f8fafc;
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
  background: #eff6ff;
  color: #1e3a8a;
}

.el-menu-vertical :deep(.el-menu-item.is-active) {
  background: #0f172a;
  color: #ffffff;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(15, 23, 42, 0.4);
}

.el-menu-vertical :deep(.el-menu-item .el-icon) {
  margin-right: 10px;
  font-size: 18px;
}
</style>
