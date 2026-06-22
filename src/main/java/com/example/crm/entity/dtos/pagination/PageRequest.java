package com.example.crm.entity.dtos.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    // Pagination (optional)
    private Integer pageNo;    // 0-based index
    private Integer pageSize;

    // Sorting (optional)
    private String sortBy;     // field name
    private String sortDir;    // ASC / DESC
}

