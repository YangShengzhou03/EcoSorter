<template>
  <div class="navigation-route">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>导航路线</span>
          <el-button type="primary" @click="refreshRoute">
            <el-icon><Refresh /></el-icon>
            刷新路线
          </el-button>
        </div>
      </template>
      
      <div class="route-container">
        <div class="route-list">
          <div v-if="routeTasks.length === 0" class="empty-state">
            <el-empty description="暂无待收集任务" />
          </div>
          
          <div v-else class="task-items">
            <div 
              v-for="(task, index) in routeTasks" 
              :key="task.taskId"
              class="task-item"
              :class="{ 'current-task': index === currentTaskIndex }"
            >
              <div class="task-header">
                <div class="task-order">
                  <el-tag :type="getPriorityType(task.priority)" size="large">
                    {{ index + 1 }}
                  </el-tag>
                </div>
                <div class="task-info">
                  <h4>{{ task.location }}</h4>
                  <p>设备ID: {{ task.deviceId }}</p>
                </div>
                <div class="task-actions">
                  <el-button 
                    v-if="index === currentTaskIndex && task.status === 'pending'"
                    type="primary" 
                    @click="startTask(task)"
                  >
                    开始任务
                  </el-button>
                  <el-button 
                    v-if="index === currentTaskIndex && task.status === 'in_progress'"
                    type="success" 
                    @click="completeTask(task)"
                  >
                    完成收集
                  </el-button>
                  <el-tag v-if="task.status === 'completed'" type="success">已完成</el-tag>
                </div>
              </div>
              
              <div class="task-details">
                <div class="detail-item">
                  <span class="label">垃圾类型:</span>
                  <span class="value">{{ task.garbageType }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">预估重量:</span>
                  <span class="value">{{ task.estimatedWeight }} kg</span>
                </div>
                <div class="detail-item">
                  <span class="label">优先级:</span>
                  <el-tag :type="getPriorityType(task.priority)">{{ getPriorityText(task.priority) }}</el-tag>
                </div>
                <div class="detail-item" v-if="task.status === 'in_progress'">
                  <span class="label">开始时间:</span>
                  <span class="value">{{ formatTime(task.startTime) }}</span>
                </div>
              </div>
              
              <div class="task-map" v-if="task.latitude && task.longitude">
                <el-button 
                  type="primary" 
                  link 
                  @click="openNavigation(task)"
                >
                  <el-icon><Location /></el-icon>
                  打开地图导航
                </el-button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="route-summary">
          <div class="summary-card">
            <div class="summary-item">
              <div class="summary-value">{{ routeTasks.length }}</div>
              <div class="summary-label">待收集</div>
            </div>
            <div class="summary-item">
              <div class="summary-value">{{ completedCount }}</div>
              <div class="summary-label">已完成</div>
            </div>
            <div class="summary-item">
              <div class="summary-value">{{ totalWeight }}</div>
              <div class="summary-label">总重量(kg)</div>
            </div>
          </div>
          
          <div class="route-info">
            <h4>路线信息</h4>
            <div class="info-item">
              <span>当前任务:</span>
              <span>{{ currentTask ? currentTask.location : '无' }}</span>
            </div>
            <div class="info-item">
              <span>剩余任务:</span>
              <span>{{ routeTasks.length - currentTaskIndex }}</span>
            </div>
            <div class="info-item">
              <span>预计完成时间:</span>
              <span>{{ estimatedCompletionTime }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>
    
    <el-dialog
      v-model="completeDialogVisible"
      title="完成收集"
      width="500px"
    >
      <el-form :model="completeForm" label-width="100px">
        <el-form-item label="实际重量">
          <el-input-number 
            v-model="completeForm.actualWeight" 
            :min="0" 
            :max="1000"
            :precision="2"
            :step="0.5"
          />
          <span style="margin-left: 8px">kg</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="completeForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmComplete" :loading="completing">
          确认完成
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Location } from '@element-plus/icons-vue'
import { collectionTaskApi } from '@/api/collectionTask'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const routeTasks = ref([])
const currentTaskIndex = ref(0)
const loading = ref(false)
const completing = ref(false)
const completeDialogVisible = ref(false)
const completeForm = ref({
  actualWeight: 0,
  notes: ''
})
const currentTask = ref(null)

const completedCount = computed(() => {
  return routeTasks.value.filter(t => t.status === 'completed').length
})

const totalWeight = computed(() => {
  return routeTasks.value
    .filter(t => t.status === 'completed')
    .reduce((sum, t) => sum + (t.actualWeight || 0), 0)
    .toFixed(2)
})

const estimatedCompletionTime = computed(() => {
  const remainingTasks = routeTasks.value.length - currentTaskIndex.value
  if (remainingTasks <= 0) return '已完成'
  const minutes = remainingTasks * 15
  return `约 ${minutes} 分钟`
})

const loadRoute = async () => {
  loading.value = true
  try {
    const response = await collectionTaskApi.getNavigationRoute(authStore.userInfo.id)
    routeTasks.value = response || []
    
    const firstPendingIndex = routeTasks.value.findIndex(t => t.status === 'pending' || t.status === 'in_progress')
    if (firstPendingIndex !== -1) {
      currentTaskIndex.value = firstPendingIndex
    }
    
    if (routeTasks.value.length > 0) {
      currentTask.value = routeTasks.value[currentTaskIndex.value]
    }
  } catch (error) {
    console.error('加载导航路线失败:', error)
    ElMessage.error('加载导航路线失败')
  } finally {
    loading.value = false
  }
}

const refreshRoute = async () => {
  await loadRoute()
  ElMessage.success('路线已刷新')
}

const startTask = async (task) => {
  try {
    await collectionTaskApi.startTask(task.taskId)
    task.status = 'in_progress'
    ElMessage.success('任务已开始')
  } catch (error) {
    console.error('开始任务失败:', error)
    ElMessage.error('开始任务失败')
  }
}

const completeTask = (task) => {
  currentTask.value = task
  completeForm.value = {
    actualWeight: task.estimatedWeight || 0,
    notes: ''
  }
  completeDialogVisible.value = true
}

const confirmComplete = async () => {
  completing.value = true
  try {
    await collectionTaskApi.completeTask(currentTask.value.taskId, completeForm.value)
    currentTask.value.status = 'completed'
    currentTask.value.actualWeight = completeForm.value.actualWeight
    completeDialogVisible.value = false
    ElMessage.success('任务已完成')
    
    if (currentTaskIndex.value < routeTasks.value.length - 1) {
      currentTaskIndex.value++
      currentTask.value = routeTasks.value[currentTaskIndex.value]
    } else {
      currentTask.value = null
    }
  } catch (error) {
    console.error('完成任务失败:', error)
    ElMessage.error('完成任务失败')
  } finally {
    completing.value = false
  }
}

const openNavigation = (task) => {
  if (task.latitude && task.longitude) {
    const url = `https://maps.google.com/maps?q=${task.latitude},${task.longitude}`
    window.open(url, '_blank')
  } else {
    ElMessage.warning('该设备没有位置信息')
  }
}

const getPriorityType = (priority) => {
  const map = { 'high': 'danger', 'medium': 'warning', 'low': 'info' }
  return map[priority] || 'info'
}

const getPriorityText = (priority) => {
  const map = { 'high': '高', 'medium': '中', 'low': '低' }
  return map[priority] || priority
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
  loadRoute()
})
</script>

