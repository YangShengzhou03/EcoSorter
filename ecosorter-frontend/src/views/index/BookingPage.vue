<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">回收预约</h1>
      <p class="page-subtitle">Booking Service</p>
    </div>

    <div class="page-content">
      <div class="section-card">
        <el-form :model="form" label-position="top" class="booking-form" :rules="rules" ref="formRef">
        <el-form-item label="回收类型" prop="type">
          <el-radio-group v-model="form.type" class="type-radio-group">
            <el-radio value="recyclable">可回收物</el-radio>
            <el-radio value="electronics">电子废弃物</el-radio>
            <el-radio value="large">大件物品</el-radio>
            <el-radio value="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="物品描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请描述您要回收的物品，如：纸箱、塑料瓶、旧家电等" 
            maxlength="300"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预估重量" prop="estimatedWeight">
              <el-input-number 
                v-model="form.estimatedWeight" 
                :min="1" 
                :max="500" 
                :step="0.5"
                controls-position="right"
                class="weight-input"
              />
              <span class="unit">kg</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预约日期" prop="appointmentDate">
              <el-date-picker
                v-model="form.appointmentDate"
                type="date"
                placeholder="选择预约日期"
                :disabled-date="disabledDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                class="date-picker"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预约时间段" prop="timeSlot">
              <el-select v-model="form.timeSlot" placeholder="选择时间段" class="time-select">
                <el-option label="09:00 - 12:00" value="morning" />
                <el-option label="14:00 - 17:00" value="afternoon" />
                <el-option label="18:00 - 20:00" value="evening" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="回收地址" prop="address">
          <el-input 
            v-model="form.address" 
            type="textarea" 
            :rows="2" 
            placeholder="请输入详细地址" 
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="form.remark" 
            type="textarea" 
            :rows="2" 
            placeholder="其他需要说明的信息（选填）" 
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item class="form-actions">
          <el-button type="primary" @click="submit" :loading="submitting" class="submit-btn">提交预约</el-button>
          <el-button @click="reset" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="section-card">
      <h3 class="info-title">预约须知</h3>
      <ul class="info-list">
        <li>请确保物品干净、干燥，无明显污染</li>
        <li>大件物品请提前说明，以便安排合适车辆</li>
        <li>回收人员将在预约时间段内联系您</li>
        <li>如需取消或修改预约，请提前24小时通知</li>
        <li>回收成功后将获得相应积分奖励</li>
      </ul>
    </div>

    <div class="section-card">
      <h3 class="info-title">我的预约记录</h3>
      <div class="bookings-list" v-loading="loadingBookings">
        <div class="booking-card" v-for="booking in bookings" :key="booking.id">
          <div class="booking-header">
            <div class="booking-id">预约号: {{ booking.id }}</div>
            <el-tag :type="getStatusType(booking.status)">{{ getStatusText(booking.status) }}</el-tag>
          </div>
          <div class="booking-content">
            <div class="booking-info">
              <div class="info-row">
                <span class="label">回收类型:</span>
                <span class="value">{{ getTypeText(booking.type) }}</span>
              </div>
              <div class="info-row">
                <span class="label">预约时间:</span>
                <span class="value">{{ booking.appointmentDate }} {{ getTimeSlotText(booking.timeSlot) }}</span>
              </div>
              <div class="info-row">
                <span class="label">预估重量:</span>
                <span class="value">{{ booking.estimatedWeight }}kg</span>
              </div>
              <div class="info-row">
                <span class="label">联系人:</span>
                <span class="value">{{ booking.contactName }}</span>
              </div>
              <div class="info-row">
                <span class="label">联系电话:</span>
                <span class="value">{{ booking.contactPhone }}</span>
              </div>
              <div class="info-row">
                <span class="label">地址:</span>
                <span class="value">{{ booking.address }}</span>
              </div>
            </div>
            <div class="booking-actions" v-if="booking.status === 'pending'">
              <el-button type="danger" size="small" @click="cancelBooking(booking.id)">取消预约</el-button>
            </div>
          </div>
        </div>
        <el-empty v-if="bookings.length === 0 && !loadingBookings" description="暂无预约记录" />
      </div>
    </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { bookingApi } from '@/api/booking'

defineOptions({
  name: 'IndexBookingPage'
})

const loadingBookings = ref(false)
const submitting = ref(false)
const bookings = ref([])

