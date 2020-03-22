package com.backend.sge.repository;

import com.backend.sge.model.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {
    @Query(value = "SELECT SUM(stock.qtd) FROM tb_stock stock WHERE stock.id_product = ?1", nativeQuery = true)
    public Integer sumStockByIdProduct(Long idProduct);
}