<style scoped>
.navigation-route {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.route-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.route-list {
  max-height: 600px;
  overflow-y: auto;
}

.empty-state {
  padding: 40px;
  text-align: center;
}

.task-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.task-item {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  background: var(--bg-white);
  transition: all 0.3s;
}

.task-item.current-task {
  border-color: #409eff;
  background: #f0f7ff;
}

.task-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.task-order {
  flex-shrink: 0;
}

.task-info {
  flex: 1;
}

.task-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  color: var(--text-primary);
}

.task-info p {
  margin: 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.task-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-item .label {
  font-size: 14px;
  color: #909399;
}

.detail-item .value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.task-map {
  padding-top: 12px;
  border-top: 1px solid #e8eaed;
}

.route-summary {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.summary-card {
  background: var(--primary-color);
  border-radius: 8px;
  padding: 20px;
  color: #fff;
}

.summary-item {
  text-align: center;
  margin-bottom: 16px;
}

.summary-item:last-child {
  margin-bottom: 0;
}

.summary-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 4px;
}

.summary-label {
  font-size: 14px;
  opacity: 0.9;
}

.route-info {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
}

.route-info h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: var(--text-primary);
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-item span:first-child {
  color: var(--text-secondary);
}

.info-item span:last-child {
  color: var(--text-primary);
}
</style>
