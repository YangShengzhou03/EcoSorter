<template>
  <div class="category-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">其他垃圾</h1>
        <p class="page-subtitle">Other Waste</p>
      </div>
    </div>

    <div class="page-content">
      <div class="category-info">
        <div class="info-card">
          <h3>什么是其他垃圾？</h3>
          <p>其他垃圾是指除可回收物、有害垃圾、厨余垃圾以外的其他生活废弃物。这些垃圾难以回收利用，通常采用焚烧或填埋的方式进行处理。</p>
        </div>

        <div class="info-card">
          <h3>投放要求</h3>
          <ul>
            <li>尽量沥干水分</li>
            <li>难以辨识类别的生活垃圾投入其他垃圾容器内</li>
            <li>大骨头、硬贝壳等投入其他垃圾</li>
            <li>受污染的纸张、塑料等投入其他垃圾</li>
            <li>烟头、灰土等投入其他垃圾</li>
          </ul>
        </div>

        <div class="info-card">
          <h3>常见物品</h3>
          <div class="items-grid">
            <div class="item-card">
              <div class="item-icon">骨头</div>
              <span>大骨头</span>
            </div>
            <div class="item-card">
              <div class="item-icon">贝壳</div>
              <span>硬贝壳</span>
            </div>
            <div class="item-card">
              <div class="item-icon">烟头</div>
              <span>烟头</span>
            </div>
            <div class="item-card">
              <div class="item-icon">纸巾</div>
              <span>卫生纸</span>
            </div>
            <div class="item-card">
              <div class="item-icon">纸张</div>
              <span>污损纸张</span>
            </div>
            <div class="item-card">
              <div class="item-icon">餐具</div>
              <span>一次性餐具</span>
            </div>
            <div class="item-card">
              <div class="item-icon">扫帚</div>
              <span>尘土</span>
            </div>
            <div class="item-card">
              <div class="item-icon">陶瓷</div>
              <span>破碎陶瓷</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3>处理方式</h3>
          <p>其他垃圾主要通过焚烧发电或卫生填埋的方式进行处理。焚烧可以减少垃圾体积，回收热能用于发电；填埋则需要进行防渗处理，防止污染地下水。</p>
        </div>

        <div class="info-card">
          <h3>提示</h3>
          <ul>
            <li>减少使用一次性用品，选择可重复使用的物品</li>
            <li>做好垃圾分类，将可回收物分离出来</li>
            <li>厨余垃圾沥干水分，减少其他垃圾的产生</li>
            <li>购买商品时选择环保包装，减少包装垃圾</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

defineOptions({
  name: 'IndexOtherPage'
})

onMounted(() => {
  document.title = '其他垃圾 - ECO-Sorter'
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
  color: #95a5a6;
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
