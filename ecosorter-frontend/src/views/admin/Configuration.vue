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
      <el-button type="primary" @click="saveConfig" :loading="saving">保存配置</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const saving = ref(false)
const config = reactive({
  systemName: 'EcoSorter 垃圾分类系统',
  maintenanceMode: false,
  pointsRatio: 10,
  dailyLimit: 100,
  heartbeatInterval: 60,
  offlineTimeout: 600
})

const loadConfig = async () => {
  try {
    const response = await adminApi.getConfiguration()
    Object.assign(config, response)
  } catch (error) {
    console.error('加载配置失败:', error)
    ElMessage.error('加载配置失败')
  }
}

const saveConfig = async () => {
  saving.value = true
  try {
    await adminApi.updateConfiguration(config)
    ElMessage.success('配置已保存')
  } catch (error) {
    console.error('保存配置失败:', error)
    ElMessage.error('保存配置失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.admin-configuration {
  padding: 0;
}

.config-section {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.config-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.form-tip {
  margin-left: 10px;
  color: var(--text-secondary);
  font-size: 12px;
}

.action-section {
  display: flex;
  gap: 12px;
}
</style>
