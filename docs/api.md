# EcoSorter API接口文档

## 项目概述

EcoSorter是一个垃圾分类管理系统，包含以下模块：
- **Java后端**：基于Spring Boot的RESTful API服务（端口8081）
- **Python AI服务**：基于FastAPI的AI识别服务（端口9000）
- **Vue3前端**：用户界面
- **Vue垃圾桶前端**：垃圾桶设备界面

## 基础信息

**Java后端**
- **Base URL**: `http://localhost:8081`
- **Content-Type**: `application/json`
- **认证方式**: Bearer Token (JWT)

**Python AI服务**
- **Base URL**: `http://localhost:9000`
- **Content-Type**: `application/json` 或 `multipart/form-data`
- **认证方式**: 部分接口需要认证

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
- `204`: 无内容（成功但无返回数据）
- `400`: 请求参数错误
- `401`: 未授权/登录过期
- `403`: 无权限访问
- `404`: 资源不存在
- `429`: 请求过于频繁
- `500`: 服务器内部错误

---

## Java后端API (端口8081)

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
```
"Logged out successfully"
```

### 1.6 获取当前用户信息
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
- `categoryName`: 分类名称 (可选)
- `status`: 状态 (可选)

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

### 8.3 搜索垃圾类别
**接口**: `GET /api/classification/search`

**查询参数**:
- `keyword`: 搜索关键词 (string)

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

### 8.4 提交分类
**接口**: `POST /api/classification/submit`

**权限**: 需要登录

**请求参数**:
```json
{
  "trashcanId": "string",
  "categoryId": "string",
  "confidence": "float (可选, 默认1.0)"
}
```

**响应**: 204 No Content

**说明**:
- 提交垃圾分类记录
- 自动计算并添加积分（基础分10分 + 置信度×10）
- 更新用户统计数据（总分类数、正确分类数、连续天数等）

### 8.5 创建垃圾类别
**接口**: `POST /api/classification/categories`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.6 更新垃圾类别
**接口**: `PUT /api/classification/categories/{categoryId}`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.7 删除垃圾类别
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

## 10. 垃圾桶设备模块 (Trashcan)

### 10.1 获取当前设备信息
**接口**: `GET /api/trashcans/me`

**权限**: 需要登录（设备角色）

**响应**:
```json
{
  "id": "string",
  "deviceId": "string",
  "deviceName": "string",
  "location": "string",
  "status": "string",
  "capacityLevel": "integer",
  "maxCapacity": "integer",
  "threshold": "integer",
  "statusText": "string",
  "lastUpdate": "string"
}
```

### 10.2 更新设备信息
**接口**: `PUT /api/trashcans/me`

**权限**: 需要登录（设备角色）

**请求参数**: Device实体

**响应**: DeviceResponse

### 10.3 更新设备状态
**接口**: `PUT /api/trashcans/status`

**权限**: 需要登录（设备角色）

**请求参数**:
```json
{
  "status": "string",
  "capacityLevel": "integer"
}
```

**响应**: DeviceResponse

### 10.4 提交垃圾分类
**接口**: `POST /api/trashcans/classification`

**权限**: 需要登录（设备角色）

**请求参数**:
```json
{
  "categoryId": "integer",
  "imageUrl": "string",
  "confidence": "float",
  "userId": "integer (可选, 默认4)"
}
```

**响应**: 204 No Content

**说明**:
- 提交垃圾分类记录
- 自动计算并添加积分（基础分10分 + 置信度×10）
- 更新用户统计数据
- 如果 userId 为 4 或未提供，则不添加积分

### 10.5 设备心跳
**接口**: `POST /api/trashcans/heartbeat`

**权限**: 需要登录（设备角色）

**请求参数**: 无

**响应**: 204 No Content

**说明**:
- 更新设备最后活跃时间
- 用于保持设备在线状态

### 10.6 设备管理员登录
**接口**: `POST /api/trashcans/admin-login`

**权限**: 需要登录（设备角色）

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

**说明**:
- 验证设备管理员密码
- 密码错误时返回 401 状态码

### 10.7 重置设备密码
**接口**: `POST /api/trashcans/reset-password`

**权限**: 需要登录（设备角色）

