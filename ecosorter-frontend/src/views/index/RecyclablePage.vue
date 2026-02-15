<template>
  <div class="category-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">可回收物</h1>
        <p class="page-subtitle">Recyclable Waste</p>
      </div>
    </div>

    <div class="page-content">
      <div class="category-info">
        <div class="info-card">
          <h3>什么是可回收物？</h3>
          <p>可回收物是指适宜回收利用和资源化利用的生活废弃物，如废纸、废塑料、废玻璃制品、废金属、废织物等。</p>
        </div>

        <div class="info-card">
          <h3>投放要求</h3>
          <ul>
            <li>轻投轻放</li>
            <li>清洁干燥、避免污染</li>
            <li>废纸尽量平整</li>
            <li>立体包装物请清空内容物，清洁后压扁投放</li>
            <li>有尖锐边角的，应包裹后投放</li>
          </ul>
        </div>

        <div class="info-card">
          <h3>常见物品</h3>
          <div class="items-grid">
            <div class="item-card">
              <div class="item-icon">纸张</div>
              <span>废纸</span>
            </div>
            <div class="item-card">
              <div class="item-icon">塑料</div>
              <span>废塑料</span>
            </div>
            <div class="item-card">
              <div class="item-icon">玻璃</div>
              <span>废玻璃</span>
            </div>
            <div class="item-card">
              <div class="item-icon">金属</div>
              <span>废金属</span>
            </div>
            <div class="item-card">
              <div class="item-icon">织物</div>
              <span>废织物</span>
            </div>
            <div class="item-card">
              <div class="item-icon">电子</div>
              <span>电子废弃物</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3>回收价值</h3>
          <p>可回收物通过回收利用，可以减少资源浪费，降低环境污染，实现资源的循环利用。每回收1吨废纸可造好纸850公斤，节省木材300公斤，比等量生产减少污染74%。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

defineOptions({
  name: 'IndexRecyclablePage'
})

onMounted(() => {
  document.title = '可回收物 - ECO-Sorter'
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
  color: #4a90e2;
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
