<template>
  <div class="collector-page">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>任务详情</span>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </template>

      <div v-if="task" class="task-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ task.taskId }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTag(task.status)" size="small">
              {{ getStatusText(task.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="位置">{{ task.location }}</el-descriptions-item>
          <el-descriptions-item label="设备ID">{{ task.deviceId }}</el-descriptions-item>
          <el-descriptions-item label="垃圾类型">{{ task.garbageType }}</el-descriptions-item>
          <el-descriptions-item label="预估重量">{{ task.estimatedWeight }} kg</el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityTag(task.priority)" size="small">
              {{ getPriorityText(task.priority) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(task.createdAt) }}</el-descriptions-item>
          <el-descriptions-item v-if="task.collectorName" label="收集员">{{ task.collectorName }}</el-descriptions-item>
          <el-descriptions-item v-if="task.completedAt" label="完成时间">{{ formatTime(task.completedAt) }}</el-descriptions-item>
        </el-descriptions>

        <div class="action-bar" v-if="task.status === 'pending' || task.status === 'in_progress'">
          <el-button v-if="task.status === 'pending'" type="primary" @click="startTask" :loading="submitting">
            开始任务
          </el-button>
          <el-button v-if="task.status === 'in_progress'" type="success" @click="completeTask" :loading="submitting">
            完成任务
          </el-button>
          <el-button v-if="task.status === 'in_progress'" @click="reportException" :loading="submitting">
            上报异常
          </el-button>
        </div>
      </div>

      <el-empty v-else description="任务不存在" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { collectorApi } from '@/api/collector'
import { formatTime, getStatusType, getStatusText, getPriorityType, getPriorityText } from '@/utils/helpers'

defineOptions({
  name: 'CollectorTaskDetail'
})

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const task = ref(null)

const loadTask = async () => {
  loading.value = true
  try {
    const taskId = route.params.taskId
    const response = await collectorApi.getTaskDetail(taskId)
    task.value = response
  } catch (error) {
    ElMessage.error('加载任务详情失败')
  } finally {
    loading.value = false
  }
}

const startTask = async () => {
  try {
    await ElMessageBox.confirm('确定要开始此任务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    submitting.value = true
    await collectorApi.startTask(task.value.taskId)
    ElMessage.success('任务已开始')
    loadTask()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  } finally {
    submitting.value = false
  }
}

const completeTask = async () => {
  try {
    await ElMessageBox.confirm('确定要完成此任务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    })
    submitting.value = true
    await collectorApi.completeTask(task.value.taskId)
    ElMessage.success('任务已完成')
    loadTask()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  } finally {
    submitting.value = false
  }
}

const reportException = async () => {
  try {
    const { value } = await ElMessageBox.prompt('请描述异常情况', '上报异常', {
      confirmButtonText: '提交',
      cancelButtonText: '取消',
      inputPattern: /.+/,
      inputErrorMessage: '请输入异常描述'
    })
    submitting.value = true
    await collectorApi.reportException(task.value.taskId, { description: value })
    ElMessage.success('异常已上报')
    loadTask()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('上报失败')
    }
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}

const getStatusTag = (status) => {
  return getStatusType(status)
}

const getPriorityTag = (priority) => {
  return getPriorityType(priority)
}

onMounted(() => {
  loadTask()
})
</script>

<style scoped>
.collector-page {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-detail {
  padding: 12px 0;
}

.action-bar {
  margin-top: 24px;
  display: flex;
  gap: 12px;
}
</style>
