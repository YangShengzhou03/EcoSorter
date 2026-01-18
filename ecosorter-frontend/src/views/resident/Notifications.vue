<template>
  <div class="page-container">
    <div class="notice-section" v-if="notices.length > 0">
      <div class="notice-header">
        <el-icon><Bell /></el-icon>
        <span>最新通知</span>
      </div>
      <div class="notice-list">
        <div class="notice-item" v-for="notice in notices" :key="notice.id" @click="viewNotice(notice)">
          <div class="notice-content">
            <div class="notice-title">{{ notice.title }}</div>
            <div class="notice-time">{{ formatDate(notice.createdAt) }}</div>
          </div>
          <el-icon class="notice-arrow"><ArrowRight /></el-icon>
        </div>
      </div>
    </div>

    <el-empty v-else description="暂无通知" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Bell, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { noticeApi } from '@/api/notice'

const router = useRouter()
const notices = ref([])

const loadNotices = async () => {
  try {
    const response = await noticeApi.getPublished()
    notices.value = response || []
  } catch (error) {
    console.error('加载通知失败:', error)
    ElMessage.error('加载通知失败')
  }
}

const viewNotice = (notice) => {
  router.push(`/resident/notifications/${notice.id}`)
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

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.notice-section {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  border: 1px solid var(--border-color);
}

.notice-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: var(--bg-light);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
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
  font-weight: 500;
  margin-bottom: 4px;
}

.notice-time {
  font-size: 12px;
  color: var(--text-secondary);
}

.notice-arrow {
  color: var(--text-secondary);
  font-size: 16px;
}
</style>
