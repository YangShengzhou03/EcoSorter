# EcoSorter API接口文档

## 项目概述

EcoSorter是一个垃圾分类管理系统，包含以下模块：
- **Java后端**：基于Spring Boot的RESTful API服务
- **Vue3前端**：用户界面
- **Python垃圾桶后端**：垃圾桶设备管理
- **Vue垃圾桶前端**：垃圾桶设备界面

## 基础信息

- **Base URL**: `http://localhost:8081`
- **Content-Type**: `application/json`
- **认证方式**: Bearer Token (JWT)

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

### HTTP状态码
- `200`: 请求成功
- `201`: 创建成功
- `400`: 请求参数错误
- `401`: 未授权/登录过期
- `403`: 无权限访问
- `404`: 资源不存在
- `429`: 请求过于频繁
- `500`: 服务器内部错误

---

## 1. 认证模块 (Authentication)

### 1.1 用户注册
**接口**: `POST /api/auth/register`

**请求参数**:
```json
{
  "username": "string (3-20字符, 仅字母数字下划线)",
  "email": "string (有效邮箱)",
  "password": "string (最少6字符)",
  "firstName": "string (可选, 最多50字符)",
  "lastName": "string (可选, 最多50字符)",
  "phone": "string (可选, 有效手机号)"
}
```

**响应**:
```json
{
  "token": "string",
  "refreshToken": "string",
  "user": {
    "id": "string",
    "username": "string",
    "email": "string",
    "role": "string"
  }
}
```

### 1.2 设备激活
**接口**: `POST /api/auth/device/activate`

**请求参数**:
```json
{
  "deviceId": "string",
  "deviceName": "string",
  "location": "string",
  "binType": "string"
}
```

**响应**:
```json
{
  "token": "string",
  "device": {
    "id": "string",
    "deviceId": "string",
    "location": "string",
    "status": "string"
  }
}
```

### 1.3 用户登录
**接口**: `POST /api/auth/login`

**请求参数**:
```json
{
  "email": "string",
  "password": "string",
  "twoFactorCode": "string (可选)",
  "rememberMe": "boolean (默认false)"
}
```

**响应**: 同注册响应

### 1.4 刷新Token
**接口**: `POST /api/auth/refresh`

**请求参数**: 
- Query参数: `refreshToken` (string)

**响应**: 同注册响应

### 1.5 用户登出
**接口**: `POST /api/auth/logout`

**权限**: 需要登录

**响应**:
```json
"Logged out successfully"
```

### 1.6 人脸登录
**接口**: `POST /api/auth/face-login-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 人脸图片文件

**响应**:
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "user": {
    "id": "string",
    "username": "string",
    "email": "string",
    "role": "string"
  }
}
```

**说明**: 
- 通过人脸识别进行登录
- 直接上传人脸图片文件
- Python端自动提取人脸特征并匹配
- 相似度 > 0.6 时认为匹配成功

### 1.7 获取当前用户信息
**接口**: `GET /api/auth/me`

**权限**: 需要登录

**响应**:
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "role": "string",
  "isActive": "boolean",
  "lastLogin": "string",
  "createdAt": "string",
  "updatedAt": "string",
  "profile": {
    "avatar": "string",
    "phone": "string",
    "fullName": "string"
  }
}
```

---

## 2. 用户模块 (User)

### 2.1 获取用户统计数据
**接口**: `GET /api/user/statistics`

**权限**: 需要登录

**响应**:
```json
{
  "totalClassifications": "integer",
  "totalPoints": "integer",
  "totalBookings": "integer",
  "totalOrders": "integer"
}
```

---

## 3. 管理员模块 (Admin)

### 3.1 获取管理员仪表盘数据
**接口**: `GET /api/admin/dashboard`

**权限**: ADMIN

**响应**:
```json
{
  "totalUsers": "integer",
  "totalBookings": "integer",
  "totalOrders": "integer",
  "totalComplaints": "integer",
  "pendingTasks": "integer",
  "deviceStatus": {
    "online": "integer",
    "offline": "integer",
    "maintenance": "integer"
  }
}
```

### 3.2 获取设备状态
**接口**: `GET /api/admin/device-status`

**权限**: ADMIN

**响应**:
```json
{
  "totalDevices": "integer",
  "onlineDevices": "integer",
  "offlineDevices": "integer",
  "fullTrashcans": "integer",
  "nearFullTrashcans": "integer"
}
```

### 3.3 获取活动记录
**接口**: `GET /api/admin/activities`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "type": "string",
    "description": "string",
    "timestamp": "string",
    "userId": "string"
  }
]
```

