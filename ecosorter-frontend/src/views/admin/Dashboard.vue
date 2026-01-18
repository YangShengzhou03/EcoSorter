<template>
  <div class="admin-dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.totalUsers }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.totalDevices }}</div>
            <div class="stat-label">设备总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.totalCollections }}</div>
            <div class="stat-label">总收集量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.totalPoints }}</div>
            <div class="stat-label">总积分</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="device-row">
      <el-col :span="8">
        <el-card title="设备状态概览">
          <template #header>
            <div class="card-header">
              <span>设备状态概览</span>
              <el-button size="small" @click="refreshDeviceStatus">刷新</el-button>
            </div>
          </template>
          <div class="device-status-overview">
            <div class="status-item">
              <span class="status-label">在线设备</span>
              <span class="status-value">{{ deviceStatus.online }}</span>
            </div>
            <div class="status-item">
              <span class="status-label">离线设备</span>
              <span class="status-value">{{ deviceStatus.offline }}</span>
            </div>
            <div class="status-item">
              <span class="status-label">故障设备</span>
              <span class="status-value">{{ deviceStatus.error }}</span>
            </div>
            <div class="status-item">
              <span class="status-label">维护中</span>
              <span class="status-value">{{ deviceStatus.maintenance }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card title="实时活动" class="activity-card">
          <template #header>
            <div class="card-header">
              <span>实时活动</span>
              <el-button size="small" @click="refreshActivities">刷新</el-button>
            </div>
          </template>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-time">{{ formatTime(activity.time) }}</div>
              <div class="activity-content">
                <span class="activity-desc">{{ activity.description }}</span>
              </div>
            </div>
            <div v-if="recentActivities.length === 0" class="no-activity">
              暂无实时活动
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const stats = reactive({
  totalUsers: 0,
  totalDevices: 0,
  totalCollections: 0,
  totalPoints: 0
})

const deviceStatus = reactive({
  online: 0,
  offline: 0,
  error: 0,
  maintenance: 0
})

const recentActivities = ref([])

const loadDashboard = async () => {
  try {
    const response = await adminApi.getDashboard()
    stats.totalUsers = response.totalUsers || 0
    stats.totalDevices = response.totalDevices || 0
    stats.totalCollections = response.totalCollections || 0
    stats.totalPoints = response.totalPoints || 0
  } catch (error) {
    console.error('加载仪表板数据失败:', error)
  }
}

const loadDeviceStatus = async () => {
  try {
    const response = await adminApi.getDeviceStatus()
    deviceStatus.online = response.online || 0
    deviceStatus.offline = response.offline || 0
    deviceStatus.error = response.error || 0
    deviceStatus.maintenance = response.maintenance || 0
  } catch (error) {
    console.error('加载设备状态失败:', error)
  }
}

const loadActivities = async () => {
  try {
    const response = await adminApi.getActivities()
    recentActivities.value = response || []
  } catch (error) {
    console.error('加载实时活动失败:', error)
  }
}

const refreshDeviceStatus = () => {
  loadDeviceStatus()
  ElMessage.success('设备状态已刷新')
}

const refreshActivities = () => {
  loadActivities()
  ElMessage.success('实时活动已刷新')
}

const formatTime = (time) => {
  const now = new Date()
  const diff = now - new Date(time)
  const minutes = Math.floor(diff / (1000 * 60))
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  
  const hours = Math.floor(minutes / 60)
  if (hours < 24) return `${hours}小时前`
  
  return new Date(time).toLocaleString()
}

onMounted(() => {
  loadDashboard()
  loadDeviceStatus()
  loadActivities()
})
</script>

<style scoped>
.admin-dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
  padding: 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
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

.device-row {
  margin-bottom: 16px;
}

.device-status-overview {
  padding: 16px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #e8eaed;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  font-size: 14px;
  color: var(--text-primary);
}

.status-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.system-overview {
  padding: 16px;
}

.system-overview p {
  margin-bottom: 10px;
  color: var(--text-primary);
}

.activity-card {
  margin-bottom: 0;
}

.activity-list {
  max-height: 400px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #e8eaed;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  width: 100px;
  font-size: 12px;
  color: var(--text-secondary);
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
  margin-left: 15px;
}

.activity-desc {
  font-size: 14px;
  color: var(--text-primary);
}

.no-activity {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>