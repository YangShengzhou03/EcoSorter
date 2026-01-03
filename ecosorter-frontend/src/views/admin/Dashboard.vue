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

const stats = reactive({
  totalUsers: 1234,
  totalDevices: 56,
  totalCollections: 8923,
  totalPoints: 45678
})

const deviceStatus = reactive({
  online: 45,
  offline: 8,
  error: 2,
  maintenance: 1
})



const recentActivities = ref([
  {
    id: 1,
    time: new Date(Date.now() - 5 * 60 * 1000),
    type: 'user_login',
    description: '用户 张三 登录系统'
  },
  {
    id: 2,
    time: new Date(Date.now() - 15 * 60 * 1000),
    type: 'device_online',
    description: '设备 DEVICE-A01 上线'
  },
  {
    id: 3,
    time: new Date(Date.now() - 30 * 60 * 1000),
    type: 'collection_completed',
    description: '收集员 李四 完成任务 #12345'
  },
  {
    id: 4,
    time: new Date(Date.now() - 45 * 60 * 1000),
    type: 'waste_classification',
    description: '用户 王五 分类垃圾 2.5kg，获得 25 积分'
  }
])



const refreshDeviceStatus = () => {
  deviceStatus.online = Math.floor(Math.random() * 50) + 40
  deviceStatus.offline = Math.floor(Math.random() * 10) + 5
  deviceStatus.error = Math.floor(Math.random() * 5)
  deviceStatus.maintenance = Math.floor(Math.random() * 3)
  
  ElMessage.success('设备状态已刷新')
}



const refreshActivities = () => {
  const newActivity = {
    id: Date.now(),
    time: new Date(),
    type: 'system_notification',
    description: '系统消息：数据同步完成'
  }
  recentActivities.value.unshift(newActivity)
  
  if (recentActivities.value.length > 10) {
    recentActivities.value = recentActivities.value.slice(0, 10)
  }
  
  ElMessage.success('实时活动已刷新')
}

const formatTime = (time) => {
  const now = new Date()
  const diff = now - time
  const minutes = Math.floor(diff / (1000 * 60))
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  
  const hours = Math.floor(minutes / 60)
  if (hours < 24) return `${hours}小时前`
  
  return time.toLocaleString()
}

onMounted(() => {
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
  border: 1px solid #e8eaed;
  border-radius: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
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
  color: #303133;
}

.status-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.system-overview {
  padding: 16px;
}

.system-overview p {
  margin-bottom: 10px;
  color: #303133;
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
  color: #6b7280;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
  margin-left: 15px;
}

.activity-desc {
  font-size: 14px;
  color: #303133;
}

.no-activity {
  text-align: center;
  padding: 40px;
  color: #6b7280;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>