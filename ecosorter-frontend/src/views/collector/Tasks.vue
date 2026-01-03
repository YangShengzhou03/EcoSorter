<template>
  <div class="collector-tasks">
    <el-card>
      <template #header>
        <span>任务管理</span>
      </template>
      
      <el-table :data="taskList" style="width: 100%">
        <el-table-column prop="taskId" label="任务编号" width="120" />
        <el-table-column prop="location" label="收集点位置" />
        <el-table-column prop="garbageType" label="垃圾类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getGarbageTypeTag(row.garbageType)">
              {{ row.garbageType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="estimatedWeight" label="预估重量(kg)" width="120" />
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityTag(row.priority)">
              {{ row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="deadline" label="截止时间" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const taskList = ref([
  {
    taskId: 'T001',
    location: '小区A栋垃圾收集点',
    garbageType: '可回收物',
    estimatedWeight: 25.5,
    priority: '高',
    status: '待接收',
    deadline: '2024-01-15 17:00'
  },
  {
    taskId: 'T002',
    location: '小区B栋垃圾收集点',
    garbageType: '厨余垃圾',
    estimatedWeight: 18.2,
    priority: '中',
    status: '进行中',
    deadline: '2024-01-15 18:00'
  },
  {
    taskId: 'T003',
    location: '小区C栋垃圾收集点',
    garbageType: '有害垃圾',
    estimatedWeight: 5.8,
    priority: '高',
    status: '待接收',
    deadline: '2024-01-15 16:00'
  },
  {
    taskId: 'T004',
    location: '小区D栋垃圾收集点',
    garbageType: '其他垃圾',
    estimatedWeight: 32.1,
    priority: '低',
    status: '已完成',
    deadline: '2024-01-15 19:00'
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

const getPriorityTag = (priority) => {
  const priorityMap = {
    '高': 'danger',
    '中': 'warning',
    '低': 'info'
  }
  return priorityMap[priority] || 'info'
}
</script>

<style scoped>
.collector-tasks {
  padding: 0;
}
</style>
