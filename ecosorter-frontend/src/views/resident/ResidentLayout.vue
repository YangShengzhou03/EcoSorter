<template>
  <div class="resident-layout">
    <el-header class="layout-header">
      <div class="header-content">
        <div class="logo-section">
          <img src="@/assets/logo.svg" alt="logo" class="logo">
          <div class="system-info">
            <span class="system-name">智能垃圾分类</span>
            <span class="system-slogan">共建绿色家园</span>
          </div>
        </div>

        <div class="header-actions">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown">
              <el-avatar :size="36" :src="userInfo.avatar">
                <el-icon>
                  <User />
                </el-icon>
              </el-avatar>
              <span class="user-name">{{ userInfo.name }}</span>
              <el-icon class="dropdown-icon">
                <ArrowDown />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown-menu">
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <el-container class="layout-container">
      <el-aside width="200px" class="layout-aside">
        <el-menu :default-active="$route.path" router background-color="#1a1a2e" text-color="#a0a0b0"
          active-text-color="#00d4aa" class="aside-menu">
          <el-menu-item index="/resident/dashboard">
            <el-icon>
              <House />
            </el-icon>
            <template #title>首页</template>
          </el-menu-item>

          <el-menu-item index="/resident/records">
            <el-icon>
              <Document />
            </el-icon>
            <template #title>投放记录</template>
          </el-menu-item>

          <el-menu-item index="/resident/points">
            <el-icon>
              <Coin />
            </el-icon>
            <template #title>积分中心</template>
          </el-menu-item>

          <el-menu-item index="/resident/complaint">
            <el-icon>
              <WarningFilled />
            </el-icon>
            <template #title>我的申诉</template>
          </el-menu-item>
        </el-menu>

        <div class="aside-footer">
          <div class="eco-stats">
            <div class="eco-stat">
              <span class="eco-value">12.5</span>
              <span class="eco-unit">kg</span>
            </div>
            <div class="eco-label">本月减碳量</div>
          </div>
        </div>
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
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import {
  House,
  Document,
  Coin,
  WarningFilled,
  User
} from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人信息功能开发中')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        authStore.logout()
        router.push('/login')
        ElMessage.success('已安全退出')
      } catch { }
      break
  }
}
</script>

<style scoped>
.resident-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f2f5;
}

.layout-header {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  padding: 0;
  height: 64px !important;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 44px;
  height: 44px;
  border-radius: 10px;
}

.system-info {
  display: flex;
  flex-direction: column;
}

.system-name {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
}

.system-slogan {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 2px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 2px;
  transition: all 0.2s;
}

.user-dropdown:hover {
  background: rgba(255, 255, 255, 0.08);
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #fff;
}

.dropdown-icon {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  transition: transform 0.3s;
}

.user-dropdown:hover .dropdown-icon {
  transform: translateY(2px);
}

.layout-container {
  flex: 1;
  overflow: hidden;
}

.layout-aside {
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  border-right: none;
  display: flex;
  flex-direction: column;
  position: relative;
  transition: width 0.3s ease;
  overflow: hidden;
}

.aside-menu {
  flex: 1;
  border-right: none;
  padding: 16px 8px;
}

.aside-menu:not(.el-menu--collapse) {
  width: 100%;
}

.aside-menu .el-menu-item {
  margin: 2px 0;
  border-radius: 4px;
  height: 44px;
  line-height: 44px;
  transition: all 0.2s;
}

.aside-menu .el-menu-item:hover {
  background: rgba(0, 212, 170, 0.1) !important;
  color: #00d4aa !important;
}

.aside-menu .el-menu-item.is-active {
  background: rgba(0, 212, 170, 0.15) !important;
  color: #00d4aa !important;
}

.aside-menu .el-menu-item .el-icon {
  font-size: 18px;
}

.aside-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.eco-stats {
  background: linear-gradient(135deg, rgba(0, 212, 170, 0.2) 0%, rgba(0, 180, 216, 0.2) 100%);
  border-radius: 12px;
  padding: 12px;
  text-align: center;
}

.eco-stat {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
}

.eco-value {
  font-size: 20px;
  font-weight: 600;
  color: #00d4aa;
}

.eco-unit {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
}

.eco-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 2px;
}

.layout-main {
  background: #f0f2f5;
  padding: 12px;
  overflow: auto;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.2s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

:deep(.user-dropdown-menu) {
  min-width: 180px;
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

:deep(.user-dropdown-menu .el-dropdown-menu__item) {
  border-radius: 8px;
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

:deep(.user-dropdown-menu .el-dropdown-menu__item:hover) {
  background: #f5f7fa;
}

:deep(.user-dropdown-menu .el-dropdown-menu__item .el-icon) {
  font-size: 16px;
}

@media (max-width: 992px) {
  .layout-aside {
    width: 72px !important;
  }

  .system-info,
  .aside-footer {
    display: none;
  }

  .user-dropdown {
    padding: 8px;
  }

  .header-content {
    padding: 0 16px;
  }
}

@media (max-width: 768px) {
  .layout-header {
    height: 56px !important;
  }

  .layout-main {
    padding: 16px;
  }
}
</style>