### 3.4 获取用户列表
**接口**: `GET /api/admin/users`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "username": "string",
    "email": "string",
    "role": "string",
    "isActive": "boolean",
    "createdAt": "string"
  }
]
```

### 3.5 创建用户
**接口**: `POST /api/admin/users`

**权限**: ADMIN

**请求参数**: 同注册请求

**响应**: UserListResponse

### 3.6 更新用户
**接口**: `PUT /api/admin/users/{userId}`

**权限**: ADMIN

**请求参数**:
```json
{
  "role": "string",
  "isActive": "boolean"
}
```

**响应**: UserListResponse

### 3.7 调整用户积分
**接口**: `PUT /api/admin/users/{userId}/points`

**权限**: ADMIN

**请求参数**:
```json
{
  "points": "integer",
  "reason": "string"
}
```

**响应**: 204 No Content

### 3.8 删除用户
**接口**: `DELETE /api/admin/users/{userId}`

**权限**: ADMIN

**响应**: 204 No Content

### 3.9 获取设备列表
**接口**: `GET /api/admin/devices`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "deviceId": "string",
    "location": "string",
    "status": "string",
    "capacityLevel": "integer",
    "maxCapacity": "integer",
    "threshold": "integer",
    "statusText": "string",
    "lastUpdate": "string"
  }
]
```

### 3.10 创建设备
**接口**: `POST /api/admin/devices`

**权限**: ADMIN

**请求参数**: DeviceListResponse

**响应**: DeviceListResponse

### 3.11 更新设备
**接口**: `PUT /api/admin/devices/{deviceId}`

**权限**: ADMIN

**请求参数**: DeviceListResponse

**响应**: DeviceListResponse

### 3.12 删除设备
**接口**: `DELETE /api/admin/devices/{deviceId}`

**权限**: ADMIN

**响应**: 204 No Content

### 3.13 重置管理员密码
**接口**: `POST /api/admin/devices/{deviceId}/reset-admin-password`

**权限**: ADMIN

**响应**: 204 No Content

