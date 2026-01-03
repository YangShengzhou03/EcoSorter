<template>
  <div class="device-status">
    <el-card>
      <template #header>
        <span>设备状态监控</span>
      </template>
      
      <el-table :data="deviceList" style="width: 100%">
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
        <el-table-column prop="lastUpdate" label="最后更新" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const deviceList = ref([
  {
    deviceId: 'DEVICE-A01',
    name: '智能垃圾箱-A01',
    location: '小区A栋垃圾收集点',
    capacity: 65,
    status: '正常',
    lastUpdate: '2024-01-15 14:30:00'
  },
  {
    deviceId: 'DEVICE-A02',
    name: '智能垃圾箱-A02',
    location: '小区A栋垃圾收集点',
    capacity: 85,
    status: '异常',
    lastUpdate: '2024-01-15 14:25:00'
  },
  {
    deviceId: 'DEVICE-B01',
    name: '智能垃圾箱-B01',
    location: '小区B栋垃圾收集点',
    capacity: 45,
    status: '正常',
    lastUpdate: '2024-01-15 14:28:00'
  },
  {
    deviceId: 'DEVICE-B02',
    name: '智能垃圾箱-B02',
    location: '小区B栋垃圾收集点',
    capacity: 92,
    status: '异常',
    lastUpdate: '2024-01-15 14:20:00'
  },
  {
    deviceId: 'DEVICE-C01',
    name: '智能垃圾箱-C01',
    location: '小区C栋垃圾收集点',
    capacity: 55,
    status: '正常',
    lastUpdate: '2024-01-15 14:32:00'
  },
  {
    deviceId: 'DEVICE-C02',
    name: '智能垃圾箱-C02',
    location: '小区C栋垃圾收集点',
    capacity: 30,
    status: '正常',
    lastUpdate: '2024-01-15 14:35:00'
  }
])

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
</script>

<style scoped>
.device-status {
  padding: 0;
}
</style>
