from yolo_recognition import app
import uvicorn

if __name__ == "__main__":
    print("=" * 50)
    print("YOLO26 垃圾识别服务 - 示例模式")
    print("=" * 50)
    print("这是一个示例实现，使用随机模拟识别结果")
    print("实际使用时需要集成真正的YOLO26模型")
    print("=" * 50)
    uvicorn.run(app, host="0.0.0.0", port=9000)
