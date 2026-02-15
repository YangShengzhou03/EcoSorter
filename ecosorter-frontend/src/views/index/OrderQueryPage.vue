<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">订单查询</h1>
      <p class="page-subtitle">Order Query</p>
    </div>

    <div class="page-content">
      <div class="section-card">
        <div class="search-bar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="输入订单号或商品名称搜索" 
          class="search-input"
          clearable
          @clear="loadOrders"
          @keyup.enter="loadOrders"
        >
          <template #append>
            <el-button :icon="Search" @click="loadOrders">搜索</el-button>
          </template>
        </el-input>
      </div>

      <div class="orders-list" v-loading="loading">
        <div class="order-card" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <div class="order-info-left">
              <div class="order-id">订单号: {{ order.id }}</div>
              <div class="order-time">{{ formatTime(order.createdAt) }}</div>
            </div>
            <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
          </div>
          
          <div class="order-content">
            <div class="order-product">
              <div class="product-image">
                <img :src="order.productImageUrl || '/placeholder.png'" :alt="order.productName" />
              </div>
              <div class="product-details">
                <div class="product-name">{{ order.productName }}</div>
                <div class="product-quantity">数量: {{ order.quantity }}</div>
                <div class="product-points">
                  <el-icon><Coin /></el-icon>
                  <span>{{ order.totalPoints }} 积分</span>
                </div>
              </div>
            </div>
            
            <div class="order-shipping">
              <div class="shipping-title">收货信息</div>
              <div class="shipping-info">
                <div class="info-item">
                  <span class="info-label">联系人:</span>
                  <span class="info-value">{{ order.contactName }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">联系电话:</span>
                  <span class="info-value">{{ order.contactPhone }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">收货地址:</span>
                  <span class="info-value">{{ order.shippingAddress }}</span>
                </div>
                <div class="info-item" v-if="order.remark">
                  <span class="info-label">备注:</span>
                  <span class="info-value">{{ order.remark }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <div class="order-actions" v-if="order.status === 'pending'">
            <el-button type="danger" size="small" @click="cancelOrder(order.id)">取消订单</el-button>
          </div>
          
          <div class="order-status-info" v-if="order.status === 'shipped'">
            <div class="status-text">订单已发货，请注意查收</div>
          </div>
          
          <div class="order-status-info" v-if="order.status === 'completed'">
            <div class="status-text success">订单已完成</div>
            <div class="status-tip">感谢您的支持，期待再次为您服务</div>
          </div>
        </div>
        
        <el-empty v-if="orders.length === 0 && !loading" description="暂无订单记录" />
      </div>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
        class="pagination"
      />
    </div>

    <div class="section-card">
      <h3 class="info-title">订单状态说明</h3>
      <div class="status-guide">
        <div class="status-item">
          <el-tag type="warning">待发货</el-tag>
          <span class="status-desc">订单已提交，等待商家发货</span>
        </div>
        <div class="status-item">
          <el-tag type="primary">已发货</el-tag>
          <span class="status-desc">商品已发出，请留意物流信息</span>
        </div>
        <div class="status-item">
          <el-tag type="success">已完成</el-tag>
          <span class="status-desc">订单已完成，感谢您的支持</span>
        </div>
        <div class="status-item">
          <el-tag type="info">已取消</el-tag>
          <span class="status-desc">订单已取消，积分已返还</span>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Coin } from '@element-plus/icons-vue'
import { orderApi } from '@/api/order'

defineOptions({
  name: 'IndexOrderQueryPage'
})

const loading = ref(false)
const searchKeyword = ref('')
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadOrders = async () => {
  loading.value = true
  try {
    const response = await orderApi.getList({
      page: currentPage.value,
      pageSize: pageSize.value,
      status: ''
    })
    
    let filteredOrders = response.records || []
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase()
      filteredOrders = filteredOrders.filter(order => 
        order.id.toString().includes(keyword) ||
        order.productName.toLowerCase().includes(keyword)
      )
    }
    
    orders.value = filteredOrders
    total.value = searchKeyword.value ? filteredOrders.length : (response.total || 0)
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const cancelOrder = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消此订单吗？取消后积分将返还到您的账户。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await orderApi.updateStatus(id, 'cancelled')
    ElMessage.success('订单已取消，积分已返还')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
    }
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
.page-container {
  min-height: calc(100vh - 60px);
  background: #f5f5f5;
  padding-bottom: 40px;
}

.page-header {
  background: #1a1a1a;
  padding: 32px 32px 24px;
  text-align: center;
  color: white;
}

.page-title {
  font-size: 28px;
  margin: 0 0 6px;
  font-weight: 600;
}

.page-subtitle {
  font-size: 14px;
  margin: 0;
  opacity: 0.9;
}

.page-content {
  margin: 16px auto 0;
  padding: 0 32px;
}

.section-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid var(--border-color);
  margin-bottom: 16px;
}

.search-bar {
  margin-bottom: 16px;
}

.search-input {
  width: 100%;
}

:deep(.search-input .el-input__wrapper) {
  padding: 6px 11px;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.order-card {
  background: var(--bg-light);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 14px;
  transition: all 0.3s;
}

.order-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}

.order-info-left {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.order-id {
  font-size: 13px;
  color: var(--text-secondary);
  font-weight: 500;
}

.order-time {
  font-size: 11px;
  color: var(--text-secondary);
}

.order-content {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
}

.order-product {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}

.product-image {
  width: 70px;
  height: 70px;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.product-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
}

.product-quantity {
  font-size: 11px;
  color: var(--text-secondary);
}

.product-points {
  display: flex;
  align-items: center;
  gap: 3px;
  color: #f59e0b;
  font-weight: 600;
  font-size: 13px;
}

.order-shipping {
  flex: 1;
}

.shipping-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.shipping-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item {
  display: flex;
  gap: 8px;
}

.info-label {
  font-size: 11px;
  color: var(--text-secondary);
  flex-shrink: 0;
  min-width: 55px;
}

.info-value {
  font-size: 11px;
  color: var(--text-primary);
  flex: 1;
}

.order-actions {
  padding-top: 10px;
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: flex-end;
}

.order-status-info {
  padding-top: 10px;
  border-top: 1px solid var(--border-color);
  text-align: center;
}

.status-text {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 3px;
}

.status-text.success {
  color: #67c23a;
}

.status-tip {
  font-size: 11px;
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
}

.info-title {
  font-size: 16px;
  margin: 0 0 12px 0;
  color: var(--text-primary);
}

.status-guide {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: var(--bg-light);
  border-radius: 6px;
}

.status-desc {
  font-size: 12px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .order-content {
    flex-direction: column;
  }
  
  .status-guide {
    grid-template-columns: 1fr;
  }
}
</style>
