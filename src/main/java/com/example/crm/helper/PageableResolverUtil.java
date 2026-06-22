package com.example.crm.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageableResolverUtil {

    //Returns Pageable if pagination info is present,
    //otherwise returns null (means NO pagination).
    public Pageable resolve(com.example.crm.entity.dtos.pagination.PageRequest pageRequest) {

        if (pageRequest == null
                || pageRequest.getPageNo() == null
                || pageRequest.getPageSize() == null) {
            return null;
        }

        //convert 1-based → 0-based
        int pageIndex = pageRequest.getPageNo() - 1;

        // safety: negative page not allowed
        if (pageIndex < 0) {
            pageIndex = 0;
        }

        return PageRequest.of(
                pageIndex,
                pageRequest.getPageSize(),
                buildSort(pageRequest)
        );
    }

    //Builds Sort object if sort info is present,
    //otherwise returns Sort.unsorted().
    public Sort buildSort(com.example.crm.entity.dtos.pagination.PageRequest pageRequest) {

        if (pageRequest == null
                || pageRequest.getSortBy() == null
                || pageRequest.getSortBy().isBlank()) {
            return Sort.unsorted();
        }

        return "DESC".equalsIgnoreCase(pageRequest.getSortDir())
                ? Sort.by(pageRequest.getSortBy()).descending()
                : Sort.by(pageRequest.getSortBy()).ascending();
    }

    //Utility method to check pagination availability.
    //(Optional but necessary)
    public boolean hasPage(com.example.crm.entity.dtos.pagination.PageRequest pageRequest) {
        return pageRequest != null
                && pageRequest.getPageNo() != null
                && pageRequest.getPageSize() != null;
    }
}
