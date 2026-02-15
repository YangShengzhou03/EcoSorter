<template>
  <div class="category-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">有害垃圾</h1>
        <p class="page-subtitle">Hazardous Waste</p>
      </div>
    </div>

    <div class="page-content">
      <div class="category-info">
        <div class="info-card">
          <h3>什么是有害垃圾？</h3>
          <p>有害垃圾是指对人体健康或者自然环境造成直接或者潜在危害的生活废弃物。这些垃圾需要特殊安全处理。</p>
        </div>

        <div class="info-card">
          <h3>投放注意事项</h3>
          <ul>
            <li>投放时请注意轻放</li>
            <li>易破损的请连带包装或包裹后轻放</li>
            <li>如易挥发，请密封后投放</li>
            <li>如有残留，请倒空后投放</li>
            <li>切勿与其他垃圾混合投放</li>
          </ul>
        </div>

        <div class="info-card">
          <h3>常见物品</h3>
          <div class="items-grid">
            <div class="item-card">
              <div class="item-icon">电池</div>
              <span>废电池</span>
            </div>
            <div class="item-card">
              <div class="item-icon">灯管</div>
              <span>废灯管</span>
            </div>
            <div class="item-card">
              <div class="item-icon">药品</div>
              <span>废药品</span>
            </div>
            <div class="item-card">
              <div class="item-icon">油漆</div>
              <span>废油漆</span>
            </div>
            <div class="item-card">
              <div class="item-icon">温度计</div>
              <span>废温度计</span>
            </div>
            <div class="item-card">
              <div class="item-icon">杀虫剂</div>
              <span>杀虫剂</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3>处理方式</h3>
          <p>有害垃圾需要经过专业的无害化处理。废电池需要回收利用其中的金属，废灯管需要处理其中的汞，废药品需要防止流入市场。这些垃圾如果处理不当，会对土壤、水源和空气造成严重污染。</p>
        </div>

        <div class="info-card">
          <h3>投放点</h3>
          <p>有害垃圾请投放至社区指定的有害垃圾收集点或回收箱。如不确定投放位置，可咨询物业管理部门或拨打环保热线。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

defineOptions({
  name: 'IndexHazardousPage'
})

onMounted(() => {
  document.title = '有害垃圾 - ECO-Sorter'
})
</script>

<style scoped>
.category-page {
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

.header-content {
  width: 100%;
  margin: 0 auto;
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

.category-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e0e0e0;
}

.info-card h3 {
  font-size: 18px;
  margin: 0 0 12px;
  color: #333;
  font-weight: 600;
}

.info-card p {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin: 0;
}

.info-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.info-card li {
  padding: 6px 0;
  padding-left: 20px;
  position: relative;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.info-card li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #e74c3c;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.item-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.item-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.item-card span {
  font-size: 13px;
  color: #666;
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

  .info-card {
    padding: 20px;
  }

  .info-card h3 {
    font-size: 16px;
  }

  .items-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
