<template>
    <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

defineOptions({
  name: 'AppRoot'
})

const authStore = useAuthStore()

onMounted(() => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  
  if (token && userInfo) {
    authStore.token = token
    authStore.userInfo = JSON.parse(userInfo)
  }
})
</script>
