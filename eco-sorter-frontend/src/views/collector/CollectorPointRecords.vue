<template>
  <div class="collector-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>积分明细</span>
          <el-button type="primary" @click="goToPoints">返回积分商城</el-button>
        </div>
      </template>

      <el-table :data="records" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column label="积分变动" width="120">
          <template #default="{ row }">
            <span :class="row.points > 0 ? 'plus' : 'minus'">
              {{ row.points > 0 ? '+' : '' }}{{ row.points }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
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
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { collectorApi } from '@/api/collector'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'CollectorPointRecords'
})

const router = useRouter()

const loading = ref(false)
const records = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filters = ref({
  type: '',
  dateRange: null
})

const loadRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    if (filters.value.type) {
      params.type = filters.value.type
    }
    
    if (filters.value.dateRange && filters.value.dateRange.length === 2) {
      params.startDate = filters.value.dateRange[0]
      params.endDate = filters.value.dateRange[1]
    }
    
    const response = await collectorApi.getPointRecords(params)
    records.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载积分记录失败')
  } finally {
    loading.value = false
  }
}

const getTypeTag = (type) => {
  const tagMap = {
    'TASK': 'success',
    'ADMIN': 'warning',
    'OTHER': 'info'
  }
  return tagMap[type] || 'info'
}

const getTypeText = (type) => {
  const textMap = {
    'TASK': '任务完成',
    'ADMIN': '管理员调整',
    'OTHER': '其他'
  }
  return textMap[type] || type
}

const goToPoints = () => {
  router.push('/collector/points')
}

onMounted(() => {
  loadRecords()
})
</script>

<style scoped>
.collector-page {
  padding: 0;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.filter-section {
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
