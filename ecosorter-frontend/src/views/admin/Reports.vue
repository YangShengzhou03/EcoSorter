<template>
  <div class="admin-page">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.totalUsers }}</div>
          <div class="stat-label">总用户数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.totalDevices }}</div>
          <div class="stat-label">设备总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.totalCollections }} kg</div>
          <div class="stat-label">总处理量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.accuracyRate }}%</div>
          <div class="stat-label">分类准确率</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>分类记录</span>
        </div>
      </template>
      <el-table :data="reportData" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="createdAt" label="分类时间">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="typeText" label="垃圾类型"/>
        <el-table-column prop="weight" label="重量(kg)"/>
        <el-table-column prop="userName" label="用户"/>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'AdminReports'
})

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
      totalCollections: dashboard.totalWeight || 0,
      accuracyRate: dashboard.accuracyRate || 0
    }
  } catch (error) {
    ElMessage.error('加载报表数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadReports()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.stats-row {
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
  padding: 16px;
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
</style>
