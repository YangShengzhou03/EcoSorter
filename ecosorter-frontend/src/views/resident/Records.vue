<template>
  <div class="page-container">
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon count-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ totalCount }}</span>
          <span class="stat-label">总次数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon points-icon">
          <el-icon><Coin /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ totalPoints }}</span>
          <span class="stat-label">总积分</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon weight-icon">
          <el-icon><Box /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ totalWeight }}kg</span>
          <span class="stat-label">总重量</span>
        </div>
      </div>
    </div>

    <div class="section-card">
      <el-table :data="records" style="width: 100%">
        <el-table-column prop="time" label="投放时间" width="160" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTagType(row.type)">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="重量(kg)" width="100" />
        <el-table-column prop="points" label="积分" width="100">
          <template #default="{ row }">
            <span class="points-plus">+{{ row.points }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Document, Coin, Box } from '@element-plus/icons-vue'

const totalCount = ref(156)
const totalPoints = ref(2340)
const totalWeight = ref(187.5)

const records = ref([
  { id: 'REC20231201001', time: '2023-12-01 14:30', type: '塑料', weight: 1.2, points: 15, status: '完成' },
  { id: 'REC20231201002', time: '2023-12-01 09:15', type: '纸张', weight: 0.8, points: 12, status: '完成' },
  { id: 'REC20231130001', time: '2023-11-30 16:45', type: '玻璃', weight: 2.1, points: 25, status: '完成' },
  { id: 'REC20231129003', time: '2023-11-29 11:20', type: '金属', weight: 1.5, points: 18, status: '待审核' },
  { id: 'REC20231128005', time: '2023-11-28 08:45', type: '厨余', weight: 0.6, points: 8, status: '完成' }
])

const getTagType = (type) => {
  const map = { '塑料': 'primary', '纸张': 'success', '玻璃': 'warning', '金属': 'info', '厨余': 'danger' }
  return map[type] || 'info'
}
</script>

<style scoped>
.page-container {
  padding: 16px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid #e8eaed;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon .el-icon {
  font-size: 22px;
}

.count-icon {
  background: #e8f4ff;
  color: #1976d2;
}

.points-icon {
  background: #e8f5e9;
  color: #2e7d32;
}

.weight-icon {
  background: #ffebee;
  color: #c62828;
}

.stat-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.4;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
  margin-top: 4px;
}

.section-card {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e8eaed;
}

.points-plus {
  color: #67c23a;
  font-weight: 600;
}
</style>
