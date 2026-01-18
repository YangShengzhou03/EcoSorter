<template>
  <div class="product-detail" v-loading="loading">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item to="/resident/points">积分商城</el-breadcrumb-item>
      <el-breadcrumb-item>{{ product?.name }}</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="main-section">
      <div class="gallery">
        <el-carousel :interval="5000" arrow="hover" height="380px">
          <el-carousel-item v-for="(image, index) in productImages" :key="index">
            <img :src="image || '/placeholder.png'" :alt="product?.name">
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="info">
        <div class="info-row">
          <h1 class="name">{{ product?.name }}</h1>
          <div class="price">
            <span class="value">{{ product?.points }}</span>
            <span class="unit">积分</span>
          </div>
        </div>

        <div class="desc">
          <p>{{ product?.description }}</p>
        </div>

        <div class="meta-action-row">
          <div class="meta">
            <span>{{ product?.category }}</span>
            <span class="sep">|</span>
            <span>库存{{ product?.stock }}件</span>
            <span class="sep">|</span>
            <span>限购{{ product?.maxPurchase }}件</span>
            <span class="sep">|</span>
            <span>已兑换{{ product?.totalPurchased }}件</span>
          </div>
          <div class="action-row">
            <div class="quantity">
              <el-input-number v-model="quantity" :min="1" :max="maxQuantity"
                :disabled="product?.status !== 'available'" />
            </div>
            <el-button type="primary" @click="buyNow" :disabled="!canBuy" :loading="submitting" class="btn">
              立即兑换
            </el-button>
          </div>
        </div>

        <div class="delivery-info">
          <div class="delivery-item">
            <span class="delivery-label">发货</span>
            <span class="delivery-value">48小时内发货，预计明天送达</span>
          </div>
          <div class="delivery-item">
            <span class="delivery-label">快递</span>
            <span class="delivery-value">免运费</span>
          </div>
          <div class="delivery-item">
            <span class="delivery-label">售后</span>
            <span class="delivery-value">7天价保 7天无理由退货 极速退款</span>
          </div>
        </div>
      </div>
    </div>

    <div class="desc-section">
      <h3>商品详情</h3>
      <p>{{ product?.description }}</p>
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
          <div class="row total"><span>剩余</span><span>{{ userPoints - totalPoints }}</span></div>
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
import { productApi } from '@/api/product'
import { orderApi } from '@/api/order'
import { userApi } from '@/api/user'

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

const productImages = computed(() => {
  if (!product.value) return []
  if (product.value.images) {
    try {
      const images = JSON.parse(product.value.images)
      return Array.isArray(images) && images.length > 0 ? images : [product.value.imageUrl]
    } catch (e) {
      return [product.value.imageUrl]
    }
  }
  return [product.value.imageUrl]
})

const totalPoints = computed(() => product.value ? product.value.points * quantity.value : 0)

const maxQuantity = computed(() => {
  if (!product.value) return 1
  const stockLimit = product.value.stock
  const purchaseLimit = product.value.maxPurchase || stockLimit
  const remainingPurchase = purchaseLimit - (product.value.totalPurchased || 0)
  return Math.min(stockLimit, remainingPurchase)
})

const canBuy = computed(() => {
  if (!product.value || product.value.status !== 'available') return false
  if (quantity.value > product.value.stock) return false
  if (product.value.maxPurchase && (product.value.totalPurchased || 0) + quantity.value > product.value.maxPurchase) return false
  if (totalPoints.value > userPoints.value) return false
  return true
})

const loadProduct = async () => {
  loading.value = true
  try {
    product.value = await productApi.getById(route.params.id)
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const loadUserPoints = async () => {
  try {
    const response = await userApi.getStatistics()
    userPoints.value = response.totalPoints || 0
  } catch (error) { }
}

const buyNow = () => {
  orderForm.contactName = ''
  orderForm.contactPhone = ''
  orderForm.shippingAddress = ''
  orderDialogVisible.value = true
}

const submitOrder = async () => {
  try {
    await orderFormRef.value.validate()
    submitting.value = true
    await orderApi.create({
      productId: product.value.id,
      quantity: quantity.value,
      contactName: orderForm.contactName,
      contactPhone: orderForm.contactPhone,
      shippingAddress: orderForm.shippingAddress
    })
    ElMessage.success('兑换成功')
    orderDialogVisible.value = false
    loadUserPoints()
    loadProduct()
    router.push('/resident/points')
  } catch (error) {
    if (error !== false) {
      const msg = error.response?.data?.message || '提交失败'
      ElMessage.error(msg)
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadProduct()
  loadUserPoints()
})
</script>

<style scoped>
.product-detail {
  padding: 12px;
  background: #fff;
  min-height: 100vh;
}

.breadcrumb {
  margin-bottom: 24px;
  padding: 12px 16px;
  border-radius: 8px;
}

.main-section {
  display: flex;
  gap: 32px;
  margin-bottom: 32px;
}

.gallery {
  flex: 0 0 380px;
}

.gallery :deep(.el-carousel) {
  border-radius: 8px;
  overflow: hidden;
}

.gallery img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #fafafa;
  border-radius: 8px;
}

.info {
  flex: 1;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 24px;
}

.name {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
  line-height: 1.4;
}

.price {
  margin-bottom: 0;
}

.price .value {
  font-size: 32px;
  font-weight: 600;
  color: #ff4d4f;
}

.price .unit {
  font-size: 14px;
  color: #ff4d4f;
  margin-left: 4px;
}

.desc {
  margin-bottom: 24px;
}

.desc p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.meta-action-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 24px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #666;
}

.meta .sep {
  color: #e0e0e0;
  margin: 0 4px;
}

.action-row {
  display: flex;
  gap: 12px;
}

.action-row .quantity {
  flex: 0 0 auto;
}

.action-row :deep(.el-input-number) {
  width: 120px;
}

.action-row :deep(.el-input-number .el-input__wrapper) {
  border-radius: 6px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.action-row :deep(.el-input-number .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

.action-row :deep(.el-input-number .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #ff4d4f inset;
}

.btn {
  flex: 1;
  height: 36px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4d4f 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(255, 77, 79, 0.2);
  transition: all 0.3s ease;
}

.btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #ff5252 0%, #f44336 100%);
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3);
  transform: translateY(-1px);
}

.btn:active:not(:disabled) {
  transform: translateY(0);
}

.btn:disabled {
  background: #f5f5f5;
  color: #ccc;
  box-shadow: none;
}

.delivery-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  margin-top: 24px;
}

.delivery-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
}

.delivery-label {
  color: #999;
  font-weight: 500;
  min-width: 40px;
}

.delivery-value {
  color: #333;
  flex: 1;
}

.desc-section {
  padding: 24px 0;
  border-top: 1px solid #f0f0f0;
}

.desc-section h3 {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 16px 0;
}

.desc-section p {
  font-size: 14px;
  color: #666;
  line-height: 1.8;
  margin: 0;
}

.summary {
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  margin-top: 16px;
}

.summary .row {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  font-size: 14px;
  color: #666;
  border-bottom: 1px dashed #e0e0e0;
}

.summary .row:last-child {
  border-bottom: none;
  padding-top: 16px;
  border-top: 2px solid #ff4d4f;
  margin-top: 8px;
  font-weight: 600;
  color: #1a1a1a;
}

.summary .row .cost {
  color: #ff4d4f;
  font-weight: 600;
}
</style>
