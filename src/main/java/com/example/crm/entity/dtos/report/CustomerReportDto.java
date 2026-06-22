package com.example.crm.entity.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReportDto {
    private Long totalCount;
    private List<CustomerSummary> customers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerSummary {
        private UUID id;
        private String name;
        private String mobileNumber;
        private LocalDate lastVisitDate;
    }
}