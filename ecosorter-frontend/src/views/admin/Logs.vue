<template>
  <div class="admin-logs">
    <div class="table-section">
      <el-table :data="logs" style="width: 100%" border>
        <el-table-column prop="timestamp" label="时间" width="180" />
        <el-table-column prop="level" label="级别" width="100" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="message" label="消息内容" min-width="300" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const logs = ref([
  { id: 1, timestamp: '2024-01-15 10:30:00', level: 'INFO', type: '用户操作', message: '用户 user001 登录成功' },
  { id: 2, timestamp: '2024-01-15 11:20:00', level: 'INFO', type: '设备状态', message: '设备 DEVICE-001 上线' },
  { id: 3, timestamp: '2024-01-15 14:15:00', level: 'WARNING', type: '设备状态', message: '设备 DEVICE-002 离线' },
  { id: 4, timestamp: '2024-01-15 15:45:00', level: 'ERROR', type: '系统错误', message: '数据库连接超时' },
  { id: 5, timestamp: '2024-01-15 16:30:00', level: 'INFO', type: '用户操作', message: '用户 user002 完成垃圾分类' }
])
</script>

<style scoped>
.admin-logs {
  padding: 0;
}

.table-section {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
}
</style>
