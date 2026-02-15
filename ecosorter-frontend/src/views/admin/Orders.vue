<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>

      <div class="filter-bar">
        <el-select v-model="filterStatus" placeholder="订单状态" @change="loadOrders" clearable style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="待发货" value="pending" />
          <el-option label="已发货" value="shipped" />
          <el-option label="已完成" value="completed" />
          <el-option label="已取消" value="cancelled" />
        </el-select>
      </div>

      <el-table :data="orders" v-loading="loading" border empty-text="暂无订单">
        <el-table-column prop="id" label="订单号" />
        <el-table-column label="商品信息">
          <template #default="{ row }">
            <div class="table-product-info">
              <img :src="row.productImageUrl || '/placeholder.png'" :alt="row.productName" />
              <div>
                <div class="table-product-name">{{ row.productName }}</div>
                <div class="table-product-detail">数量: {{ row.quantity }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" />
        <el-table-column prop="contactPhone" label="联系电话" />
        <el-table-column prop="shippingAddress" label="收货地址" show-overflow-tooltip />
        <el-table-column prop="trackingNumber" label="快递单号" />
        <el-table-column prop="totalPoints" label="消耗积分" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
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
    </el-card>

    <el-dialog v-model="statusDialogVisible" title="订单详情" width="600px">
      <div class="order-detail" v-if="selectedOrder">
        <div class="detail-section">
          <h4>商品信息</h4>
          <div class="detail-product-info">
            <img :src="selectedOrder.productImageUrl || '/placeholder.png'" :alt="selectedOrder.productName" />
            <div>
              <div class="product-name">{{ selectedOrder.productName }}</div>
              <div class="product-detail">数量: {{ selectedOrder.quantity }}</div>
              <div class="product-detail">消耗积分: {{ selectedOrder.totalPoints }}</div>
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
            <span class="value">{{ formatTime(selectedOrder.createdAt) }}</span>
          </div>
          <div class="info-row">
            <span class="label">订单状态:</span>
            <el-tag :type="getStatusType(selectedOrder.status)">
              {{ getStatusText(selectedOrder.status) }}
            </el-tag>
          </div>
          <div class="info-row" v-if="selectedOrder.status === 'shipped' || selectedOrder.status === 'completed'">
            <span class="label">快递单号:</span>
            <span class="value">{{ selectedOrder.trackingNumber || '-' }}</span>
          </div>
          <div class="info-row" v-if="selectedOrder.status === 'shipped' || selectedOrder.status === 'completed'">
            <span class="label">更新快递单号:</span>
            <el-input v-model="trackingNumberInput" placeholder="请输入快递单号" style="width: 200px" />
            <el-button type="primary" size="small" @click="updateTrackingNumber" style="margin-left: 10px">更新</el-button>
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
import { formatTime, getStatusType, getStatusText } from '@/utils/helpers'

defineOptions({
  name: 'AdminOrders'
})

const loading = ref(false)
const statusDialogVisible = ref(false)
const selectedOrder = ref(null)
const trackingNumberInput = ref('')

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
    orders.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const openStatusDialog = (row) => {
  selectedOrder.value = row
  trackingNumberInput.value = row.trackingNumber || ''
  statusDialogVisible.value = true
}

const confirmShipment = async () => {
  try {
    await orderApi.updateStatus(selectedOrder.value.id, 'shipped')
    ElMessage.success('发货成功')
    statusDialogVisible.value = false
    loadOrders()
  } catch (error) {
    ElMessage.error('发货失败')
  }
}

const updateTrackingNumber = async () => {
  if (!trackingNumberInput.value.trim()) {
    ElMessage.warning('请输入快递单号')
    return
  }
  try {
    await orderApi.updateTrackingNumber(selectedOrder.value.id, trackingNumberInput.value)
    ElMessage.success('快递单号更新成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('更新快递单号失败')
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
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.table-product-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.table-product-info img {
  width: 50px;
  height: 50px;
  border-radius: 0;
  object-fit: cover;
}

.table-product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.table-product-detail {
  font-size: 12px;
  color: var(--text-secondary);
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

.detail-product-info {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: var(--bg-light);
  border-radius: 0;
}

.detail-product-info img {
  width: 80px;
  height: 80px;
  border-radius: 0;
  object-fit: cover;
}

.detail-product-info .product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.detail-product-info .product-detail {
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
