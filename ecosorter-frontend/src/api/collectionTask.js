import request from '@/utils/request'

export const collectionTaskApi = {
  getNavigationRoute(collectorId) {
    return request({
      url: `/api/collection-tasks/navigation/${collectorId}`,
      method: 'get'
    })
  },

  getTasksByCollector(collectorId) {
    return request({
      url: `/api/collection-tasks/collector/${collectorId}`,
      method: 'get'
    })
  },

  getTasksByStatus(status) {
    return request({
      url: `/api/collection-tasks/status/${status}`,
      method: 'get'
    })
  },

  startTask(taskId) {
    return request({
      url: `/api/collection-tasks/${taskId}/start`,
      method: 'post'
    })
  },

  completeTask(taskId, data) {
    return request({
      url: `/api/collection-tasks/${taskId}/complete`,
      method: 'post',
      data
    })
  },

  generateTasks() {
    return request({
      url: '/api/collection-tasks/generate',
      method: 'post'
    })
  }
}
