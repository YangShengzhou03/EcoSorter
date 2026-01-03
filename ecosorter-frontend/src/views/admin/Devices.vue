<template>
  <div class="admin-devices">
    <div class="table-section">
      <el-table :data="devices" style="width: 100%" border>
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="name" label="设备名称" width="150" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="capacity" label="容量" width="100" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const devices = ref([
  { id: 1, deviceId: 'DEVICE-001', name: '智能垃圾桶1', location: '小区A栋', status: '在线', capacity: 45 },
  { id: 2, deviceId: 'DEVICE-002', name: '智能垃圾桶2', location: '小区B栋', status: '离线', capacity: 80 },
  { id: 3, deviceId: 'DEVICE-003', name: '智能垃圾桶3', location: '小区C栋', status: '故障', capacity: 90 },
  { id: 4, deviceId: 'DEVICE-004', name: '智能垃圾桶4', location: '小区D栋', status: '在线', capacity: 30 },
  { id: 5, deviceId: 'DEVICE-005', name: '智能垃圾桶5', location: '小区E栋', status: '在线', capacity: 60 }
])
</script>

<style scoped>
.admin-devices {
  padding: 0;
}

.table-section {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
}
</style>
