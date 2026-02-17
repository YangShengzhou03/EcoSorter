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
        <el-table-column label="操作" width="320">
          <template #default="{ row }">
            <el-button size="small" @click="editDevice(row)">编辑</el-button>
            <el-button size="small" type="warning" @click="resetAdminPassword(row)">重置管理员密码</el-button>
            <el-button size="small" type="danger" @click="deleteDevice(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑设备' : '添加设备'" width="700px">
      <el-form :model="deviceForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="deviceForm.deviceId" :disabled="isEdit" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="deviceForm.location" placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="地图选点" prop="mapLocation">
          <div class="map-container">
            <div id="amap-container" class="amap-container"></div>
            <el-button type="primary" size="small" @click="openMapPicker" :icon="Location">在地图上选点</el-button>
          </div>
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

    <el-dialog v-model="mapDialogVisible" title="选择位置" width="800px" :close-on-click-modal="false">
      <div class="map-picker-container">
        <div id="map-picker" class="map-picker"></div>
        <div class="map-info">
          <el-alert type="info" :closable="false">
            <template #title>
              <div class="selected-location">
                <span>已选择位置：</span>
                <strong>{{ selectedLocation.address || '请在地图上点击选择位置' }}</strong>
              </div>
            </template>
          </el-alert>
          <div class="coordinate-info">
            <div class="coord-item">
              <span class="coord-label">纬度：</span>
              <span class="coord-value">{{ selectedLocation.lat }}</span>
            </div>
            <div class="coord-item">
              <span class="coord-label">经度：</span>
              <span class="coord-value">{{ selectedLocation.lng }}</span>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="mapDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmLocation" :disabled="!selectedLocation.lat || !selectedLocation.lng">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
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
const mapDialogVisible = ref(false)
const mapInstance = ref(null)
const markerInstance = ref(null)
const geocoder = ref(null)

const selectedLocation = ref({
  lat: null,
  lng: null,
  address: ''
})

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

const resetAdminPassword = async (row) => {
  try {
    await ElMessageBox.confirm('确定要重置该设备的管理员密码吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await adminApi.resetAdminPassword(row.id)
    ElMessage.success('管理员密码重置成功，新密码为: 123456')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

const loadAMapScript = () => {
  return new Promise((resolve, reject) => {
    if (window.AMap) {
      resolve(window.AMap)
      return
    }
    
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = 'https://webapi.amap.com/maps?v=2.0&key=YOUR_AMAP_KEY&plugin=AMap.Geocoder,AMap.Marker'
    script.onload = () => {
      resolve(window.AMap)
    }
    script.onerror = () => {
      reject(new Error('高德地图加载失败'))
    }
    document.head.appendChild(script)
  })
}

const initMap = async () => {
  try {
    const AMap = await loadAMapScript()
    
    mapInstance.value = new AMap.Map('map-picker', {
      zoom: 11,
      center: [116.397428, 39.90923],
      viewMode: '3D'
    })
    
    mapInstance.value.on('click', onMapClick)
    
    geocoder.value = new AMap.Geocoder({
      city: '全国'
    })
    
    if (deviceForm.value.latitude && deviceForm.value.longitude) {
      const position = [deviceForm.value.longitude, deviceForm.value.latitude]
      mapInstance.value.setCenter(position)
      addMarker(position)
      
      geocoder.value.getAddress(position, (status, result) => {
        if (status === 'complete' && result.info === 'OK') {
          selectedLocation.value.address = result.regeocode.formattedAddress
        }
      })
    }
  } catch (error) {
    ElMessage.error('地图加载失败，请检查网络连接')
  }
}

const addMarker = (position) => {
  if (markerInstance.value) {
    mapInstance.value.remove(markerInstance.value)
  }
  
  markerInstance.value = new window.AMap.Marker({
    position: position,
    map: mapInstance.value,
    animation: 'AMAP_ANIMATION_DROP'
  })
}

const onMapClick = (e) => {
  const lnglat = e.lnglat
  const position = [lnglat.getLng(), lnglat.getLat()]
  
  selectedLocation.value.lat = lnglat.getLat()
  selectedLocation.value.lng = lnglat.getLng()
  
  addMarker(position)
  
  if (geocoder.value) {
    geocoder.value.getAddress(position, (status, result) => {
      if (status === 'complete' && result.info === 'OK') {
        selectedLocation.value.address = result.regeocode.formattedAddress
      }
    })
  }
}

const openMapPicker = async () => {
  mapDialogVisible.value = true
  
  await nextTick()
  
  if (!mapInstance.value) {
    await initMap()
  } else {
    if (deviceForm.value.latitude && deviceForm.value.longitude) {
      const position = [deviceForm.value.longitude, deviceForm.value.latitude]
      mapInstance.value.setCenter(position)
      addMarker(position)
      
      selectedLocation.value.lat = deviceForm.value.latitude
      selectedLocation.value.lng = deviceForm.value.longitude
    }
  }
}

const confirmLocation = () => {
  deviceForm.value.latitude = selectedLocation.value.lat
  deviceForm.value.longitude = selectedLocation.value.lng
  
  if (selectedLocation.value.address && !deviceForm.value.location) {
    deviceForm.value.location = selectedLocation.value.address
  }
  
  mapDialogVisible.value = false
  ElMessage.success('位置已选择')
}

watch(mapDialogVisible, (newVal) => {
  if (!newVal) {
    selectedLocation.value = {
      lat: null,
      lng: null,
      address: ''
    }
  }
})

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

.map-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.amap-container {
  width: 100%;
  height: 200px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.map-picker-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.map-picker {
  width: 100%;
  height: 400px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.map-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.selected-location {
  display: flex;
  align-items: center;
  gap: 8px;
}

.coordinate-info {
  display: flex;
  gap: 16px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}

.coord-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.coord-label {
  font-size: 13px;
  color: #606266;
}

.coord-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
</style>
