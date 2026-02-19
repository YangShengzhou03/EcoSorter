<template>
  <div class="resident-dashboard">
    <el-carousel :interval="4000" height="300px" class="banner-carousel">
      <el-carousel-item v-for="banner in displayBanners" :key="banner.id">
        <div class="banner-item" :style="{ background: banner.background }">
          <div class="banner-content">
            <h2 class="banner-title">{{ banner.title }}</h2>
            <p class="banner-description">{{ banner.description }}</p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <el-card class="quick-access-card">
      <template #header>
        <div class="card-header">
          <span>常用功能</span>
        </div>
      </template>
      <div class="quick-access-grid">
        <div class="quick-access-item" @click="navigateTo('/resident/points')">
          <div class="quick-icon points-icon">
            <el-icon><Coin /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">积分商城</div>
            <div class="quick-desc">兑换商品</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/resident/orders')">
          <div class="quick-icon order-icon">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">我的订单</div>
            <div class="quick-desc">查看订单</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/resident/point-records')">
          <div class="quick-icon records-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">积分明细</div>
            <div class="quick-desc">查看积分记录</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/resident/notifications')">
          <div class="quick-icon notification-icon">
            <el-icon><Bell /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">通知消息</div>
            <div class="quick-desc">查看通知</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { bannerApi } from '@/api/banner'
import {
  Coin,
  ShoppingCart,
  Document,
  Bell
} from '@element-plus/icons-vue'

defineOptions({
  name: 'ResidentDashboard'
})

const router = useRouter()

const banners = ref([])

const defaultBanner = {
  id: 'default',
  title: '欢迎使用',
  description: '智能垃圾分类系统',
  background: '#000000'
}

const displayBanners = computed(() => {
  return banners.value.length > 0 ? banners.value : [defaultBanner]
})

const navigateTo = (path) => {
  router.push(path)
}

const loadBanners = async () => {
  try {
    const response = await bannerApi.getList('user')
    banners.value = response || []
  } catch (error) {
    ElMessage.error('加载轮播图失败')
  }
}

onMounted(() => {
  loadBanners()
})

onActivated(() => {
  loadBanners()
})
</script>

<style scoped>
.resident-dashboard {
  padding: 0;
}

.banner-carousel {
  margin-bottom: 20px;
}

.banner-carousel :deep(.el-carousel__item) {
  background-color: transparent !important;
  background-image: none !important;
  overflow: hidden;
}

.banner-carousel :deep(.el-carousel__item--card) {
  border-radius: 8px;
}

.banner-item {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.banner-content {
  text-align: center;
  color: white;
  padding: 20px;
  z-index: 1;
}

.banner-title {
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 12px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.banner-description {
  font-size: 20px;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.quick-access-card {
  margin-bottom: 20px;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.quick-access-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
}

.quick-access-item:hover {
  background: #eff6ff;
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.quick-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-radius: 8px;
}

.quick-icon .el-icon {
  font-size: 20px;
}

.points-icon {
  background: #fef3c7;
  color: #d97706;
}

.order-icon {
  background: #dbeafe;
  color: #2563eb;
}

.records-icon {
  background: #dcfce7;
  color: #15803d;
}

.notification-icon {
  background: #fef3c7;
  color: #d97706;
}

.quick-info {
  flex: 1;
}

.quick-title {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 3px;
}

.quick-desc {
  font-size: 11px;
  color: #64748b;
}
</style>
