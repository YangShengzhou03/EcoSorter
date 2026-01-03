<template>
  <div class="admin-configuration">
    <div class="config-section">
      <h3>基本设置</h3>
      <el-form :model="config" label-width="120px">
        <el-form-item label="系统名称">
          <el-input v-model="config.systemName" />
        </el-form-item>
        <el-form-item label="维护模式">
          <el-switch v-model="config.maintenanceMode" />
        </el-form-item>
      </el-form>
    </div>

    <div class="config-section">
      <h3>积分配置</h3>
      <el-form :model="config" label-width="120px">
        <el-form-item label="积分比例">
          <el-input-number v-model="config.pointsRatio" :min="0" :step="0.1" />
          <span class="form-tip">每1kg垃圾获得的积分</span>
        </el-form-item>
        <el-form-item label="每日上限">
          <el-input-number v-model="config.dailyLimit" :min="0" />
          <span class="form-tip">用户每日可获得的最大积分</span>
        </el-form-item>
      </el-form>
    </div>

    <div class="config-section">
      <h3>设备配置</h3>
      <el-form :model="config" label-width="120px">
        <el-form-item label="心跳间隔">
          <el-input-number v-model="config.heartbeatInterval" :min="30" />
          <span class="form-tip">秒</span>
        </el-form-item>
        <el-form-item label="离线超时">
          <el-input-number v-model="config.offlineTimeout" :min="300" />
          <span class="form-tip">秒</span>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-section">
      <el-button type="primary" @click="saveConfig">保存配置</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const config = reactive({
  systemName: 'EcoSorter 垃圾分类系统',
  maintenanceMode: false,
  pointsRatio: 10,
  dailyLimit: 100,
  heartbeatInterval: 60,
  offlineTimeout: 600
})

const saveConfig = () => {
  ElMessage.success('配置已保存')
}
</script>

<style scoped>
.admin-configuration {
  padding: 0;
}

.config-section {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.config-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.form-tip {
  margin-left: 10px;
  color: #6b7280;
  font-size: 12px;
}

.action-section {
  display: flex;
  gap: 12px;
}
</style>
