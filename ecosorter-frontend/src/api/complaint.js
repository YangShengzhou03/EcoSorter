import request from '@/utils/request'

export const complaintApi = {
  submitComplaint(data) {
    return request({
      url: '/api/complaints',
      method: 'post',
      data
    })
  },

  getMyComplaints() {
    return request({
      url: '/api/complaints/my',
      method: 'get'
    })
  },

  getAdminList(params) {
    return request({
      url: '/api/complaints/admin',
      method: 'get',
      params
    })
  },

  processComplaint(id, data) {
    return request({
      url: `/api/complaints/admin/${id}`,
      method: 'put',
      data
    })
  },

  deleteComplaint(id) {
    return request({
      url: `/api/complaints/${id}`,
      method: 'delete'
    })
  }
}