**请求参数**:
```json
{
  "newPassword": "string"
}
```

**响应**: 204 No Content

### 10.8 清除设备数据
**接口**: `POST /api/trashcans/clear-data`

**权限**: 需要登录（设备角色）

**响应**: 204 No Content

### 10.9 报告设备故障
**接口**: `POST /api/trashcan/fault`

**权限**: 需要登录（设备角色）

**请求参数**:
```json
{
  "description": "string"
}
```

**响应**: 204 No Content

**说明**:
- 报告设备故障
- 将设备状态设置为 "error"
- 更新设备最后更新时间

---

## 11. 垃圾桶公共接口 (Trashcan Public)

### 11.1 获取附近垃圾桶
**接口**: `GET /api/trashcans/nearby`

**查询参数**:
- `latitude`: 纬度 (float, 可选)
- `longitude`: 经度 (float, 可选)
- `radius`: 半径 (float, 默认5000米)

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
    "distance": "float (米)"
  }
]
```

### 11.2 根据ID获取垃圾桶
**接口**: `GET /api/trashcans/{id}`

**响应**: DeviceListResponse

### 11.3 根据设备ID获取垃圾桶
**接口**: `GET /api/trashcans/device/{deviceId}`

**响应**: DeviceListResponse

---

## 12. 收集任务模块 (Collection Task)

### 12.1 根据状态获取任务
**接口**: `GET /api/collection-tasks/status/{status}`

**权限**: ADMIN

**响应**: CollectionTaskResponse数组

### 12.2 生成任务
**接口**: `POST /api/collection-tasks/generate`

**权限**: ADMIN

**响应**: 204 No Content

### 12.3 重新分配任务
**接口**: `POST /api/collection-tasks/{taskId}/reassign`

**权限**: ADMIN

**请求参数**:
```json
{
  "newCollectorId": "integer"
}
```

**响应**: CollectionTaskResponse

### 12.4 获取待处理异常
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

### 12.5 审核异常
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

## 13. 投诉模块 (Complaint)

### 13.1 提交投诉
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

### 13.2 获取我的投诉
**接口**: `GET /api/complaints/my`

**权限**: 需要登录

**响应**: ComplaintResponse数组

### 13.3 获取所有投诉（管理员）
**接口**: `GET /api/complaints/admin`

**权限**: ADMIN

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `status`: 投诉状态 (可选)

**响应**: 分页数据

### 13.4 处理投诉
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

### 13.5 删除投诉
**接口**: `DELETE /api/complaints/{id}`

**权限**: 需要登录

**响应**: 204 No Content

---

## 14. 二维码登录模块 (QR Login)

### 14.1 创建二维码会话
**接口**: `POST /api/qr-login/create`

**权限**: 需要登录（设备角色）

**响应**:
```json
{
  "qrCode": "string",
  "status": "string"
}
```

**说明**:
- 创建新的二维码登录会话
- 二维码有效期5分钟
- 返回二维码字符串和初始状态

### 14.2 查询二维码状态
**接口**: `GET /api/qr-login/status/{qrCode}`

**权限**: 需要登录（设备角色）

**响应**:
```json
{
  "token": "string (可选)",
  "status": "string",
  "message": "string"
}
```

**状态说明**:
- `pending`: 等待扫描
- `scanned`: 已扫描，等待确认
- `confirmed`: 已确认登录，返回token
- `expired`: 已过期

### 14.3 扫描二维码
**接口**: `POST /api/qr-login/scan`

**权限**: 需要登录（用户角色）

**请求参数**:
```json
{
  "qrCode": "string"
}
```

**响应**:
```json
{
  "qrCode": "string",
  "status": "string",
  "message": "string"
}
```

**说明**:
- 手机APP扫描二维码后调用此接口
- 将二维码状态从pending改为scanned
- 用户需要在手机上确认登录

### 14.4 确认登录
**接口**: `POST /api/qr-login/confirm`

**权限**: 需要登录（用户角色）

**请求参数**:
```json
{
  "qrCode": "string"
}
```

**响应**:
```json
{
  "qrCode": "string",
  "status": "string",
  "message": "string"
}
```

**说明**:
- 用户在手机上点击确认登录后调用此接口
- 将二维码状态从scanned改为confirmed
- 返回登录token给垃圾桶端

---

## 15. 通知模块 (Notice)

### 15.1 获取所有通知
**接口**: `GET /api/notices`

**权限**: 无需认证

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `keyword`: 搜索关键词 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "title": "string",
      "content": "string",
      "published": "boolean",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 15.2 获取已发布通知
**接口**: `GET /api/notices/published`

**权限**: 无需认证

**响应**: NoticeResponse 数组

### 15.3 获取通知详情
**接口**: `GET /api/notices/{id}`

**权限**: 无需认证

**响应**: NoticeResponse

### 15.4 创建通知
**接口**: `POST /api/notices`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "content": "string",
  "published": "boolean"
}
```

