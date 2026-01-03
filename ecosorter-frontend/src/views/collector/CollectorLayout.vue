<template>
  <div class="collector-layout">
    <el-container class="layout-container">
      <el-header class="layout-header">
        <div class="header-content">
          <div class="logo-section">
            <img src="@/assets/logo.svg" alt="EcoSorter" class="logo" />
            <span class="logo-text">收集员端</span>
          </div>
          
          <div class="user-section">
            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-info">
                <el-avatar :size="32" :src="userInfo.avatar || defaultAvatar" />
                <span class="username">{{ userInfo.name }}</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-container class="layout-body">
        <el-aside width="200px" class="layout-aside">
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical"
            router
            background-color="#fff"
            text-color="#606266"
            active-text-color="#409EFF"
          >
            <el-menu-item index="/collector/dashboard">
              <el-icon><Monitor /></el-icon>
              <span>工作台</span>
            </el-menu-item>
            
            <el-menu-item index="/collector/tasks">
              <el-icon><List /></el-icon>
              <span>任务管理</span>
            </el-menu-item>
            
            <el-menu-item index="/collector/collection">
              <el-icon><Van /></el-icon>
              <span>清运作业</span>
            </el-menu-item>
            
            <el-menu-item index="/collector/records">
              <el-icon><Document /></el-icon>
              <span>工作记录</span>
            </el-menu-item>
            
            <el-menu-item index="/collector/device-status">
              <el-icon><Warning /></el-icon>
              <span>设备状态</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main class="layout-main">
          <router-view />
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
  background: #f5f7fa;
  overflow: hidden;
}

.layout-container {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.layout-header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0;
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

.logo {
  height: 32px;
}

.logo-text {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
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
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #606266;
}

.layout-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
  flex-shrink: 0;
  overflow-y: auto;
}

.el-menu-vertical {
  border-right: none;
  padding: 10px 0;
}

.el-menu-vertical .el-menu-item {
  height: 48px;
  line-height: 48px;
  margin: 2px 10px;
  border-radius: 4px;
}

.el-menu-vertical .el-menu-item:hover {
  background-color: #f5f7fa;
}

.el-menu-vertical .el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409EFF;
}

.el-menu-vertical .el-menu-item .el-icon {
  margin-right: 8px;
}

.layout-main {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
  height: 100%;
}
</style>