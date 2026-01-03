<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px" class="admin-sidebar">
        <div class="logo">
          <img src="@/assets/logo.svg" alt="EcoSorter" />
          <span>管理系统</span>
        </div>
        <el-menu
          :default-active="$route.path"
          class="admin-menu"
          background-color="#ffffff"
          text-color="#303133"
          active-text-color="#10b981"
          router
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Monitor /></el-icon>
            <span>监控面板</span>
          </el-menu-item>
          <el-menu-item index="/admin/configuration">
            <el-icon><Setting /></el-icon>
            <span>系统配置</span>
          </el-menu-item>
          <el-menu-item index="/admin/reports">
            <el-icon><Document /></el-icon>
            <span>报表统计</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/devices">
            <el-icon><Monitor /></el-icon>
            <span>设备管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/logs">
            <el-icon><Document /></el-icon>
            <span>系统日志</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="admin-header">
          <div class="header-left">
            <h2>{{ getPageTitle() }}</h2>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-icon><User /></el-icon>
                {{ userInfo?.username || '管理员' }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="admin-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import {
  Monitor,
  Setting,
  Document,
  User
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)

const getPageTitle = () => {
  const titles = {
    '/admin/dashboard': '监控面板',
    '/admin/configuration': '系统配置',
    '/admin/reports': '报表统计',
    '/admin/users': '用户管理',
    '/admin/devices': '设备管理',
    '/admin/logs': '系统日志'
  }
  return titles[route.path] || '管理系统'
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人资料功能开发中')
      break
    case 'logout':
      authStore.logout()
      router.push('/login')
      ElMessage.success('退出登录成功')
      break
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  overflow: hidden;
}

.admin-layout :deep(.el-container) {
  height: 100vh;
}

.admin-layout :deep(.el-aside) {
  height: 100vh;
  overflow-y: auto;
}

.admin-layout :deep(.el-container > .el-container) {
  height: 100vh;
  overflow: hidden;
}

.admin-layout :deep(.el-header) {
  height: 56px;
}

.admin-layout :deep(.el-main) {
  height: calc(100vh - 56px);
  overflow-y: auto;
}

.admin-sidebar {
  background-color: #ffffff;
  color: #303133;
  border-right: 1px solid #e8eaed;
}

.logo {
  display: flex;
  align-items: center;
  padding: 13px;
  border-bottom: 1px solid #e8eaed;
}

.logo img {
  width: 28px;
  height: 28px;
  margin-right: 8px;
}

.logo span {
  font-size: 16px;
  font-weight: 600;
  color: #10b981;
}

.admin-menu {
  border: none;
}

.admin-header {
  background-color: #ffffff;
  border-bottom: 1px solid #e8eaed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
}

.header-left h2 {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-info .el-icon {
  margin-right: 8px;
}

.admin-main {
  background-color: #f5f7fa;
  padding: 16px;
}
</style>