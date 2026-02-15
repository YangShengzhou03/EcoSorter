<template>
  <div class="contact-page">
    <div class="page-header">
      <h1 class="page-title">联系我们</h1>
      <p class="page-subtitle">Contact Us</p>
    </div>

    <div class="page-content">
      <div class="section-card">
        <h2 class="section-title">联系方式</h2>
        <div class="contact-list">
          <div class="contact-item">
            <div class="contact-label">客服热线</div>
            <div class="contact-value">400-888-8888</div>
            <div class="contact-time">工作日 9:00-18:00</div>
          </div>
          <div class="contact-item">
            <div class="contact-label">商务合作</div>
            <div class="contact-value">business@ecosorter.com</div>
          </div>
          <div class="contact-item">
            <div class="contact-label">意见反馈</div>
            <div class="contact-value">feedback@ecosorter.com</div>
          </div>
          <div class="contact-item">
            <div class="contact-label">公司地址</div>
            <div class="contact-value">江西科技师范大学红角洲校区18栋520室</div>
          </div>
        </div>
      </div>

      <div class="section-card">
        <h2 class="section-title">常见问题</h2>
        <el-collapse v-model="activeNames" class="faq-collapse">
          <el-collapse-item title="如何使用智能垃圾分类设备？" name="1">
            <div class="faq-content">
              <p>使用智能垃圾分类设备非常简单：</p>
              <ol>
                <li>在设备前扫描二维码或刷IC卡登录</li>
                <li>将垃圾放入识别区域，系统会自动识别垃圾类型</li>
                <li>根据系统提示将垃圾投入对应的分类桶</li>
                <li>投放完成后，系统会自动计算积分并记录到您的账户</li>
              </ol>
            </div>
          </el-collapse-item>
          <el-collapse-item title="积分如何兑换商品？" name="2">
            <div class="faq-content">
              <p>积分兑换流程：</p>
              <ol>
                <li>登录您的账户，进入"积分商城"</li>
                <li>浏览商品，选择您想要兑换的商品</li>
                <li>点击"立即兑换"，填写收货地址</li>
                <li>确认订单，等待商家发货</li>
              </ol>
            </div>
          </el-collapse-item>
          <el-collapse-item title="如何预约上门回收服务？" name="3">
            <div class="faq-content">
              <p>上门回收预约步骤：</p>
              <ol>
                <li>进入"服务中心"，选择"回收预约"</li>
                <li>填写回收物品信息、预约时间、联系方式等</li>
                <li>提交预约申请</li>
                <li>回收人员会在预约时间段内联系您上门回收</li>
              </ol>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineOptions({
  name: 'IndexContactPage'
})

const activeNames = ref(['1'])
</script>

<style scoped>
.contact-page {
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
  border: 1px solid #e0e0e0;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  margin: 0 0 12px;
  color: #333;
  font-weight: 600;
}

.contact-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 10px;
}

.contact-item {
  padding: 10px 12px;
  background: #f9f9f9;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.contact-label {
  font-size: 11px;
  color: #666;
  margin-bottom: 2px;
}

.contact-value {
  font-size: 13px;
  color: #333;
  font-weight: 600;
  margin-bottom: 2px;
}

.contact-time {
  font-size: 10px;
  color: #999;
}

.message-form {
  max-width: 100%;
}

.type-select {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 14px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #303133;
  padding-bottom: 4px;
  font-size: 14px;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner),
:deep(.el-select .el-input__wrapper) {
  padding: 6px 11px;
}

.form-actions {
  margin-top: 20px;
  margin-bottom: 0;
}

.faq-collapse {
  border: none;
}

:deep(.el-collapse-item__header) {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  background: #f9f9f9;
  border-radius: 4px;
  padding: 10px 12px;
  margin-bottom: 6px;
}

:deep(.el-collapse-item__wrap) {
  background: transparent;
  border: none;
}

:deep(.el-collapse-item__content) {
  padding: 10px 12px;
  background: white;
  border-radius: 4px;
}

.faq-content {
  line-height: 1.5;
  color: #666;
  font-size: 13px;
}

.faq-content p {
  margin: 0 0 6px 0;
}

.faq-content ol {
  margin: 0;
  padding-left: 18px;
}

.faq-content li {
  margin-bottom: 3px;
}

@media (max-width: 768px) {
  .page-header {
    padding: 40px 20px 30px;
  }

  .page-title {
    font-size: 28px;
  }

  .page-subtitle {
    font-size: 14px;
  }

  .page-content {
    margin: -10px auto 30px;
    padding: 0 20px;
  }

  .section-card {
    padding: 20px;
  }

  .section-title {
    font-size: 16px;
  }
}
</style>
