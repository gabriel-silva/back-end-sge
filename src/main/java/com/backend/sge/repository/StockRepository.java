package com.backend.sge.repository;

import com.backend.sge.model.Stock;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {
}