### 3.14 获取报告列表
**接口**: `GET /api/admin/reports`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "type": "string",
    "title": "string",
    "description": "string",
    "status": "string",
    "createdAt": "string"
  }
]
```

---

## 4. 预约模块 (Booking)

### 4.1 获取用户预约列表
**接口**: `GET /api/bookings`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "type": "string",
      "description": "string",
      "estimatedWeight": "integer",
      "appointmentDate": "string",
      "timeSlot": "string",
      "contactName": "string",
      "contactPhone": "string",
      "address": "string",
      "remark": "string",
      "status": "string",
      "createdAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 4.2 获取预约详情
**接口**: `GET /api/bookings/{id}`

**权限**: 需要登录

**响应**: BookingResponse

### 4.3 创建预约
**接口**: `POST /api/bookings`

**权限**: 需要登录

**请求参数**:
```json
{
  "type": "string",
  "description": "string (可选, 最多500字符)",
  "estimatedWeight": "integer",
  "appointmentDate": "string (YYYY-MM-DD格式)",
  "timeSlot": "string",
  "contactName": "string (最多50字符)",
  "contactPhone": "string (有效手机号)",
  "address": "string (最多200字符)",
  "remark": "string (可选, 最多500字符)"
}
```

**响应**: BookingResponse

### 4.4 取消预约
**接口**: `POST /api/bookings/{id}/cancel`

**权限**: 需要登录

**响应**: BookingResponse

---

## 5. 订单模块 (Order)

### 5.1 获取用户订单列表
**接口**: `GET /api/orders`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `status`: 订单状态 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "userId": "string",
      "productId": "string",
      "productName": "string",
      "quantity": "integer",
      "totalPoints": "integer",
      "status": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 5.2 获取所有订单（管理员）
**接口**: `GET /api/orders/all`

**权限**: ADMIN

**查询参数**: 同5.1

**响应**: 同5.1

### 5.3 获取订单详情
**接口**: `GET /api/orders/{id}`

**权限**: 需要登录

**响应**: OrderResponse

### 5.4 创建订单
**接口**: `POST /api/orders`

**权限**: 需要登录

**请求参数**: Order实体

**响应**: OrderResponse

### 5.5 更新订单状态
**接口**: `PUT /api/orders/{id}/status`

**权限**: ADMIN

**查询参数**:
- `status`: 订单状态

**响应**: OrderResponse

### 5.6 更新物流单号
**接口**: `PUT /api/orders/{id}/tracking-number`

**权限**: ADMIN

**查询参数**:
- `trackingNumber`: 物流单号

**响应**: OrderResponse

---

## 6. 积分模块 (Point)

### 6.1 获取积分记录列表（分页）
**接口**: `GET /api/points/records/page`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认0)
- `size`: 每页数量 (默认10)

**响应**: 分页数据

---

## 7. 商品模块 (Product)

### 7.1 获取商品列表
**接口**: `GET /api/products`

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `category`: 商品分类 (可选)
- `status`: 商品状态 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "category": "string",
      "points": "integer",
      "stock": "integer",
      "image": "string",
      "status": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 7.2 获取商品详情
**接口**: `GET /api/products/{id}`

**响应**: ProductResponse

### 7.3 创建商品
**接口**: `POST /api/products`

**权限**: ADMIN

**请求参数**:
```json
{
  "name": "string",
  "description": "string",
  "category": "string",
  "points": "integer",
  "stock": "integer",
  "image": "string",
  "status": "string"
}
```

**响应**: ProductResponse

### 7.4 更新商品
**接口**: `PUT /api/products/{id}`

**权限**: ADMIN

**请求参数**: 同7.3

**响应**: ProductResponse

### 7.5 删除商品
**接口**: `DELETE /api/products/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 8. 分类模块 (Classification)

### 8.1 获取分类历史记录
**接口**: `GET /api/classification/history`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认0)
- `size`: 每页数量 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDirection`: 排序方向 (默认desc)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "userId": "string",
      "category": "string",
      "confidence": "float",
      "imageUrl": "string",
      "createdAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 8.2 获取垃圾类别列表
**接口**: `GET /api/classification/categories`

**响应**:
```json
[
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "color": "string",
    "icon": "string",
    "examples": ["string"]
  }
]
```

### 8.3 创建垃圾类别
**接口**: `POST /api/classification/categories`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.4 更新垃圾类别
**接口**: `PUT /api/classification/categories/{categoryId}`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.5 删除垃圾类别
**接口**: `DELETE /api/classification/categories/{categoryId}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 9. 收集员模块 (Collector)

### 9.1 获取收集员仪表盘数据
**接口**: `GET /api/collector/dashboard`

**权限**: COLLECTOR

**响应**:
```json
{
  "totalTasks": "integer",
  "completedTasks": "integer",
  "pendingTasks": "integer",
  "totalCollections": "integer",
  "todayCollections": "integer"
}
```

### 9.2 获取任务列表
**接口**: `GET /api/collector/tasks`

**权限**: COLLECTOR

