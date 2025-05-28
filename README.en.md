# 🌿 **EcoSorter** — Intelligent Garbage Classification Guidance System · Requirements Analysis and Design  
[![Gitee Stars](https://gitee.com/Yangshengzhou/eco-sorter/badge/star.svg?theme=dark)](https://gitee.com/Yangshengzhou/eco-sorter)
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

### Table of Contents
- [I. System Overview](#i-system-overview)
- [II. User Roles](#ii-user-roles)
  - [2.1 Residents 👥](#21-residents-👥)
  - [2.2 System Administrators 👨💻](#22-system-administrators-👨💻)
  - [2.3 Smart Garbage Bins 🗑️](#23-smart-garbage-bins-🗑️)
  - [2.4 Garbage Collectors 🧹](#24-garbage-collectors-🧹)
  - [2.5 Emergency Notification Module 🚨](#25-emergency-notification-module-🚨)
- [III. System Functional Requirements](#iii-system-functional-requirements)
  - [3.1 Garbage Classification Recognition 🤖](#31-garbage-classification-recognition-🤖)
  - [3.2 Points Reward System 🌟](#32-points-reward-system-🌟)
  - [3.3 Education and Promotion 📚](#33-education-and-promotion-📚)
  - [3.4 Data Visualization 📊](#34-data-visualization-📊)
  - [3.5 Report Generation 📄](#35-report-generation-📄)
  - [3.6 User Management 👤](#36-user-management-👤)
  - [3.7 Feedback System 📩](#37-feedback-system-📩)
- [IV. Non-Functional Requirements](#iv-non-functional-requirements)
  - [4.1 Accuracy ✅](#41-accuracy-✅)
  - [4.2 Response Speed ⚡](#42-response-speed-⚡)
  - [4.3 Security 🔒](#43-security-🔒)
  - [4.4 Scalability 🌐](#44-scalability-🌐)
  - [4.5 Reliability 🛠️](#45-reliability-🛠️)
- [V. Subsystem Design](#v-subsystem-design)
  - [5.1 User Interaction Subsystem 📱](#51-user-interaction-subsystem-📱)
  - [5.2 AI Processing Subsystem 🤖](#52-ai-processing-subsystem-🤖)
  - [5.3 Points Management Subsystem 🌟](#53-points-management-subsystem-🌟)
  - [5.4 Data Statistical Analysis Subsystem 📊](#54-data-statistical-analysis-subsystem-📊)
  - [5.5 Backend Management Subsystem 👨💻](#55-backend-management-subsystem-👨💻)
- [VI. Information Management](#vi-information-management)
  - [6.1 User Information 📇](#61-user-information-📇)
  - [6.2 Garbage Classification Information 🗄️](#62-garbage-classification-information-🗄️)
  - [6.3 Points Transaction Information 💰](#63-points-transaction-information-💰)
  - [6.4 System Logs 📝](#64-system-logs-📝)
- [VII. UML Diagram Explanation](#vii-uml-diagram-explanation)
  - [7.1 Use Case Diagram 📌](#71-use-case-diagram-📌)
  - [7.2 Class Diagram 🏷️](#72-class-diagram-🏷️)
  - [7.3 Object Diagram Instance 👥](#73-object-diagram-instance-👥)
  - [7.4 Package Diagram 📦](#74-package-diagram-📦)
  - [7.5 Sequence/Collaboration Diagram 🔄](#75-sequencecollaboration-diagram-🔄)
  - [7.6 State Diagram ⏳](#76-state-diagram-⏳)
  - [7.7 Activity Diagram 🏃](#77-activity-diagram-🏃)
  - [7.8 Component Diagram 🔌](#78-component-diagram-🔌)
  - [7.9 Deployment Diagram 🌐](#79-deployment-diagram-🌐)
- [VIII. Detailed State Diagram Description](#viii-detailed-state-diagram-description)
  - [8.1 State Transitions 🔁](#81-state-transitions-🔁)
  - [8.2 Key Events 📅](#82-key-events-📅)
- [IX. Detailed Activity Diagram Description](#ix-detailed-activity-diagram-description)
  - [9.1 Main Process ✅](#91-main-process-✅)
  - [9.2 Decision Points ⚖️](#92-decision-points-⚖️)
- [X. Detailed Collaboration Diagram Description](#x-detailed-collaboration-diagram-description)
  - [10.1 Object Interaction 👥🗨️](#101-object-interaction-👥🗨️)
- [XI. Detailed Sequence Diagram Description](#xi-detailed-sequence-diagram-description)
  - [11.1 Timing Process ⏱️](#111-timing-process-⏱️)
- [XII. Detailed Package Diagram Description](#xii-detailed-package-diagram-description)
  - [12.1 Module Dependencies 📦→📦](#121-module-dependencies-📦→📦)
- [XIII. Another Detailed Collaboration Diagram Description](#xiii-another-detailed-collaboration-diagram-description)
  - [13.1 Data Association 🔗](#131-data-association-🔗)
- [XIV. Detailed Use Case Diagram Description](#xiv-detailed-use-case-diagram-description)
  - [14.1 Role-Function Matrix 🧩](#141-role-function-matrix-🧩)
- [XV. Detailed Component Diagram Description](#xv-detailed-component-diagram-description)
  - [15.1 Technical Architecture 🏗️](#151-technical-architecture-🏗️)
- [XVI. Detailed Deployment Diagram Description](#xvi-detailed-deployment-diagram-description)
  - [16.1 Physical Architecture 🌐](#161-physical-architecture-🌐)

---

### I. System Overview 🌱

In today's society, garbage classification plays a crucial role in the green and sustainable development of cities. To achieve intelligent management of community garbage classification, enhance residents' participation, and reduce operational costs, we have designed the EcoSorter Intelligent Garbage Classification Guidance System. This system comprehensively integrates core technologies such as AI image recognition, points incentives, and data visualization to cover all aspects of garbage classification, from garbage recognition and classification to user incentive guidance, and then to data analysis and management, providing a one-stop solution for community garbage classification. Through this system, not only can garbage types be automatically and accurately identified, but also residents can be encouraged to actively participate in correct garbage classification behaviors through a points reward system. At the same time, rich data analysis tools can help managers deeply understand the situation of garbage classification and make scientific and reasonable decisions, thereby promoting the green development of cities.

### II. User Roles 👥👨💻🗑️🧹🚨

#### 2.1 Residents 👥

Residents, as direct participants in community garbage classification, play a key role in the entire system. Their daily behaviors directly affect the effectiveness of garbage classification. Residents can participate in garbage classification in the following ways:
- **Garbage Disposal**: Residents can choose to scan the QR code on the garbage bin, and the system will automatically identify the garbage type; they can also manually select the garbage type to complete the garbage disposal. This diversified disposal method facilitates residents' use in different scenarios.
- **Points Management**: Residents can query their total points and detailed records at any time to understand their contributions to garbage classification activities. At the same time, they can participate in various activities and exchange points for a variety of gifts, such as daily necessities and environmental protection supplies, which greatly stimulates residents' enthusiasm for participating in garbage classification.
- **Information Acquisition**: The system will regularly push classification guides, policy notifications, and activity invitations to residents. Through this information, residents can timely understand the latest garbage classification knowledge and community activities, improving their environmental awareness and motivation.

#### 2.2 System Administrators 👨💻

System administrators are the core role in system operation and data management, responsible for ensuring the stable operation of the system and the security and reliability of data. Their main responsibilities include:
- **System Maintenance**: Administrators need to regularly update the software version to ensure that the system has the latest functions and security patches; at the same time, perform data backup and recovery work to prevent data loss; when the system fails, repair it in time to ensure the normal operation of the system.
- **User Management**: Administrators are responsible for reviewing the registration of new users to ensure the authenticity and legality of user information; configure permissions according to users' roles and responsibilities to ensure that different users can only access the functions and data within their permissions; when users forget their passwords, administrators can reset them; in addition, they need to monitor users' status in real time and promptly discover and handle abnormal situations.
- **System Configuration**: Administrators need to set the parameters of garbage bins, such as recognition sensitivity and full overflow warning threshold; define activity rules, including points calculation rules and activity reward mechanisms; manage notification strategies to ensure that notifications are sent to users in a timely and appropriate manner.

#### 2.3 Smart Garbage Bins 🗑️

Smart garbage bins, as intelligent terminal devices for garbage classification, have advanced automatic recognition and data interaction functions. Their main operations include:
- **Automatic Recognition**: Smart garbage bins are equipped with high-precision image sensors that can collect real-time images of garbage. By calling advanced AI models, the garbage bins can classify the garbage in real time and accurately determine the type of garbage.
- **Data Interaction**: Smart garbage bins connect to the cloud server through the network and upload data such as recognition results, disposal records, and device status to the server. This data is crucial for the system's data analysis and management.
- **Notification Trigger**: When an abnormal situation such as full overflow or failure occurs in the garbage bin, the notification mechanism will be immediately triggered, and the abnormal information will be sent to the administrator in real time through SMS, APP push, etc., so that the administrator can handle it in time.

#### 2.4 Garbage Collectors 🧹

The main tasks of garbage collectors are to maintain the status of garbage bins and perform garbage collection and transportation work to ensure the smooth progress of garbage classification work. Their work content includes:
- **Equipment Inspection**: Garbage collectors need to regularly check the cleanliness and operation status of garbage bins, timely discover and record fault information of garbage bins, such as sensor damage and door lock failure.
- **Garbage Collection and Transportation**: When collecting and transporting garbage, garbage collectors scan the QR code of the garbage bin to record information such as collection time, garbage weight, and category. This information is helpful for subsequent data analysis and management.
- **Data Reporting**: Garbage collectors can submit work records and abnormal feedback through the mobile application. For example, when finding that a certain garbage bin frequently fails, they can report it in time so that the administrator can arrange maintenance.

#### 2.5 Emergency Notification Module 🚨

The emergency notification module is an emergency response component when the system is abnormal, which can ensure that relevant personnel are notified in time in case of emergency. Its main functions include:
- **Multi-Level Notification**: This module supports a variety of notification channels, such as SMS, APP push, and voice alarm. According to the severity of the emergency and the different notification objects, the appropriate notification method can be selected.
- **Quick Trigger**: When an emergency event such as device failure or data abnormality occurs, the emergency notification module will automatically trigger the notification chain to ensure that relevant personnel can know the situation and take corresponding measures in the first time.

### III. System Functional Requirements 🤖🌟📚📊📄📩

The EcoSorter system integrates a variety of functions to meet the needs of different users and promote the efficient development of garbage classification work.

#### 3.1 Garbage Classification Recognition 🤖
- **Technical Implementation**: Based on deep learning image recognition algorithms such as ResNet, the system can classify common garbage types such as recyclables, kitchen waste, hazardous waste, and other waste in real time. The algorithm has been optimized with a large amount of training data and has a high recognition accuracy.
- **User Experience**: When residents dispose of garbage, the system will display the recognition results synchronously. If the resident misclassifies the garbage, the system will immediately give a voice prompt and provide graphic guidance to help the resident classify the garbage correctly.

#### 3.2 Points Reward System 🌟
- **Points Rules**: The system has formulated detailed points rules to encourage residents to actively participate in garbage classification. Correctly classifying and disposing of garbage once can earn 1 point, and double points will be awarded for the correct disposal of kitchen waste. In addition, residents can also earn extra points by inviting friends and completing questionnaires.
- **Exchange System**: The points mall provides a variety of products for residents to exchange, including daily necessities, environmental protection gifts, and other categories. The mall will update the inventory in real time to ensure that residents can exchange their favorite products in time.

#### 3.3 Education and Promotion 📚
- **Knowledge Base**: The system has established a knowledge base in the form of pictures, texts, and videos, covering classification guides, policy interpretations, typical case libraries, etc. Residents can access this knowledge at any time through the mobile APP or Web terminal to improve their garbage classification level.
- **Activity Management**: The system organizes activities such as online check-in challenges and offline lectures, supporting residents to sign up for activities and track their progress. Through these activities, residents' environmental awareness and participation will be enhanced.

#### 3.4 Data Visualization 📊
- **Real-Time Dashboard**: The system provides a real-time dashboard that displays information such as the community's daily garbage disposal volume, classification accuracy, and garbage bin status distribution. Managers can timely understand the community's garbage classification situation through the real-time dashboard, discover problems, and handle them in time.
- **Trend Analysis**: The system will generate classification quality reports, user activity rankings, and equipment failure rate charts on a weekly, monthly, and annual basis. Through the analysis of this data, managers can understand the development trend of garbage classification and provide a basis for decision-making.

#### 3.5 Report Generation 📄
- **Automatic Generation**: The system will automatically generate monthly, quarterly, and annual reports, including classification effectiveness, resource consumption, user behavior analysis, etc. These reports can comprehensively reflect the community's garbage classification situation and provide important references for government regulatory departments and community properties.
- **Export Function**: The reports support PDF and Excel format export, which is convenient for government regulatory departments and community properties to file and further analyze.

#### 3.6 User Management 👤
- **Multi-Terminal Login**: The system supports registration with mobile phone numbers, WeChat, ID cards, etc., and provides facial recognition for quick login, which is convenient for users to use.
- **Privacy Protection**: The system encrypts and stores sensitive information such as user addresses and contact information, following the principle of minimum permissions to ensure the security and privacy of user information.

#### 3.7 Feedback System 📩
- **Two-Way Communication**: Users can submit feedback information such as classification questions, equipment failures, or suggestions through the system. Administrators will respond to users' feedback within 48 hours to ensure that users' problems are solved in a timely manner.
- **Problem Archiving**: The system will automatically match common problems with the knowledge base for answers, and archive historical feedback for subsequent query and traceability.

### IV. Non-Functional Requirements ✅⚡🔒🌐🛠️

In addition to rich functions, EcoSorter also focuses on system performance and security to ensure the stable operation of the system and the security of user data.

#### 4.1 Accuracy ✅
- **Recognition Rate**: The system's accuracy on the training set reaches more than 95%, and the accuracy in actual application scenarios also exceeds 90%. To ensure the stability of the recognition rate, the system will regularly optimize and update the model.
- **Data Accuracy**: The error rate of points calculation is controlled within 0.1%, and the real-time error of device status monitoring does not exceed 5 minutes, ensuring the accuracy and reliability of the data.

#### 4.2 Response Speed ⚡
- **Interaction Delay**: The operation response time of the mobile interface does not exceed 1 second, and the return time of AI recognition results is controlled within 3 seconds, ensuring that users can obtain a smooth user experience.
- **Notification Timeliness**: The time from triggering to delivering emergency notifications does not exceed 5 minutes, and the delivery time of ordinary messages does not exceed 30 minutes, ensuring the timely transmission of information.

#### 4.3 Security 🔒
- **Data Encryption**: The system uses the HTTPS protocol to encrypt data at the transmission layer to prevent data from being intercepted or tampered during transmission. At the storage layer, sensitive data such as user information and image data is encrypted with AES-256 to ensure data security.
- **Permission Control**: The system uses a role-based access control (RBAC) mechanism to assign different permissions according to users' roles and responsibilities. At the same time, full auditing of administrators' operation logs is carried out to ensure the traceability of operations.

#### 4.4 Scalability 🌐
- **Technical Architecture**: The system uses a microservice architecture design, supporting containerized deployment (such as Docker) and dynamic scaling. This architecture design enables the system to flexibly adjust resource configuration according to business needs, improving the scalability of the system.
- **Function Expansion**: The system reserves hardware interfaces such as weighing sensors and RFID tag readers to facilitate subsequent function expansion. At the same time, it supports multi-community cascade management to meet the garbage classification management needs of large-scale communities.

#### 4.5 Reliability 🛠️
- **System Availability**: The system availability reaches more than 99.9%, and the failure recovery time of key businesses does not exceed 1 hour. Through redundant design and backup strategies, the system can be quickly restored in case of failure.
- **Disaster Recovery Backup**: The database is fully backed up daily, and the application server uses a remote disaster recovery plan to support minute-level switching. In the event of a major disaster or failure, the security of data and the rapid recovery of the system can be ensured.

### V. Subsystem Design 📱🤖🌟📊👨💻

EcoSorter consists of multiple subsystems, each with a clear functional positioning, working together to achieve the overall functions of the system.

#### 5.1 User Interaction Subsystem 📱
- **Responsibilities**: This subsystem provides mobile APP (supporting iOS and Android systems) and Web terminal interfaces to facilitate users to perform operations such as garbage disposal and points query. At the same time, it integrates function modules such as camera calling, QR code scanning, and message pushing to provide users with a convenient interaction experience.
- **Technical Selection**: React Native is used for cross-platform development, combined with the Redux state management library, to improve development efficiency and code maintainability.

#### 5.2 AI Processing Subsystem 🤖
- **Responsibilities**: This subsystem is responsible for image preprocessing, feature extraction, and classification model inference. At the same time, it uses community disposal data to continuously iterate the algorithm to continuously improve the recognition accuracy.
- **Technical Selection**: The TensorFlow framework is used for model training and inference, and NVIDIA GPUs are used to accelerate the inference process to improve processing speed. Kubernetes is used to manage training tasks to achieve efficient resource utilization and automated task scheduling.

#### 5.3 Points Management Subsystem 🌟
- **Responsibilities**: This subsystem implements a points calculation, freezing, and deduction rule engine, supporting batch issuance of activity rewards. At the same time, it is responsible for the management of exchange orders, including inventory verification, logistics tracking, and after-sales processing.
- **Data Model**: A points flow table is established to record the type, time, and quantity of points changes; a gift exchange table is established to record gift IDs, points consumption, user addresses, etc., to facilitate reconciliation and auditing.

#### 5.4 Data Statistical Analysis Subsystem 📊
- **Responsibilities**: This subsystem collects user behavior data (such as disposal records and points changes) and device data (such as status and location), performs data cleaning and aggregation analysis, generates visual reports and early warning indicators, and provides decision support for managers.
- **Technical Selection**: Flink is used for real-time stream processing, Hadoop for offline analysis, and ECharts for data visualization to ensure the efficiency of data processing and the visualization effect.

#### 5.5 Backend Management Subsystem 👨💻
- **Responsibilities**: This subsystem provides comprehensive management tools for administrators, including user management, device monitoring, and system configuration. At the same time, it audits operation logs, records operation trajectories and abnormal events, and supports keyword search and export.
- **Permission System**: A three-level permission system of super administrator, regional administrator, and ordinary administrator is adopted to ensure that administrators at different levels can only perform operations within their permissions, and operations are traceable.

### VI. Information Management 📇🗄️💰📝

Effective information management is the key to the successful operation of EcoSorter. The system manages and maintains various types of information in detail.

#### 6.1 User Information 📇
- **Fields**: User information includes user ID, name, mobile phone number, address, points, registration time, account status, etc. This information comprehensively records users' basic situation and participation in garbage classification.
- **Storage**: A distributed database (such as MySQL Cluster) is used to store user information. Sensitive fields are encrypted, and data desensitization is performed regularly to ensure the security and privacy of user information.

#### 6.2 Garbage Classification Information 🗄️
- **Fields**: Garbage classification information records detailed information about each disposal, including record ID, user ID, garbage image, classification result, disposal time, garbage bin number, latitude and longitude, etc.
- **Purpose**: This data is not only used to train AI models to improve recognition accuracy but also to help analyze classification trends in the region, locate high-frequency misclassification points, and provide a basis for optimizing garbage classification work.

#### 6.3 Points Transaction Information 💰
- **Fields**: Points transaction information includes transaction ID, user ID, points change type (acquisition/consumption), quantity, time, associated activity/exchange record, etc.
- **Scenarios**: This information can be used for user reconciliation, operational review (such as activity ROI analysis), abnormal points verification, etc., to ensure the fairness and transparency of points transactions.

#### 6.4 System Logs 📝
- **Fields**: System logs record all operation trajectories, including log ID, operator, module name, operation content, time, IP address, response status, etc.
- **Audit**: It supports filtering logs by time period, module, operation type, etc., and automatically marks and warns abnormal logs to facilitate system monitoring and problem troubleshooting by administrators.

### VII. UML Diagram Explanation 📌🏷️👥📦🔄⏳🏃🔌🌐

#### 7.1 Use Case Diagram 📌
- **Core Role**: The use case diagram defines the boundaries of the system and clearly describes the interaction relationship between roles and functions. Through the use case diagram, you can intuitively understand the functional scope of the system and the responsibilities of each role.
- **Role Division**:
  - **Active Roles**: Including human participants such as residents, system administrators, and garbage collectors, who actively interact with the system to complete their respective tasks.
  - **Passive Roles**: Smart garbage bins as device roles and the emergency notification module as a system component passively receive and process information in the system.
- **Example Use Case Relationships**:
  - **Include Relationship**: The "Dispose Garbage" use case includes basic operation steps such as "Scan QR Code" and "Select Garbage Type", which are essential links in the garbage disposal process.
  - **Extend Relationship**: The "Query Points" use case can be extended to "View Points Details". When the user clicks the "Details" button, the system will display detailed changes in points.
  - **Generalization Relationship**: "System Administrator" is generalized into "Super Administrator" and "Regional Administrator". Administrators at different levels have different permissions and responsibilities.
- **Visualization Value**: The use case diagram is very useful during requirement reviews, helping team members align business processes and ensuring that the core requirements of all roles are covered.

#### 7.2 Class Diagram 🏷️
- **Core Role**: The class diagram describes the static structure of the system, defining entity classes, interfaces, and their relationships. Through the class diagram, you can deeply understand the data flow and logical relationships within the system.
- **Key Class Explanation**:
| Class Name | Attribute Example | Method Example | Relationship |
| --- | --- | --- | --- |
| **User** | id, name, phone, points | queryPoints(), redeemGift() | Aggregation → Points Record |
| **Garbage** | imagePath, type, timestamp | submitForRecognition() | Association → Garbage Bin |
| **BinManager** | binId, location, status | checkStatus(), recordCleaning() | Dependency → Data Storage |
| **AIProcessor** | modelVersion, accuracy | preprocessImage(), classify() | Implementation → ImageRecognition Interface |
- **Relationship Symbols**:
  - **Inheritance** (Resident → System User): Indicated by the `<<extends>>` symbol, reflecting that subclasses inherit the attributes and methods of the parent class.
  - **Association** (User → Points Record): Represented by a solid line with an arrow, indicating an association relationship between two classes.
  - **Dependency** (AI Processor → Database): Represented by a dashed line with an arrow, indicating that the implementation of one class depends on another class.

#### 7.3 Object Diagram Instance 👥
- **Core Role**: The object diagram shows the instantiated state of classes and is used to verify data interaction logic. Through the object diagram, you can intuitively see the relationships and data flow between various objects in the system.
- **Example Scenario**: Taking the record of user "Zhang San" disposing of recyclables as an example:User {
  id: 1001,
  name: "Zhang San",
  points: 200,
  records: [GarbageCollectionRecord#1]
}
GarbageCollectionRecord#1 {
  recordId: "20250528001",
  userId: 1001,
  garbageType: "Recyclables",
  binId: "A01",
  time: "2025-05-28 14:30:00"
}
RecyclingBin#A01 {
  binId: "A01",
  location: "East of Building 1",
  status: "Normal"
}- **Purpose**: During the database design phase, the object diagram can assist in verifying the ER diagram to ensure the correctness of the entity-relationship mapping.

#### 7.4 Package Diagram 📦
- **Core Role**: The package diagram organizes system components in a modular way, clearly showing the dependencies between various components. Through the package diagram, you can understand the overall architecture and module division of the system.
- **Package Structure Design**:Intelligent Garbage Classification System
├─ User Interaction Package (UI)
│  ├─ Mobile APP
│  └─ Web Management Terminal
├─ Business Logic Package (Service)
│  ├─ Garbage Recognition Service
│  ├─ Points Management Service
│  └─ Device Monitoring Service
├─ Data Processing Package (Data)
│  ├─ Database Module (RDS)
│  └─ Cloud Storage Module (OSS)
├─ Infrastructure Package (Infra)
│  ├─ Log Service
│  └─ Notification Service
└─ Third-Party Service Package (3rd Party)
   ├─ AI Model Service (TensorFlow)
   └─ Map Service (Gaode/Baidu)- **Dependency Relationships**:
  - **UI Package** → **Service Package**: Calls business logic through RESTful API (JSON format) to separate the user interface from business logic.
  - **Service Package** → **Data Package**: Reads and writes data to decouple business logic from data storage.
  - **Infra Package** → **Data Package**: Log and notification data are stored in the data processing package to ensure effective management of system logs and notifications.

#### 7.5 Sequence/Collaboration Diagram 🔄
- **Core Role**: Sequence diagrams and collaboration diagrams are used to describe the timing and collaboration relationships of interactions between objects. Sequence diagrams emphasize the time sequence of interactions between objects, while collaboration diagrams focus more on the spatial relationships between objects.
- **Sequence Diagram Example: Garbage Recognition Process**sequenceDiagram
  User->>GarbageRecognitionUI: Trigger recognition (take photo/scan code)
  GarbageRecognitionUI->>GarbageRecognitionService: Send image data
  GarbageRecognitionService->>MachineLearningModel: Call classification model
  MachineLearningModel-->>GarbageRecognitionService: Return type (e.g., "Plastic Bottle")
  GarbageRecognitionService-->>GarbageRecognitionUI: Result notification
  GarbageRecognitionUI->>User: Display classification result and points reward
  User->>RecyclingBin: Dispose of garbage
  RecyclingBin->>DataStorage: Record disposal time and location- **Collaboration Diagram Focus**: Objects are associated through keys such as `userId` and `binId`, emphasizing "spatial relationships" rather than time sequences, which helps to understand the collaboration methods between objects.

#### 7.6 State Diagram ⏳
- **Core Role**: The state diagram describes the state migration process of objects and is suitable for process control scenarios. Through the state diagram, you can clearly understand the behavior and transition conditions of the system in different states.
- **Garbage Recognition State Machine**:[*] --> Idle
Idle --> DataProcessing: TriggerRecognition
DataProcessing --> Recognition: ImageSent
Recognition --> ResultDisplayed: RecognitionSucceeded
ResultDisplayed --> Completed: GarbageThrown
Completed --> DataStorage: DataUploaded
DataStorage --> [*]: End- **Exception Branches**:
  - Recognition --> ErrorHandling: RecognitionFailed
  - ErrorHandling --> Idle: RetryTriggered

#### 7.7 Activity Diagram 🏃
- **Core Role**: The activity diagram shows the steps, decision points, and parallel paths of the business process. Through the activity diagram, you can fully understand the全貌 of the business process, discover potential problems, and optimization points.
- **Main Process Activity Diagram**:graph LR
A[Trigger Recognition] --> B[Process Image]
B --> C[Preprocess Image]
C --> D[Extract Features]
D --> E[Classify Garbage]
E --> F{Recognition Succeeded?}
F -->|Yes| G[Throw Garbage]
F -->|No| H[Error Handling]
G --> I[Data Uploaded]
H --> J[Log Error]
J --> K[Retry Recognition?]
K -->|Yes| A
K -->|No| L[End]
I --> M[Notify User] --> L
#### 7.8 Component Diagram 🔌
- **Core Role**: The component diagram describes the physical components of the system and their dependencies. Through the component diagram, you can understand the composition structure of the system's hardware and software components and their interaction methods.
- **Technical Component Architecture**:+----------------+    +----------------+    + -------------+
|  User Interface|    | Image Processing|    | Machine Learning|
|    (React)     | --> |    Service     | --> |     Model     |
+-------+--------+    +-------+--------+    +------+--------+
        |                        |                    ^
        |                        |                    |
+-------v--------+    +-------v--------+    +------v--------+
| Notification   |    |     Cloud     |    |     Database  |
|    Service     |    |    Storage     |    |    (MySQL)    |
+----------------+    +----------------+    +---------------+- **Interface Definition**:
  - **ImageProcessingAPI**: `POST /api/recognize` (receive image, return classification result)
  - **NotificationAPI**: `POST /api/notify` (send APP notification/SMS)

#### 7.9 Deployment Diagram 🌐
- **Core Role**: The deployment diagram plans the physical deployment of hardware and software, clarifying the network communication path. Through the deployment diagram, you can understand the deployment situation of the system in the actual environment and ensure the stability and reliability of the system.
- **Three-Tier Architecture Design**:
  - **Terminal Layer**:
    - Resident Mobile Phones (iOS/Android APP): Connect to the cloud through 4G/5G networks to interact with the system.
    - Smart Garbage Bins (Hardware Configuration: ARM Processor + Camera + IoT Module): Support Wi-Fi/Bluetooth/4G networking. The edge computing module preprocesses images to reduce data transmission volume.
  - **Cloud Layer**:
    - **Application Server**: Use Alibaba Cloud ECS (deployed in multiple availability zones), run Spring Boot/Node.js microservices, and perform load balancing through Nginx to improve system availability and performance.
    - **Database Server**: Use Alibaba Cloud RDS for PostgreSQL (master-slave replication + read-write separation) to store core business data and ensure data security and reliability.
    - **AI Server**: Alibaba Cloud GPU instance (NVIDIA A100), deploy TensorFlow model service, support batch inference, and improve model processing efficiency.
    - **Message Queue**: Alibaba Cloud RocketMQ, asynchronously process tasks such as points updates and notification sending, reduce peak loads, and improve system concurrency processing capabilities.
  - **Network Layer**:
    - Terminal and Cloud: Use HTTPS (port 443) for encrypted communication. Smart garbage bins use MQTT over TLS (port 8883) to ensure secure data transmission.
    - Internal Services: Use VPC private networks, and services call each other through RPC (such as gRPC) with a latency <10ms to improve communication efficiency between services.

### VIII. Detailed State Diagram Description 🔁📅

#### 8.1 State Transitions 🔁
1. **Idle (Idle State)**
 - **Trigger Condition**: The system enters the idle state after starting up or after the previous recognition process ends.
 - **Waiting Event**: In the idle state, the system waits for trigger events such as users scanning codes, taking photos, or the garbage bin detecting garbage input.
2. **DataProcessing (Data Processing State)**
 - **Execution Action**: After receiving the garbage image (resolution ≥1024x768) uploaded by the smart garbage bin or mobile phone, the system first verifies the legitimacy of the image, including the image format and size. Then it triggers image preprocessing operations such as denoising and normalization to improve the image quality for subsequent recognition processing.
 - **Transition Condition**: When the image preprocessing is completed, the `ImageSent` event is triggered, and the system enters the recognition state.
3. **Recognition (Recognition State)**
 - **Execution Action**: The system calls the AI model service to input the preprocessed image data into the model for recognition. The recognition process usually takes no more than 2 seconds. After the model outputs the classification result, the system parses the model output and verifies the classification confidence (threshold ≥0.7).
 - **Transition Condition**: If the classification confidence meets the standard, the `RecognitionSucceeded` event is triggered, and the system enters the result display state; if the classification confidence does not meet the standard, the `RecognitionFailed` event is triggered, and the system enters the error handling state.
4. **ResultDisplayed (Result Display State)**
 - **Execution Action**: The system displays the recognition result to the user through the mobile APP or garbage bin display screen, including the garbage type and the points that can be obtained. At the same time, the system records the recognition result and user behavior data.
 - **Transition Condition**: When the user throws the garbage into the corresponding garbage bin, the `GarbageThrown` event is triggered, and the system enters the completion state.
5. **Completed (Completion State)**
 - **Execution Action**: The system updates the user's points balance, records the garbage disposal record, and updates the status of the garbage bin. At the same time, the system sends the disposal data to the cloud server for storage and analysis.
 - **Transition Condition**: When the data upload is completed, the `DataUploaded` event is triggered, and the system returns to the idle state.
6. **ErrorHandling (Error Handling State)**
 - **Execution Action**: When the recognition fails or an error occurs, the system records the error information and provides the user with error handling suggestions, such as adjusting the shooting angle or re-uploading the image.
 - **Transition Condition**: When the user triggers the retry operation, the `RetryTriggered` event is triggered, and the system returns to the idle state to restart the recognition process.

#### 8.2 Key Events 📅
- **TriggerRecognition**: This event is triggered when the user initiates a garbage recognition request through the mobile APP or garbage bin, indicating the start of the garbage recognition process.
- **ImageSent**: This event is triggered when the system completes image preprocessing and sends the image data to the AI model service, indicating that the image has been successfully sent for recognition.
- **RecognitionSucceeded**: This event is triggered when the AI model successfully recognizes the garbage type and the confidence meets the standard, indicating that the recognition process has been successfully completed.
- **RecognitionFailed**: This event is triggered when the AI model fails to recognize the garbage type or the confidence does not meet the standard, indicating that the recognition process has failed.
- **GarbageThrown**: This event is triggered when the system detects that the user has thrown the garbage into the corresponding garbage bin, indicating that the garbage disposal action has been completed.
- **DataUploaded**: This event is triggered when the system completes uploading all data to the cloud server, indicating that the entire garbage classification process has been successfully completed.
- **RetryTriggered**: This event is triggered when the user initiates a retry operation after a recognition failure, indicating that the system will restart the recognition process.

### IX. Detailed Activity Diagram Description ✅⚖️

#### 9.1 Main Process ✅
1. **Trigger Recognition**: The process starts when the user triggers a garbage recognition request through the mobile APP or garbage bin.
2. **Process Image**: The system receives the garbage image uploaded by the user and starts processing the image.
3. **Preprocess Image**: The system performs preprocessing operations on the image, such as denoising, normalization, and scaling, to improve the quality of the image.
4. **Extract Features**: The system extracts features from the preprocessed image for subsequent classification.
5. **Classify Garbage**: The system inputs the extracted features into the AI classification model to determine the type of garbage.
6. **Check Recognition Result**: The system checks the output result of the model and verifies whether the classification confidence meets the threshold (≥0.7).
7. **Throw Garbage**: If the recognition is successful, the user is guided to throw the garbage into the corresponding garbage bin.
8. **Record Disposal**: The system records the disposal behavior, including the garbage type, time, location, and user information.
9. **Upload Data**: The system uploads the disposal data to the cloud server for storage and analysis.
10. **Notify User**: The system notifies the user of the disposal result and updates the user's points balance.

#### 9.2 Decision Points ⚖️
1. **Recognition Succeeded?**: This decision point checks whether the AI model's recognition confidence meets the threshold (≥0.7). If it meets the threshold, the process proceeds to "Throw Garbage"; otherwise, it proceeds to "Error Handling".
2. **Retry Recognition?**: This decision point appears after an error occurs. If the user chooses to retry, the process returns to "Trigger Recognition"; otherwise, the process ends.

### X. Detailed Collaboration Diagram Description 👥🗨️

#### 10.1 Object Interaction 👥🗨️
1. **User** → **GarbageRecognitionUI**: The user triggers a garbage recognition request through the mobile APP or garbage bin interface.
2. **GarbageRecognitionUI** → **GarbageRecognitionService**: The interface sends the image data to the garbage recognition service for processing.
3. **GarbageRecognitionService** → **MachineLearningModel**: The service calls the AI model service to perform garbage classification.
4. **MachineLearningModel** → **GarbageRecognitionService**: The AI model returns the classification result to the service.
5. **GarbageRecognitionService** → **GarbageRecognitionUI**: The service sends the recognition result to the interface for display.
6. **User** → **RecyclingBin**: The user throws the garbage into the corresponding garbage bin based on the recognition result.
7. **RecyclingBin** → **DataStorage**: The garbage bin records the disposal time and location and sends the data to the data storage service.
8. **DataStorage** → **PointsManagementService**: The data storage service notifies the points management service to update the user's points.
9. **PointsManagementService** → **User**: The points management service sends a points update notification to the user.

### XI. Detailed Sequence Diagram Description ⏱️

#### 11.1 Timing Process ⏱️
1. **0-1s**: The user triggers a garbage recognition request through the mobile APP or garbage bin interface.
2. **1-2s**: The interface captures or receives the image and sends it to the backend service.
3. **2-4s**: The backend service preprocesses the image and calls the AI model for recognition.
4. **4-5s**: The AI model returns the classification result to the backend service.
5. **5-6s**: The backend service processes the result and sends it to the interface for display.
6. **6-10s**: The user views the result and throws the garbage into the corresponding garbage bin.
7. **10-12s**: The garbage bin detects the garbage input and records the disposal information.
8. **12-15s**: The garbage bin uploads the disposal data to the cloud server.
9. **15-17s**: The cloud server processes the data and updates the user's points.
10. **17-18s**: The user receives a notification of points update through the APP.

### XII. Detailed Package Diagram Description 📦→📦

#### 12.1 Module Dependencies 📦→📦
1. **User Interaction Package (UI)**: Depends on the Business Logic Package (Service) to obtain business data and perform operations.
2. **Business Logic Package (Service)**: Depends on the Data Processing Package (Data) to read and write data and depends on the Third-Party Service Package (3rd Party) to call external services.
3. **Data Processing Package (Data)**: Depends on the Infrastructure Package (Infra) to use basic services such as logs and notifications.
4. **Infrastructure Package (Infra)**: As a basic service package, it does not depend on other packages.
5. **Third-Party Service Package (3rd Party)**: As an external service package, it does not depend on internal packages.

### XIII. Another Detailed Collaboration Diagram Description 🔗

#### 13.1 Data Association 🔗
1. **User** ↔ **PointsRecord**: A user can have multiple points records, and each points record belongs to a specific user.
2. **Garbage** ↔ **GarbageBin**: Garbage is disposed of in a specific garbage bin, and each garbage bin records multiple garbage disposal events.
3. **GarbageBin** ↔ **BinManager**: A garbage bin manager is responsible for managing multiple garbage bins, and each garbage bin is managed by a specific manager.
4. **AIProcessor** ↔ **Garbage**: The AI processor processes garbage images to determine the type of garbage.
5. **DataStorage** ↔ **All Entities**: The data storage component is responsible for storing data related to all entities.

### XIV. Detailed Use Case Diagram Description 🧩

#### 14.1 Role-Function Matrix 🧩
| Role | Dispose Garbage | Query Points | Manage Users | Configure System | Maintain Bins | Receive Alerts |
| --- | --- | --- | --- | --- | --- | --- |
| Resident | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| System Administrator | ❌ | ❌ | ✅ | ✅ | ❌ | ✅ |
| Garbage Collector | ❌ | ❌ | ❌ | ❌ | ✅ | ❌ |
| Smart Garbage Bin | ❌ | ❌ | ❌ | ❌ | ❌ | ✅ |
| Emergency Notification Module | ❌ | ❌ | ❌ | ❌ | ❌ | ✅ |
