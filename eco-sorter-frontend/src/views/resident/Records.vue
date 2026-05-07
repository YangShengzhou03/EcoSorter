<template>
  <div class="resident-page">
    <el-card class="records-card">
      <template #header>
        <div class="card-header">
          <span>投递记录</span>
          <el-button size="small" @click="loadRecords">刷新</el-button>
        </div>
      </template>

      <div class="filter-bar">
        <el-select v-model="filterCategory" placeholder="垃圾分类" @change="loadRecords" clearable style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="可回收物" value="可回收物" />
          <el-option label="厨余垃圾" value="厨余垃圾" />
          <el-option label="有害垃圾" value="有害垃圾" />
          <el-option label="其他垃圾" value="其他垃圾" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="状态" @change="loadRecords" clearable style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="完成" value="completed" />
          <el-option label="待审核" value="pending" />
        </el-select>
      </div>

      <el-table :data="records" v-loading="loading" border style="width: 100%" empty-text="暂无投递记录">
        <el-table-column prop="createdAt" label="投放时间">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="类型">
          <template #default="{ row }">
            <el-tag :type="getTagType(row.categoryName)">{{ row.categoryName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="置信度">
          <template #default="{ row }">
            {{ (row.confidence * 100).toFixed(0) }}%
          </template>
        </el-table-column>
        <el-table-column prop="points" label="积分">
          <template #default="{ row }">
            <span class="points-plus">+{{ row.points }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
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
import { classificationApi } from '@/api/classification'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'ResidentRecords'
})

const loading = ref(false)
const records = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterCategory = ref('')
const filterStatus = ref('')

const loadRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    
    if (filterCategory.value) {
      params.categoryName = filterCategory.value
    }
    
    if (filterStatus.value) {
      params.status = filterStatus.value
    }
    
    const response = await classificationApi.getClassificationHistory(params)
    records.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载记录失败')
  } finally {
    loading.value = false
  }
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
  loadRecords()
})
</script>

<style scoped>
.points-plus {
  color: #67c23a;
  font-weight: 600;
}
</style>
