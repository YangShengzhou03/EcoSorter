<template>
  <div class="category-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">厨余垃圾</h1>
        <p class="page-subtitle">Kitchen Waste</p>
      </div>
    </div>

    <div class="page-content">
      <div class="category-info">
        <div class="info-card">
          <h3>什么是厨余垃圾？</h3>
          <p>厨余垃圾是指居民日常生活及食品加工、饮食服务、单位供餐等活动中产生的垃圾，包括丢弃不用的菜叶、剩菜、剩饭、果皮、蛋壳、茶渣、骨头等。</p>
        </div>

        <div class="info-card">
          <h3>投放要求</h3>
          <ul>
            <li>沥干水分后投放</li>
            <li>有包装的应去包装后投放</li>
            <li>避免混入其他垃圾</li>
            <li>大骨头、硬贝壳等不易腐烂的请投放到其他垃圾</li>
            <li>厨余垃圾应日产日清，避免长时间存放</li>
          </ul>
        </div>

        <div class="info-card">
          <h3>常见物品</h3>
          <div class="items-grid">
            <div class="item-card">
              <div class="item-icon">蔬菜</div>
              <span>蔬菜叶</span>
            </div>
            <div class="item-card">
              <div class="item-icon">剩饭</div>
              <span>剩饭剩菜</span>
            </div>
            <div class="item-card">
              <div class="item-icon">果皮</div>
              <span>果皮果核</span>
            </div>
            <div class="item-card">
              <div class="item-icon">蛋壳</div>
              <span>蛋壳</span>
            </div>
            <div class="item-card">
              <div class="item-icon">茶渣</div>
              <span>茶渣咖啡渣</span>
            </div>
            <div class="item-card">
              <div class="item-icon">骨头</div>
              <span>小骨头</span>
            </div>
            <div class="item-card">
              <div class="item-icon">玉米</div>
              <span>玉米芯</span>
            </div>
            <div class="item-card">
              <div class="item-icon">面包</div>
              <span>面包糕点</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3>处理方式</h3>
          <p>厨余垃圾经过收集后，可以进行堆肥处理、沼气发酵等方式进行资源化利用。厨余垃圾中含有丰富的有机物质，是生产有机肥料和生物能源的重要原料。</p>
        </div>

        <div class="info-card">
          <h3>小贴士</h3>
          <ul>
            <li>减少厨余垃圾产生：按需做饭，避免浪费</li>
            <li>厨余垃圾应沥干水分，减少运输和处理成本</li>
            <li>大骨头、硬贝壳等难以腐烂的物品应投放到其他垃圾</li>
            <li>厨余垃圾袋最好使用可降解垃圾袋</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

defineOptions({
  name: 'IndexKitchenPage'
})

onMounted(() => {
  document.title = '厨余垃圾 - ECO-Sorter'
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
  color: #f39c12;
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
