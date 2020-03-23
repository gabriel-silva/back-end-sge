package com.backend.sge.repository;

import com.backend.sge.model.MeasurementUnit;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MeasurementUnitRepository extends PagingAndSortingRepository<MeasurementUnit, Long> {
}
