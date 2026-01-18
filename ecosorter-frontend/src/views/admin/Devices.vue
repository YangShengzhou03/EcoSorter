<template>
  <div class="admin-devices">
    <div class="table-section">
      <div class="table-header">
        <h2>设备管理</h2>
        <el-button type="primary" @click="showCreateDialog">添加设备</el-button>
      </div>
      
      <el-table :data="devices" style="width: 100%" border v-loading="loading">
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="name" label="设备名称" width="150" />
        <el-table-column prop="location" label="位置" />
        <el-table-column label="容量" width="200">
          <template #default="{ row }">
            <el-progress 
              :percentage="getCapacityPercentage(row)" 
              :color="getCapacityColor(row)"
              :status="getCapacityStatus(row)"
            />
            <span class="capacity-text">{{ row.capacity }} / {{ row.maxCapacity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="threshold" label="阈值(%)" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新" width="180">
          <template #default="{ row }">
            {{ formatTime(row.lastUpdate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="editDevice(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteDevice(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑设备' : '添加设备'" width="500px">
      <el-form :model="deviceForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="deviceForm.deviceId" :disabled="isEdit" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="deviceForm.location" placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="最大容量" prop="maxCapacity">
          <el-input-number v-model="deviceForm.maxCapacity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="告警阈值(%)" prop="threshold">
          <el-input-number v-model="deviceForm.threshold" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="deviceForm.status" placeholder="请选择状态">
            <el-option label="在线" value="online" />
            <el-option label="离线" value="offline" />
            <el-option label="错误" value="error" />
            <el-option label="维护中" value="maintenance" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@/api/admin'

const devices = ref([])
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const currentDeviceId = ref(null)

const deviceForm = ref({
  deviceId: '',
  location: '',
  maxCapacity: 100,
  threshold: 80,
  status: 'online'
})

const rules = {
  deviceId: [
    { required: true, message: '请输入设备ID', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入设备位置', trigger: 'blur' }
  ],
  maxCapacity: [
    { required: true, message: '请输入最大容量', trigger: 'blur' }
  ],
  threshold: [
    { required: true, message: '请输入告警阈值', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const loadDevices = async () => {
  loading.value = true
  try {
    const response = await adminApi.getDevices()
    devices.value = response || []
  } catch (error) {
    console.error('加载设备数据失败:', error)
    ElMessage.error('加载设备数据失败')
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  currentDeviceId.value = null
  deviceForm.value = {
    deviceId: '',
    location: '',
    maxCapacity: 100,
    threshold: 80,
    status: 'online'
  }
  dialogVisible.value = true
}

const editDevice = (row) => {
  isEdit.value = true
  currentDeviceId.value = row.id
  deviceForm.value = {
    deviceId: row.deviceId,
    location: row.location,
    maxCapacity: row.maxCapacity,
    threshold: row.threshold,
    status: row.status
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await adminApi.updateDevice(currentDeviceId.value, deviceForm.value)
      ElMessage.success('更新设备成功')
    } else {
      await adminApi.createDevice(deviceForm.value)
      ElMessage.success('添加设备成功')
    }
    
    dialogVisible.value = false
    loadDevices()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

const deleteDevice = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该设备吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await adminApi.deleteDevice(row.id)
    ElMessage.success('删除成功')
    loadDevices()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const getCapacityPercentage = (row) => {
  if (!row.maxCapacity || row.maxCapacity === 0) return 0
  return Math.round((row.capacity / row.maxCapacity) * 100)
}

const getCapacityColor = (row) => {
  const percentage = getCapacityPercentage(row)
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= row.threshold) return '#e6a23c'
  return '#67c23a'
}

const getCapacityStatus = (row) => {
  const percentage = getCapacityPercentage(row)
  if (percentage >= 90) return 'exception'
  if (percentage >= row.threshold) return 'warning'
  return 'success'
}

const getStatusType = (status) => {
  const statusMap = {
    'online': 'success',
    'offline': 'info',
    'error': 'danger',
    'maintenance': 'warning'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'online': '在线',
    'offline': '离线',
    'error': '错误',
    'maintenance': '维护中'
  }
  return statusMap[status] || status
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
  loadDevices()
})
</script>

<style scoped>
.admin-devices {
  padding: 0;
}

.table-section {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.table-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.capacity-text {
  font-size: 12px;
  color: var(--text-secondary);
  margin-left: 8px;
}
</style>
