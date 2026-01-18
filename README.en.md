# EcoSorter - Intelligent Waste Sorting Supervision System

An intelligent waste sorting management system based on Spring Boot + Vue.js

## Project Introduction

EcoSorter is an intelligent waste sorting supervision system based on Spring Boot + Vue.js technology stack, providing a complete waste sorting management solution including user management, waste classification recognition, data statistical analysis, and other functions.

### Core Features

- **User Authentication** - Supports user registration and login, multi-role permission management
- **Waste Classification Recognition** - Integrates Baidu AI image recognition API to achieve waste type recognition
- **Data Statistics** - Visualized charts display classification statistics
- **User Management** - Multi-role permission management system (Administrator, Collector, Resident, Trash Can Device)
- **Task Management** - Waste collection task scheduling and management
- **Complaint Management** - Resident complaint feedback system
- **Notification & Announcement** - System notification and announcement management
- **Points System** - Resident waste classification points rewards

### System Features

- **Modern Technology Stack** - Spring Boot 3.x + Vue 3
- **Security & Reliability** - Spring Security + JWT authentication
- **Data Visualization** - Element Plus component library
- **High Performance** - Optimized build configuration

## Project Structure

```
EcoSorter/
├── ecosorter-backend/          # Spring Boot backend service
│   ├── src/main/java/com/ecosorter/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/         # Controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── exception/          # Exception handling
│   │   ├── model/             # Entity classes
│   │   ├── repository/         # Data access layer
│   │   ├── service/           # Business logic layer
│   │   └── EcoSorterApplication.java
│   ├── src/main/resources/
│   │   └── application.yml   # Application configuration
│   └── pom.xml              # Maven dependencies
├── ecosorter-frontend/       # Vue.js frontend application
│   ├── src/
│   │   ├── api/              # API interfaces
│   │   ├── stores/           # State management
│   │   ├── styles/           # Style files
│   │   ├── utils/            # Utility functions
│   │   ├── views/            # Page components
│   │   ├── App.vue           # Root component
│   │   ├── main.js           # Entry file
│   │   └── router.js         # Router configuration
│   ├── package.json          # Project dependencies
│   └── vue.config.js        # Vue CLI configuration
├── UML代码/                 # UML design documents
├── data.sql                 # Database initialization script
├── README.md                # Project documentation (Chinese)
└── README.en.md             # Project documentation (English)
```

## Technology Stack

### Frontend Technologies

- **Vue.js 3.3.11** - Progressive JavaScript framework
- **Vue Router 4.2.5** - Official router manager
- **Pinia 2.1.7** - State management library
- **Element Plus 2.4.4** - Vue 3 component library
- **Axios 1.6.2** - HTTP client

### Backend Technologies

- **Spring Boot 3.5.6** - Microservice framework
- **Spring Security** - Security framework
- **Spring Data JPA** - Data access framework
- **MySQL** - Relational database
- **Jakarta Validation** - Data validation
- **Jackson** - JSON processing

### Development Tools

- **Node.js** - JavaScript runtime
- **Java 17** - Programming language
- **Maven** - Project build tool
- **Vue CLI** - Vue project scaffolding

## Quick Start

### Environment Requirements

- Node.js >= 16.0.0
- npm >= 8.0.0
- Java >= 17
- Maven >= 3.6
- MySQL >= 8.0

### Backend Deployment

1. **Configure Database**

```sql
CREATE DATABASE ecosorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Modify Configuration File**

Edit `ecosorter-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecosorter?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: root
    password: your_password

baidu:
  api:
    key: your_baidu_api_key
    secret: your_baidu_secret_key
```

3. **Initialize Database**

```bash
mysql -u root -p ecosorter < data.sql
```

4. **Build and Run**

```bash
cd ecosorter-backend
mvn clean install
mvn spring-boot:run
```

The backend service will start at `http://localhost:8081`

### Frontend Deployment

1. **Install Dependencies**

```bash
cd ecosorter-frontend
npm install
```

2. **Development Environment**

```bash
npm run serve
```

Visit `http://localhost:8080`

3. **Production Build**

```bash
npm run build
```

Build artifacts will be output to the `dist` directory

### Test Accounts

The system provides the following test accounts:

- **Administrator Account**: admin / 123456
- **Collector Account**: collector / 123456
- **Resident Account**: resident / 123456
- **Trash Can Device**: trashcan / 123456

## API Documentation

### Authentication

- **POST** `/api/auth/register` - User registration
- **POST** `/api/auth/login` - User login
- **POST** `/api/auth/logout` - User logout
- **POST** `/api/auth/refresh` - Refresh token
- **GET** `/api/auth/me` - Get current user information

### User Management

- **GET** `/api/users` - Get user list
- **GET** `/api/users/{id}` - Get user details
- **PUT** `/api/users/{id}` - Update user information
- **DELETE** `/api/users/{id}` - Delete user