**响应**: NoticeResponse

### 15.5 更新通知
**接口**: `PUT /api/notices/{id}`

**权限**: ADMIN

**请求参数**: 同15.4

**响应**: NoticeResponse

### 15.6 删除通知
**接口**: `DELETE /api/notices/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 16. 轮播图模块 (Banner)

### 16.1 获取所有轮播图
**接口**: `GET /api/banners`

**权限**: 无需认证

**查询参数**:
- `target`: 目标位置 (可选)

**响应**: BannerResponse 数组

### 16.2 获取轮播图详情
**接口**: `GET /api/banners/{id}`

**响应**: BannerResponse

### 16.3 创建轮播图
**接口**: `POST /api/banners`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "imageUrl": "string",
  "linkUrl": "string",
  "order": "integer",
  "active": "boolean"
}
```

**响应**: BannerResponse

### 16.4 更新轮播图
**接口**: `PUT /api/banners/{id}`

**权限**: ADMIN

**请求参数**: 同16.3

**响应**: BannerResponse

### 16.5 删除轮播图
**接口**: `DELETE /api/banners/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 17. 用户资料模块 (Profile)

### 17.1 获取用户资料
**接口**: `GET /api/profile`

**权限**: 需要登录

**响应**:
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "fullName": "string",
  "phone": "string",
  "avatar": "string",
  "address": "string"
}
```

### 17.2 更新用户资料
**接口**: `PUT /api/profile`

**权限**: 需要登录

**请求参数**:
```json
{
  "fullName": "string",
  "phone": "string",
  "address": "string"
}
```

**响应**: UserResponse

### 17.3 更新头像
**接口**: `PUT /api/profile/avatar`

**权限**: 需要登录

**请求类型**: `application/json`

**请求参数**:
```json
{
  "avatar": "string (头像URL)"
}
```

**响应**: UserResponse

---

## 18. 上传模块 (Upload)

### 18.1 上传头像
**接口**: `POST /api/upload/avatar`

**权限**: 需要登录

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 头像文件

**响应**:
```json
{
  "url": "string",
  "filename": "string"
}
```

### 18.2 上传图片
**接口**: `POST /api/upload/image`

**权限**: 需要登录

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 图片文件

**响应**: 同18.1

---

## Python AI服务API (端口9000)

## 19. 识别模块 (Recognition)

### 19.1 识别垃圾（通过URL）
**接口**: `POST /api/recognition/recognize`

**请求参数**:
- `image_url`: string (图片URL)
- `authorization`: string (可选, 认证token)

**响应**:
```json
{
  "data": {
    "item": "string",
    "category": "string",
    "confidence": "integer",
    "advice": "string",
    "categoryId": "integer"
  },
  "success": true
}
```

**说明**:
- 使用YOLOv8模型识别垃圾
- 支持本地URL（会自动下载图片）
- 返回置信度最高的识别结果

### 19.2 识别垃圾（通过文件上传）
**接口**: `POST /api/recognition/recognize-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 图片文件

**响应**:
```json
{
  "data": {
    "item": "string",
    "category": "string",
    "confidence": "integer",
    "advice": "string",
    "categoryId": "integer"
  },
  "success": true
}
```

**说明**:
- 直接上传图片文件进行识别
- 支持多种图片格式
- 返回置信度最高的识别结果

### 19.3 多目标识别
**接口**: `POST /api/recognition/recognize-multi`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 图片文件

**响应**:
```json
{
  "data": [
    {
      "item": "string",
      "category": "string",
      "confidence": "float",
      "advice": "string",
      "categoryId": "integer"
    }
  ],
  "success": true,
  "count": "integer"
}
```

**说明**:
- 识别图片中的多个物体
- 只返回置信度 >= 0.5 的结果
- 去重处理，相同物品只返回一次

---

## 20. 人脸识别模块 (Face)

### 20.1 人脸注册
**接口**: `POST /api/face/register-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `userId`: integer (用户ID)
- `file`: 人脸图片文件

