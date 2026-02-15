<template>
  <div class="collector-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>设备状态</span>
          <el-button size="small" @click="loadDeviceStatus">刷新</el-button>
        </div>
      </template>

      <div class="filter-bar">
        <el-select v-model="filterStatus" placeholder="设备状态" @change="loadDeviceStatus" clearable style="width: 120px">
          <el-option label="全部" value="" />
          <el-option label="正常" value="正常" />
          <el-option label="异常" value="异常" />
          <el-option label="维护中" value="维护中" />
        </el-select>
      </div>

      <el-table :data="deviceList" v-loading="loading" border style="width: 100%" empty-text="暂无设备">
        <el-table-column prop="deviceId" label="设备编号" width="150" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="capacityLevel" label="容量" width="150">
          <template #default="{ row }">
            <div class="capacity-wrapper">
              <el-progress 
                :percentage="getCapacityPercentage(row)" 
                :color="getCapacityColor(row)"
                :stroke-width="8"
              />
              <span class="capacity-text">{{ row.capacityLevel }} / {{ row.maxCapacity }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadDeviceStatus"
          @current-change="loadDeviceStatus"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { collectorApi } from '@/api/collector'
import { getCapacityColor, getCapacityPercentage, getStatusType, getStatusText } from '@/utils/helpers'

defineOptions({
  name: 'CollectorDeviceStatus'
})

const loading = ref(false)
const deviceList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')

const loadDeviceStatus = async () => {
  loading.value = true
  try {
    const response = await collectorApi.getDevices()
    deviceList.value = response || []
    total.value = response.length || 0
  } catch (error) {
    ElMessage.error('加载设备状态失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDeviceStatus()
})
</script>

<style scoped>
.collector-page {
  padding: 0;
}

.filter-bar {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
}

.capacity-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
}

.capacity-text {
  font-size: 11px;
  color: #606266;
  white-space: nowrap;
}

.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