**查询参数**:
- `page`: 页码 (可选)
- `pageSize`: 每页数量 (可选)

**响应**:
```json
[
  {
    "id": "string",
    "trashcanId": "string",
    "trashcanLocation": "string",
    "status": "string",
    "priority": "string",
    "assignedAt": "string",
    "completedAt": "string"
  }
]
```

### 9.3 获取任务详情
**接口**: `GET /api/collector/tasks/{taskId}`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.4 开始任务
**接口**: `POST /api/collector/tasks/{taskId}/start`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.5 完成任务
**接口**: `POST /api/collector/tasks/{taskId}/complete`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.6 报告异常
**接口**: `POST /api/collector/tasks/{taskId}/exception`

**权限**: COLLECTOR

**请求参数**:
```json
{
  "description": "string"
}
```

**响应**: 204 No Content

### 9.7 获取设备列表
**接口**: `GET /api/collector/devices`

**权限**: COLLECTOR

**响应**: DeviceListResponse数组

### 9.8 获取统计数据
**接口**: `GET /api/collector/statistics`

**权限**: COLLECTOR

**响应**: UserStatisticsResponse

### 9.9 获取积分记录
**接口**: `GET /api/collector/point-records`

**权限**: COLLECTOR

**查询参数**:
- `type`: 积分类型 (可选)
- `startDate`: 开始日期 (可选)
- `endDate`: 结束日期 (可选)
- `page`: 页码 (可选)
- `pageSize`: 每页数量 (可选)

**响应**: PointRecordResponse数组

### 9.10 获取订单列表
**接口**: `GET /api/collector/orders`

**权限**: COLLECTOR

**查询参数**:
- `status`: 订单状态 (可选)
- `startDate`: 开始日期 (可选)
- `endDate`: 结束日期 (可选)
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)

**响应**: OrderResponse数组

### 9.11 创建订单
**接口**: `POST /api/collector/orders`

**权限**: COLLECTOR

**请求参数**: CreateOrderRequest

**响应**: OrderResponse

### 9.12 获取订单详情
**接口**: `GET /api/collector/orders/{orderId}`

**权限**: COLLECTOR

**响应**: OrderResponse

### 9.13 取消订单
**接口**: `POST /api/collector/orders/{orderId}/cancel`

**权限**: COLLECTOR

**响应**: 204 No Content

---

## 10. 收集任务模块 (Collection Task)

### 10.1 根据状态获取任务
**接口**: `GET /api/collection-tasks/status/{status}`

**权限**: ADMIN

**响应**: CollectionTaskResponse数组

### 10.2 生成任务
**接口**: `POST /api/collection-tasks/generate`

**权限**: ADMIN

**响应**: 204 No Content

### 10.3 重新分配任务
**接口**: `POST /api/collection-tasks/{taskId}/reassign`

**权限**: ADMIN

**请求参数**:
```json
{
  "newCollectorId": "integer"
}
```

**响应**: CollectionTaskResponse

