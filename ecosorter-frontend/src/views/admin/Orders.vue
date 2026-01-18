<template>
  <div class="orders-page">
    <div class="page-header">
      <h2>订单管理</h2>
    </div>

    <div class="filter-bar">
      <el-select v-model="filterStatus" placeholder="订单状态" @change="loadOrders" clearable style="width: 150px">
        <el-option label="全部" value="" />
        <el-option label="待发货" value="pending" />
        <el-option label="已发货" value="shipped" />
        <el-option label="已完成" value="completed" />
        <el-option label="已取消" value="cancelled" />
      </el-select>
    </div>

    <div class="orders-list">
      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="id" label="订单号" width="100" />
        <el-table-column label="商品信息" min-width="200">
          <template #default="{ row }">
            <div class="product-info">
              <img :src="row.productImageUrl || '/placeholder.png'" :alt="row.productName"></img>
              <div>
                <div class="product-name">{{ row.productName }}</div>
                <div class="product-detail">数量: {{ row.quantity }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="shippingAddress" label="收货地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="totalPoints" label="消耗积分" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              link
              type="primary"
              @click="openStatusDialog(row)"
              v-if="row.status === 'pending'"
            >
              发货
            </el-button>
            <el-button
              link
              type="success"
              @click="updateOrderStatus(row.id, 'completed')"
              v-if="row.status === 'shipped'"
            >
              完成
            </el-button>
            <el-button
              link
              type="info"
              @click="updateOrderStatus(row.id, 'cancelled')"
              v-if="row.status === 'pending'"
            >
              取消
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
    </div>

    <el-dialog v-model="statusDialogVisible" title="订单详情" width="600px">
      <div class="order-detail" v-if="selectedOrder">
        <div class="detail-section">
          <h4>商品信息</h4>
          <div class="product-detail">
            <img :src="selectedOrder.productImageUrl || '/placeholder.png'" :alt="selectedOrder.productName"></img>
            <div>
              <div class="product-name">{{ selectedOrder.productName }}</div>
              <div class="product-info">数量: {{ selectedOrder.quantity }}</div>
              <div class="product-info">消耗积分: {{ selectedOrder.totalPoints }}</div>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <h4>收货信息</h4>
          <div class="info-row">
            <span class="label">联系人:</span>
            <span class="value">{{ selectedOrder.contactName }}</span>
          </div>
          <div class="info-row">
            <span class="label">联系电话:</span>
            <span class="value">{{ selectedOrder.contactPhone }}</span>
          </div>
          <div class="info-row">
            <span class="label">收货地址:</span>
            <span class="value">{{ selectedOrder.shippingAddress }}</span>
          </div>
          <div class="info-row" v-if="selectedOrder.remark">
            <span class="label">备注:</span>
            <span class="value">{{ selectedOrder.remark }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4>订单信息</h4>
          <div class="info-row">
            <span class="label">订单号:</span>
            <span class="value">{{ selectedOrder.id }}</span>
          </div>
          <div class="info-row">
            <span class="label">下单时间:</span>
            <span class="value">{{ formatDate(selectedOrder.createdAt) }}</span>
          </div>
          <div class="info-row">
            <span class="label">订单状态:</span>
            <el-tag :type="getStatusType(selectedOrder.status)">
              {{ getStatusText(selectedOrder.status) }}
            </el-tag>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="statusDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="confirmShipment" v-if="selectedOrder?.status === 'pending'">
          确认发货
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi } from '@/api/order'

const loading = ref(false)
const statusDialogVisible = ref(false)
const selectedOrder = ref(null)

const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')

const loadOrders = async () => {
  loading.value = true
  try {
    const response = await orderApi.getAllList({
      page: currentPage.value,
      pageSize: pageSize.value,
      status: filterStatus.value
    })
    orders.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const openStatusDialog = (row) => {
  selectedOrder.value = row
  statusDialogVisible.value = true
}

const confirmShipment = async () => {
  try {
    await orderApi.updateStatus(selectedOrder.value.id, 'shipped')
    ElMessage.success('发货成功')
    statusDialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('发货失败:', error)
    ElMessage.error('发货失败')
  }
}

const updateOrderStatus = async (id, status) => {
  const statusText = {
    completed: '完成',
    cancelled: '取消'
  }
  
  ElMessageBox.confirm(`确定要${statusText[status]}该订单吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await orderApi.updateStatus(id, status)
      ElMessage.success(`${statusText[status]}成功`)
      loadOrders()
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getStatusType = (status) => {
  const statusMap = {
    pending: 'warning',
    shipped: 'primary',
    completed: 'success',
    cancelled: 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待发货',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
}

.filter-bar {
  margin-bottom: 20px;
}

.orders-list {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  border: 1px solid var(--border-color);
}

.product-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.product-info img {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.product-detail {
  font-size: 12px;
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.order-detail {
  padding: 16px 0;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.product-detail {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: var(--bg-light);
  border-radius: 8px;
}

.product-detail img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.product-detail .product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.product-detail .product-info {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 4px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.info-row .label {
  font-size: 13px;
  color: var(--text-secondary);
  flex-shrink: 0;
  width: 80px;
}

.info-row .value {
  font-size: 13px;
  color: var(--text-primary);
  flex: 1;
}
</style>
