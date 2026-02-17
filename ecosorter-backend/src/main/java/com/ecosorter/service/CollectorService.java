package com.ecosorter.service;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.CreateOrderRequest;
import com.ecosorter.dto.OrderResponse;
import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.dto.UserStatisticsResponse;
import com.ecosorter.enums.TaskStatus;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.CollectionTaskRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import com.ecosorter.repository.TaskExceptionRepository;
import com.ecosorter.enums.OrderStatus;
import com.ecosorter.model.TaskException;
import com.ecosorter.enums.TaskPriority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectorService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final CollectionTaskRepository collectionTaskRepository;
    private final UserRepository userRepository;
    private final PointRecordRepository pointRecordRepository;
    private final OrderRepository orderRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final TaskExceptionRepository taskExceptionRepository;
    private final OrderService orderService;
    
    public CollectorService(TrashcanDataRepository trashcanDataRepository, 
                           CollectionTaskRepository collectionTaskRepository,
                           UserRepository userRepository,
                           PointRecordRepository pointRecordRepository,
                           OrderRepository orderRepository,
                           UserStatisticsRepository userStatisticsRepository,
                           TaskExceptionRepository taskExceptionRepository,
                           OrderService orderService) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.collectionTaskRepository = collectionTaskRepository;
        this.userRepository = userRepository;
        this.pointRecordRepository = pointRecordRepository;
        this.orderRepository = orderRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.taskExceptionRepository = taskExceptionRepository;
        this.orderService = orderService;
    }
    
    public CollectorDashboardResponse getDashboard(Long userId) {
        CollectorDashboardResponse response = new CollectorDashboardResponse();
        
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.PENDING.getValue());
        List<com.ecosorter.model.CollectionTask> inProgressTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.IN_PROGRESS.getValue());
        List<com.ecosorter.model.CollectionTask> completedTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.COMPLETED.getValue());
        
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);
        
        long todayTasksCount = tasks.stream()
                .filter(task -> task.getCreatedAt() != null && 
                        !task.getCreatedAt().isBefore(todayStart) && 
                        task.getCreatedAt().isBefore(todayEnd))
                .count();
        
        response.setTodayTasks((int) todayTasksCount);
        response.setCompletedTasks(completedTasks.size());
        response.setInProgressTasks(inProgressTasks.size());
        response.setAbnormalDevices(trashcanDataRepository.findFullTrashcans().size());
        
        return response;
    }
    
    public List<CollectorTaskResponse> getTasks(Long userId, Integer page, Integer pageSize) {
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorId(userId);
        
        if (page != null && pageSize != null && page > 0 && pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            if (startIndex < tasks.size()) {
                int endIndex = Math.min(startIndex + pageSize, tasks.size());
                tasks = tasks.subList(startIndex, endIndex);
            } else {
                tasks = java.util.Collections.emptyList();
            }
        }
        
        return tasks.stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }
    
    public CollectorTaskResponse getTaskDetail(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public CollectorTaskResponse startTask(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        if (task.getStatus() != TaskStatus.PENDING) {
            throw new RuntimeException("Task is not in pending status");
        }
        
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public CollectorTaskResponse completeTask(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new RuntimeException("Task is not in progress status");
        }
        
        task.setStatus(TaskStatus.COMPLETED);
        task.setEndTime(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public void reportException(String taskId, Long userId, String description) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        TaskException exception = new TaskException();
        exception.setTaskId(task.getId());
        exception.setReporterId(userId);
        exception.setExceptionType(TaskException.ExceptionType.fromValue(TaskPriority.MEDIUM.name()));
        exception.setDescription(description);
        exception.setStatus(TaskStatus.PENDING);
        exception.setCreatedAt(LocalDateTime.now());
        exception.setUpdatedAt(LocalDateTime.now());
        taskExceptionRepository.save(exception);
        
        task.setStatus(TaskStatus.PENDING);
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
    }
    
    public List<DeviceListResponse> getDevices() {
        List<com.ecosorter.model.TrashcanData> trashcans = trashcanDataRepository.findAll();
        
        return trashcans.stream()
                .map(this::convertToDeviceListResponse)
                .collect(Collectors.toList());
    }
    
    private CollectorTaskResponse convertToTaskResponse(com.ecosorter.model.CollectionTask task) {
        CollectorTaskResponse collectorTask = new CollectorTaskResponse();
        collectorTask.setTaskId(task.getTaskId());
        
        if (task.getTrashcanId() != null) {
            TrashcanData trashcan = trashcanDataRepository.selectById(task.getTrashcanId());
            if (trashcan != null) {
                collectorTask.setLocation(trashcan.getLocation());
                collectorTask.setDeviceId(trashcan.getDeviceId());
            }
        }
        
        if (task.getCollectorId() != null) {
            User collector = userRepository.selectById(task.getCollectorId());
            if (collector != null) {
                collectorTask.setCollectorName(collector.getUsername());
            }
        }
        
        collectorTask.setGarbageType(task.getGarbageType());
        collectorTask.setEstimatedWeight(task.getEstimatedWeight() != null ? task.getEstimatedWeight().doubleValue() : 0.0);
        collectorTask.setPriority(task.getPriority().name().toLowerCase());
        collectorTask.setStatus(task.getStatus().name().toLowerCase());
        collectorTask.setCreatedAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : "");
        collectorTask.setCompletedAt(task.getEndTime() != null ? task.getEndTime().toString() : "");
        return collectorTask;
    }
    
    private DeviceListResponse convertToDeviceListResponse(com.ecosorter.model.TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? trashcan.getThreshold().intValue() : 0);
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? trashcan.getUpdatedAt().toString() : "");
        return device;
    }
    
    private String getStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "正常";
            case "offline": return "异常";
            case "maintenance": return "维护中";
            case "error": return "异常";
            default: return "未知";
        }
    }
    
    public UserStatisticsResponse getStatistics(Long userId) {
        UserStatisticsResponse response = new UserStatisticsResponse();
        
        com.ecosorter.model.UserStatistics statistics = userStatisticsRepository.findByUserId(userId).orElse(null);
        if (statistics != null) {
            response.setTotalPoints(statistics.getTotalPoints() != null ? statistics.getTotalPoints().longValue() : 0L);
            response.setTotalCount(statistics.getTotalClassifications() != null ? statistics.getTotalClassifications().longValue() : 0L);
            response.setCorrectClassifications(statistics.getCorrectClassifications() != null ? statistics.getCorrectClassifications() : 0);
        } else {
            response.setTotalPoints(0L);
            response.setTotalCount(0L);
            response.setCorrectClassifications(0);
        }
        
        return response;
    }
    
    public List<PointRecordResponse> getPointRecords(Long userId, String type, String startDate, String endDate, Integer page, Integer pageSize) {
        List<com.ecosorter.model.PointRecord> records;
        
        if (type != null && !type.isEmpty()) {
            records = pointRecordRepository.findByUserIdAndType(userId, type);
        } else {
            records = pointRecordRepository.findByUserId(userId);
        }
        
        if (startDate != null && !startDate.isEmpty()) {
            java.time.LocalDateTime startDateTime = java.time.LocalDateTime.parse(startDate + "T00:00:00");
            records = records.stream()
                    .filter(record -> record.getCreatedAt() != null && !record.getCreatedAt().isBefore(startDateTime))
                    .collect(Collectors.toList());
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            java.time.LocalDateTime endDateTime = java.time.LocalDateTime.parse(endDate + "T23:59:59");
            records = records.stream()
                    .filter(record -> record.getCreatedAt() != null && !record.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
        }
        
        if (page != null && pageSize != null && page > 0 && pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            if (startIndex < records.size()) {
                int endIndex = Math.min(startIndex + pageSize, records.size());
                records = records.subList(startIndex, endIndex);
            } else {
                records = java.util.Collections.emptyList();
            }
        }
        
        return records.stream()
                .map(this::convertToPointRecordResponse)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getOrders(Long userId, String status, String startDate, String endDate, Integer page, Integer pageSize) {
        List<com.ecosorter.model.Order> orders = orderRepository.findByUserId(userId);
        
        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> status.equals(order.getStatus().name()))
                    .collect(Collectors.toList());
        }
        
        if (startDate != null && !startDate.isEmpty()) {
            java.time.LocalDateTime startDateTime = java.time.LocalDateTime.parse(startDate + "T00:00:00");
            orders = orders.stream()
                    .filter(order -> order.getCreatedAt() != null && !order.getCreatedAt().isBefore(startDateTime))
                    .collect(Collectors.toList());
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            java.time.LocalDateTime endDateTime = java.time.LocalDateTime.parse(endDate + "T23:59:59");
            orders = orders.stream()
                    .filter(order -> order.getCreatedAt() != null && !order.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
        }
        
        if (page != null && pageSize != null && page > 0 && pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            if (startIndex < orders.size()) {
                int endIndex = Math.min(startIndex + pageSize, orders.size());
                orders = orders.subList(startIndex, endIndex);
            } else {
                orders = java.util.Collections.emptyList();
            }
        }
        
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse getOrderDetail(Long orderId, Long userId) {
        com.ecosorter.model.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        if (!order.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Order not found");
        }
        
        return convertToOrderResponse(order);
    }
    
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        com.ecosorter.model.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        if (!order.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Order not found");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        com.ecosorter.model.Order order = new com.ecosorter.model.Order();
        order.setUserId(userId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setContactName(request.getContactName());
        order.setContactPhone(request.getContactPhone());
        order.setShippingAddress(request.getShippingAddress());
        
        return orderService.createOrder(userId, order);
    }
    
    private PointRecordResponse convertToPointRecordResponse(com.ecosorter.model.PointRecord record) {
        PointRecordResponse response = new PointRecordResponse();
        response.setId(record.getId());
        response.setPoints(record.getPoints() != null ? record.getPoints() : 0);
        response.setType(record.getType() != null ? record.getType().name() : "");
        response.setDescription(record.getDescription());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
    
    private OrderResponse convertToOrderResponse(com.ecosorter.model.Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity() != null ? order.getQuantity() : 0);
        response.setTotalPoints(order.getTotalPoints() != null ? order.getTotalPoints() : 0);
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : "");
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}
