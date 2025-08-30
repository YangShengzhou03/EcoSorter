#!/bin/bash

# EcoSorter 项目设置脚本
# 用于快速设置开发环境

echo "🚀 开始设置 EcoSorter 开发环境..."

# 检查 Node.js 版本
if ! command -v node &> /dev/null; then
    echo "❌ Node.js 未安装，请先安装 Node.js 18+"
    exit 1
fi

NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "❌ Node.js 版本过低，请安装 Node.js 18+"
    exit 1
fi

echo "✅ Node.js 版本: $(node -v)"

# 检查 pnpm（可选但推荐）
if command -v pnpm &> /dev/null; then
    echo "📦 使用 pnpm 作为包管理器"
    PACKAGE_MANAGER="pnpm"
else
    echo "📦 使用 npm 作为包管理器"
    PACKAGE_MANAGER="npm"
fi

# 安装根目录依赖
echo "📦 安装根目录依赖..."
$PACKAGE_MANAGER install

# 安装后端依赖
echo "📦 安装后端依赖..."
cd backend && $PACKAGE_MANAGER install && cd ..

# 安装前端依赖
echo "📦 安装前端依赖..."
cd frontend && $PACKAGE_MANAGER install && cd ..

# 安装垃圾桶Web端依赖
echo "📦 安装垃圾桶Web端依赖..."
cd trash-bin-web && $PACKAGE_MANAGER install && cd ..

# 复制环境变量文件
if [ ! -f .env ]; then
    echo "📝 创建环境变量文件..."
    cp .env.example .env
    echo "⚠️  请编辑 .env 文件配置数据库和其他设置"
fi

# 设置 Git hooks
echo "🔧 设置 Git hooks..."
$PACKAGE_MANAGER run prepare

echo ""
echo "🎉 开发环境设置完成！"
echo ""
echo "下一步操作："
echo "1. 编辑 .env 文件配置数据库连接"
echo "2. 运行 'npm run dev' 启动开发服务器"
echo "3. 访问 http://localhost:3000/api 查看 API 文档"
echo "4. 访问 http://localhost:5173 查看前端应用"
echo ""
echo "📚 更多信息请查看 docs/ 目录下的文档"