from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import Optional, List, Dict, Any
import uvicorn
import pymysql
from datetime import datetime
import os
import shutil
from pathlib import Path
import random

app = FastAPI(title="YOLO26 Garbage Recognition API", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

DATABASE_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USERNAME', 'root'),
    'password': os.getenv('DB_PASSWORD', '123456'),
    'database': os.getenv('DB_NAME', 'eco_sorter'),
    'charset': 'utf8mb4'
}

JWT_SECRET = os.getenv('JWT_SECRET', 'ecosorter-jwt-secret-key-2024-change-in-production')

UPLOAD_DIR = Path("uploads")
UPLOAD_DIR.mkdir(exist_ok=True)

class RecognitionResult(BaseModel):
    item: str
    category: str
    confidence: int
    advice: str
    categoryId: Optional[int] = None

class RecognitionResponse(BaseModel):
    data: RecognitionResult
    success: bool

GARBAGE_ITEMS = [
    {"item": "塑料瓶", "category": "可回收物"},
    {"item": "纸箱", "category": "可回收物"},
    {"item": "玻璃瓶", "category": "可回收物"},
    {"item": "金属罐", "category": "可回收物"},
    {"item": "废电池", "category": "有害垃圾"},
    {"item": "废灯管", "category": "有害垃圾"},
    {"item": "过期药品", "category": "有害垃圾"},
    {"item": "果皮", "category": "厨余垃圾"},
    {"item": "菜叶", "category": "厨余垃圾"},
    {"item": "剩饭", "category": "厨余垃圾"},
    {"item": "鱼骨", "category": "厨余垃圾"},
    {"item": "塑料袋", "category": "其他垃圾"},
    {"item": "烟头", "category": "其他垃圾"},
    {"item": "纸巾", "category": "其他垃圾"},
    {"item": "陶瓷碎片", "category": "其他垃圾"}
]

def get_db_connection():
    return pymysql.connect(**DATABASE_CONFIG)

def get_category_mapping():
    category_map = {
        '可回收物': 1,
        '有害垃圾': 2,
        '厨余垃圾': 3,
        '其他垃圾': 4
    }
    return category_map

def get_advice(category: str) -> str:
    advice_map = {
        '可回收物': '请投放到可回收物垃圾桶',
        '有害垃圾': '请投放到有害垃圾桶',
        '厨余垃圾': '请投放到厨余垃圾桶',
        '其他垃圾': '请投放到其他垃圾桶'
    }
    return advice_map.get(category, '请正确分类投放')

def get_category_id(category_name: str) -> Optional[int]:
    try:
        conn = get_db_connection()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT id FROM waste_categories WHERE name = %s", (category_name,))
        result = cursor.fetchone()
        cursor.close()
        conn.close()
        return result['id'] if result else None
    except Exception as e:
        print(f"Error getting category ID: {e}")
        return None

