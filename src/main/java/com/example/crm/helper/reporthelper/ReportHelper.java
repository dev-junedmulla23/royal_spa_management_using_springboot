package com.example.crm.helper.reporthelper;

import com.example.crm.entity.Customer;
import com.example.crm.entity.dtos.report.CustomerReportDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportHelper {

    public CustomerReportDto buildCustomerReport(List<Customer> customers) {

        List<CustomerReportDto.CustomerSummary> summaries = customers.stream()
                .map(c -> CustomerReportDto.CustomerSummary.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .mobileNumber(c.getMobileNumber())
                        .lastVisitDate(c.getLastVisitDate())
                        .build())
                .collect(Collectors.toList());

        return CustomerReportDto.builder()
                .totalCount((long) customers.size())
                .customers(summaries)
                .build();
    }
}
