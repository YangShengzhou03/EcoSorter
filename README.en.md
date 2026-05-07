# ECO-SORTER Smart Waste Classification System

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/stargazers)&nbsp;[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/network/members)&nbsp;[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/issues)&nbsp;[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/EcoSorter?style=for-the-badge)](https://github.com/YangShengzhou03/EcoSorter/blob/main/LICENSE)&nbsp;[![Vue.js](https://img.shields.io/badge/Vue.js-3.3.11-4FC08D?style=for-the-badge&logo=vue.js)](https://vuejs.org/)&nbsp;[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)&nbsp;[![Python](https://img.shields.io/badge/Python-3.10-3776AB?style=for-the-badge&logo=python)](https://www.python.org/)

<h3>Smart Waste Classification System | AI Recognition + Frontend-Backend Separation</h3>

[Quick Start](#quick-start) • [Tech Stack](#tech-stack)

</div>

## Quick Start

### Docker Compose One-Click Deployment (Recommended)

```bash
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/docker-compose.yaml -o docker-compose.yaml && \
docker-compose up -d && \
sleep 20 && \
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/data.sql -o data.sql && \
docker exec -i ecosorter-mysql mysql -uroot -p123456 < data.sql && \
docker ps
```

Visit `http://server-ip`, Account: admin@qq.com, Password: 123456

### Docker Manual Deployment

```bash
# 1. Create network and download init script
docker network create ecosorter-network
curl -sSL https://gitee.com/Yangshengzhou/eco-sorter/raw/master/data.sql -o data.sql

# 2. Pull all images
docker pull mariadb:10.11
docker pull yangshengzhou/ecosorter:backend-v1
docker pull yangshengzhou/ecosorter:trashcan-backend-v1
docker pull yangshengzhou/ecosorter:frontend-v1
docker pull yangshengzhou/ecosorter:trashcan-frontend-v1

# 3. Start MariaDB and initialize
docker run -d --network ecosorter-network --restart always --name ecosorter-mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=eco_sorter -p 3306:3306 mariadb:10.11
sleep 10
docker exec -i ecosorter-mysql mysql -uroot -p123456 < data.sql

# 4. Start all services
docker run -d --name ecosorter-backend --network ecosorter-network --restart always -p 8081:8081 yangshengzhou/ecosorter:backend-v1
docker run -d --name trashcan-backend --network ecosorter-network --restart always -p 9000:9000 yangshengzhou/ecosorter:trashcan-backend-v1
docker run -d --name ecosorter-frontend --network ecosorter-network --restart always -p 80:80 yangshengzhou/ecosorter:frontend-v1
docker run -d --name trashcan-frontend --network ecosorter-network --restart always -p 8080:80 yangshengzhou/ecosorter:trashcan-frontend-v1
```

## Access URLs

| Service | URL |
|---------|-----|
| Eco Frontend | http://localhost |
| Smart Trashcan Frontend | http://localhost:8080 |
| Java Backend API | http://localhost:8081 |
| Python AI Service | http://localhost:9000 |

## Default Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@qq.com | 123456 |
| Collector | collector@qq.com | 123456 |
| Resident | resident@qq.com | 123456 |

## Tech Stack

**Frontend**: Vue 3.3.11 + Element Plus + Vue Router + Pinia  
**Backend**: Spring Boot 3.1.0 + MyBatis Plus + Spring Security + JWT  
**AI Service**: Python 3.10 + FastAPI + YOLOv8 + face_recognition  
**Mobile**: Flutter 3.2.6  
**Database**: MariaDB 10.11  
**Deployment**: Docker + Nginx

## License

MIT License
