# ECO-SORTER 智能垃圾分类系统

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/stargazers)&nbsp;[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/network/members)&nbsp;[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/issues)&nbsp;[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/EcoSorter?style=for-the-badge)](https://github.com/YangShengzhou03/EcoSorter/blob/main/LICENSE)&nbsp;[![Vue.js](https://img.shields.io/badge/Vue.js-3.3.11-4FC08D?style=for-the-badge&logo=vue.js)](https://vuejs.org/)&nbsp;[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)&nbsp;[![Python](https://img.shields.io/badge/Python-3.10-3776AB?style=for-the-badge&logo=python)](https://www.python.org/)

<h3>智能垃圾分类管理系统 | AI识别 + 前后端分离</h3>

[快速开始](#快速开始) • [技术栈](#技术栈)

</div>

## 快速开始

### Docker Compose 一键部署（推荐）

```bash
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/docker-compose.yaml -o docker-compose.yaml && \
docker-compose up -d && \
sleep 20 && \
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/data.sql -o data.sql && \
docker exec -i ecosorter-mysql mysql -uroot -p123456 < data.sql && \
docker ps
```

访问 `http://服务器IP`，账号：admin@qq.com，密码：123456

### Docker 手动部署

```bash
# 1. 创建网络并下载初始化脚本
docker network create ecosorter-network
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/data.sql -o data.sql

# 2. 拉取所有镜像
docker pull mariadb:10.11
docker pull yangshengzhou/ecosorter:backend-v1
docker pull yangshengzhou/ecosorter:trashcan-backend-v1
docker pull yangshengzhou/ecosorter:frontend-v1
docker pull yangshengzhou/ecosorter:trashcan-frontend-v1

# 3. 启动 MariaDB 并初始化
docker run -d --network ecosorter-network --restart always --name ecosorter-mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=eco_sorter -p 3306:3306 mariadb:10.11
sleep 10
docker exec -i ecosorter-mysql mysql -uroot -p123456 < data.sql

# 4. 启动所有服务
docker run -d --name ecosorter-backend --network ecosorter-network --restart always -p 8081:8081 yangshengzhou/ecosorter:backend-v1
docker run -d --name trashcan-backend --network ecosorter-network --restart always -p 9000:9000 yangshengzhou/ecosorter:trashcan-backend-v1
docker run -d --name ecosorter-frontend --network ecosorter-network --restart always -p 80:80 yangshengzhou/ecosorter:frontend-v1
docker run -d --name trashcan-frontend --network ecosorter-network --restart always -p 8080:80 yangshengzhou/ecosorter:trashcan-frontend-v1
```

## 访问地址

| 服务 | 地址 |
|------|------|
| Eco前端 | http://localhost |
| 智能垃圾桶前端 | http://localhost:8080 |
| Java后端API | http://localhost:8081 |
| Python AI服务 | http://localhost:9000 |

## 默认账号

| 角色 | 邮箱 | 密码 |
|------|------|------|
| 管理员 | admin@qq.com | 123456 |
| 收集员 | collector@qq.com | 123456 |
| 居民 | resident@qq.com | 123456 |

## 技术栈

**前端**: Vue 3.3.11 + Element Plus + Vue Router + Pinia  
**后端**: Spring Boot 3.1.0 + MyBatis Plus + Spring Security + JWT  
**AI服务**: Python 3.10 + FastAPI + YOLOv8 + face_recognition  
**移动端**: Flutter 3.2.6  
**数据库**: MariaDB 10.11  
**部署**: Docker + Nginx

## License

MIT License
