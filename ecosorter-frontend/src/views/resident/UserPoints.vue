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
            <el-button type="primary" link @click="goToRecords">积分明细</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="products-card">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span>积分商城</span>
            <el-button link type="primary" @click="goToOrders">我的订单</el-button>
          </div>
          <el-select v-model="selectedCategory" placeholder="选择分类" @change="loadProducts" clearable style="width: 150px">
            <el-option label="全部" value="" />
            <el-option label="生活用品" value="生活用品" />
            <el-option label="电子产品" value="电子产品" />
            <el-option label="食品饮料" value="食品饮料" />
            <el-option label="学习用品" value="学习用品" />
          </el-select>
        </div>
      </template>

      <div class="products-grid" v-loading="loading">
        <div class="product-card" v-for="product in products" :key="product.id" @click="viewProductDetail(product)">
          <div class="product-image">
            <img :src="product.imageUrl || '/placeholder.png'" :alt="product.name" />
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
        <el-empty v-if="!loading && products.length === 0" description="暂无商品" />
      </div>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[8, 16, 24]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadProducts"
          @current-change="loadProducts"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Coin } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { productApi } from '@/api/product'

defineOptions({
  name: 'ResidentUserPoints'
})

const router = useRouter()

const loading = ref(false)

const points = ref(0)

const products = ref([])
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)
const selectedCategory = ref('')

const loadPoints = async () => {
  try {
    const response = await userApi.getStatistics()
    points.value = response.totalPoints || 0
  } catch (error) {
    ElMessage.error('加载积分数据失败')
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
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

const viewProductDetail = (product) => {
  router.push(`/resident/product/${product.id}`)
}

const goToOrders = () => {
  router.push('/resident/orders')
}

const goToRecords = () => {
  router.push('/resident/point-records')
}

onMounted(async () => {
  await Promise.all([
    loadPoints(),
    loadProducts()
  ])
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

.products-card,
.orders-card,
.records-card {
  margin-bottom: 16px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.products-grid:deep(.el-empty) {
  grid-column: 1 / -1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.product-card {
  background: #ffffff;
  border: 1px solid #e4e7ed;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.product-card:hover {
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
  padding: 16px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-points {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #e6a23c;
  font-weight: 600;
  margin-bottom: 4px;
}

.product-stock {
  font-size: 12px;
  color: #909399;
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

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-left span {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
