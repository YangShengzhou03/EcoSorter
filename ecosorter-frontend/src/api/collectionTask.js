import request from '@/utils/request'

export const collectionTaskApi = {
  getTasksByStatus(status) {
    return request({
      url: `/api/collection-tasks/status/${status}`,
      method: 'get'
    })
  },

  reassignTask(taskId, newCollectorId) {
    return request({
      url: `/api/collection-tasks/${taskId}/reassign`,
      method: 'post',
      data: { newCollectorId }
    })
  },

  getPendingExceptions() {
    return request({
      url: '/api/collection-tasks/exceptions/pending',
      method: 'get'
    })
  },

  reviewException(exceptionId, data) {
    return request({
      url: `/api/collection-tasks/exceptions/${exceptionId}/review`,
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
