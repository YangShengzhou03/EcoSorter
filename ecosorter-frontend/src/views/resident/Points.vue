<template>
  <div class="page-container">
    <div class="section-card balance-card">
      <div class="balance-main">
        <div class="balance-icon">
          <el-icon><Coin /></el-icon>
        </div>
        <div class="balance-info">
          <span class="balance-label">当前积分</span>
          <span class="balance-value">{{ points }}</span>
        </div>
      </div>
      <div class="balance-stats">
        <div class="stat-item">
          <span class="stat-value earned">{{ totalEarned }}</span>
          <span class="stat-label">累计获得</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-value used">{{ totalUsed }}</span>
          <span class="stat-label">已使用</span>
        </div>
      </div>
    </div>

    <div class="section-card">
      <el-table :data="records" style="width: 100%">
        <el-table-column prop="time" label="时间" width="160" />
        <el-table-column prop="desc" label="说明" />
        <el-table-column prop="change" label="积分变动" width="100">
          <template #default="{ row }">
            <span :class="row.change > 0 ? 'plus' : 'minus'">
              {{ row.change > 0 ? '+' : '' }}{{ row.change }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="100" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Coin } from '@element-plus/icons-vue'

const points = ref(356)
const totalEarned = ref(2850)
const totalUsed = ref(2494)

const records = ref([
  { time: '2023-12-01 14:30', desc: '塑料瓶回收获得', change: 15, balance: 356 },
  { time: '2023-12-01 09:15', desc: '纸张回收获得', change: 12, balance: 341 },
  { time: '2023-11-30 16:45', desc: '玻璃瓶回收获得', change: 25, balance: 329 },
  { time: '2023-11-28 10:20', desc: '兑换环保购物袋', change: -50, balance: 304 },
  { time: '2023-11-25 14:00', desc: '金属回收获得', change: 20, balance: 354 }
])
</script>

<style scoped>
.page-container {
  padding: 16px;
}

.section-card {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e8eaed;
  margin-bottom: 16px;
}

.balance-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #e8f5e9;
  color: #1f2937;
}

.balance-main {
  display: flex;
  align-items: center;
  gap: 16px;
}

.balance-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.balance-icon .el-icon {
  font-size: 24px;
  color: #2e7d32;
}

.balance-info {
  display: flex;
  flex-direction: column;
}

.balance-label {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 4px;
}

.balance-value {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
}

.balance-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 22px;
  font-weight: 600;
  color: #1f2937;
}

.stat-value.earned {
  color: #1f2937;
}

.stat-value.used {
  color: #6b7280;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #e8eaed;
}

.plus {
  color: #67c23a;
  font-weight: 600;
}

.minus {
  color: #f56c6c;
  font-weight: 600;
}
</style>
