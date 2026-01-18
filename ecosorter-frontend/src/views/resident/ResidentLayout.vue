<template>
  <div class="resident-layout">
    <el-container class="layout-container">
      <el-header class="layout-header">
        <div class="header-content">
          <div class="logo-section">
            <div class="logo">EcoSorter</div>
          </div>

          <div class="header-actions">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notification-badge">
              <el-button circle @click="goToNotifications" class="notification-btn">
                <el-icon>
                  <Bell />
                </el-icon>
              </el-button>
            </el-badge>

            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-dropdown">
                <el-avatar :size="36" :src="userInfo.avatar">
                  <el-icon>
                    <User />
                  </el-icon>
                </el-avatar>
                <span class="user-name">{{ userInfo.username || '居民' }}</span>
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

      <el-container class="layout-body">
        <el-aside width="200px" class="layout-aside">
          <el-menu :default-active="$route.path" router class="aside-menu">
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { noticeApi } from '@/api/notice'
import {
  House,
  Document,
  Coin,
  WarningFilled,
  Bell,
  User
} from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

const userInfo = computed(() => authStore.userInfo)
const unreadCount = ref(0)

const loadUnreadCount = async () => {
  try {
    const response = await noticeApi.getUnreadCount()
    unreadCount.value = response || 0
  } catch (error) {
    console.error('加载未读通知数量失败:', error)
  }
}

const goToNotifications = () => {
  router.push('/resident/notifications')
}

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/resident/profile')
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

onMounted(() => {
  loadUnreadCount()
})
</script>

<style scoped>
.resident-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-light);
}

.layout-header {
  padding: 0;
  height: 60px;
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-badge {
  margin-right: 8px;
}

.notification-btn {
  width: 36px;
  height: 36px;
  padding: 0;
  border: none;
  background: transparent;
  color: var(--text-secondary);
  transition: all 0.3s;
}

.notification-btn:hover {
  color: var(--primary-color);
  background: var(--bg-light);
}

.user-dropdown {
  font-size: 14px;
  color: var(--text-primary);
}

.dropdown-icon {
  color: var(--text-secondary);
  font-size: 12px;
}

.layout-container {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.layout-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.layout-aside {
  background-color: var(--el-menu-bg-color);
  flex-shrink: 0;
  overflow-y: auto;
}

.aside-menu {
  flex: 1;
  border-right: none;
  padding: 16px 8px;
}

.aside-menu .el-menu-item {
  margin: 2px 0;
  border-radius: 4px;
  height: 40px;
  line-height: 40px;
}

.layout-main {
  padding: 16px;
  overflow-y: auto;
  height: 100%;
}

.main-content {
  margin: 0 auto;
}

@media (max-width: 768px) {
  .layout-header {
    height: 56px;
  }

  .layout-main {
    padding: 16px;
  }
}
</style>
