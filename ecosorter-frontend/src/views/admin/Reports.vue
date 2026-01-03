<template>
  <div class="admin-reports">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-number">1,234</div>
        <div class="stat-label">总用户数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">56</div>
        <div class="stat-label">设备总数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">2,456 kg</div>
        <div class="stat-label">总处理量</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">94.2%</div>
        <div class="stat-label">分类准确率</div>
      </div>
    </div>

    <div class="table-section">
      <el-table :data="reportData" style="width: 100%" border>
        <el-table-column prop="time" label="分类时间" width="180" />
        <el-table-column prop="type" label="垃圾类型" width="120" />
        <el-table-column prop="weight" label="重量(kg)" width="100" />
        <el-table-column prop="user" label="用户" width="120" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const reportData = ref([
  { time: '2024-01-15 10:30:00', type: '可回收垃圾', weight: '2.5', user: 'user001' },
  { time: '2024-01-15 11:20:00', type: '有害垃圾', weight: '0.5', user: 'user002' },
  { time: '2024-01-15 14:15:00', type: '湿垃圾', weight: '1.8', user: 'user003' },
  { time: '2024-01-15 15:45:00', type: '干垃圾', weight: '3.2', user: 'user004' },
  { time: '2024-01-15 16:30:00', type: '可回收垃圾', weight: '1.5', user: 'user005' }
])
</script>

<style scoped>
.admin-reports {
  padding: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

.table-section {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
}
</style>
