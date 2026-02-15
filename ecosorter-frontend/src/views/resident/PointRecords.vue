<template>
  <div class="admin-page">
    <el-row :gutter="16" class="stats-row">
      <el-col :span="24">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon points-icon">
              <el-icon><Coin /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">当前积分</div>
              <div class="stat-value">{{ points }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="records-card">
      <template #header>
        <div class="card-header">
          <span>积分明细</span>
          <el-button size="small" @click="loadRecords">刷新</el-button>
        </div>
      </template>

      <el-table :data="records" v-loading="loading" border style="width: 100%">
        <el-table-column prop="createdAt" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
        <el-table-column prop="pointsChange" label="积分变动" width="120">
          <template #default="{ row }">
            <span :class="row.pointsChange > 0 ? 'plus' : 'minus'">
              {{ row.pointsChange > 0 ? '+' : '' }}{{ row.pointsChange }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="120" />
      </el-table>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadRecords"
          @current-change="loadRecords"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Coin } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { pointApi } from '@/api/point'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'ResidentPointRecords'
})

const loading = ref(false)
const points = ref(0)
const records = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadRecords = async () => {
  loading.value = true
  try {
    const [statsResponse, recordsResponse] = await Promise.all([
      userApi.getStatistics(),
      pointApi.getRecordsPage(currentPage.value - 1, pageSize.value)
    ])
    points.value = statsResponse.totalPoints || 0
    records.value = recordsResponse.content || []
    total.value = recordsResponse.totalElements || 0
  } catch (error) {
    ElMessage.error('加载积分数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRecords()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.stats-row {
  margin-bottom: 16px;
}

.stat-card {
  margin-bottom: 0;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.points-icon {
  background: #fdf6ec;
  color: #e6a23c;
}

.stat-icon .el-icon {
  font-size: 24px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.records-card {
  margin-bottom: 16px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
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