**响应**:
```json
{
  "success": true,
  "message": "string"
}
```

**说明**:
- 注册用户人脸信息
- 文件大小限制：512KB
- 需要清晰的正脸照片
- 自动提取人脸特征并存储

### 20.2 人脸验证
**接口**: `POST /api/face/verify-with-file`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 人脸图片文件

**响应**:
```json
{
  "success": true,
  "verified": "boolean",
  "userId": "integer",
  "username": "string",
  "confidence": "float",
  "message": "string"
}
```

**说明**:
- 验证人脸是否匹配已注册用户
- 相似度 > 0.7 时认为匹配成功
- 只验证已激活人脸的居民用户
- 文件大小限制：512KB

---

## 21. 系统信息模块 (System)

### 21.1 获取根路径信息
**接口**: `GET /`

**响应**:
```json
{
  "message": "EcoSorter Recognition API v2.0",
  "port": 9000,
  "status": "running",
  "model": "YOLOv8s",
  "classes": "integer"
}
```

### 21.2 健康检查
**接口**: `GET /api/health`

**响应**:
```json
{
  "status": "healthy",
  "port": 9000,
  "service": "EcoSorter Recognition",
  "version": "2.0.0",
  "model": "YOLOv8s",
  "classes_mapped": "integer",
  "model_loaded": "boolean"
}
```

### 21.3 获取模型信息
**接口**: `GET /api/model/info`

**响应**:
```json
{
  "model_name": "YOLOv8s",
  "model_type": "object_detection",
  "total_classes": 80,
  "classes_mapped": "integer",
  "categories": {
    "可回收物": "金属、塑料、玻璃、纸张、电子产品等",
    "厨余垃圾": "食物、水果、植物、动物等",
    "其他垃圾": "一次性用品、陶瓷、卫生用品等",
    "有害垃圾": "电池、药品等（需自定义训练）"
  }
}
```

---

## 附录

### 垃圾分类说明

系统支持四种垃圾分类：

1. **可回收物**（蓝色）
   - 纸类、塑料、玻璃、金属、织物等
   - 投放建议：清洁干燥、压扁投放

2. **有害垃圾**（红色）
   - 电池、灯管、药品、油漆等
   - 投放建议：轻放、防止破损

3. **厨余垃圾**（绿色）
   - 剩菜剩饭、果皮、茶叶渣等
   - 投放建议：沥干水分、去除包装

4. **其他垃圾**（灰色）
   - 污染纸张、烟蒂、陶瓷碎片等
   - 投放建议：分类投放、难以识别的物品

### 积分规则

- 正确分类垃圾：基础分（10分）+ 置信度加成（0-10分）
- 完成收集任务：+50分
- 提交投诉：+5分
- 兑换商品：-相应积分

### 用户角色

- **ADMIN**: 管理员，拥有所有权限
- **COLLECTOR**: 收集员，负责垃圾收集任务
- **RESIDENT**: 居民，普通用户
- **DEVICE**: 设备，智能垃圾桶设备

### 注意事项

1. 所有需要认证的接口都需要在请求头中携带JWT token：
   ```
   Authorization: Bearer {token}
   ```

2. 分页参数：
   - `page`: 页码，从0或1开始（根据具体接口）
   - `pageSize`: 每页数量，默认10

3. 时间格式：
   - 日期：`YYYY-MM-DD`
   - 日期时间：`YYYY-MM-DD HH:mm:ss`

4. 文件上传限制：
   - 头像：最大2MB
   - 图片：最大5MB
   - 人脸图片：最大512KB

5. 错误处理：
   - 所有错误响应都包含 `code` 和 `message` 字段
   - 客户端应根据 `code` 进行相应的错误处理
