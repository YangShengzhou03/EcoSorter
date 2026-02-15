<template>
  <div class="notice-detail-page">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item to="/collector/notifications">通知列表</el-breadcrumb-item>
      <el-breadcrumb-item>通知详情</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="notice-detail-card" v-if="notice" v-loading="loading">
      <h1 class="notice-title">{{ notice.title }}</h1>
      
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
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Clock } from '@element-plus/icons-vue'
import { noticeApi } from '@/api/notice'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'CollectorNotificationDetail'
})

const route = useRoute()

const notice = ref(null)
const loading = ref(false)

const formatDate = (dateString) => {
  return formatTime(dateString)
}

const loadNotice = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const response = await noticeApi.getById(id)
    notice.value = response
  } catch (error) {
    ElMessage.error('加载通知失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadNotice()
})
</script>

<style scoped>
.notice-detail-page {
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.notice-detail-card {
  margin: 0 auto;
  min-height: 60vh;
}

.notice-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #909399;
}

.meta-item .el-icon {
  font-size: 16px;
}

.notice-content {
  font-size: 15px;
  line-height: 1.8;
  color: #303133;
  padding: 20px 0;
}

.notice-content :deep(img) {
  max-width: 100%;
  height: auto;
  margin: 16px 0;
}

.notice-content :deep(h1),
.notice-content :deep(h2),
.notice-content :deep(h3) {
  margin: 24px 0 12px 0;
  color: #303133;
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
