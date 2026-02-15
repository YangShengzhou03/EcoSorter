<template>
  <div class="collector-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>通知列表</span>
          <el-button size="small" @click="loadNotices">刷新</el-button>
        </div>
      </template>

      <el-table :data="notices" v-loading="loading" border style="width: 100%" empty-text="暂无通知">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="createdAt" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewNotice(row)">查看</el-button>
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
          @size-change="loadNotices"
          @current-change="loadNotices"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { noticeApi } from '@/api/notice'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'CollectorNotifications'
})

const router = useRouter()
const loading = ref(false)
const notices = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadNotices = async () => {
  loading.value = true
  try {
    const response = await noticeApi.getPublished()
    notices.value = response || []
    total.value = response.length || 0
  } catch (error) {
    ElMessage.error('加载通知失败')
  } finally {
    loading.value = false
  }
}

const viewNotice = (notice) => {
  router.push(`/collector/notifications/${notice.id}`)
}

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
.collector-page {
  padding: 0;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
