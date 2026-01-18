<template>
  <div class="collector-collection">
    <div class="task-list">
      <div class="task-header">
        <h3>待处理任务</h3>
        <span class="task-count">{{ pendingTasks.length }}</span>
      </div>
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      <div v-else-if="pendingTasks.length === 0" class="empty">
        <el-empty description="暂无待处理任务" />
      </div>
      <div v-else class="task-cards">
        <div v-for="task in pendingTasks" :key="task.taskId" class="task-card">
          <div class="task-header-row">
            <span class="task-id">{{ task.taskId }}</span>
            <el-tag :type="getPriorityType(task.priority)">{{ task.priority }}</el-tag>
          </div>
          <div class="task-info">
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ task.location }}</span>
            </div>
            <div class="info-item">
              <el-icon><Box /></el-icon>
              <span>{{ task.garbageType }}</span>
            </div>
            <div class="info-item">
              <el-icon><ScaleToOriginal /></el-icon>
              <span>预估: {{ task.estimatedWeight }}kg</span>
            </div>
          </div>
          <div class="task-actions">
            <el-button type="primary" size="small" @click="startTask(task)">开始收集</el-button>
            <el-button type="info" size="small" @click="openNavigation(task)">导航</el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="task-list">
      <div class="task-header">
        <h3>进行中任务</h3>
        <span class="task-count">{{ inProgressTasks.length }}</span>
      </div>
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      <div v-else-if="inProgressTasks.length === 0" class="empty">
        <el-empty description="暂无进行中任务" />
      </div>
      <div v-else class="task-cards">
        <div v-for="task in inProgressTasks" :key="task.taskId" class="task-card active">
          <div class="task-header-row">
            <span class="task-id">{{ task.taskId }}</span>
            <el-tag type="warning">进行中</el-tag>
          </div>
          <div class="task-info">
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ task.location }}</span>
            </div>
            <div class="info-item">
              <el-icon><Box /></el-icon>
              <span>{{ task.garbageType }}</span>
            </div>
            <div class="info-item">
              <el-icon><ScaleToOriginal /></el-icon>
              <span>预估: {{ task.estimatedWeight }}kg</span>
            </div>
          </div>
          <div class="task-actions">
            <el-button type="success" size="small" @click="completeTask(task)">完成收集</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="completeDialogVisible" title="完成收集" width="500px">
      <el-form :model="completeForm" label-width="100px">
        <el-form-item label="实际重量">
          <el-input-number v-model="completeForm.actualWeight" :min="0" :max="1000" :precision="2" />
          <span style="margin-left: 10px">kg</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="completeForm.notes" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmComplete">确认完成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Location, Box, ScaleToOriginal } from '@element-plus/icons-vue'
import { collectionTaskApi } from '@/api/collectionTask'

const loading = ref(false)
const tasks = ref([])
const completeDialogVisible = ref(false)
const currentTask = ref(null)
const completeForm = ref({
  actualWeight: 0,
  notes: ''
})

const pendingTasks = computed(() => tasks.value.filter(t => t.status === 'pending'))
const inProgressTasks = computed(() => tasks.value.filter(t => t.status === 'in_progress'))

const loadTasks = async () => {
  loading.value = true
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const collectorId = userInfo.id || 2
    const response = await collectionTaskApi.getTasksByCollector(collectorId)
    tasks.value = response || []
  } catch (error) {
    console.error('加载任务失败:', error)
    ElMessage.error('加载任务失败')
  } finally {
    loading.value = false
  }
}

const getPriorityType = (priority) => {
  const priorityMap = {
    '高': 'danger',
    '中': 'warning',
    '低': 'info'
  }
  return priorityMap[priority] || 'info'
}

const startTask = async (task) => {
  try {
    await collectionTaskApi.startTask(task.id)
    task.status = 'in_progress'
    ElMessage.success(`任务 ${task.taskId} 已开始`)
  } catch (error) {
    console.error('开始任务失败:', error)
    ElMessage.error('开始任务失败')
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

const completeTask = (task) => {
  currentTask.value = task
  completeForm.value = {
    actualWeight: task.estimatedWeight || 0,
    notes: ''
  }
  completeDialogVisible.value = true
}

const confirmComplete = async () => {
  try {
    await collectionTaskApi.completeTask(currentTask.value.id, completeForm.value)
    currentTask.value.status = 'completed'
    completeDialogVisible.value = false
    ElMessage.success(`任务 ${currentTask.value.taskId} 已完成`)
    loadTasks()
  } catch (error) {
    console.error('完成任务失败:', error)
    ElMessage.error('完成任务失败')
  }
}

onMounted(() => {
  loadTasks()
})
</script>

<style scoped>
.collector-collection {
  padding: 0;
}

.task-list {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.task-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.task-count {
  background: #ecf5ff;
  color: #409eff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.task-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 12px;
}

.task-card {
  border: 1px solid #e8eaed;
  border-radius: 6px;
  padding: 12px;
  transition: all 0.3s;
}

.task-card.active {
  border-color: #409eff;
  background: #ecf5ff;
}

.task-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.task-id {
  font-weight: 600;
  color: #303133;
}

.task-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 14px;
}

.task-actions {
  display: flex;
  gap: 8px;
}

.loading,
.empty {
  padding: 20px 0;
}
</style>
