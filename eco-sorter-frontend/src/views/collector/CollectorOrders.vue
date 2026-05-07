<template>
  <div class="collector-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的订单</span>
          <el-button type="primary" @click="goToPoints">返回积分商城</el-button>
        </div>
      </template>

      <el-table :data="orders" v-loading="loading" border>
        <el-table-column prop="id" label="订单ID" width="100" />
        <el-table-column label="商品信息" width="300">
          <template #default="{ row }">
            <div class="product-info">
              <img :src="row.productImageUrl || '/placeholder.png'" :alt="row.productName" class="product-thumb" />
              <div class="product-details">
                <div class="product-name">{{ row.productName }}</div>
                <div class="product-quantity">数量: {{ row.quantity }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="积分" width="120">
          <template #default="{ row }">
            <span class="points-value">{{ row.totalPoints }} 积分</span>
          </template>
        </el-table-column>
        <el-table-column prop="trackingNumber" label="快递单号" width="150">
          <template #default="{ row }">
            {{ row.trackingNumber || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="viewOrderDetail(row)">查看详情</el-button>
            <el-button 
              size="small" 
              type="danger" 
              link 
              @click="cancelOrder(row)"
              v-if="row.status === 'PENDING'"
            >
              取消订单
            </el-button>
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
          @size-change="loadOrders"
          @current-change="loadOrders"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { collectorApi } from '@/api/collector'
import { formatTime } from '@/utils/helpers'

defineOptions({
  name: 'CollectorOrders'
})

const router = useRouter()

const loading = ref(false)
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filters = ref({
  status: '',
  dateRange: null
})

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    if (filters.value.status) {
      params.status = filters.value.status
    }
    
    if (filters.value.dateRange && filters.value.dateRange.length === 2) {
      params.startDate = filters.value.dateRange[0]
      params.endDate = filters.value.dateRange[1]
    }
    
    const response = await collectorApi.getOrders(params)
    orders.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const typeMap = {
    'PENDING': 'warning',
    'SHIPPED': 'primary',
    'COMPLETED': 'success',
    'CANCELLED': 'info'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'PENDING': '待发货',
    'SHIPPED': '已发货',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return textMap[status] || status
}

const viewOrderDetail = (order) => {
  router.push(`/collector/order/${order.id}`)
}

const cancelOrder = async (order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await collectorApi.cancelOrder(order.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
    }
  }
}

const goToPoints = () => {
  router.push('/collector/points')
}

onMounted(() => {
  loadOrders()
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

.product-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.product-details {
  flex: 1;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.product-quantity {
  font-size: 12px;
  color: #909399;
}

.points-value {
  color: #e6a23c;
  font-weight: 600;
}
</style>
