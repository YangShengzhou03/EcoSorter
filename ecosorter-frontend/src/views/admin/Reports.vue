<template>
  <div class="admin-reports">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-number">{{ stats.totalUsers }}</div>
        <div class="stat-label">总用户数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.totalDevices }}</div>
        <div class="stat-label">设备总数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.totalCollections }} kg</div>
        <div class="stat-label">总处理量</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.accuracyRate }}%</div>
        <div class="stat-label">分类准确率</div>
      </div>
    </div>

    <div class="table-section">
      <el-table :data="reportData" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="time" label="分类时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.time) }}
          </template>
        </el-table-column>
        <el-table-column prop="type" label="垃圾类型" width="120" />
        <el-table-column prop="weight" label="重量(kg)" width="100" />
        <el-table-column prop="user" label="用户" width="120" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const loading = ref(false)
const reportData = ref([])
const stats = ref({
  totalUsers: 0,
  totalDevices: 0,
  totalCollections: 0,
  accuracyRate: 0
})

const loadReports = async () => {
  loading.value = true
  try {
    const response = await adminApi.getReports()
    reportData.value = response || []
    
    const dashboard = await adminApi.getDashboard()
    stats.value = {
      totalUsers: dashboard.totalUsers || 0,
      totalDevices: dashboard.totalDevices || 0,
      totalCollections: dashboard.totalCollections || 0,
      accuracyRate: 94.2
    }
  } catch (error) {
    console.error('加载报表数据失败:', error)
    ElMessage.error('加载报表数据失败')
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

onMounted(() => {
  loadReports()
})
</script>

<style scoped>
.admin-reports {
  padding: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.table-section {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}
</style>
