# ECO-SORTER Intelligent Waste Sorting System

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/stargazers)&nbsp;[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/network/members)&nbsp;[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/issues)&nbsp;[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/EcoSorter?style=for-the-badge)](https://github.com/YangShengzhou03/EcoSorter/blob/main/LICENSE)&nbsp;[![Vue.js](https://img.shields.io/badge/Vue.js-3.3.11-4FC08D?style=for-the-badge&logo=vue.js)](https://vuejs.org/)&nbsp;[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)&nbsp;[![Python](https://img.shields.io/badge/Python-3.8+-3776AB?style=for-the-badge&logo=python)](https://www.python.org/)

<div align="center">
  <h3>A modern intelligent waste sorting management system with a front-end and back-end separation architecture</h3>
  <p>AI-based waste sorting platform connecting residents, collectors, administrators, and smart trash can devices</p>
</div>

[Quick Start](#quick-start) • [Features](#features) • [Technical Architecture](#technical-architecture) • [API Documentation](#api-documentation)

</div>

## Project Overview

ECO-SORTER is an intelligent waste sorting management system that adopts a front-end and back-end separation architecture, integrates AI image recognition technology, and achieves automatic waste sorting. The system includes four subsystems: resident terminal, collector terminal, management backend, and smart trash can terminal, providing a complete waste sorting solution.

Core system features include:
- AI intelligent recognition of waste categories (recyclable, hazardous, kitchen waste, other waste)
- Points reward mechanism to encourage residents to sort correctly
- Smart trash can device management
- Collection task scheduling and management
- Data statistics and analysis

## Features

### Resident Terminal Features

**User Authentication** - Registration and login, password modification, personal information management

**Waste Sorting** - Photo recognition of waste categories, viewing sorting history, getting sorting guidance

**Points System** - Viewing points balance, points details, points redemption for products

**Product Redemption** - Browsing points mall, redeeming products, viewing order status

**Recycling Appointment** - Booking door-to-door recycling service, viewing appointment records, canceling appointments

**Complaint Appeal** - Filing appeals for sorting results, viewing appeal processing progress

**Notifications** - Viewing system notifications and announcements

**Personal Center** - Personal information management, password modification, viewing statistics

### Collector Terminal Features

**Task Management** - Viewing assigned collection tasks, starting/completing tasks, reporting exceptions

**Device Monitoring** - Viewing trash can device status, capacity monitoring

**Data Statistics** - Viewing personal work statistics, task completion status

**Personal Center** - Personal information management, password modification

### Management Backend Features

**Data Dashboard** - Overall system data statistics, chart display

**User Management** - User list, user information management, points adjustment

**Device Management** - Trash can device management, device status monitoring, device add/edit/delete

**Task Management** - Collection task management, task assignment, exception handling

**Product Management** - Points mall product management, product listing/delisting

**Order Management** - Redemption order management, order status update

**Banner Management** - Homepage banner management

**Category Management** - Waste category management, category example management

**Notification Management** - System notification publishing, management

**Complaint Management** - User complaint handling, appeal review

**Report Statistics** - Data reports, statistical analysis

### Smart Trash Can Terminal Features

**Device Initialization** - Device activation, device information configuration

**User Login** - SMS verification code login, QR code login, NFC login, face recognition, guest mode

**Waste Recognition** - Camera photo capture, AI recognition of waste categories, disposal guidance

**Work Status** - Device status display, banner display

## Quick Start

### Environment Requirements

**Development Environment**

- Java 17+ (backend development)
- Python 3.8+ (AI recognition service)
- Node.js 16+ (frontend development)
- MySQL 8.0+ (database)
- Maven 3.6+ (backend build)

**Production Environment**

- Server: Linux/Windows Server
- Memory: 4GB+ RAM
- Storage: 10GB+ available space

### Environment Configuration

**Java 17**
```bash
# Download and install Java 17
# Configure JAVA_HOME environment variable
# Add bin directory to PATH
java -version
```

**Python 3.8+**
```bash
# Download and install Python 3.8+
# Add Scripts directory to PATH
python --version
```

**Node.js 16+**
```bash
# Download and install Node.js 16+
node -v
npm -v
```

**MySQL 8.0**
```bash
# Download and install MySQL 8.0
# Create database and execute initialization script
mysql -u root -p < data.sql
```

### Installation and Deployment

**1. Clone the project**

```bash
git clone https://github.com/YangShengzhou03/EcoSorter.git
cd eco-sorter
```

**2. Database Configuration**

```sql
-- Create database
CREATE DATABASE eco_sorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Import data
USE eco_sorter;
SOURCE data.sql;
```

**3. Backend Deployment (Java)**

Edit `ecosorter-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eco_sorter?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

app:
  jwt:
    secret: ecosorter-jwt-secret-key-2024-change-in-production
    expires-ms: 86400000

server:
  port: 8081
```

Start backend service:

```bash
cd ecosorter-backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

**4. AI Recognition Service Deployment (Python)**

Edit database configuration in `trashcan-backend/yolo_recognition.py`:

```python
DATABASE_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'your_password',
    'database': 'eco_sorter',
    'charset': 'utf8mb4'
}
```

Install dependencies and start:

```bash
cd trashcan-backend
pip install -r requirements.txt
python App.py
```

**5. Main System Frontend Deployment**

```bash
cd ecosorter-frontend
npm install
npm run serve
```

Access URL: http://localhost:8080

**6. Smart Trash Can Frontend Deployment**

```bash
cd trashcan-frontend
npm install
npm run serve
```

Access URL: http://localhost:3000

## Default Accounts

The system creates default users for testing during initialization:

| Role | Email | Password |
| --- | --- | --- |
| Resident | resident@qq.com | 123456 |
| Collector | collector@qq.com | 123456 |
| Administrator | admin@qq.com | 123456 |
| Trash Can | trashcan@qq.com | 123456 |

## Technical Architecture

### System Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Resident Frontend│   │  Collector Frontend│ │  Admin Frontend │
│                 │    │                 │    │                 │
│  Vue 3.3.11     │    │  Vue 3.3.11     │    │  Vue 3.3.11     │
│  Element Plus   │    │  Element Plus   │    │  Element Plus   │
│  Port: 8080     │    │  Port: 8080     │    │  Port: 8080     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │    Spring Boot Backend  │
                    │                         │
                    │  Spring Boot 3.1.0     │
                    │  MyBatis Plus 3.5.7    │
                    │  Port: 8081             │
                    └─────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
          ┌─────────────────┐         ┌─────────────────┐
          │   MySQL Database│         │  AI Recognition  │
          │                 │         │    Service       │
          │  MySQL 8.0      │         │                 │
          │  Port: 3306     │         │  Python 3.8+    │
          │                 │         │  FastAPI        │
          │                 │         │  Port: 9000      │
          └─────────────────┘         └─────────────────┘
                    │                           │
                    └─────────────┬─────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │  Smart Trash Can       │
                    │  Frontend             │
                    │                         │
                    │  Vue 3.3.11            │
                    │  Element Plus          │
                    │  Port: 3000            │
                    └─────────────────────────┘
```

### Technology Stack Details

**Resident/Collector/Admin Frontend Technology Stack**

| Technology | Version | Purpose |
| --- | --- | --- |
| Vue.js | 3.3.11 | Frontend framework |
| Element Plus | 2.4.4 | UI component library |
| Vue Router | 4.2.5 | Routing management |
| Pinia | 2.1.7 | State management |
| Axios | 1.6.2 | HTTP client |

**Backend Technology Stack (Java)**

| Technology | Version | Purpose |
| --- | --- | --- |
| Spring Boot | 3.1.0 | Java enterprise development framework |
| MyBatis Plus | 3.5.7 | Data persistence framework |
| MySQL | 8.0+ | Relational database |
| JWT | 0.11.5 | JSON Web Token authentication |
| Spring Security | 6.1.0 | Security framework |
| Aliyun OSS | 3.17.4 | Alibaba Cloud Object Storage |

**AI Recognition Service Technology Stack (Python)**

| Technology | Version | Purpose |
| --- | --- | --- |
| Python | 3.8+ | Programming language |
| FastAPI | 0.104.1 | Web framework |
| Uvicorn | 0.24.0 | ASGI server |
| PyMySQL | 1.1.0 | MySQL driver |
| python-jose | 3.3.0 | JWT handling |

**Smart Trash Can Frontend Technology Stack**

| Technology | Version | Purpose |
| --- | --- | --- |
| Vue.js | 3.3.11 | Frontend framework |
| Element Plus | 2.4.4 | UI component library |
| Vue Router | 4.2.5 | Routing management |

### Project Structure

```
eco-sorter/
├── data.sql                              # Database initialization script
├── ecosorter-backend/                     # Spring Boot backend
│   ├── src/main/java/com/ecosorter/
│   │   ├── config/                       # Configuration classes
│   │   ├── controller/                   # Controllers
│   │   ├── dto/                         # Data transfer objects
│   │   ├── enums/                       # Enum classes
│   │   ├── exception/                    # Exception handling
│   │   ├── model/                       # Entity classes
│   │   ├── repository/                   # Data access layer
│   │   └── service/                     # Business logic layer
│   ├── src/main/resources/
│   │   └── application.yml              # Configuration file
│   └── pom.xml                         # Maven configuration
├── ecosorter-frontend/                  # Resident/Collector/Admin frontend
│   ├── src/
│   │   ├── api/                         # API interfaces
│   │   ├── assets/                      # Static resources
│   │   ├── components/                  # Layout components
│   │   ├── router.js                    # Routing configuration
│   │   ├── stores/                      # State management
│   │   ├── styles/                      # Global styles
│   │   ├── utils/                       # Utility classes
│   │   ├── views/                       # Page components
│   │   │   ├── admin/                  # Admin pages
│   │   │   ├── collector/              # Collector pages
│   │   │   ├── index/                  # Homepage pages
│   │   │   └── resident/               # Resident pages
│   │   ├── App.vue                      # Root component
│   │   └── main.js                     # Entry file
│   ├── package.json                     # Dependency configuration
│   └── vue.config.js                   # Vue configuration
├── trashcan-backend/                    # AI recognition service
│   ├── App.py                          # Application entry
│   ├── yolo_recognition.py             # YOLO recognition module
│   └── requirements.txt                # Python dependencies
└── trashcan-frontend/                  # Smart trash can frontend
    ├── src/
    │   ├── api/                        # API interfaces
    │   ├── router.js                   # Routing configuration
    │   ├── utils/                      # Utility classes
    │   ├── views/                      # Page components
    │   ├── App.vue                     # Root component
    │   └── main.js                     # Entry file
    ├── package.json                    # Dependency configuration
    └── vue.config.js                  # Vue configuration
```

## API Documentation

### Authentication Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| User Registration | POST | /api/auth/register | Register new user |
| User Login | POST | /api/auth/login | User login |
| Refresh Token | POST | /api/auth/refresh | Refresh access token |
| User Logout | POST | /api/auth/logout | User logout |
| Get Current User | GET | /api/auth/me | Get current logged-in user information |

### User Management Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get User Statistics | GET | /api/user/statistics | Get user statistics data |

### Waste Classification Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Classification History | GET | /api/classification/history | Get user classification history records |
| Get Category List | GET | /api/classification/categories | Get all waste categories |
| Create Category | POST | /api/classification/categories | Create new waste category (admin) |
| Update Category | PUT | /api/classification/categories/{id} | Update waste category (admin) |
| Delete Category | DELETE | /api/classification/categories/{id} | Delete waste category (admin) |

### Trash Can Device Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Device Information | GET | /api/trashcan/me | Get trash can device information |
| Update Device Status | PUT | /api/trashcan/status | Update trash can device status |
| Submit Classification Record | POST | /api/trashcan/classification | Submit waste classification record |

### Points Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Points Records | GET | /api/points/records | Get user points records |
| Get Points Records (Paginated) | GET | /api/points/records/page | Get points records paginated |
| Get Total Points | GET | /api/points/total | Get user total points |

### Product Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Product List | GET | /api/products | Get product list |
| Get Product Details | GET | /api/products/{id} | Get product details |
| Create Product | POST | /api/products | Create product (admin) |
| Update Product | PUT | /api/products/{id} | Update product (admin) |
| Delete Product | DELETE | /api/products/{id} | Delete product (admin) |

### Order Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get User Orders | GET | /api/orders | Get current user orders |
| Get All Orders | GET | /api/orders/all | Get all orders (admin) |
| Get Order Details | GET | /api/orders/{id} | Get order details |
| Create Order | POST | /api/orders | Create redemption order |
| Update Order Status | PUT | /api/orders/{id}/status | Update order status (admin) |
| Update Tracking Number | PUT | /api/orders/{id}/tracking-number | Update tracking number (admin) |

### Booking Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get User Bookings | GET | /api/bookings | Get current user bookings |
| Get Booking Details | GET | /api/bookings/{id} | Get booking details |
| Create Booking | POST | /api/bookings | Create recycling appointment |
| Cancel Booking | POST | /api/bookings/{id}/cancel | Cancel booking |

### Collection Task Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Tasks by Status | GET | /api/collection-tasks/status/{status} | Get tasks by status (admin) |
| Generate Tasks | POST | /api/collection-tasks/generate | Generate collection tasks (admin) |
| Reassign Task | POST | /api/collection-tasks/{taskId}/reassign | Reassign task (admin) |
| Get Pending Exceptions | GET | /api/collection-tasks/exceptions/pending | Get pending exceptions (admin) |
| Review Exception | POST | /api/collection-tasks/exceptions/{exceptionId}/review | Review exception (admin) |

### Complaint Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Submit Complaint | POST | /api/complaints | Submit complaint |
| Get My Complaints | GET | /api/complaints/my | Get current user complaints |
| Get All Complaints | GET | /api/complaints/admin | Get all complaints (admin) |
| Get Pending Count | GET | /api/complaints/admin/pending-count | Get pending complaint count (admin) |
| Process Complaint | PUT | /api/complaints/admin/{id} | Process complaint (admin) |
| Delete Complaint | DELETE | /api/complaints/{id} | Delete complaint |

### Banner Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Banner List | GET | /api/banners | Get banner list |
| Get Banner Details | GET | /api/banners/{id} | Get banner details |
| Create Banner | POST | /api/banners | Create banner (admin) |
| Update Banner | PUT | /api/banners/{id} | Update banner (admin) |
| Delete Banner | DELETE | /api/banners/{id} | Delete banner (admin) |

### Notification Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Notification List | GET | /api/notices | Get notification list |
| Get Published Notifications | GET | /api/notices/published | Get published notifications |
| Get Notification Details | GET | /api/notices/{id} | Get notification details |
| Create Notification | POST | /api/notices | Create notification (admin) |
| Update Notification | PUT | /api/notices/{id} | Update notification (admin) |
| Delete Notification | DELETE | /api/notices/{id} | Delete notification (admin) |

### Admin Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Dashboard Data | GET | /api/admin/dashboard | Get admin dashboard data |
| Get Device Status | GET | /api/admin/device-status | Get device status statistics |
| Get Activity Records | GET | /api/admin/activities | Get activity records |
| Get User List | GET | /api/admin/users | Get user list |
| Create User | POST | /api/admin/users | Create user |
| Update User | PUT | /api/admin/users/{userId} | Update user information |
| Delete User | DELETE | /api/admin/users/{userId} | Delete user |
| Adjust User Points | PUT | /api/admin/users/{userId}/points | Adjust user points |
| Get Device List | GET | /api/admin/devices | Get device list |
| Create Device | POST | /api/admin/devices | Create device |
| Update Device | PUT | /api/admin/devices/{deviceId} | Update device information |
| Delete Device | DELETE | /api/admin/devices/{deviceId} | Delete device |
| Regenerate Device Token | POST | /api/admin/devices/{deviceId}/regenerate-token | Regenerate device authentication token |
| Get Reports | GET | /api/admin/reports | Get data reports |

### Collector Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Dashboard | GET | /api/collector/dashboard | Get collector dashboard data |
| Get Task List | GET | /api/collector/tasks | Get collector task list |
| Get Task Details | GET | /api/collector/tasks/{taskId} | Get task details |
| Start Task | POST | /api/collector/tasks/{taskId}/start | Start task |
| Complete Task | POST | /api/collector/tasks/{taskId}/complete | Complete task |
| Report Exception | POST | /api/collector/tasks/{taskId}/exception | Report exception |
| Get Device List | GET | /api/collector/devices | Get device list |

### Profile Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Get Profile | GET | /api/profile | Get personal profile |
| Update Profile | PUT | /api/profile | Update personal profile |
| Update Avatar | PUT | /api/profile/avatar | Update avatar |
| Change Password | POST | /api/profile/change-password | Change password |

### File Upload Interfaces

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Upload Avatar | POST | /api/upload/avatar | Upload avatar |

### AI Recognition Interfaces (Python Backend)

| Interface | Method | Path | Description |
| --- | --- | --- | --- |
| Health Check | GET | / | API health check |
| Waste Recognition | POST | /api/recognition/recognize | Recognize waste category |
| Upload Image | POST | /api/upload | Upload image |
| Health Check | GET | /api/health | Health check |

## Port Configuration

| Service | Port | Description |
| --- | --- | --- |
| Main System Frontend | 8080 | Vue.js development server |
| Main System Backend | 8081 | Spring Boot application |
| Trash Can Frontend | 3000 | Vue.js development server |
| AI Recognition Service | 9000 | FastAPI application |
| MySQL Database | 3306 | Database service |

## Development Guide

### Frontend Development

```bash
# Main system frontend
cd ecosorter-frontend
npm install
npm run serve

# Trash can frontend
cd trashcan-frontend
npm install
npm run serve
```

### Backend Development

```bash
# Java backend
cd ecosorter-backend
mvn clean install
mvn spring-boot:run

# Python backend
cd trashcan-backend
pip install -r requirements.txt
python App.py
```

### Database Operations

```bash
# Connect to database
mysql -u root -p

# Select database
USE eco_sorter;

# View table structure
SHOW TABLES;

# Execute SQL script
SOURCE data.sql;
```

## FAQ

### 1. Port Already in Use

If the port is already in use, you can modify the port number in the configuration file:

- Main system frontend: `ecosorter-frontend/vue.config.js`
- Main system backend: `ecosorter-backend/src/main/resources/application.yml`
- Trash can frontend: `trashcan-frontend/vue.config.js`
- AI recognition service: `trashcan-backend/App.py`

### 2. Database Connection Failed

Check if the database configuration in `application.yml` is correct:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eco_sorter
    username: root
    password: your_password
```

### 3. CORS Issues

Ensure the backend has CORS configured to allow frontend access:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Contact

- Project URL: [https://github.com/YangShengzhou03/EcoSorter](https://github.com/YangShengzhou03/EcoSorter)
- Issue Tracker: [Issues](https://github.com/YangShengzhou03/EcoSorter/issues)

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork this project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Submit a Pull Request

## Acknowledgments

Thanks to all developers who have contributed to this project!

---

**Note**: This project is for learning and research purposes only, please do not use it for commercial purposes.
