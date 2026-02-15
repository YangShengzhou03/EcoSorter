export const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

export const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

export const getCapacityColor = (capacity) => {
  if (capacity >= 90) return '#F56C6C'
  if (capacity >= 75) return '#E6A23C'
  if (capacity >= 50) return '#409EFF'
  return '#67C23A'
}

export const getCapacityColorByRow = (row) => {
  const percentage = getCapacityPercentage(row)
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= row.threshold) return '#e6a23c'
  return '#67c23a'
}

export const getCapacityPercentage = (row) => {
  if (!row.maxCapacity || row.maxCapacity === 0) return 0
  return Math.round((row.capacity / row.maxCapacity) * 100)
}

export const getCapacityStatus = (row) => {
  const percentage = getCapacityPercentage(row)
  if (percentage >= 90) return 'exception'
  if (percentage >= row.threshold) return 'warning'
  return 'success'
}

export const getRoleName = (role) => {
  const roleMap = {
    admin: '管理员',
    resident: '居民',
    collector: '收集员'
  }
  return roleMap[role] || role
}

export const getRoleColor = (role) => {
  const colorMap = {
    admin: '#f56c6c',
    resident: '#409eff',
    collector: '#67c23a'
  }
  return colorMap[role] || '#909399'
}

export const getStatusType = (status) => {
  const statusMap = {
    'online': 'success',
    'offline': 'info',
    'error': 'danger',
    'maintenance': 'warning',
    'pending': 'warning',
    'in_progress': 'warning',
    'completed': 'success',
    'shipped': 'primary',
    'delivered': 'success',
    'cancelled': 'info',
    'pending_review': 'warning',
    'resolved': 'success',
    'processing': 'primary',
    'rejected': 'danger',
    '正常': 'success',
    '异常': 'danger',
    '维护中': 'warning'
  }
  return statusMap[status] || 'info'
}

export const getStatusText = (status) => {
  const statusMap = {
    'online': '在线',
    'offline': '离线',
    'error': '错误',
    'maintenance': '维护中',
    'pending': '待处理',
    'in_progress': '进行中',
    'completed': '已完成',
    'shipped': '已发货',
    'delivered': '已送达',
    'cancelled': '已取消',
    'pending_review': '待审核',
    'resolved': '已解决',
    'processing': '处理中',
    'rejected': '已拒绝',
    '正常': '正常',
    '异常': '异常',
    '维护中': '维护中'
  }
  return statusMap[status] || status
}

export const getPriorityType = (priority) => {
  const priorityMap = {
    'high': 'danger',
    'medium': 'warning',
    'low': 'info'
  }
  return priorityMap[priority] || 'info'
}

export const getPriorityText = (priority) => {
  const priorityMap = {
    'high': '高',
    'medium': '中',
    'low': '低'
  }
  return priorityMap[priority] || priority
}

export const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

export const validatePhone = (phone) => {
  const re = /^1[3-9]\d{9}$/
  return re.test(phone)
}

export const validatePassword = (password) => {
  return password && password.length >= 6
}