### 10.4 获取待处理异常
**接口**: `GET /api/collection-tasks/exceptions/pending`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "taskId": "string",
    "type": "string",
    "description": "string",
    "status": "string",
    "reportedAt": "string"
  }
]
```

### 10.5 审核异常
**接口**: `POST /api/collection-tasks/exceptions/{exceptionId}/review`

**权限**: ADMIN

**请求参数**:
```json
{
  "status": "string",
  "reviewNotes": "string"
}
```

**响应**: TaskExceptionResponse

---

## 11. 投诉模块 (Complaint)

### 11.1 提交投诉
**接口**: `POST /api/complaints`

**权限**: 需要登录

**请求参数**:
```json
{
  "classificationId": "string",
  "type": "string (最多50字符)",
  "description": "string (5-500字符)"
}
```

**响应**:
```json
{
  "id": "string",
  "userId": "string",
  "classificationId": "string",
  "type": "string",
  "description": "string",
  "status": "string",
  "createdAt": "string",
  "processedAt": "string",
  "adminResponse": "string"
}
```

### 11.2 获取我的投诉
**接口**: `GET /api/complaints/my`

**权限**: 需要登录

**响应**: ComplaintResponse数组

### 11.3 获取所有投诉（管理员）
**接口**: `GET /api/complaints/admin`

**权限**: ADMIN

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `status`: 投诉状态 (可选)

**响应**: 分页数据

### 11.4 处理投诉
**接口**: `PUT /api/complaints/admin/{id}`

**权限**: ADMIN

**请求参数**:
```json
{
  "status": "string",
  "adminResponse": "string"
}
```

**响应**: ComplaintResponse

### 11.5 删除投诉
**接口**: `DELETE /api/complaints/{id}`

**权限**: 需要登录

**响应**: 204 No Content

---

## 12. 通知模块 (Notice)

### 12.1 获取通知列表
**接口**: `GET /api/notices`

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `keyword`: 关键词搜索 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "title": "string",
      "content": "string",
      "type": "string",
      "status": "string",
      "priority": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 12.2 获取已发布通知
**接口**: `GET /api/notices/published`

**响应**: NoticeResponse数组

### 12.3 获取通知详情
**接口**: `GET /api/notices/{id}`

**响应**: NoticeResponse

### 12.4 创建通知
**接口**: `POST /api/notices`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "content": "string",
  "type": "string",
  "priority": "string",
  "status": "string"
}
```

**响应**: NoticeResponse

### 12.5 更新通知
**接口**: `PUT /api/notices/{id}`

**权限**: ADMIN

**请求参数**: 同12.4

**响应**: NoticeResponse

### 12.6 删除通知
**接口**: `DELETE /api/notices/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 13. 轮播图模块 (Banner)

### 13.1 获取活跃轮播图
**接口**: `GET /api/banners`

**查询参数**:
- `target`: 目标位置 (可选)

**响应**:
```json
[
  {
    "id": "string",
    "title": "string",
    "imageUrl": "string",
    "link": "string",
    "target": "string",
    "order": "integer",
    "isActive": "boolean",
    "createdAt": "string",
    "updatedAt": "string"
  }
]
```

### 13.2 获取轮播图详情
**接口**: `GET /api/banners/{id}`

**响应**: BannerResponse

### 13.3 创建轮播图
**接口**: `POST /api/banners`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "imageUrl": "string",
  "link": "string",
  "target": "string",
  "order": "integer",
  "isActive": "boolean"
}
```

**响应**: BannerResponse

### 13.4 更新轮播图
**接口**: `PUT /api/banners/{id}`

**权限**: ADMIN

**请求参数**: 同13.3

**响应**: BannerResponse

### 13.5 删除轮播图
**接口**: `DELETE /api/banners/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 14. 个人资料模块 (Profile)

### 14.1 获取个人资料
**接口**: `GET /api/profile`

**权限**: 需要登录

**响应**:
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "phone": "string",
  "fullName": "string",
  "avatarUrl": "string",
  "address": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

### 14.2 更新个人资料
**接口**: `PUT /api/profile`

**权限**: 需要登录

**请求参数**: User实体

**响应**: ProfileResponse

### 14.3 更新头像
**接口**: `PUT /api/profile/avatar`

**权限**: 需要登录

**请求参数**:
```json
{
  "avatar": "string (头像URL)"
}
```

**响应**: ProfileResponse

---

## 15. 文件上传模块 (Upload)

### 15.1 上传头像
**接口**: `POST /api/upload/avatar`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 文件对象

**响应**:
```json
{
  "url": "string",
  "filename": "string",
  "size": "integer"
}
```

### 15.2 上传图片
**接口**: `POST /api/upload/image`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 文件对象

**响应**:
```json
{
  "url": "string",
  "filename": "string",
  "size": "integer"
}
```

---

## 16. 垃圾桶设备模块 (Trashcan)

### 16.1 获取设备信息
**接口**: `GET /api/trashcan/me`

**权限**: 需要设备认证令牌

**响应**:
```json
{
  "id": "string",
  "deviceId": "string",
  "location": "string",
  "capacityLevel": "integer",
  "maxCapacity": "integer",
  "threshold": "integer",
  "status": "string",
  "statusText": "string",
  "lastUpdate": "string"
}
```

### 16.2 更新设备信息
**接口**: `PUT /api/trashcan/me`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "deviceName": "string (可选)",
  "location": "string (可选)",
  "binType": "string (可选)",
  "maxCapacity": "integer (可选)",
  "threshold": "integer (可选)"
}
```

