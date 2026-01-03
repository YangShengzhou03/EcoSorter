<template>
  <div class="collector-dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon style="font-size: 32px; color: #409EFF;"><List /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">今日任务</div>
              <div class="stat-value">{{ todayTasks }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon style="font-size: 32px; color: #67C23A;"><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">已完成</div>
              <div class="stat-value">{{ completedTasks }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon style="font-size: 32px; color: #E6A23C;"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">进行中</div>
              <div class="stat-value">{{ inProgressTasks }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon style="font-size: 32px; color: #F56C6C;"><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">异常设备</div>
              <div class="stat-value">{{ abnormalDevices }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="task-card" style="margin-top: 20px;">
      <template #header>
        <span>待处理任务</span>
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
        <el-table-column prop="estimatedWeight" label="预估重量" width="100" />
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="getPriorityTag(row.priority)">
              {{ row.priority }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  List, 
  CircleCheck, 
  Clock, 
  Warning
} from '@element-plus/icons-vue'

const todayTasks = ref(12)
const completedTasks = ref(8)
const inProgressTasks = ref(3)
const abnormalDevices = ref(2)

const taskList = ref([
  {
    taskId: 'T001',
    location: '小区A栋垃圾收集点',
    garbageType: '可回收物',
    estimatedWeight: 25.5,
    priority: '高'
  },
  {
    taskId: 'T002',
    location: '小区B栋垃圾收集点',
    garbageType: '厨余垃圾',
    estimatedWeight: 18.2,
    priority: '中'
  },
  {
    taskId: 'T003',
    location: '小区C栋垃圾收集点',
    garbageType: '有害垃圾',
    estimatedWeight: 5.8,
    priority: '高'
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
.collector-dashboard {
  padding: 0;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 50px;
}

.stat-icon {
  margin-right: 15px;
}

.stat-icon .el-icon {
  font-size: 24px !important;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}
</style>