const form = reactive({
  type: 'recyclable',
  description: '',
  estimatedWeight: 5,
  appointmentDate: '',
  timeSlot: '',
  contactName: '',
  contactPhone: '',
  address: '',
  remark: ''
})

const rules = {
  type: [{ required: true, message: '请选择回收类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入物品描述', trigger: 'blur' }],
  estimatedWeight: [{ required: true, message: '请输入预估重量', trigger: 'blur' }],
  appointmentDate: [{ required: true, message: '请选择预约日期', trigger: 'change' }],
  timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }],
  contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [{ required: true, message: '请输入回收地址', trigger: 'blur' }]
}

const formRef = ref(null)

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
}

const submit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      await bookingApi.create(form)
      ElMessage.success('预约提交成功，回收人员将在预约时间段内联系您')
      reset()
      loadBookings()
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '提交失败')
    } finally {
      submitting.value = false
    }
  })
}

const reset = () => {
  form.type = 'recyclable'
  form.description = ''
  form.estimatedWeight = 5
  form.appointmentDate = ''
  form.timeSlot = ''
  form.contactName = ''
  form.contactPhone = ''
  form.address = ''
  form.remark = ''
  formRef.value?.resetFields()
}

const loadBookings = async () => {
  loadingBookings.value = true
  try {
    const res = await bookingApi.getList()
    bookings.value = res.records || []
  } catch (error) {
    ElMessage.error('加载预约记录失败')
  } finally {
    loadingBookings.value = false
  }
}

const cancelBooking = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消此预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await bookingApi.cancel(id)
    ElMessage.success('预约已取消')
    loadBookings()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消预约失败')
    }
  }
}

const getStatusType = (status) => {
  const statusMap = {
    pending: 'warning',
    confirmed: 'primary',
    completed: 'success',
    cancelled: 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待确认',
    confirmed: '已确认',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const getTypeText = (type) => {
  const typeMap = {
    recyclable: '可回收物',
    electronics: '电子废弃物',
    large: '大件物品',
    other: '其他'
  }
  return typeMap[type] || type
}

const getTimeSlotText = (timeSlot) => {
  const timeMap = {
    morning: '09:00 - 12:00',
    afternoon: '14:00 - 17:00',
    evening: '18:00 - 20:00'
  }
  return timeMap[timeSlot] || timeSlot
}

onMounted(() => {
  loadBookings()
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
  padding: 40px 32px 30px;
  text-align: center;
  color: white;
}

.page-title {
  font-size: 32px;
  margin: 0 0 8px;
  font-weight: 600;
}

.page-subtitle {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.page-content {
  margin: 20px auto 0;
  padding: 0 32px;
}

.section-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid var(--border-color);
  margin-bottom: 16px;
}

.booking-form {
  width: 100%;
}

.type-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.weight-input {
  width: 180px;
}

.unit {
  margin-left: 8px;
  color: var(--text-secondary);
}

.date-picker,
.time-select {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 16px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #303133;
  padding-bottom: 4px;
  font-size: 14px;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner),
:deep(.el-select .el-input__wrapper),
:deep(.el-date-editor) {
  padding: 6px 11px;
}

:deep(.el-input-number) {
  width: 180px;
}

.form-actions {
  margin-top: 24px;
  margin-bottom: 0;
}

.submit-btn {
  min-width: 100px;
  padding: 8px 16px;
}

.reset-btn {
  min-width: 70px;
  margin-left: 12px;
  padding: 8px 16px;
}

.info-title {
  font-size: 16px;
  margin: 0 0 12px 0;
  color: var(--text-primary);
}

.info-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.info-list li {
  padding: 6px 0;
  padding-left: 20px;
  position: relative;
  font-size: 13px;
  color: var(--text-primary);
  line-height: 1.5;
}

.info-list li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #67c23a;
  font-weight: bold;
  font-size: 12px;
}

.bookings-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.booking-card {
  background: var(--bg-light);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 12px;
  transition: all 0.3s;
}

.booking-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.booking-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-color);
}

.booking-id {
  font-size: 13px;
  color: var(--text-secondary);
  font-weight: 500;
}

.booking-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.booking-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-row {
  display: flex;
  gap: 8px;
}

.info-row .label {
  font-size: 12px;
  color: var(--text-secondary);
  flex-shrink: 0;
  min-width: 70px;
}

.info-row .value {
  font-size: 13px;
  color: var(--text-primary);
  flex: 1;
}

.booking-actions {
  margin-top: 6px;
  padding-top: 8px;
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: flex-end;
}
</style>