### Waste Classification

- **POST** `/api/classification/recognize` - Waste recognition
- **GET** `/api/classification/history` - Get classification history

### Collection Tasks

- **GET** `/api/collection/tasks` - Get collection task list
- **PUT** `/api/collection/tasks/{id}/complete` - Complete task

### Complaint Management

- **POST** `/api/complaint` - Submit complaint
- **GET** `/api/complaint` - Get complaint list

### Notification & Announcement

- **GET** `/api/notice` - Get notification list
- **POST** `/api/notice` - Create notification

### Profile

- **GET** `/api/profile` - Get profile
- **PUT** `/api/profile` - Update profile

### Administrator Functions

- **GET** `/api/admin/dashboard` - Administrator dashboard
- **GET** `/api/admin/users` - User management
- **GET** `/api/admin/devices` - Device management
- **GET** `/api/admin/reports` - Data reports
- **GET** `/api/admin/logs` - System logs

### Collector Functions

- **GET** `/api/collector/dashboard` - Collector dashboard
- **GET** `/api/collector/tasks` - Task list
- **GET** `/api/collector/navigation` - Navigation route

### Trash Can Device

- **GET** `/api/bin/status` - Device status
- **POST** `/api/bin/upload` - Upload data

## Interface Preview

### Login Page

Simple login interface supporting username and password login, with quick login functionality for test accounts.

### Home Page

Displays system introduction and core features, including intelligent recognition, data statistics, batch processing, and points rewards.

### Resident Portal

- **Dashboard** - View personal waste classification statistics and points
- **Classification Records** - View waste classification history
- **Points** - View points details and redemption records
- **Complaint** - Submit waste classification related complaints
- **Profile** - Manage personal information

### Collector Portal

- **Dashboard** - View task statistics and work progress
- **Tasks** - View and manage collection tasks
- **Navigation** - View optimal collection routes
- **Collection** - Execute waste collection operations
- **Records** - View collection records
- **Device Status** - View trash can device status

### Administrator Portal

- **Dashboard** - View overall system data statistics
- **Configuration** - System parameter configuration
- **Reports** - View various data reports
- **Users** - User management
- **Notifications** - Publish system notifications
- **Devices** - Device management
- **Logs** - View system logs

### Trash Can Device

- **Main Interface** - Device main interface displaying device status and operation buttons

## Configuration

### Frontend Configuration (vue.config.js)

```javascript
module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
}
```

### Backend Configuration (application.yml)

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecosorter
    username: root
    password: your_password
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: none

baidu:
  api:
    key: your_baidu_api_key
    secret: your_baidu_secret_key
```

## Development Guide

### Code Standards

- Follow Vue.js style guide
- Use ESLint for code checking
- Use Prettier for code formatting

### Commit Standards

```bash
# Format: type(scope): description
git commit -m "feat(auth): add user registration feature"
git commit -m "fix(ui): fix login page style issue"
git commit -m "docs(readme): update API documentation"
```

### Testing

```bash
# Frontend testing
npm run lint        # Code check
npm run lint:fix    # Auto fix

# Backend testing
mvn test            # Run tests
mvn clean install   # Build project
```

## System Architecture

### System Roles

The system includes four roles:

1. **Administrator (ADMIN)** - System management, user management, device management, data statistics
2. **Collector (COLLECTOR)** - Waste collection task management, route planning, device monitoring
3. **Resident (RESIDENT)** - Waste classification recognition, points management, complaint feedback
4. **Trash Can Device (TRASHCAN)** - Device data upload, status reporting

### Business Process

1. **User Registration/Login** - Users enter the system through registration or login
2. **Waste Classification** - Residents upload waste images for recognition
3. **Points Rewards** - Correct classification earns points rewards
4. **Task Scheduling** - System automatically generates collection tasks
5. **Task Execution** - Collectors view and execute collection tasks
6. **Data Statistics** - Administrators view various statistical data

## Database Design

### Main Data Tables

- **users** - User table
- **user_profiles** - User profile table
- **classifications** - Waste classification record table
- **collection_tasks** - Collection task table
- **complaints** - Complaint table
- **notices** - Notification and announcement table
- **banners** - Banner table
- **waste_categories** - Waste category table
- **trashcan_data** - Trash can data table

## FAQ

### 1. Backend startup failure

Check if MySQL service is started and database configuration is correct.

### 2. Frontend cannot access backend

Check if backend service is started, port is correct, and CORS configuration is correct.

### 3. Waste recognition failure

Check if Baidu API key is configured correctly and network connection is normal.

## Contributing

Welcome to submit Issues and Pull Requests!

1. Fork this project
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add some amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## License

This project is open source under the MIT License.

## Contact & Support

- Email: yangsz03@foxmail.com
- Issue Feedback: GitHub Issues

---

If this project helps you, please give it a Star! Let's build a better intelligent waste sorting system together!
