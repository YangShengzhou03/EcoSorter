<template>
  <div class="admin-dashboard">
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
          <div class="stat-number">{{ stats.totalWeight }} kg</div>
          <div class="stat-label">总处理量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.pendingOrders }}</div>
          <div class="stat-label">待发货</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="quick-access-card">
      <template #header>
        <div class="card-header">
          <span>常用功能</span>
        </div>
      </template>
      <div class="quick-access-grid">
        <div class="quick-access-item" @click="navigateTo('/admin/users')">
          <div class="quick-icon user-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">用户管理</div>
            <div class="quick-desc">管理系统用户</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/devices')">
          <div class="quick-icon device-icon">
            <el-icon><Monitor /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">设备管理</div>
            <div class="quick-desc">管理智能垃圾桶</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/task-management')">
          <div class="quick-icon task-icon">
            <el-icon><List /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">任务管理</div>
            <div class="quick-desc">管理收集任务</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/products')">
          <div class="quick-icon product-icon">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">商品管理</div>
            <div class="quick-desc">管理积分商城</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/orders')">
          <div class="quick-icon order-icon">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">订单管理</div>
            <div class="quick-desc">查看兑换订单</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/notifications')">
          <div class="quick-icon notice-icon">
            <el-icon><Bell /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">通知管理</div>
            <div class="quick-desc">发送系统通知</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/categories')">
          <div class="quick-icon category-icon">
            <el-icon><Grid /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">分类管理</div>
            <div class="quick-desc">管理垃圾分类</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/complaints')">
          <div class="quick-icon complaint-icon">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">申诉管理</div>
            <div class="quick-desc">处理用户申诉</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/admin/reports')">
          <div class="quick-icon report-icon">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">报表管理</div>
            <div class="quick-desc">查看数据报表</div>
          </div>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="device-row">
      <el-col :span="8">
        <el-card>
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
        <el-card>
          <template #header>
            <div class="card-header">
              <span>实时活动</span>
              <el-button size="small" @click="refreshActivities">刷新</el-button>
            </div>
          </template>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-time">{{ formatTime(activity.createdAt) }}</div>
              <div class="activity-content">
                <span class="activity-desc">{{ activity.description }}</span>
              </div>
            </div>
            <el-empty v-if="recentActivities.length === 0" description="暂无实时活动" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'
import {
  User,
  Monitor,
  List,
  Goods,
  Tickets,
  Bell,
  Grid,
  ChatDotRound,
  DataAnalysis
} from '@element-plus/icons-vue'

defineOptions({
  name: 'AdminDashboard'
})

const router = useRouter()

const stats = reactive({
  totalUsers: 0,
  totalDevices: 0,
  totalWeight: 0,
  pendingOrders: 0
})

const deviceStatus = reactive({
  online: 0,
  offline: 0,
  error: 0,
  maintenance: 0
})

const recentActivities = ref([])

const navigateTo = (path) => {
  router.push(path)
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  
  return date.toLocaleDateString('zh-CN')
}

const loadDashboard = async () => {
  try {
    const response = await adminApi.getDashboard()
    stats.totalUsers = response.totalUsers || 0
    stats.totalDevices = response.totalDevices || 0
    stats.totalWeight = response.totalWeight || 0
    stats.pendingOrders = response.pendingOrders || 0
  } catch (error) {
    ElMessage.error('加载仪表板数据失败')
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
    ElMessage.error('加载设备状态失败')
  }
}

const loadActivities = async () => {
  try {
    const response = await adminApi.getActivities()
    recentActivities.value = response || []
  } catch (error) {
    ElMessage.error('加载实时活动失败')
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

onMounted(() => {
  loadDashboard()
  loadDeviceStatus()
  loadActivities()
})

onActivated(() => {
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
  padding: 20px;
  border: none;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #1e3a8a;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
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
  border-bottom: 1px solid #e5e7eb;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  font-size: 14px;
  color: #4b5563;
  font-weight: 500;
}

.status-value {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
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
  border-bottom: 1px solid #e5e7eb;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  width: 100px;
  font-size: 12px;
  color: #94a3b8;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
  margin-left: 15px;
}

.activity-desc {
  font-size: 14px;
  color: #1e293b;
}

.no-activity {
  text-align: center;
  padding: 40px;
  color: #94a3b8;
}

.quick-access-card {
  margin-bottom: 16px;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.quick-access-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
}

.quick-access-item:hover {
  background: #eff6ff;
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.quick-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-radius: 8px;
}

.quick-icon .el-icon {
  font-size: 24px;
}

.user-icon {
  background: #dbeafe;
  color: #1e40af;
}

.device-icon {
  background: #ffedd5;
  color: #c2410c;
}

.task-icon {
  background: #dcfce7;
  color: #15803d;
}

.product-icon {
  background: #fce7f3;
  color: #be185d;
}

.order-icon {
  background: #f3e8ff;
  color: #7c3aed;
}

.notice-icon {
  background: #fef9c3;
  color: #a16207;
}

.category-icon {
  background: #e0f2fe;
  color: #0369a1;
}

.complaint-icon {
  background: #fee2e2;
  color: #dc2626;
}

.report-icon {
  background: #f3e8ff;
  color: #7c3aed;
}

.log-icon {
  background: #e0e7ff;
  color: #4338ca;
}

.quick-info {
  flex: 1;
}

.quick-title {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 4px;
}

.quick-desc {
  font-size: 12px;
  color: #64748b;
}
</style>
