package com.backend.sge.repository;

import com.backend.sge.model.ProductEntry;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductEntryRepository extends PagingAndSortingRepository<ProductEntry, Long> {
}
