<template>
  <div class="admin-page">
    <div class="admin-header">
      <span class="title">设备管理</span>
      <el-button type="danger" @click="logout" plain>退出</el-button>
    </div>
    <div class="admin-body">
      <div class="info-card">
        <div class="card-header">
          <span class="card-title">设备信息</span>
          <div class="card-actions">
            <el-button @click="openEditDialog">编辑</el-button>
            <el-button @click="refreshData">刷新</el-button>
          </div>
        </div>
        <div class="info-content">
          <div class="info-row">
            <div class="info-item"><span class="label">设备名称</span><span class="value">{{ deviceInfo.deviceName || '-'
                }}</span></div>
            <div class="info-item"><span class="label">设备位置</span><span class="value">{{ deviceInfo.location || '-'
                }}</span></div>
          </div>
          <div class="info-row">
            <div class="info-item"><span class="label">垃圾类型</span><el-tag :type="binTypeTag">{{ binTypeLabel }}</el-tag>
            </div>
            <div class="info-item"><span class="label">设备状态</span><el-tag type="success">在线</el-tag></div>
            <div class="info-item"><span class="label">报警阈值</span><span class="value highlight">{{ deviceInfo.threshold
                }}%</span></div>
          </div>
          <div class="capacity-row">
            <span class="label">当前容量</span>
            <div class="capacity-info">
              <el-progress :percentage="capacityPercentage" :color="capacityColor" :stroke-width="14" />
              <span class="capacity-text">{{ deviceInfo.currentCapacity }} / {{ deviceInfo.maxCapacity }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="action-section">
        <div class="action-card">
          <div class="card-header"><span class="card-title">状态切换</span></div>
          <div class="button-row">
            <el-button type="success" @click="changeStatus('online')">在线</el-button>
            <el-button type="warning" @click="changeStatus('maintenance')">维护中</el-button>
            <el-button type="danger" @click="changeStatus('error')">故障</el-button>
          </div>
        </div>
        <div class="action-card">
          <div class="card-header"><span class="card-title">设备操作</span></div>
          <div class="button-row">
            <el-button @click="resetPassword">重置密码</el-button>
            <el-button type="warning" @click="clearData">清空数据</el-button>
            <el-button type="info" @click="clearToken">清除令牌</el-button>
          </div>
        </div>
      </div>
      <div class="fault-card">
        <div class="card-header"><span class="card-title">故障上报</span></div>
        <div class="fault-form">
          <el-select v-model="faultForm.type" placeholder="请选择故障类型" style="width: 180px">
            <el-option label="设备故障" value="DEVICE_FAILURE" />
            <el-option label="道路不通" value="ROAD_BLOCKED" />
            <el-option label="无法进入" value="ACCESS_DENIED" />
            <el-option label="其他" value="OTHER" />
          </el-select>
          <el-input v-model="faultForm.description" placeholder="请输入故障描述" style="flex: 1" />
          <el-button type="primary" @click="submitFault" :loading="submitting">提交上报</el-button>
        </div>
      </div>
    </div>
    <el-dialog v-model="showEditDialog" title="编辑设备" width="420px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="editForm.deviceName" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="editForm.location" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="editForm.binType" style="width: 100%">
            <el-option label="可回收物" value="recyclable" />
            <el-option label="有害垃圾" value="hazardous" />
            <el-option label="厨余垃圾" value="kitchen" />
            <el-option label="其他垃圾" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="容量"><el-input-number v-model="editForm.maxCapacity" :min="1" :max="1000"
            style="width: 100%" /></el-form-item>
        <el-form-item label="阈值"><el-input-number v-model="editForm.threshold" :min="1" :max="100"
            style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveDeviceInfo">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { trashcanApi } from '@/api/trashcan'

defineOptions({ name: 'TrashcanAdmin' })

const router = useRouter()
const showEditDialog = ref(false)
const submitting = ref(false)
const editForm = ref({ deviceName: '', location: '', binType: '', maxCapacity: 100, threshold: 80 })
const faultForm = ref({ type: '', description: '' })
const deviceInfo = ref({ deviceName: '', location: '', binType: '', currentCapacity: 0, maxCapacity: 100, threshold: 80 })

const binTypeMap = { recyclable: { label: '可回收物', tag: 'success' }, hazardous: { label: '有害垃圾', tag: 'danger' }, kitchen: { label: '厨余垃圾', tag: 'warning' }, other: { label: '其他垃圾', tag: 'info' } }
const binTypeLabel = computed(() => binTypeMap[deviceInfo.value.binType]?.label || deviceInfo.value.binType)
const binTypeTag = computed(() => binTypeMap[deviceInfo.value.binType]?.tag || 'info')
const capacityPercentage = computed(() => deviceInfo.value.maxCapacity ? Math.round((deviceInfo.value.currentCapacity / deviceInfo.value.maxCapacity) * 100) : 0)
const capacityColor = computed(() => { const p = capacityPercentage.value; if (p >= 90) return '#f56c6c'; if (p >= 70) return '#e6a23c'; return '#67c23a' })

const loadDeviceInfo = async () => {
  try {
    const res = await trashcanApi.getTrashcanInfo()
    if (res) deviceInfo.value = { deviceName: res.deviceName || '', location: res.location || '', binType: res.binType || '', currentCapacity: res.capacityLevel || 0, maxCapacity: res.maxCapacity || 100, threshold: res.threshold || 80 }
  } catch (e) { }
}

const resetPassword = async () => {
  try {
    await ElMessageBox.confirm('确定重置管理员密码？', '确认', { type: 'warning' })
    const pwd = Math.random().toString(36).slice(-8)
    await trashcanApi.resetAdminPassword(pwd)
    ElMessageBox.alert(`新密码: ${pwd}`, '成功', { type: 'success' })
  } catch (e) { if (e !== 'cancel') ElMessage.error('失败') }
}

const clearData = async () => {
  try {
    await ElMessageBox.confirm('确定清空数据？不可恢复！', '确认', { type: 'warning' })
    await trashcanApi.clearDeviceData()
    ElMessage.success('成功')
    loadDeviceInfo()
  } catch (e) { if (e !== 'cancel') ElMessage.error('失败') }
}

const clearToken = async () => {
  try {
    await ElMessageBox.confirm('清除后需重新激活！', '确认', { type: 'warning' })
    localStorage.removeItem('deviceInitialized')
    localStorage.removeItem('trashcanAdminLoggedIn')
    localStorage.removeItem('token')
    router.push('/init')
  } catch (e) { }
}

const changeStatus = async (status) => {
  const map = { online: '在线', maintenance: '维护中', error: '故障' }
  try {
    await ElMessageBox.confirm(`设置为"${map[status]}"？`, '确认', { type: 'warning' })
    await trashcanApi.updateTrashcanStatus({ status })
    ElMessage.success('成功')
    loadDeviceInfo()
  } catch (e) { if (e !== 'cancel') ElMessage.error('失败') }
}

const submitFault = async () => {
  if (!faultForm.value.type || !faultForm.value.description) { ElMessage.warning('请填写完整'); return }
  try {
    submitting.value = true
    await trashcanApi.reportFault(faultForm.value)
    ElMessage.success('成功')
    faultForm.value = { type: '', description: '' }
  } catch (e) { ElMessage.error('失败') }
  finally { submitting.value = false }
}

const refreshData = async () => { try { await loadDeviceInfo(); ElMessage.success('刷新成功') } catch (e) { ElMessage.error('失败') } }
const openEditDialog = () => { editForm.value = { ...deviceInfo.value }; showEditDialog.value = true }
const saveDeviceInfo = async () => {
  try { await trashcanApi.updateTrashcanInfo(editForm.value); ElMessage.success('成功'); showEditDialog.value = false; loadDeviceInfo() }
  catch (e) { ElMessage.error('失败') }
}
const logout = () => { localStorage.removeItem('trashcanAdminLoggedIn'); router.push('/work') }

onMounted(loadDeviceInfo)
</script>

<style scoped>
.admin-page {
  width: 100vw;
  height: 100vh;
  background: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.admin-header {
  background: white;
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e8e8e8;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.admin-body {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
}

.info-card,
.action-card,
.fault-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card-header {
  padding: 14px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.card-actions {
  display: flex;
  gap: 8px;
}

.info-content {
  padding: 16px 20px;
}

.info-row {
  display: flex;
  gap: 40px;
  margin-bottom: 12px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.info-item .label {
  color: #8c8c8c;
  font-size: 14px;
  min-width: 70px;
}

.info-item .value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

.info-item .value.highlight {
  color: #fa8c16;
  font-weight: 600;
}

.capacity-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.capacity-row .label {
  color: #8c8c8c;
  font-size: 14px;
  min-width: 70px;
}

.capacity-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 16px;
}

.capacity-info .el-progress {
  flex: 1;
}

.capacity-text {
  font-size: 14px;
  color: #595959;
  font-weight: 500;
  min-width: 80px;
}

.action-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.button-row {
  padding: 16px 20px;
  display: flex;
  gap: 12px;
}

.fault-form {
  padding: 16px 20px;
  display: flex;
  gap: 12px;
  align-items: center;
}

@media (max-width: 768px) {
  .admin-body {
    padding: 12px;
  }

  .info-row {
    flex-direction: column;
    gap: 12px;
  }

  .action-section {
    grid-template-columns: 1fr;
  }

  .capacity-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .capacity-info {
    width: 100%;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .capacity-info .el-progress {
    width: 100%;
  }

  .fault-form {
    flex-direction: column;
  }

  .fault-form .el-select,
  .fault-form .el-input,
  .fault-form .el-button {
    width: 100% !important;
  }
}
</style>
