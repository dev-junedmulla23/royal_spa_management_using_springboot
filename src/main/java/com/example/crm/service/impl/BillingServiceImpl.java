package com.example.crm.service.impl;

import com.example.crm.entity.Appointment;
import com.example.crm.entity.Billing;
import com.example.crm.entity.Customer;
import com.example.crm.entity.dtos.billing.BillingRequest;
import com.example.crm.entity.dtos.billing.BillingResponse;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.BillingMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.AppointmentRepository;
import com.example.crm.repository.BillingRepository;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.service.BillingService;
import com.example.crm.util.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final CustomerRepository customerRepository;
    private final BillingRepository billingRepository;
    private final AppointmentRepository appointmentRepository;
    private BillingMapper billingMapper;

    @Override
    public ApiResponse<?> createBilling(BillingRequest request) {


        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));


        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + request.getAppointmentId()));

        BigDecimal baseAmount = BigDecimal.ZERO;

        if (appointment.getSelectedPackage() != null) {
            baseAmount = appointment.getSelectedPackage().getPrice();
        } else if (appointment.getSpaService() != null) {
            baseAmount = appointment.getSpaService().getPrice();
        }

        BigDecimal discount = request.getDiscountAmount() != null
                ? request.getDiscountAmount()
                : BigDecimal.ZERO;

        BigDecimal finalAmount = baseAmount.subtract(discount);


        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }


        BigDecimal paidAmount = request.getPaidAmount();

        PaymentStatus status;

        if (paidAmount.compareTo(finalAmount) >= 0) {
            status = PaymentStatus.PAID;
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            status = PaymentStatus.PARTIAL;
        } else {
            status = PaymentStatus.PENDING;
        }


        String invoiceNumber = "INV-" + System.currentTimeMillis();

        Billing billing = billingMapper.toEntity(request);

        billing.setCustomer(customer);
        billing.setAppointment(appointment);
        billing.setBillingDate(LocalDate.now());
        billing.setInvoiceNumber(invoiceNumber);
        billing.setTotalAmount(finalAmount);
        billing.setDiscountAmount(discount);
        billing.setPaymentStatus(status);


        Billing saved = billingRepository.save(billing);

        return ApiResponseUtil.created(billingMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getAllBillings() {
        return ApiResponseUtil.fetchedList(billingRepository.findAll().stream().map(billingMapper::toResponse).toList());
    }

    @Override
    public ApiResponse<?> getBillingById(Long id) {

        Billing billing=billingRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("billing not found found with "+id));

        return ApiResponseUtil.fetched(billingMapper.toResponse(billing));
    }

    @Override
    public ApiResponse<?> getBillingsByCustomerId(UUID customerId) {

        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer not found with "+customerId));

        List<BillingResponse> list=billingRepository.findByCustomer_Id(customerId).stream().map(billingMapper::toResponse).toList();

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> updatePayment(Long id, BigDecimal paidAmount) {

        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found with id " + id));

        // Update paid amount
        billing.setPaidAmount(paidAmount);

        // Calculate final amount
        BigDecimal finalAmount = billing.getTotalAmount()
                .subtract(billing.getDiscountAmount() != null ? billing.getDiscountAmount() : BigDecimal.ZERO);

        // Set payment status
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            billing.setPaymentStatus(PaymentStatus.PENDING);
        } else if (paidAmount.compareTo(finalAmount) < 0) {
            billing.setPaymentStatus(PaymentStatus.PARTIAL);
        } else {
            billing.setPaymentStatus(PaymentStatus.PAID);
        }

        Billing updated = billingRepository.save(billing);

        return ApiResponseUtil.updated(billingMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<?> getTotalRevenue() {

        BigDecimal totalRevenue = billingRepository.getTotalRevenue();
        Long totalBills = billingRepository.countAllBills();

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        response.put("totalBills", totalBills);

        return ApiResponseUtil.fetched(response);
    }

    @Override
    public ApiResponse<?> getTodayRevenue() {

        LocalDate today = LocalDate.now();

        BigDecimal totalRevenue = billingRepository.getTodayRevenue(today);
        Long totalBills = billingRepository.countTodayBills(today);

        Map<String, Object> response = new HashMap<>();
        response.put("date", today);
        response.put("totalRevenue", totalRevenue);
        response.put("totalBills", totalBills);

        return ApiResponseUtil.fetched(response);
    }
}
