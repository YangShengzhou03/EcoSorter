<template>
  <div class="product-detail" v-loading="loading">
    <div class="main-section">
      <div class="gallery">
        <img :src="product?.imageUrl || '/placeholder.png'" :alt="product?.name" class="product-image">
      </div>

      <div class="info">
        <h1 class="name">{{ product?.name }}</h1>

        <div class="sales-info">
          <span class="sales-text">已兑换 {{ product?.totalPurchased || 0 }} 件</span>
          <span class="review-text">好评率100%</span>
          <span class="stock-info">库存剩余 {{ product?.stock || 0 }} 件</span>
          <span class="date-info">上架日期 {{ formatDate(product?.createdAt) }}</span>
        </div>

        <div class="price-section">
          <span class="value">{{ product?.points }}</span>
          <span class="currency">积分</span>
        </div>

        <div class="delivery-info">
          <div class="delivery-item">
            <el-icon class="icon">
              <Van />
            </el-icon>
            <span>快递 免运费</span>
          </div>
          <div class="delivery-item">
            <el-icon class="icon">
              <Box />
            </el-icon>
            <span>不支持7天无理由退货</span>
          </div>
          <div class="delivery-item">
            <el-icon class="icon">
              <CreditCard />
            </el-icon>
            <span>仅支持积分兑换</span>
          </div>
        </div>

        <div class="divider"></div>

        <div class="quantity-section">
          <span class="quantity-label">数量</span>
          <el-input-number v-model="quantity" :min="1" :max="maxQuantity" :disabled="product?.status !== 'available'"
            controls-position="right" />
          <span class="stock-text">有货（每人限购{{ product?.maxPurchase || 1 }}件）</span>
        </div>

        <el-button type="primary" @click="buyNow" :disabled="!canBuy" :loading="submitting">立即兑换</el-button>
      </div>
    </div>

    <el-tabs class="detail-tabs">
      <el-tab-pane label="商品详情"></el-tab-pane>
    </el-tabs>

    <div class="rating-info">
      <p class="desc-text">{{ product?.description }}</p>
    </div>

    <el-dialog v-model="orderDialogVisible" title="确认兑换" width="400px">
      <el-form :model="orderForm" :rules="orderRules" ref="orderFormRef" label-width="70px">
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="orderForm.contactName" placeholder="姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="contactPhone">
          <el-input v-model="orderForm.contactPhone" placeholder="手机号" />
        </el-form-item>
        <el-form-item label="地址" prop="shippingAddress">
          <el-input v-model="orderForm.shippingAddress" type="textarea" :rows="2" placeholder="收货地址" />
        </el-form-item>
        <div class="summary">
          <div class="row"><span>数量</span><span>{{ quantity }}件</span></div>
          <div class="row"><span>积分</span><span class="cost">{{ totalPoints }}</span></div>
          <div class="row total"><span>剩余</span><span>{{ remainingPoints }}</span></div>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="orderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder" :loading="submitting">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Van, Box, CreditCard } from '@element-plus/icons-vue'
import { productApi } from '@/api/product'
import { collectorApi } from '@/api/collector'

defineOptions({
  name: 'CollectorProductDetail'
})

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const submitting = ref(false)
const product = ref(null)
const quantity = ref(1)
const userPoints = ref(0)
const orderDialogVisible = ref(false)
const orderFormRef = ref(null)

const orderForm = reactive({
  contactName: '',
  contactPhone: '',
  shippingAddress: ''
})

const orderRules = {
  contactName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误', trigger: 'blur' }
  ],
  shippingAddress: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

const totalPoints = computed(() => (product.value?.points || 0) * quantity.value)

const remainingPoints = computed(() => userPoints.value - totalPoints.value)

const maxQuantity = computed(() => {
  if (!product.value) return 1
  const { stock, maxPurchase, userPurchased } = product.value
  const limit = maxPurchase || stock
  const remaining = limit - (userPurchased || 0)
  return Math.max(0, Math.min(stock, remaining))
})

