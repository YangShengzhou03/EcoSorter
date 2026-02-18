package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.BookingRequest;
import com.ecosorter.dto.BookingResponse;
import com.ecosorter.enums.BookingStatus;
import com.ecosorter.enums.BookingType;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Booking;
import com.ecosorter.model.User;
import com.ecosorter.repository.BookingRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.util.PaginationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }
    
    public IPage<BookingResponse> getUserBookings(Long userId, int page, int pageSize) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Page<Booking> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<Booking>()
                .eq(Booking::getUserId, userId)
                .orderByDesc(Booking::getCreatedAt);

        IPage<Booking> bookingPage = bookingRepository.selectPage(mpPage, wrapper);
        return toResponsePage(bookingPage);
    }
    
    public IPage<BookingResponse> getAllBookings(int page, int pageSize, String status) {
        Page<Booking> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<Booking>()
                .orderByDesc(Booking::getCreatedAt);
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Booking::getStatus, status.trim());
        }
        IPage<Booking> bookingPage = bookingRepository.selectPage(mpPage, wrapper);
        return toResponsePage(bookingPage);
    }
    
    public BookingResponse getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.selectById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found");
        }
        if (!booking.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Booking not found");
        }
        return convertToResponse(booking);
    }
    
    @Transactional
    public BookingResponse createBooking(Long userId, BookingRequest request) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new BadRequestException("Type is required");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Description is required");
        }
        if (request.getEstimatedWeight() == null || request.getEstimatedWeight() <= 0) {
            throw new BadRequestException("Estimated weight must be positive");
        }
        if (request.getAppointmentDate() == null || request.getAppointmentDate().trim().isEmpty()) {
            throw new BadRequestException("Appointment date is required");
        }
        if (request.getTimeSlot() == null || request.getTimeSlot().trim().isEmpty()) {
            throw new BadRequestException("Time slot is required");
        }
        if (request.getContactName() == null || request.getContactName().trim().isEmpty()) {
            throw new BadRequestException("Contact name is required");
        }
        if (request.getContactPhone() == null || request.getContactPhone().trim().isEmpty()) {
            throw new BadRequestException("Contact phone is required");
        }
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Address is required");
        }

        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setType(BookingType.valueOf(request.getType().toUpperCase()));
        booking.setDescription(request.getDescription());
        booking.setEstimatedWeight(request.getEstimatedWeight());
        booking.setAppointmentDate(request.getAppointmentDate());
        booking.setTimeSlot(request.getTimeSlot());
        booking.setContactName(request.getContactName());
        booking.setContactPhone(request.getContactPhone());
        booking.setAddress(request.getAddress());
        booking.setRemark(request.getRemark());
        booking.setStatus(BookingStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        bookingRepository.insert(booking);

        return convertToResponse(booking);
    }
    
    @Transactional
    public BookingResponse cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.selectById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found");
        }
        
        if (!booking.getUserId().equals(userId)) {
            throw new BadRequestException("You can only cancel your own bookings");
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be cancelled");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.updateById(booking);
        return convertToResponse(booking);
    }

    private IPage<BookingResponse> toResponsePage(IPage<Booking> bookingPage) {
        Page<BookingResponse> responsePage = new Page<>(bookingPage.getCurrent(), bookingPage.getSize(), bookingPage.getTotal());
        responsePage.setRecords(bookingPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
        return responsePage;
    }

    private BookingResponse convertToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUserId());
        response.setType(booking.getType().name().toLowerCase());
        response.setDescription(booking.getDescription());
        response.setEstimatedWeight(booking.getEstimatedWeight());
        response.setAppointmentDate(booking.getAppointmentDate());
        response.setTimeSlot(booking.getTimeSlot());
        response.setContactName(booking.getContactName());
        response.setContactPhone(booking.getContactPhone());
        response.setAddress(booking.getAddress());
        response.setRemark(booking.getRemark());
        response.setStatus(booking.getStatus().name().toLowerCase());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
}
