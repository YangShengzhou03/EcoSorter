<template>
  <div class="collector-layout">
    <el-container class="layout-container">
      <el-header class="layout-header">
        <div class="header-content">
          <div class="logo-section">
            <div class="logo">EcoSorter</div>
            <span class="logo-text">收集员端</span>
          </div>

          <div class="user-section">
            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-info">
                <el-avatar :size="32" :src="userInfo.avatar || defaultAvatar" />
                <span class="username">{{ userInfo.username || '收集员' }}</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-container class="layout-body">
        <el-aside width="200px" class="layout-aside">
          <el-menu :default-active="$route.path" class="el-menu-vertical" router>
            <el-menu-item index="/collector/dashboard">
              <el-icon>
                <Monitor />
              </el-icon>
              <span>工作台</span>
            </el-menu-item>

            <el-menu-item index="/collector/tasks">
              <el-icon>
                <List />
              </el-icon>
              <span>任务管理</span>
            </el-menu-item>

            <el-menu-item index="/collector/collection">
              <el-icon>
                <Van />
              </el-icon>
              <span>清运作业</span>
            </el-menu-item>

            <el-menu-item index="/collector/records">
              <el-icon>
                <Document />
              </el-icon>
              <span>工作记录</span>
            </el-menu-item>

            <el-menu-item index="/collector/device-status">
              <el-icon>
                <Warning />
              </el-icon>
              <span>设备状态</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main class="layout-main">
          <div class="main-content">
            <router-view v-slot="{ Component }">
              <transition name="fade-slide" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  Monitor,
  List,
  Van,
  Document,
  Warning,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

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
</script>

<style scoped>
.collector-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-light);
  overflow: hidden;
}

.layout-container {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.layout-header {
  height: 60px !important;
  flex-shrink: 0;
}

.layout-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-text {
  font-size: 14px;
  color: var(--text-secondary);
}

.user-section {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
}

.user-info:hover {
  background-color: var(--bg-light);
}

.username {
  font-size: 14px;
  color: var(--text-primary);
}

.layout-aside {
  background-color: var(--el-menu-bg-color);
  flex-shrink: 0;
  overflow-y: auto;
}

.el-menu-vertical {
  border-right: none;
  padding: 10px 0;
}

.el-menu-vertical .el-menu-item {
  height: 44px;
  line-height: 44px;
  margin: 2px 10px;
  border-radius: 4px;
}

.el-menu-vertical .el-menu-item .el-icon {
  margin-right: 8px;
}

.layout-main {
  padding: 16px;
  overflow-y: auto;
  height: 100%;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
