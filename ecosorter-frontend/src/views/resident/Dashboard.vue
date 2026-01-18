<template>
  <div class="page-container">
    <el-carousel :interval="5000" arrow="hover" height="200px" class="banner-carousel">
      <el-carousel-item v-for="(item, index) in banners" :key="index">
        <div class="carousel-item">
          <div class="carousel-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon count-icon">
          <el-icon>
            <List />
          </el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalCount }}</span>
          <span class="stat-label">累计投放次数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon weight-icon">
          <el-icon>
            <Box />
          </el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalWeight }}kg</span>
          <span class="stat-label">累计回收重量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon points-icon">
          <el-icon>
            <Coin />
          </el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalPoints }}</span>
          <span class="stat-label">可用积分</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon carbon-icon">
          <el-icon>
            <Food />
          </el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ carbonSaved }}kg</span>
          <span class="stat-label">减少碳排放</span>
        </div>
      </div>
    </div>

    <div class="notice-section" v-if="notices.length > 0">
      <div class="notice-header">
        <el-icon>
          <Bell />
        </el-icon>
        <span>最新通知</span>
        <el-link type="primary" :underline="false" @click="goToNotifications" class="view-all-link">
          查看全部 <el-icon>
            <ArrowRight />
          </el-icon>
        </el-link>
      </div>
      <div class="notice-list">
        <div class="notice-item" v-for="notice in notices" :key="notice.id" @click="viewNotice(notice)">
          <div class="notice-content">
            <div class="notice-title">{{ notice.title }}</div>
            <div class="notice-time">{{ formatDate(notice.createdAt) }}</div>
          </div>
          <el-icon class="notice-arrow">
            <ArrowRight />
          </el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Coin, Box, Food, List, Bell, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api/user'
import { noticeApi } from '@/api/notice'
import { bannerApi } from '@/api/banner'

const router = useRouter()

const totalPoints = ref(0)
const totalCount = ref(0)
const totalWeight = ref(0)
const carbonSaved = ref(0)

const banners = ref([])
const notices = ref([])

const loadStatistics = async () => {
  try {
    const response = await userApi.getStatistics()
    totalPoints.value = response.totalPoints || 0
    totalCount.value = response.totalCount || 0
    totalWeight.value = response.totalWeight || 0
    carbonSaved.value = response.carbonSaved || 0
  } catch (error) {
    console.error('加载统计数据失败:', error)
    totalPoints.value = 0
    totalCount.value = 0
    totalWeight.value = 0
    carbonSaved.value = 0
  }
}

const loadBanners = async () => {
  try {
    const response = await bannerApi.getList('user')
    banners.value = response || []
  } catch (error) {
    console.error('加载轮播图失败:', error)
    banners.value = []
  }
}

const loadNotices = async () => {
  try {
    const response = await noticeApi.getPublished()
    notices.value = response || []
  } catch (error) {
    console.error('加载通知失败:', error)
    notices.value = []
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    if (hours === 0) {
      const minutes = Math.floor(diff / (1000 * 60))
      return minutes < 1 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  }
}

const viewNotice = (notice) => {
  router.push(`/resident/notifications/${notice.id}`)
}

const goToNotifications = () => {
  router.push('/resident/notifications')
}

onMounted(() => {
  loadStatistics()
  loadBanners()
  loadNotices()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.banner-carousel {
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.carousel-item {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: #2c3e50;
}

.carousel-content {
  text-align: center;
  padding: 0 20px;
  position: relative;
  z-index: 2;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.carousel-content h3 {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.carousel-content p {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.notice-section {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid var(--border-color);
}

.notice-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.notice-header .el-icon {
  color: #10b981;
  font-size: 18px;
}

.view-all-link {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.notice-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background: var(--bg-light);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}

.notice-item:hover {
  background: var(--primary-light);
  transform: translateX(4px);
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-size: 14px;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.notice-time {
  font-size: 12px;
  color: var(--text-secondary);
}

.notice-arrow {
  color: var(--text-secondary);
  font-size: 14px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid var(--border-color);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon .el-icon {
  font-size: 22px;
}

.count-icon {
  background: #e8f4ff;
  color: #1976d2;
}

.weight-icon {
  background: #ffebee;
  color: #c62828;
}

.points-icon {
  background: #e8f5e9;
  color: #2e7d32;
}

.carbon-icon {
  background: #f3e5f5;
  color: #7b1fa2;
}

.stat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 4px;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .carousel-content h3 {
    font-size: 22px;
  }

  .carousel-content p {
    font-size: 14px;
  }
}
</style>
