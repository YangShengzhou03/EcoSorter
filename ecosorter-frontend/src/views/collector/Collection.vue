<template>
  <div class="collection-page">
    <el-card class="task-card" v-if="currentTask">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务编号">{{ currentTask.taskId }}</el-descriptions-item>
        <el-descriptions-item label="收集点位置">{{ currentTask.location }}</el-descriptions-item>
        <el-descriptions-item label="垃圾类型">{{ currentTask.garbageType }}</el-descriptions-item>
        <el-descriptions-item label="预估重量">{{ currentTask.estimatedWeight }} kg</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ currentTask.priority }}</el-descriptions-item>
        <el-descriptions-item label="截止时间">{{ currentTask.deadline }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="scan-card">
          <div class="scan-content">
            <div class="scan-area" @click="startScan">
              <el-icon style="font-size: 48px; color: #409EFF;"><Camera /></el-icon>
              <p>点击扫码识别垃圾桶</p>
            </div>
            
            <div v-if="scanResult" class="scan-result">
              <el-descriptions :column="1" border>
                <el-descriptions-item label="设备编号">{{ scanResult.deviceId }}</el-descriptions-item>
                <el-descriptions-item label="设备位置">{{ scanResult.location }}</el-descriptions-item>
                <el-descriptions-item label="当前容量">{{ scanResult.capacity }}%</el-descriptions-item>
                <el-descriptions-item label="状态">{{ scanResult.status }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="weight-card">
          <div class="weight-content">
            <el-form :model="weightForm" label-width="80px">
              <el-form-item label="实际重量">
                <el-input-number 
                  v-model="weightForm.actualWeight" 
                  :min="0" 
                  :max="1000" 
                  :precision="2"
                  :step="0.1"
                  style="width: 100%;"
                >
                  <template #append>kg</template>
                </el-input-number>
              </el-form-item>
              
              <el-form-item label="称重时间">
                <el-date-picker
                  v-model="weightForm.weightTime"
                  type="datetime"
                  placeholder="选择时间"
                  style="width: 100%;"
                />
              </el-form-item>
              
              <el-form-item label="备注">
                <el-input
                  v-model="weightForm.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注信息"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="recordWeight" style="width: 100%;">
                  记录重量
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px;">
      <el-table :data="processRecords" style="width: 100%">
        <el-table-column prop="time" label="时间" width="160" />
        <el-table-column prop="title" label="操作" width="120" />
        <el-table-column prop="content" label="详情" min-width="200" />
      </el-table>
    </el-card>

    <div class="action-buttons" v-if="currentTask">
      <el-button type="success" @click="completeCollection">
        完成清运
      </el-button>
      <el-button type="danger" @click="reportProblem">
        问题上报
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Camera } from '@element-plus/icons-vue'

const currentTask = ref({
  taskId: 'T001',
  location: '小区A栋垃圾收集点',
  garbageType: '可回收物',
  estimatedWeight: 25.5,
  priority: '高',
  status: 'in_progress',
  deadline: '2024-01-15 17:00'
})

const scanResult = ref(null)

const weightForm = ref({
  actualWeight: 0,
  weightTime: new Date(),
  remark: ''
})

const processRecords = ref([
  {
    time: '2024-01-15 14:30:00',
    title: '开始清运任务',
    content: '到达指定收集点，开始清运作业'
  },
  {
    time: '2024-01-15 14:35:00',
    title: '扫描设备',
    content: '成功扫描智能垃圾箱-A01，设备状态正常'
  }
])

const startScan = () => {
  ElMessage.info('正在扫描设备...')
  
  setTimeout(() => {
    scanResult.value = {
      deviceId: 'DEVICE-A01',
      location: '小区A栋垃圾收集点',
      capacity: 75,
      status: '正常'
    }
    
    processRecords.value.push({
      time: new Date().toLocaleString(),
      title: '设备扫描完成',
      content: `成功扫描${scanResult.value.deviceId}，容量${scanResult.value.capacity}%，状态${scanResult.value.status}`
    })
    
    ElMessage.success('设备扫描成功')
  }, 2000)
}

const recordWeight = () => {
  if (weightForm.value.actualWeight <= 0) {
    ElMessage.warning('请输入有效的重量')
    return
  }
  
  processRecords.value.push({
    time: weightForm.value.weightTime.toLocaleString(),
    title: '称重记录',
    content: `实际重量：${weightForm.value.actualWeight}kg，备注：${weightForm.value.remark || '无'}`
  })
  
  ElMessage.success('重量记录成功')
  
  weightForm.value.actualWeight = 0
  weightForm.value.remark = ''
  weightForm.value.weightTime = new Date()
}

const completeCollection = () => {
  ElMessageBox.confirm(
    '确定要完成当前清运任务吗？',
    '确认完成',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    processRecords.value.push({
      time: new Date().toLocaleString(),
      title: '清运完成',
      content: '清运任务已完成，所有垃圾已妥善处理'
    })
    
    currentTask.value.status = 'completed'
    ElMessage.success('清运任务完成！')
  }).catch(() => {
    ElMessage.info('已取消完成操作')
  })
}

const reportProblem = () => {
  ElMessageBox.prompt('请描述遇到的问题：', '问题上报', {
    confirmButtonText: '提交',
    cancelButtonText: '取消',
    inputType: 'textarea'
  }).then(({ value }) => {
    if (value) {
      processRecords.value.push({
        time: new Date().toLocaleString(),
        title: '问题上报',
        content: value
      })
      
      ElMessage.success('问题已上报，管理员会尽快处理')
    } else {
      ElMessage.warning('请输入问题描述')
    }
  }).catch(() => {
    ElMessage.info('已取消上报')
  })
}
</script>

<style scoped>
.collection-page {
  padding: 0;
  min-height: 100%;
}

.task-card {
  margin-bottom: 20px;
}

.scan-card, .weight-card {
  height: 400px;
}

.scan-content {
  text-align: center;
}

.scan-area {
  padding: 60px 20px;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.scan-area:hover {
  border-color: #409EFF;
  background-color: #f0f9ff;
}

.scan-result {
  margin-top: 20px;
  text-align: left;
}

.weight-content {
  padding: 20px 0;
}

.action-buttons {
  margin-top: 30px;
  margin-bottom: 20px;
  text-align: center;
}

.action-buttons .el-button {
  margin: 0 10px;
}
</style>
