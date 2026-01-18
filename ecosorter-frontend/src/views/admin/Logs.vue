<template>
  <div class="admin-logs">
    <div class="table-section">
      <el-table :data="logs" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="timestamp" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.timestamp) }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTag(row.level)">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="message" label="消息内容" min-width="300" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const loading = ref(false)
const logs = ref([])

const loadLogs = async () => {
  loading.value = true
  try {
    const response = await adminApi.getLogs()
    logs.value = response || []
  } catch (error) {
    console.error('加载日志数据失败:', error)
    ElMessage.error('加载日志数据失败')
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const getLevelTag = (level) => {
  const levelMap = {
    'INFO': 'info',
    'WARNING': 'warning',
    'ERROR': 'danger',
    'DEBUG': ''
  }
  return levelMap[level] || ''
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.admin-logs {
  padding: 0;
}

.table-section {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}
</style>
