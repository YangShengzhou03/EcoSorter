<template>
  <div class="collector-tasks">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>任务管理</span>
          <el-button type="primary" @click="refreshTasks">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-table :data="taskList" v-loading="loading" style="width: 100%">
        <el-table-column prop="taskId" label="任务编号" width="140" />
        <el-table-column prop="location" label="收集点位置" />
        <el-table-column prop="garbageType" label="垃圾类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getGarbageTypeTag(row.garbageType)">
              {{ row.garbageType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="estimatedWeight" label="预估重量(kg)" width="120" />
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityTag(row.priority)">
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'pending'"
              type="primary" 
              size="small" 
              @click="startTask(row)"
            >
              开始
            </el-button>
            <el-button 
              v-if="row.status === 'in_progress'"
              type="success" 
              size="small" 
              @click="completeTask(row)"
            >
              完成
            </el-button>
            <el-button 
              type="primary" 
              link 
              size="small"
              @click="navigateTo(row)"
            >
              导航
            </el-button>
          </template>
        </el-table-column>
      </el-table>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { collectorApi } from '@/api/collector'
import { collectionTaskApi } from '@/api/collectionTask'

const loading = ref(false)
const completing = ref(false)
const taskList = ref([])
const completeDialogVisible = ref(false)
const completeForm = ref({
  actualWeight: 0,
  notes: ''
})
const currentTask = ref(null)

const loadTasks = async () => {
  loading.value = true
  try {
    const response = await collectorApi.getTasks()
    taskList.value = response || []
  } catch (error) {
    console.error('加载任务数据失败:', error)
    ElMessage.error('加载任务数据失败')
  } finally {
    loading.value = false
  }
}

const refreshTasks = async () => {
  await loadTasks()
  ElMessage.success('任务已刷新')
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
  } catch (error) {
    console.error('完成任务失败:', error)
    ElMessage.error('完成任务失败')
  } finally {
    completing.value = false
  }
}

const navigateTo = (task) => {
  ElMessage.info('导航功能开发中')
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

const getGarbageTypeTag = (type) => {
  const typeMap = {
    '可回收物': 'success',
    '厨余垃圾': 'warning',
    '有害垃圾': 'danger',
    '其他垃圾': 'info'
  }
  return typeMap[type] || 'info'
}

const getPriorityTag = (priority) => {
  const priorityMap = {
    'high': 'danger',
    'medium': 'warning',
    'low': 'info'
  }
  return priorityMap[priority] || 'info'
}

const getPriorityText = (priority) => {
  const priorityMap = {
    'high': '高',
    'medium': '中',
    'low': '低'
  }
  return priorityMap[priority] || priority
}

const getStatusTag = (status) => {
  const statusMap = {
    'pending': 'info',
    'in_progress': 'warning',
    'completed': 'success'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'pending': '待处理',
    'in_progress': '进行中',
    'completed': '已完成'
  }
  return statusMap[status] || status
}

onMounted(() => {
  loadTasks()
})
</script>

<style scoped>
.collector-tasks {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
