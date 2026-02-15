# Trashcan Backend

YOLO26 垃圾识别服务 - FastAPI 实现

## 环境要求

- Python 3.8+
- MySQL 5.7+

## 安装依赖

```bash
pip install -r requirements.txt
```

## 环境变量

创建 `.env` 文件（可选）：

```
DB_HOST=localhost
DB_USERNAME=root
DB_PASSWORD=123456
DB_NAME=eco_sorter
JWT_SECRET=your-secret-key
```

## 运行服务

```bash
python App.py
```

服务将在 `http://localhost:9000` 启动

## API 端点

### 健康检查
- `GET /api/health` - 服务健康状态

### 图片上传
- `POST /api/upload` - 上传图片文件

### 垃圾识别
- `POST /api/recognition/recognize` - 识别垃圾图片
  - 参数: `image_url` (string)

### 分类记录
- `POST /api/recognition/classification` - 保存分类记录
  - 参数: `image_url`, `category_id`, `confidence`

### 测试
- `GET /api/test/recognize` - 测试识别功能

## 注意事项

当前为示例模式，使用随机模拟识别结果。实际使用时需要集成真正的 YOLO26 模型。
