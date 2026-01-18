<template>
  <div class="device-status">
    <el-card>
      <template #header>
        <span>设备状态监控</span>
      </template>
      
      <el-table :data="deviceList" v-loading="loading" style="width: 100%">
        <el-table-column prop="deviceId" label="设备编号" width="140" />
        <el-table-column prop="name" label="设备名称" min-width="150" />
        <el-table-column prop="location" label="设备位置" min-width="200" />
        <el-table-column prop="capacity" label="当前容量" width="100">
          <template #default="{ row }">
            <el-progress :percentage="row.capacity" :color="getCapacityColor(row.capacity)" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="设备状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新" width="160">
          <template #default="{ row }">
            {{ formatTime(row.lastUpdate) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const loading = ref(false)
const deviceList = ref([])

const loadDeviceStatus = async () => {
  loading.value = true
  try {
    const response = await adminApi.getDevices()
    deviceList.value = response || []
  } catch (error) {
    console.error('加载设备状态失败:', error)
    ElMessage.error('加载设备状态失败')
  } finally {
    loading.value = false
  }
}

const getStatusTag = (status) => {
  const statusMap = {
    '正常': 'success',
    '异常': 'danger',
    '维护中': 'warning'
  }
  return statusMap[status] || 'info'
}

const getCapacityColor = (capacity) => {
  if (capacity >= 90) return '#F56C6C'
  if (capacity >= 75) return '#E6A23C'
  if (capacity >= 50) return '#409EFF'
  return '#67C23A'
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
  loadDeviceStatus()
})
</script>

<style scoped>
.device-status {
  padding: 0;
}
</style>
