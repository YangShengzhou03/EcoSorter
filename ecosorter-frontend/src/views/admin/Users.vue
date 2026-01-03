<template>
  <div class="admin-users">
    <div class="table-section">
      <el-table :data="users" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="role" label="角色" width="100" />
        <el-table-column prop="status" label="状态" width="80" />
        <el-table-column prop="points" label="积分" width="100" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const users = ref([
  { id: 1, username: 'admin', role: '管理员', status: '正常', points: 0 },
  { id: 2, username: 'user001', role: '普通用户', status: '正常', points: 150 },
  { id: 3, username: 'user002', role: '普通用户', status: '禁用', points: 80 },
  { id: 4, username: 'collector01', role: '收集员', status: '正常', points: 200 },
  { id: 5, username: 'user003', role: '普通用户', status: '正常', points: 120 }
])
</script>

<style scoped>
.admin-users {
  padding: 0;
}

.table-section {
  background: #fff;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  padding: 16px;
}
</style>