def save_classification(user_id: int, trashcan_id: Optional[int], waste_category_id: Optional[int], 
                       image_url: str, confidence_score: float, ai_suggestion: str,
                       ip_address: Optional[str] = None, user_agent: Optional[str] = None) -> bool:
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        sql = """
        INSERT INTO classifications 
        (user_id, trashcan_id, waste_category_id, image_url, confidence_score, ai_suggestion, 
         ip_address, user_agent, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        
        now = datetime.now()
        cursor.execute(sql, (
            user_id, trashcan_id, waste_category_id, image_url, confidence_score, ai_suggestion,
            ip_address, user_agent, now, now
        ))
        
        conn.commit()
        cursor.close()
        conn.close()
        return True
    except Exception as e:
        print(f"Error saving classification: {e}")
        return False

def get_user_id_from_token(token: str) -> Optional[int]:
    try:
        import jwt
        secret = JWT_SECRET
        decoded = jwt.decode(token, secret, algorithms=["HS256"])
        return decoded.get('userId')
    except Exception as e:
        print(f"Error decoding token: {e}")
        return None

def yolo26_recognize(image_url: str) -> Dict[str, Any]:
    garbage_item = random.choice(GARBAGE_ITEMS)
    confidence = random.randint(75, 95)
    
    return {
        "item": garbage_item["item"],
        "category": garbage_item["category"],
        "confidence": confidence
    }

@app.get("/")
async def root():
    return {
        "message": "YOLO26 Garbage Recognition API is running", 
        "port": 9000,
        "status": "示例模式",
        "description": "这是一个YOLO26识别的示例实现，使用随机模拟识别结果"
    }

@app.post("/api/recognition/recognize", response_model=RecognitionResponse)
async def recognize_garbage(image_url: str, authorization: Optional[str] = None):
    try:
        recognition_result = yolo26_recognize(image_url)
        
        category_id = get_category_id(recognition_result["category"])
        advice = get_advice(recognition_result["category"])
        
        result = RecognitionResult(
            item=recognition_result["item"],
            category=recognition_result["category"],
            confidence=recognition_result["confidence"],
            advice=advice,
            categoryId=category_id
        )
        
        return RecognitionResponse(data=result, success=True)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/recognition/classification")
async def submit_classification(
    image_url: str,
    category_id: Optional[int] = None,
    confidence: float = 0.85,
    authorization: Optional[str] = None,
    ip_address: Optional[str] = None,
    user_agent: Optional[str] = None
):
    try:
        user_id = None
        trashcan_id = None
        
        if authorization and authorization.startswith("Bearer "):
            token = authorization[7:]
            user_id = get_user_id_from_token(token)
            
            if user_id:
                conn = get_db_connection()
                cursor = conn.cursor(pymysql.cursors.DictCursor)
                cursor.execute("SELECT id FROM trashcan_data WHERE device_id = %s", (str(user_id),))
                result = cursor.fetchone()
                cursor.close()
                conn.close()
                if result:
                    trashcan_id = result['id']
        
        if not category_id:
            recognition_result = yolo26_recognize(image_url)
            category_id = get_category_id(recognition_result["category"])
        
        ai_suggestion = f"YOLO26识别结果 - 示例模式"
        
        success = save_classification(
            user_id=user_id if user_id else 1,
            trashcan_id=trashcan_id,
            waste_category_id=category_id,
            image_url=image_url,
            confidence_score=confidence,
            ai_suggestion=ai_suggestion,
            ip_address=ip_address,
            user_agent=user_agent
        )
        
        if success:
            return {"success": True, "message": "分类记录保存成功"}
        else:
            raise HTTPException(status_code=500, detail="保存分类记录失败")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/upload")
async def upload_image(file: UploadFile = File(...)):
    try:
        file_extension = os.path.splitext(file.filename)[1]
        new_filename = f"{datetime.now().strftime('%Y%m%d_%H%M%S')}_{file.filename}"
        file_path = UPLOAD_DIR / new_filename
        
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
        
        file_url = f"/uploads/{new_filename}"
        
        return {"url": file_url, "filename": new_filename, "message": "上传成功"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"上传失败: {str(e)}")

@app.get("/api/health")
async def health_check():
    return {
        "status": "healthy", 
        "port": 9000, 
        "service": "YOLO26 Recognition",
        "mode": "示例模式"
    }

@app.get("/api/test/recognize")
async def test_recognize():
    test_url = "http://example.com/test.jpg"
    recognition_result = yolo26_recognize(test_url)
    
    return {
        "test_image": test_url,
        "recognition_result": recognition_result,
        "message": "YOLO26识别测试成功"
    }

if __name__ == "__main__":
    print("=" * 50)
    print("YOLO26 垃圾识别服务 - 示例模式")
    print("=" * 50)
    print("这是一个示例实现，使用随机模拟识别结果")
    print("实际使用时需要集成真正的YOLO26模型")
    print("=" * 50)
    uvicorn.run(app, host="0.0.0.0", port=9000)
