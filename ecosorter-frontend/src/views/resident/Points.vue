<template>
  <div class="page-container">
    <div class="section-card balance-card">
      <div class="balance-main">
        <div class="balance-icon">
          <el-icon><Coin /></el-icon>
        </div>
        <div class="balance-info">
          <span class="balance-label">当前积分</span>
          <span class="balance-value">{{ points }}</span>
        </div>
      </div>
      <div class="balance-stats">
        <div class="stat-item">
          <span class="stat-value earned">{{ totalEarned }}</span>
          <span class="stat-label">累计获得</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-value used">{{ totalUsed }}</span>
          <span class="stat-label">已使用</span>
        </div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="points-tabs">
      <el-tab-pane label="积分商城" name="shop">
        <div class="shop-section">
          <div class="products-grid" v-loading="loading">
            <div class="product-card" v-for="product in products" :key="product.id" @click="viewProductDetail(product)">
              <div class="product-image">
                <img :src="product.imageUrl || '/placeholder.png'" :alt="product.name"></img>
              </div>
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-points">
                  <el-icon><Coin /></el-icon>
                  <span>{{ product.points }} 积分</span>
                </div>
                <div class="product-stock">库存: {{ product.stock }}</div>
              </div>
            </div>
          </div>

          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[8, 16, 24]"
            layout="total, sizes, prev, pager, next"
            @size-change="loadProducts"
            @current-change="loadProducts"
            class="pagination"
          />
        </div>
      </el-tab-pane>

      <el-tab-pane label="我的订单" name="orders">
        <div class="orders-section">
          <div class="orders-list" v-loading="ordersLoading">
            <div class="order-card" v-for="order in orders" :key="order.id">
              <div class="order-header">
                <div class="order-id">订单号: {{ order.id }}</div>
                <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
              </div>
              <div class="order-content">
                <div class="order-product">
                  <img :src="order.productImageUrl || '/placeholder.png'" :alt="order.productName"></img>
                  <div class="product-details">
                    <div class="product-name">{{ order.productName }}</div>
                    <div class="product-quantity">数量: {{ order.quantity }}</div>
                    <div class="product-points">消耗积分: {{ order.totalPoints }}</div>
                  </div>
                </div>
                <div class="order-info">
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
                  <div class="info-item">
                    <span class="info-label">下单时间:</span>
                    <span class="info-value">{{ formatTime(order.createdAt) }}</span>
                  </div>
                </div>
              </div>
            </div>

            <el-pagination
              v-model:current-page="orderCurrentPage"
              v-model:page-size="orderPageSize"
              :total="orderTotal"
              :page-sizes="[5, 10, 20]"
              layout="total, sizes, prev, pager, next"
              @size-change="loadOrders"
              @current-change="loadOrders"
              class="pagination"
            />
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="积分明细" name="records">
        <el-table :data="records" v-loading="recordsLoading" style="width: 100%">
          <el-table-column prop="createdAt" label="时间" width="160">
            <template #default="{ row }">
              {{ formatTime(row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" />
          <el-table-column prop="pointsChange" label="积分变动" width="100">
            <template #default="{ row }">
              <span :class="row.pointsChange > 0 ? 'plus' : 'minus'">
                {{ row.pointsChange > 0 ? '+' : '' }}{{ row.pointsChange }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="balance" label="余额" width="100" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Coin } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { productApi } from '@/api/product'
import { orderApi } from '@/api/order'
import { pointApi } from '@/api/point'

const router = useRouter()

const activeTab = ref('shop')
const loading = ref(false)
const ordersLoading = ref(false)
const recordsLoading = ref(false)
const submitting = ref(false)

const points = ref(0)
const totalEarned = ref(0)
const totalUsed = ref(0)
const records = ref([])

const products = ref([])
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)
const selectedCategory = ref('')

const orders = ref([])
const orderCurrentPage = ref(1)
const orderPageSize = ref(10)
const orderTotal = ref(0)

const loadPoints = async () => {
  recordsLoading.value = true
  try {
    const [statsResponse, recordsResponse] = await Promise.all([
      userApi.getStatistics(),
      pointApi.getRecords()
    ])
    points.value = statsResponse.totalPoints || 0
    totalEarned.value = statsResponse.totalPoints || 0
    totalUsed.value = 0
    records.value = recordsResponse.map(record => ({
      ...record,
      pointsChange: record.points,
      balance: 0
    }))
    
    const earned = records.value.filter(r => r.points > 0).reduce((sum, r) => sum + r.points, 0)
    const used = records.value.filter(r => r.points < 0).reduce((sum, r) => sum + Math.abs(r.points), 0)
    totalEarned.value = earned
    totalUsed.value = used
  } catch (error) {
    console.error('加载积分数据失败:', error)
    ElMessage.error('加载积分数据失败')
  } finally {
    recordsLoading.value = false
  }
}

const loadProducts = async () => {
  loading.value = true
  try {
    const response = await productApi.getList({
      page: currentPage.value,
      pageSize: pageSize.value,
      category: selectedCategory.value,
      status: 'available'
    })
    products.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载商品列表失败:', error)
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

const loadOrders = async () => {
  ordersLoading.value = true
  try {
    const response = await orderApi.getList({
      page: orderCurrentPage.value,
      pageSize: orderPageSize.value
    })
    orders.value = response.content || []
    orderTotal.value = response.totalElements || 0
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('加载订单列表失败')
  } finally {
    ordersLoading.value = false
  }
}

const viewProductDetail = (product) => {
  router.push(`/resident/product/${product.id}`)
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
  loadPoints()
  loadProducts()
  loadOrders()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.section-card {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  border: 1px solid var(--border-color);
  margin-bottom: 16px;
}

.balance-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #e8f5e9;
  color: #1f2937;
}

.balance-main {
  display: flex;
  align-items: center;
  gap: 16px;
}

.balance-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.balance-icon .el-icon {
  font-size: 24px;
  color: #2e7d32;
}

.balance-info {
  display: flex;
  flex-direction: column;
}

.balance-label {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 4px;
}

.balance-value {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
}

.balance-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 22px;
  font-weight: 600;
  color: #1f2937;
}

.stat-value.earned {
  color: #1f2937;
}

.stat-value.used {
  color: #6b7280;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #e8eaed;
}

.points-tabs {
  margin-top: 16px;
}

.shop-section {
  padding: 16px 0;
}

.filter-bar {
  margin-bottom: 16px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.product-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 12px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-points {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #f59e0b;
  font-weight: 600;
  margin-bottom: 4px;
}

.product-stock {
  font-size: 12px;
  color: var(--text-secondary);
}

.orders-section {
  padding: 16px 0;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
}

.order-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}

.order-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.order-id {
  font-size: 14px;
  color: var(--text-secondary);
}

.order-content {
  display: flex;
  gap: 16px;
}

.order-product {
  flex-shrink: 0;
}

.order-product img {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-details .product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.product-quantity,
.product-points {
  font-size: 12px;
  color: var(--text-secondary);
}

.order-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  gap: 8px;
}

.info-label {
  font-size: 13px;
  color: var(--text-secondary);
  flex-shrink: 0;
}

.info-value {
  font-size: 13px;
  color: var(--text-primary);
  flex: 1;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.product-detail {
  display: flex;
  gap: 20px;
}

.detail-image {
  flex-shrink: 0;
}

.detail-image img {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
}

.detail-info {
  flex: 1;
}

.detail-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.detail-points {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #f59e0b;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.detail-stock {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 12px;
}

.detail-description {
  font-size: 14px;
  color: var(--text-primary);
  line-height: 1.6;
  margin-bottom: 20px;
}

.order-form {
  background: var(--bg-light);
  padding: 16px;
  border-radius: 8px;
}

.total-points {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

.total-value {
  font-size: 24px;
  font-weight: 600;
  color: #f59e0b;
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
