<template>
  <div class="collector-dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.todayTasks }}</div>
          <div class="stat-label">今日任务</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.completedTasks }}</div>
          <div class="stat-label">已完成</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.inProgressTasks }}</div>
          <div class="stat-label">进行中</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-number">{{ stats.abnormalDevices }}</div>
          <div class="stat-label">异常设备</div>
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
        <div class="quick-access-item" @click="navigateTo('/collector/tasks')">
          <div class="quick-icon task-icon">
            <el-icon><List /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">收集任务</div>
            <div class="quick-desc">查看收集任务</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/collector/device-status')">
          <div class="quick-icon device-icon">
            <el-icon><Monitor /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">设备状态</div>
            <div class="quick-desc">查看设备状态</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/collector/notifications')">
          <div class="quick-icon notification-icon">
            <el-icon><Bell /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">通知消息</div>
            <div class="quick-desc">查看通知</div>
          </div>
        </div>
        <div class="quick-access-item" @click="navigateTo('/collector/profile')">
          <div class="quick-icon profile-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="quick-info">
            <div class="quick-title">个人信息</div>
            <div class="quick-desc">管理个人资料</div>
          </div>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="device-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>今日任务</span>
              <el-button size="small" @click="refreshTasks">刷新</el-button>
            </div>
          </template>
          <div class="task-list">
            <div v-if="stats.todayTasks === 0" class="no-tasks">
              今日暂无任务
            </div>
            <div v-else class="task-summary">
              <p>今日有 {{ stats.todayTasks }} 个任务</p>
              <el-button type="primary" link @click="navigateTo('/collector/tasks')">
                查看详情
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>设备状态概览</span>
              <el-button size="small" @click="refreshTasks">刷新</el-button>
            </div>
          </template>
          <div class="device-status-overview">
            <div class="status-item">
              <span class="status-label">异常设备</span>
              <span class="status-value">{{ stats.abnormalDevices }}</span>
            </div>
            <div class="status-item">
              <span class="status-label">进行中任务</span>
              <span class="status-value">{{ stats.inProgressTasks }}</span>
            </div>
            <div class="status-item">
              <span class="status-label">已完成任务</span>
              <span class="status-value">{{ stats.completedTasks }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { collectorApi } from '@/api/collector'
import {
  List,
  Monitor,
  User,
  Bell
} from '@element-plus/icons-vue'

defineOptions({
  name: 'CollectorDashboard'
})

const router = useRouter()

const stats = reactive({
  todayTasks: 0,
  completedTasks: 0,
  inProgressTasks: 0,
  abnormalDevices: 0
})

const navigateTo = (path) => {
  router.push(path)
}

const loadDashboard = async () => {
  try {
    const response = await collectorApi.getDashboard()
    stats.todayTasks = response.todayTasks || 0
    stats.completedTasks = response.completedTasks || 0
    stats.inProgressTasks = response.inProgressTasks || 0
    stats.abnormalDevices = response.abnormalDevices || 0
  } catch (error) {
    ElMessage.error('加载仪表板数据失败')
  }
}

const refreshTasks = () => {
  loadDashboard()
  ElMessage.success('任务已刷新')
}

onMounted(() => {
  loadDashboard()
})
</script>

<style scoped>
.collector-dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 12px;
}

.stat-card {
  text-align: center;
  padding: 16px;
  border: none;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1e3a8a;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  font-weight: 500;
}

.device-row {
  margin-bottom: 12px;
}

.device-status-overview {
  padding: 12px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e5e7eb;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  font-size: 13px;
  color: #4b5563;
  font-weight: 500;
}

.status-value {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
}

.task-list {
  max-height: 400px;
  overflow-y: auto;
}

.task-item {
  padding: 12px 0;
  border-bottom: 1px solid #e5e7eb;
}

.task-item:last-child {
  border-bottom: none;
}

.task-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-location {
  font-size: 14px;
  color: #1e293b;
  font-weight: 500;
}

.task-meta {
  display: flex;
  gap: 8px;
}

.no-tasks {
  text-align: center;
  padding: 40px;
  color: #94a3b8;
}

.quick-access-card {
  margin-bottom: 12px;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.quick-access-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
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
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-radius: 8px;
}

.quick-icon .el-icon {
  font-size: 20px;
}

.task-icon {
  background: #dcfce7;
  color: #15803d;
}

.device-icon {
  background: #ffedd5;
  color: #c2410c;
}

.profile-icon {
  background: #f3e8ff;
  color: #7c3aed;
}

.notification-icon {
  background: #fef3c7;
  color: #d97706;
}

.quick-info {
  flex: 1;
}

.quick-title {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 3px;
}

.quick-desc {
  font-size: 11px;
  color: #64748b;
}
</style>
