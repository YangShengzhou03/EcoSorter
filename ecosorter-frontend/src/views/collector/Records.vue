<template>
  <div class="collector-records">
    <div class="table-section">
      <el-table :data="records" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="taskId" label="任务ID" width="120" />
        <el-table-column prop="location" label="收集地点" width="180" />
        <el-table-column prop="garbageType" label="垃圾类型" width="120" />
        <el-table-column prop="actualWeight" label="实际重量(kg)" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.endTime) }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { collectorApi } from '@/api/collector'

const loading = ref(false)
const records = ref([])

const loadRecords = async () => {
  loading.value = true
  try {
    const response = await collectorApi.getCollectionRecords()
    records.value = response || []
  } catch (error) {
    console.error('加载收集记录失败:', error)
    ElMessage.error('加载收集记录失败')
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
    minute: '2-digit'
  })
}

const getStatusType = (status) => {
  const statusMap = {
    'completed': 'success',
    'in_progress': 'warning',
    'pending': 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'completed': '已完成',
    'in_progress': '进行中',
    'pending': '待处理'
  }
  return statusMap[status] || status
}

onMounted(() => {
  loadRecords()
})
</script>

<style scoped>
.collector-records {
  padding: 0;
}

.table-section {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}
</style>