const canBuy = computed(() => {
  if (!product.value || product.value.status !== 'available') return false
  if (quantity.value > product.value.stock) return false
  const { maxPurchase, userPurchased } = product.value
  if (maxPurchase && (userPurchased || 0) + quantity.value > maxPurchase) return false
  return totalPoints.value <= userPoints.value
})

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const loadProduct = async () => {
  loading.value = true
  try {
    product.value = await productApi.getById(route.params.id)
  } catch {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const loadUserPoints = async () => {
  try {
    const response = await collectorApi.getStatistics()
    userPoints.value = response.totalPoints || 0
  } catch {
    ElMessage.error('加载积分失败')
  }
}

const loadData = async () => {
  await Promise.all([loadProduct(), loadUserPoints()])
}

const resetOrderForm = () => {
  Object.assign(orderForm, { contactName: '', contactPhone: '', shippingAddress: '' })
}

const buyNow = () => {
  resetOrderForm()
  orderDialogVisible.value = true
}

const submitOrder = async () => {
  try {
    await orderFormRef.value.validate()
    submitting.value = true
    await collectorApi.createOrder({
      productId: product.value.id,
      quantity: quantity.value,
      ...orderForm
    })
    ElMessage.success('兑换成功')
    orderDialogVisible.value = false
    await loadData()
    router.push('/collector/points')
  } catch (error) {
    if (error !== false) {
      ElMessage.error(error.response?.data?.message || '提交失败')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.product-detail {
  margin: 0 auto;
  max-width: 1200px;
  padding: 16px 0;
}

.main-section {
  display: flex;
  gap: 32px;
  margin-bottom: 20px;
}

.gallery {
  flex: 0 0 320px;
}

.product-image {
  width: 100%;
  height: 380px;
  object-fit: contain;
  display: block;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fafafa;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info {
  flex: 1;
}

.name {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 12px;
  line-height: 1.4;
}

.sales-info {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 14px;
}

.sales-text {
  color: #ff6600;
}

.review-text,
.stock-info,
.date-info {
  color: #909399;
}

.price-section {
  display: flex;
  align-items: baseline;
  margin-bottom: 18px;
}

.currency {
  font-size: 18px;
  color: #ff4400;
  margin-left: 4px;
}

.price-section .value {
  font-size: 36px;
  font-weight: 600;
  color: #ff4400;
}

.delivery-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 18px;
}

.delivery-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  padding: 4px 0;
}

.delivery-item .icon {
  font-size: 18px;
  color: #409eff;
}

.divider {
  height: 1px;
  background: #f0f0f0;
  margin: 16px 0;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.quantity-label {
  font-size: 14px;
  color: #303133;
}

.quantity-section :deep(.el-input-number) {
  width: 100px;
}

.quantity-section :deep(.el-input-number .el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  border-radius: 4px;
  transition: all 0.3s;
}

.quantity-section :deep(.el-input-number .el-input__wrapper:hover),
.quantity-section :deep(.el-input-number .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.stock-text {
  font-size: 13px;
  color: #909399;
}

.rating-info {
  font-size: 14px;
  color: #303133;
  padding: 16px 0;
}

.rating-info .desc-text {
  margin: 0;
  line-height: 1.6;
  color: #606266;
}

.summary {
  padding: 16px;
  background: #f5f7fa;
  margin-top: 16px;
}

.summary .row {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  font-size: 14px;
  color: #606266;
  border-bottom: 1px dashed #dcdfe6;
}

.summary .row:last-child {
  border-bottom: none;
  padding-top: 16px;
  border-top: 2px solid #f56c6c;
  margin-top: 8px;
  font-weight: 600;
  color: #303133;
}

.summary .row .cost {
  color: #f56c6c;
  font-weight: 600;
}
</style>
