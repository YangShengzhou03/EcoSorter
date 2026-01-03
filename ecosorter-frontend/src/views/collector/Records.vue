<template>
  <div class="collector-records">
    <el-card>
      <template #header>
        <span>工作记录</span>
      </template>
      
      <el-table :data="recordList" style="width: 100%">
        <el-table-column prop="taskId" label="任务编号" width="120" />
        <el-table-column prop="location" label="收集点位置" min-width="200" />
        <el-table-column prop="garbageType" label="垃圾类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getGarbageTypeTag(row.garbageType)">
              {{ row.garbageType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="actualWeight" label="实际重量(kg)" width="120" />
        <el-table-column prop="status" label="任务状态" width="100" />
        <el-table-column prop="startTime" label="开始时间" width="160" />
        <el-table-column prop="endTime" label="完成时间" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const recordList = ref([
  {
    taskId: 'T001',
    location: '小区A栋垃圾收集点',
    garbageType: '可回收物',
    actualWeight: 28.2,
    status: '已完成',
    startTime: '2024-01-15 14:30:00',
    endTime: '2024-01-15 15:15:00'
  },
  {
    taskId: 'T002',
    location: '小区B栋垃圾收集点',
    garbageType: '厨余垃圾',
    actualWeight: 19.5,
    status: '已完成',
    startTime: '2024-01-15 16:00:00',
    endTime: '2024-01-15 16:35:00'
  },
  {
    taskId: 'T003',
    location: '小区C栋垃圾收集点',
    garbageType: '有害垃圾',
    actualWeight: 6.2,
    status: '已完成',
    startTime: '2024-01-15 09:00:00',
    endTime: '2024-01-15 09:20:00'
  },
  {
    taskId: 'T004',
    location: '小区D栋垃圾收集点',
    garbageType: '其他垃圾',
    actualWeight: 30.5,
    status: '已完成',
    startTime: '2024-01-15 10:30:00',
    endTime: '2024-01-15 11:10:00'
  }
])

const getGarbageTypeTag = (type) => {
  const typeMap = {
    '可回收物': 'success',
    '厨余垃圾': 'warning',
    '有害垃圾': 'danger',
    '其他垃圾': 'info'
  }
  return typeMap[type] || 'info'
}
</script>

<style scoped>
.collector-records {
  padding: 0;
}
</style>
