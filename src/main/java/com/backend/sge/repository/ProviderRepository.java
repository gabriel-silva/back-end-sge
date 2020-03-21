package com.backend.sge.repository;

import com.backend.sge.model.Provider;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProviderRepository extends PagingAndSortingRepository<Provider, Long> {
}
