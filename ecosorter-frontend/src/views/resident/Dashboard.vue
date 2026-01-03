<template>
  <div class="page-container">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon count-icon">
          <el-icon><List /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalCount }}</span>
          <span class="stat-label">累计投放次数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon weight-icon">
          <el-icon><Box /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalWeight }}kg</span>
          <span class="stat-label">累计回收重量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon points-icon">
          <el-icon><Coin /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ totalPoints }}</span>
          <span class="stat-label">累计获得积分</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon carbon-icon">
          <el-icon><Food /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ carbonSaved }}kg</span>
          <span class="stat-label">减少碳排放</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Coin, Box, Food, List } from '@element-plus/icons-vue'

const totalPoints = ref(1256)
const totalCount = ref(156)
const totalWeight = ref(187.5)
const carbonSaved = ref(125)
</script>

<style scoped>
.page-container {
  padding: 16px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
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

.weight-icon {
  background: #ffebee;
  color: #c62828;
}

.points-icon {
  background: #e8f5e9;
  color: #2e7d32;
}

.carbon-icon {
  background: #f3e5f5;
  color: #7b1fa2;
}

.stat-content {
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
</style>
