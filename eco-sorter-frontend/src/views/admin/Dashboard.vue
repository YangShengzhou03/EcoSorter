<template>
  <div class="dashboard">
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalUsers }}</span>
        <span class="stat-label">用户</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalDevices }}</span>
        <span class="stat-label">设备</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalWeight }}kg</span>
        <span class="stat-label">处理量</span>
      </div>
      <div class="stat-item highlight">
        <span class="stat-value">{{ stats.pendingOrders }}</span>
        <span class="stat-label">待发货</span>
      </div>
      <div class="stat-item online">
        <span class="stat-value">{{ deviceStatus.online }}</span>
        <span class="stat-label">在线</span>
      </div>
      <div class="stat-item error">
        <span class="stat-value">{{ deviceStatus.error }}</span>
        <span class="stat-label">故障</span>
      </div>
    </div>

    <div class="quick-nav">
      <div class="nav-item" @click="$router.push('/admin/users')">
        <el-icon><User /></el-icon>
        <span>用户</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/devices')">
        <el-icon><Monitor /></el-icon>
        <span>设备</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/task-management')">
        <el-icon><List /></el-icon>
        <span>任务</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/products')">
        <el-icon><Goods /></el-icon>
        <span>商品</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/orders')">
        <el-icon><Tickets /></el-icon>
        <span>订单</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/notifications')">
        <el-icon><Bell /></el-icon>
        <span>通知</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/categories')">
        <el-icon><Grid /></el-icon>
        <span>分类</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/complaints')">
        <el-icon><Warning /></el-icon>
        <span>申诉</span>
      </div>
      <div class="nav-item" @click="$router.push('/admin/reports')">
        <el-icon><DataAnalysis /></el-icon>
        <span>报表</span>
      </div>
    </div>

    <div class="activity-section">
      <div class="section-header">
        <span>实时动态</span>
        <el-button text size="small" @click="loadActivities">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
      <div class="activity-list" v-if="recentActivities.length">
        <div v-for="item in recentActivities" :key="item.id" class="activity-row">
          <span class="activity-time">{{ formatTime(item.createdAt) }}</span>
          <span class="activity-text">{{ item.description }}</span>
        </div>
      </div>
      <el-empty v-else description="暂无动态" :image-size="60" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onActivated } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'
import { User, Monitor, List, Goods, Tickets, Bell, Grid, Warning, DataAnalysis, Refresh } from '@element-plus/icons-vue'

defineOptions({ name: 'AdminDashboard' })

const stats = reactive({ totalUsers: 0, totalDevices: 0, totalWeight: 0, pendingOrders: 0 })
const deviceStatus = reactive({ online: 0, offline: 0, error: 0, maintenance: 0 })
const recentActivities = ref([])

const formatTime = (time) => {
  if (!time) return ''
  const diff = Date.now() - new Date(time).getTime()
  const m = Math.floor(diff / 60000)
  if (m < 1) return '刚刚'
  if (m < 60) return `${m}分钟前`
  const h = Math.floor(m / 60)
  if (h < 24) return `${h}小时前`
  return `${Math.floor(h / 24)}天前`
}

const loadDashboard = async () => {
  try {
    const res = await adminApi.getDashboard()
    Object.assign(stats, {
      totalUsers: res.totalUsers || 0,
      totalDevices: res.totalDevices || 0,
      totalWeight: res.totalWeight || 0,
      pendingOrders: res.pendingOrders || 0
    })
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const loadDeviceStatus = async () => {
  try {
    const res = await adminApi.getDeviceStatus()
    Object.assign(deviceStatus, {
      online: res.online || 0,
      offline: res.offline || 0,
      error: res.error || 0,
      maintenance: res.maintenance || 0
    })
  } catch (e) {}
}

const loadActivities = async () => {
  try {
    recentActivities.value = await adminApi.getActivities() || []
  } catch (e) {}
}

onMounted(() => { loadDashboard(); loadDeviceStatus(); loadActivities() })
onActivated(() => { loadDashboard(); loadDeviceStatus(); loadActivities() })
</script>

<style scoped>
.dashboard { padding: 0; }

.stats-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  background: white;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 8px;
  border-radius: 6px;
  background: #f8fafc;
}

.stat-item .stat-value {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
}

.stat-item .stat-label {
  font-size: 12px;
  color: #64748b;
}

.stat-item.highlight { background: #fef2f2; }
.stat-item.highlight .stat-value { color: #dc2626; }
.stat-item.online { background: #ecfdf5; }
.stat-item.online .stat-value { color: #059669; }
.stat-item.error { background: #fef2f2; }
.stat-item.error .stat-value { color: #dc2626; }

.quick-nav {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: #475569;
  transition: all 0.2s;
}

.nav-item:hover {
  background: #f1f5f9;
  border-color: #3b82f6;
  color: #3b82f6;
}

.nav-item .el-icon { font-size: 16px; }

.activity-section {
  background: white;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
}

.activity-list { max-height: 300px; overflow-y: auto; }

.activity-row {
  display: flex;
  gap: 12px;
  padding: 10px 16px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 13px;
}

.activity-row:last-child { border-bottom: none; }

.activity-time {
  color: #94a3b8;
  width: 60px;
  flex-shrink: 0;
}

.activity-text { color: #334155; }
</style>
