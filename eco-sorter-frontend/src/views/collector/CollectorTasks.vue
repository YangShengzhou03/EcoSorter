<template>
  <div class="collector-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收集任务</span>
          <el-button size="small" @click="loadTasks">刷新</el-button>
        </div>
      </template>

      <div class="filter-bar">
        <el-select v-model="filterStatus" placeholder="任务状态" @change="loadTasks" clearable style="width: 120px">
          <el-option label="全部" value="" />
          <el-option label="待处理" value="pending" />
          <el-option label="进行中" value="in_progress" />
          <el-option label="已完成" value="completed" />
        </el-select>
        <el-select v-model="filterPriority" placeholder="优先级" @change="loadTasks" clearable style="width: 120px">
          <el-option label="全部" value="" />
          <el-option label="高" value="high" />
          <el-option label="中" value="medium" />
          <el-option label="低" value="low" />
        </el-select>
      </div>

      <el-table :data="taskList" v-loading="loading" border style="width: 100%" empty-text="暂无任务">
        <el-table-column prop="taskId" label="任务ID" width="120" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="garbageType" label="垃圾类型" width="120" />
        <el-table-column prop="estimatedWeight" label="预估重量" width="120">
          <template #default="{ row }">
            {{ row.estimatedWeight }} kg
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityTag(row.priority)" size="small">
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="goToDetail(row)">查看</el-button>
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
          @size-change="loadTasks"
          @current-change="loadTasks"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { collectorApi } from '@/api/collector'
import { useRouter } from 'vue-router'
import { getStatusType, getStatusText, getPriorityType, getPriorityText } from '@/utils/helpers'

defineOptions({
  name: 'CollectorTasks'
})

const router = useRouter()
const loading = ref(false)
const taskList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')
const filterPriority = ref('')

const loadTasks = async () => {
  loading.value = true
  try {
    const response = await collectorApi.getTasks()
    let filteredTasks = response || []
    
    if (filterStatus.value) {
      filteredTasks = filteredTasks.filter(task => task.status === filterStatus.value)
    }
    
    if (filterPriority.value) {
      filteredTasks = filteredTasks.filter(task => task.priority === filterPriority.value)
    }
    
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    taskList.value = filteredTasks.slice(start, end)
    total.value = filteredTasks.length
  } catch (error) {
    ElMessage.error('加载任务数据失败')
  } finally {
    loading.value = false
  }
}

const goToDetail = (task) => {
  router.push(`/collector/tasks/${task.taskId}`)
}

const getPriorityTag = (priority) => {
  return getPriorityType(priority)
}

const getStatusTag = (status) => {
  return getStatusType(status)
}

onMounted(() => {
  loadTasks()
})
</script>

<style scoped>
.collector-page {
  padding: 0;
}

.filter-bar {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
}

.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
