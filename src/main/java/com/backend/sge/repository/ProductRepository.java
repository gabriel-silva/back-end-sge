package com.backend.sge.repository;

import com.backend.sge.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    public Optional<Product> findByStatusIsTrueAndId(Long id);
}