**响应**: DeviceListResponse

### 16.3 更新设备状态
**接口**: `PUT /api/trashcan/status`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "capacityLevel": "integer"
}
```

**响应**: DeviceListResponse

### 16.4 提交分类记录
**接口**: `POST /api/trashcan/classification`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "imageUrl": "string",
  "categoryId": "integer",
  "confidence": "float"
}
```

**响应**: 204 No Content

### 16.5 管理员登录
**接口**: `POST /api/trashcan/admin-login`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "password": "string"
}
```

**响应**:
```json
{
  "success": "boolean",
  "message": "string",
  "deviceId": "string"
}
```

### 16.6 重置管理员密码
**接口**: `POST /api/trashcan/reset-password`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "newPassword": "string"
}
```

**响应**: 204 No Content

### 16.7 设备心跳
**接口**: `POST /api/trashcan/heartbeat`

**权限**: 需要设备认证令牌

**响应**: 204 No Content

**说明**: 
- 垃圾桶设备定期发送心跳请求
- 用于更新设备的最后活跃时间
- 用于判断设备在线/离线状态
- 默认心跳间隔为30分钟

### 16.8 清除设备数据
**接口**: `POST /api/trashcan/clear-data`

**权限**: 需要设备认证令牌

**响应**: 204 No Content

---

## 17. Python后端接口 (AI识别服务)

**Base URL**: `http://localhost:9000`

### 17.1 服务状态
**接口**: `GET /`

**响应**:
```json
{
  "message": "EcoSorter Recognition API",
  "port": 9000,
  "status": "running"
}
```

### 17.2 健康检查
**接口**: `GET /api/health`

**响应**:
```json
{
  "status": "healthy",
  "port": 9000,
  "service": "EcoSorter Recognition"
}
```

### 17.3 垃圾识别
**接口**: `POST /api/recognition/recognize`

**请求参数**:
- `image_url`: 图片URL (Query参数)
- `authorization`: 认证令牌 (可选, Header)

**响应**:
```json
{
  "data": {
    "item": "塑料瓶",
    "category": "可回收物",
    "confidence": 85,
    "advice": "请投放到可回收物垃圾桶",
    "categoryId": 1
  },
  "success": true
}
```

### 17.4 人脸注册
**接口**: `POST /api/face/register-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `userId`: 用户ID (Query参数)
- `file`: 人脸图片文件

**响应**:
```json
{
  "success": true,
  "message": "人脸注册成功"
}
```

**说明**:
- 文件大小限制: 512KB
- 支持的图片格式: JPG, PNG
- 从图片中提取人脸特征向量并存储到数据库
- 如果无法检测到人脸，返回错误信息

### 17.5 人脸验证
**接口**: `POST /api/face/verify-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 人脸图片文件

**响应**:
```json
{
  "success": true,
  "verified": true,
  "userId": 1,
  "username": "resident",
  "confidence": 0.85,
  "message": "验证成功，匹配用户: resident"
}
```

**说明**:
- 文件大小限制: 512KB
- 支持的图片格式: JPG, PNG
- 仅验证居民角色(RESIDENT)的用户
- 相似度阈值: 0.6
- 返回匹配度最高的用户信息
