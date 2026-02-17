<template>
  <div class="admin-page">
    <div class="admin-header">
      <div class="header-left">
        <h1>垃圾桶管理后台</h1>
        <p>设备管理终端</p>
      </div>
      <el-button type="danger" @click="logout" plain>
        退出登录
      </el-button>
    </div>

    <div class="admin-content">
      <el-card class="info-card">
        <template #header>
          <div class="card-header">
            <span>设备信息</span>
            <div class="header-actions">
              <el-button text @click="openEditDialog">
                编辑
              </el-button>
              <el-button text @click="refreshData">
                刷新
              </el-button>
            </div>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="设备位置">{{ deviceInfo.location }}</el-descriptions-item>
          <el-descriptions-item label="垃圾桶类型">
            <el-tag :type="binTypeTag">{{ binTypeLabel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag type="success">在线</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="当前容量">
            <div class="capacity-display">
              <el-progress 
                :percentage="capacityPercentage" 
                :color="capacityColor"
                :stroke-width="16"
              />
              <span class="capacity-text">{{ deviceInfo.currentCapacity }} / {{ deviceInfo.maxCapacity }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="报警阈值">{{ deviceInfo.threshold }}%</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-dialog v-model="showEditDialog" title="编辑设备信息" width="500px">
        <el-form :model="editForm" label-width="100px">
          <el-form-item label="设备名称">
            <el-input v-model="editForm.deviceName" placeholder="请输入设备名称" />
          </el-form-item>
          <el-form-item label="设备位置">
            <el-input v-model="editForm.location" placeholder="请输入设备位置" />
          </el-form-item>
          <el-form-item label="垃圾桶类型">
            <el-select v-model="editForm.binType" placeholder="请选择垃圾桶类型" style="width: 100%">
              <el-option label="可回收物" value="recyclable" />
              <el-option label="有害垃圾" value="hazardous" />
              <el-option label="厨余垃圾" value="kitchen" />
              <el-option label="其他垃圾" value="other" />
            </el-select>
          </el-form-item>
          <el-form-item label="最大容量">
            <el-input-number v-model="editForm.maxCapacity" :min="1" :max="1000" />
          </el-form-item>
          <el-form-item label="报警阈值">
            <el-input-number v-model="editForm.threshold" :min="1" :max="100" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="saveDeviceInfo">保存</el-button>
        </template>
      </el-dialog>

      <el-card class="action-card" style="margin-top: 20px">
        <template #header>
          <div class="card-header">
            <span>管理操作</span>
          </div>
        </template>
        <div class="action-list">
          <el-button type="primary" @click="resetPassword">
            重置管理员密码
          </el-button>
          <el-button type="warning" @click="clearData">
            清空设备数据
          </el-button>
          <el-button type="info" @click="clearToken">
            清除设备令牌
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'TrashcanAdmin'
})

const router = useRouter()

const showEditDialog = ref(false)
const editForm = ref({
  deviceName: '',
  location: '',
  binType: '',
  maxCapacity: 100,
  threshold: 80
})

const deviceInfo = ref({
  deviceName: '',
  location: '',
  binType: '',
  currentCapacity: 0,
  maxCapacity: 100,
  threshold: 80
})

const binTypeMap = {
  recyclable: { label: '可回收物', tag: 'success' },
  hazardous: { label: '有害垃圾', tag: 'danger' },
  kitchen: { label: '厨余垃圾', tag: 'warning' },
  other: { label: '其他垃圾', tag: 'info' }
}

const binTypeLabel = computed(() => binTypeMap[deviceInfo.value.binType]?.label || deviceInfo.value.binType)
const binTypeTag = computed(() => binTypeMap[deviceInfo.value.binType]?.tag || 'info')

const capacityPercentage = computed(() => {
  if (!deviceInfo.value.maxCapacity || deviceInfo.value.maxCapacity === 0) return 0
  return Math.round((deviceInfo.value.currentCapacity / deviceInfo.value.maxCapacity) * 100)
})

const capacityColor = computed(() => {
  const percentage = capacityPercentage.value
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= 70) return '#e6a23c'
  return '#67c23a'
})

const loadDeviceInfo = async () => {
  try {
    const response = await trashcanApi.getTrashcanInfo()
    if (response) {
      deviceInfo.value = {
        deviceName: response.deviceName || '',
        location: response.location || '',
        binType: response.binType || '',
        currentCapacity: response.capacityLevel || 0,
        maxCapacity: response.maxCapacity || 100,
        threshold: response.threshold || 80
      }
    }
  } catch (error) {
  }
}

const resetPassword = async () => {
  try {
    await ElMessageBox.confirm('确定要重置管理员密码吗？', '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const newPassword = Math.random().toString(36).slice(-8)
    await trashcanApi.resetAdminPassword(newPassword)
    
    ElMessageBox.alert(`新密码: ${newPassword}`, '密码重置成功', {
      confirmButtonText: '确定',
      type: 'success'
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

const clearData = async () => {
  try {
    await ElMessageBox.confirm('确定要清空设备数据吗？此操作不可恢复！', '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await trashcanApi.clearDeviceData()
    ElMessage.success('数据清空成功')
    await loadDeviceInfo()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空数据失败')
    }
  }
}

const clearToken = async () => {
  try {
    await ElMessageBox.confirm('确定要清除设备令牌吗？清除后需要重新激活设备！', '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    localStorage.removeItem('deviceInitialized')
    localStorage.removeItem('trashcanAdminLoggedIn')
    localStorage.removeItem('token')
    
    ElMessage.success('设备令牌已清除')
    router.push('/init')
  } catch (error) {
    if (error !== 'cancel') {
    }
  }
}

const refreshData = async () => {
  try {
    await loadDeviceInfo()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('刷新数据失败')
  }
}

const openEditDialog = () => {
  editForm.value = {
    deviceName: deviceInfo.value.deviceName,
    location: deviceInfo.value.location,
    binType: deviceInfo.value.binType,
    maxCapacity: deviceInfo.value.maxCapacity,
    threshold: deviceInfo.value.threshold
  }
  showEditDialog.value = true
}

const saveDeviceInfo = async () => {
  try {
    await trashcanApi.updateTrashcanInfo(editForm.value)
    ElMessage.success('设备信息更新成功')
    showEditDialog.value = false
    await loadDeviceInfo()
  } catch (error) {
    ElMessage.error('更新设备信息失败')
  }
}

const logout = () => {
  localStorage.removeItem('trashcanAdminLoggedIn')
  router.push('/work')
  ElMessage.success('已退出登录')
}

onMounted(() => {
  loadDeviceInfo()
})
</script>

<style scoped>
.admin-page {
  width: 100vw;
  height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.admin-header {
  background: white;
  padding: 12px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid #ebeef5;
}

.header-left {
  display: flex;
  flex-direction: column;
}

.header-left h1 {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 2px;
}

.header-left p {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

.admin-content {
  flex: 1;
  padding: 24px 32px;
  overflow-y: auto;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
}

.info-card,
.action-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.capacity-display {
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 100%;
}

.capacity-text {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.action-list {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-list .el-button {
  flex: 1;
  min-width: 140px;
}

:deep(.el-card__header) {
  padding: 14px 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
  color: #606266;
  font-size: 14px;
}

:deep(.el-descriptions__content) {
  color: #303133;
  font-size: 14px;
}

@media (max-width: 768px) {
  .admin-header {
    padding: 10px 20px;
  }

  .header-left h1 {
    font-size: 14px;
  }

  .admin-content {
    padding: 16px 20px;
  }

  :deep(.el-descriptions) {
    font-size: 13px;
  }
}
</style>
