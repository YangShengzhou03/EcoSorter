@echo off
chcp 65001 >nul
echo ========================================
echo YOLO26 垃圾识别服务启动脚本
echo ========================================
echo.

echo 检查 Python 环境...
python --version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到 Python，请先安装 Python 3.8+
    pause
    exit /b 1
)

echo [成功] Python 环境检查通过
echo.

echo 检查依赖包...
pip show fastapi >nul 2>&1
if errorlevel 1 (
    echo [信息] 正在安装依赖包...
    pip install -r requirements.txt
    if errorlevel 1 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
    echo [成功] 依赖安装完成
) else (
    echo [成功] 依赖包检查通过
)
echo.

echo 检查数据库连接...
python -c "import pymysql; pymysql.connect(host='localhost', user='root', password='123456', database='eco_sorter')" >nul 2>&1
if errorlevel 1 (
    echo [警告] 无法连接到数据库，请确保 MySQL 正在运行且配置正确
    echo [信息] 服务将启动，但数据库相关功能可能无法正常工作
) else (
    echo [成功] 数据库连接正常
)
echo.

echo ========================================
echo 正在启动 YOLO26 识别服务...
echo ========================================
echo.

python yolo_recognition.py

pause
