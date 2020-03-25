package com.backend.sge.repository;

import com.backend.sge.model.ProductInput;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductEntryRepository extends PagingAndSortingRepository<ProductInput, Long> {
}
