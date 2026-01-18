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
          <span class="stat-label">可用积分（剩余积分）</span>
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
      <el-table :data="records" v-loading="loading" style="width: 100%">
        <el-table-column prop="createdAt" label="投放时间" width="200">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="类型" width="150">
          <template #default="{ row }">
            <el-tag :type="getTagType(row.categoryName)">{{ row.categoryName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="置信度" width="150">
          <template #default="{ row }">
            {{ (row.confidence * 100).toFixed(0) }}%
          </template>
        </el-table-column>
        <el-table-column prop="points" label="积分" width="150">
          <template #default="{ row }">
            <span class="points-plus">+{{ row.points }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="150">
          <template #default="{ row }">
            <el-tag :type="row.status === 'completed' ? 'success' : 'warning'">
              {{ row.status === 'completed' ? '完成' : '待审核' }}
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
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadRecords"
          @current-change="loadRecords"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Document, Coin, Box } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api/user'
import { classificationApi } from '@/api/classification'

const loading = ref(false)
const totalCount = ref(0)
const totalPoints = ref(0)
const totalWeight = ref(0)
const records = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadStatistics = async () => {
  try {
    const response = await userApi.getStatistics()
    totalCount.value = response.totalCount || 0
    totalPoints.value = response.totalPoints || 0
    totalWeight.value = response.totalWeight || 0
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
  }
}

const loadRecords = async () => {
  loading.value = true
  try {
    const response = await classificationApi.getClassificationHistory({
      page: currentPage.value - 1,
      size: pageSize.value
    })
    records.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载记录失败:', error)
    ElMessage.error('加载记录失败')
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getTagType = (type) => {
  const map = { 
    '可回收物': 'success', 
    '厨余垃圾': 'warning', 
    '有害垃圾': 'danger', 
    '其他垃圾': 'info' 
  }
  return map[type] || 'info'
}

onMounted(() => {
  loadStatistics()
  loadRecords()
})
</script>

<style scoped>
.page-container {
  padding: 0;
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
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  border: 1px solid var(--border-color);
}

.points-plus {
  color: #67c23a;
  font-weight: 600;
}
</style>
