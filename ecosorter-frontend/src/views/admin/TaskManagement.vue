<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>待处理异常</span>
        </div>
      </template>
      
      <el-table :data="exceptions" v-loading="loading" style="width: 100%" border empty-text="暂无异常">
        <el-table-column prop="taskId" label="任务编号"/>
        <el-table-column prop="taskLocation" label="任务地点"/>
        <el-table-column prop="reporterName" label="上报人"/>
        <el-table-column prop="exceptionType" label="异常类型">
          <template #default="{ row }">
            <el-tag :type="getExceptionTypeTag(row.exceptionType)">
              {{ getExceptionTypeText(row.exceptionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="详细描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="上报时间">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'pending'"
              type="success" 
              size="small"
              @click="reviewException(row, 'resolved')"
            >
              批准
            </el-button>
            <el-button 
              v-if="row.status === 'pending'"
              type="danger" 
              size="small"
              @click="reviewException(row, 'rejected')"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-card style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>任务管理</span>
          <el-button type="success" @click="generateTasks">
            生成任务
          </el-button>
        </div>
      </template>
          
          <el-table :data="tasks" v-loading="tasksLoading" style="width: 100%" border empty-text="暂无任务">
            <el-table-column prop="taskId" label="任务编号"/>
            <el-table-column prop="location" label="收集点位置"/>
            <el-table-column prop="garbageType" label="垃圾类型">
              <template #default="{ row }">
                <el-tag :type="getGarbageTypeTag(row.garbageType)">
                  {{ row.garbageType }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="estimatedWeight" label="预估重量"/>
            <el-table-column prop="priority" label="优先级">
              <template #default="{ row }">
                <el-tag :type="getPriorityTag(row.priority)">
                  {{ getPriorityText(row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusTag(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="collectorName" label="分配给"/>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button 
                  v-if="row.status === 'pending' || row.status === 'cancelled'"
                  type="primary" 
                  size="small"
                  @click="reassignTask(row)"
                >
                  重新分配
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-card>

    <el-dialog
      v-model="reviewDialogVisible"
      title="审核异常"
      width="500px"
    >
      <el-form :model="reviewForm" label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="reviewForm.status">
            <el-radio label="resolved">批准</el-radio>
            <el-radio label="rejected">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="reviewForm.reviewNotes"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReview" :loading="reviewing">
          确认
        </el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="reassignDialogVisible"
      title="重新分配任务"
      width="500px"
    >
      <el-form :model="reassignForm" label-width="100px">
        <el-form-item label="任务编号">
          <span>{{ reassignForm.taskId }}</span>
        </el-form-item>
        <el-form-item label="当前分配">
          <span>{{ reassignForm.currentCollector }}</span>
        </el-form-item>
        <el-form-item label="分配给">
          <el-select v-model="reassignForm.newCollectorId" placeholder="请选择收集员">
            <el-option
              v-for="collector in collectors"
              :key="collector.id"
              :label="collector.username"
              :value="collector.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reassignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReassign" :loading="reassigning">
          确认分配
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { collectionTaskApi } from '@/api/collectionTask'
import { adminApi } from '@/api/admin'
import { formatTime, getPriorityType, getPriorityText, getStatusType, getStatusText } from '@/utils/helpers'

const getStatusTag = (status) => {
  return getStatusType(status)
}

const getPriorityTag = (priority) => {
  return getPriorityType(priority)
}

defineOptions({
  name: 'AdminTaskManagement'
})

const loading = ref(false)
const tasksLoading = ref(false)
const reviewing = ref(false)
const reassigning = ref(false)
const exceptions = ref([])
const tasks = ref([])
const collectors = ref([])
const reviewDialogVisible = ref(false)
const reassignDialogVisible = ref(false)
const reviewForm = ref({
  exceptionId: null,
  status: '',
  reviewNotes: ''
})
const reassignForm = ref({
  taskId: '',
  currentCollector: '',
  newCollectorId: null
})

const loadExceptions = async () => {
  loading.value = true
  try {
    const response = await collectionTaskApi.getPendingExceptions()
    exceptions.value = response || []
  } catch (error) {
    ElMessage.error('加载异常数据失败')
  } finally {
    loading.value = false
  }
}

const loadTasks = async () => {
  tasksLoading.value = true
  try {
    const response = await collectionTaskApi.getTasksByStatus('pending')
    tasks.value = response || []
  } catch (error) {
    ElMessage.error('加载任务数据失败')
  } finally {
    tasksLoading.value = false
  }
}

const loadCollectors = async () => {
  try {
    const response = await adminApi.getUsers()
    collectors.value = response.filter(user => user.role === 'COLLECTOR')
  } catch (error) {
    ElMessage.error('加载收集员数据失败')
  }
}

const reviewException = (exception, status) => {
  reviewForm.value = {
    exceptionId: exception.id,
    status: status || 'resolved',
    reviewNotes: ''
  }
  reviewDialogVisible.value = true
}

const confirmReview = async () => {
  reviewing.value = true
  try {
    await collectionTaskApi.reviewException(
      reviewForm.value.exceptionId,
      {
        status: reviewForm.value.status,
        reviewNotes: reviewForm.value.reviewNotes
      }
    )
    reviewDialogVisible.value = false
    ElMessage.success('审核完成')
    loadExceptions()
    loadTasks()
  } catch (error) {
    ElMessage.error('审核失败')
  } finally {
    reviewing.value = false
  }
}

const reassignTask = (task) => {
  reassignForm.value = {
    taskId: task.taskId,
    currentCollector: task.collectorName || '未分配',
    newCollectorId: null
  }
  reassignDialogVisible.value = true
}

const confirmReassign = async () => {
  if (!reassignForm.value.newCollectorId) {
    ElMessage.warning('请选择收集员')
    return
  }
  
  reassigning.value = true
  try {
    await collectionTaskApi.reassignTask(
      reassignForm.value.taskId,
      reassignForm.value.newCollectorId
    )
    reassignDialogVisible.value = false
    ElMessage.success('任务已重新分配')
    loadTasks()
  } catch (error) {
    ElMessage.error('重新分配失败')
  } finally {
    reassigning.value = false
  }
}

const generateTasks = async () => {
  try {
    await collectionTaskApi.generateTasks()
    ElMessage.success('任务生成成功')
    loadTasks()
  } catch (error) {
    ElMessage.error('生成任务失败')
  }
}

const getExceptionTypeTag = (type) => {
  const typeMap = {
    'DEVICE_FAILURE': 'danger',
    'ROAD_BLOCKED': 'warning',
    'ACCESS_DENIED': 'info',
    'OTHER': 'info'
  }
  return typeMap[type] || 'info'
}

const getExceptionTypeText = (type) => {
  const typeMap = {
    'DEVICE_FAILURE': '设备故障',
    'ROAD_BLOCKED': '道路不通',
    'ACCESS_DENIED': '无法进入',
    'OTHER': '其他'
  }
  return typeMap[type] || type
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

onMounted(async () => {
  await Promise.all([
    loadExceptions(),
    loadTasks(),
    loadCollectors()
  ])
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}
</style>
