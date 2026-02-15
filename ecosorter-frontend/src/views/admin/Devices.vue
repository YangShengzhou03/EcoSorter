<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>设备管理</span>
          <el-button type="primary" @click="showCreateDialog">添加设备</el-button>
        </div>
      </template>
      
      <el-table :data="devices" style="width: 100%" border v-loading="loading" empty-text="暂无设备">
        <el-table-column prop="deviceId" label="设备ID" />
        <el-table-column prop="location" label="位置" />
        <el-table-column label="容量">
          <template #default="{ row }">
            <el-progress 
              :percentage="getCapacityPercentage(row)" 
              :color="getCapacityColorByRow(row)"
              :status="getCapacityStatus(row)"
            />
            <span class="capacity-text">{{ row.capacityLevel }} / {{ row.maxCapacity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="threshold" label="阈值(%)" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新">
          <template #default="{ row }">
            {{ formatTime(row.lastUpdate) }}
          </template>
        </el-table-column>
        <el-table-column prop="authToken" label="认证令牌" width="200">
          <template #default="{ row }">
            <el-input 
              v-model="row.authToken" 
              readonly 
              size="small" 
              :show-password="true"
              style="width: 100%"
            >
              <template #append>
                <el-button 
                  @click="copyToken(row.authToken)" 
                  size="small"
                  icon="CopyDocument"
                />
              </template>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="editDevice(row)">编辑</el-button>
            <el-button size="small" type="warning" @click="regenerateToken(row)">重新生成令牌</el-button>
            <el-button size="small" type="danger" @click="deleteDevice(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑设备' : '添加设备'" width="500px">
      <el-form :model="deviceForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="deviceForm.deviceId" :disabled="isEdit" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="deviceForm.location" placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input-number v-model="deviceForm.latitude" :precision="8" :min="-90" :max="90" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input-number v-model="deviceForm.longitude" :precision="8" :min="-180" :max="180" placeholder="请输入经度" />
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
import { formatTime, getCapacityColorByRow, getCapacityPercentage, getCapacityStatus, getStatusType, getStatusText } from '@/utils/helpers'

defineOptions({
  name: 'AdminDevices'
})

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
  latitude: null,
  longitude: null,
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
    latitude: null,
    longitude: null,
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
    latitude: row.latitude,
    longitude: row.longitude,
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
      ElMessage.error('删除失败')
    }
  }
}

const copyToken = async (token) => {
  try {
    await navigator.clipboard.writeText(token)
    ElMessage.success('令牌已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const regenerateToken = async (row) => {
  try {
    await ElMessageBox.confirm('确定要重新生成该设备的认证令牌吗？旧令牌将立即失效。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await adminApi.regenerateAuthToken(row.id)
    ElMessage.success('令牌重新生成成功')
    loadDevices()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重新生成令牌失败')
    }
  }
}

onMounted(() => {
  loadDevices()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.capacity-text {
  font-size: 12px;
  color: var(--text-secondary);
  margin-left: 8px;
}
</style>
