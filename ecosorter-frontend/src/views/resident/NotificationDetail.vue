<template>
  <div class="page-container">
    <el-card class="notice-detail-card" v-if="notice">
      <template #header>
        <div class="card-header">
          <span class="card-title">{{ notice.title }}</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <div class="notice-meta">
        <span class="meta-item">
          <el-icon><Clock /></el-icon>
          <span>{{ formatDate(notice.createdAt) }}</span>
        </span>
      </div>

      <div class="notice-content">
        <div v-html="notice.content"></div>
      </div>
    </el-card>

    <el-empty v-else description="通知不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Clock } from '@element-plus/icons-vue'
import { noticeApi } from '@/api/notice'

const route = useRoute()
const router = useRouter()

const notice = ref(null)

const loadNotice = async () => {
  try {
    const id = route.params.id
    const response = await noticeApi.getById(id)
    notice.value = response
  } catch (error) {
    console.error('加载通知详情失败:', error)
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadNotice()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.notice-detail-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-color);
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--text-secondary);
}

.meta-item .el-icon {
  font-size: 16px;
}

.notice-content {
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-primary);
  padding: 20px 0;
}

.notice-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 16px 0;
}

.notice-content :deep(h1),
.notice-content :deep(h2),
.notice-content :deep(h3) {
  margin: 24px 0 12px 0;
  color: var(--text-primary);
}

.notice-content :deep(p) {
  margin: 12px 0;
}

.notice-content :deep(ul),
.notice-content :deep(ol) {
  margin: 12px 0;
  padding-left: 24px;
}

.notice-content :deep(li) {
  margin: 8px 0;
}
</style>
