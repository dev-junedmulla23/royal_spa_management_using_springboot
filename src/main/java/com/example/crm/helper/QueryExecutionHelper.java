package com.example.crm.helper;



import com.example.crm.entity.dtos.pagination.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class QueryExecutionHelper {

    private final PageableResolverUtil pageableResolverUtil;

    // Fetch data with pagination (page info MUST be present).
    // Returns Page<T>
    public <T> Page<T> fetchPage(
            PageRequest request,
            Function<Pageable, Page<T>> pagedCall
    ) {
        if (!pageableResolverUtil.hasPage(request)) {
            throw new IllegalStateException("Pagination info not provided");
        }

        Pageable pageable = pageableResolverUtil.resolve(request);
        return pagedCall.apply(pageable);
    }

    //Fetch data without pagination.
    //If sort is provided → sorted List
    //If sort is not provided → full List/
    public <T> List<T> fetchList(
            PageRequest request,
            Function<Sort, List<T>> sortedCall,
            Supplier<List<T>> normalCall
    ) {
        Sort sort = pageableResolverUtil.buildSort(request);

        if (sort.isSorted()) {
            return sortedCall.apply(sort);
        }

        return normalCall.get();
    }
}
