package com.example.crm.mappers;

import com.example.crm.entity.Billing;
import com.example.crm.entity.dtos.billing.BillingRequest;
import com.example.crm.entity.dtos.billing.BillingResponse;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {


    public Billing toEntity(BillingRequest request) {

        Billing billing = new Billing();

        billing.setDiscountAmount(
                request.getDiscountAmount() != null ? request.getDiscountAmount() : null
        );

        billing.setPaidAmount(request.getPaidAmount());
        billing.setPaymentMethod(request.getPaymentMethod());

        return billing;
    }

    public BillingResponse toResponse(Billing billing) {

        BillingResponse response = new BillingResponse();

        response.setId(billing.getId());
        response.setInvoiceNumber(billing.getInvoiceNumber());

        if (billing.getCustomer() != null) {
            response.setCustomerName(billing.getCustomer().getName());
        }


        if (billing.getAppointment() != null) {

            // Spa Service
            if (billing.getAppointment().getSpaService() != null) {
                response.setServiceName(
                        billing.getAppointment().getSpaService().getName()
                );
            }

            // Service Package
            if (billing.getAppointment().getSelectedPackage() != null) {
                response.setPackageName(
                        billing.getAppointment().getSelectedPackage().getPackageName()
                );
            }
        }


        response.setTotalAmount(billing.getTotalAmount());
        response.setDiscountAmount(billing.getDiscountAmount());
        response.setPaidAmount(billing.getPaidAmount());

        response.setPaymentStatus(billing.getPaymentStatus());
        response.setPaymentMethod(billing.getPaymentMethod());

        response.setBillingDate(billing.getBillingDate());

        return response;
    }
}