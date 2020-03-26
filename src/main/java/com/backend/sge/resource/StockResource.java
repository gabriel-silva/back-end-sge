package com.backend.sge.resource;

import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Stock;
import com.backend.sge.repository.StockRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class StockResource {

    @Autowired
    private StockRepository stockRepository;

    @ApiOperation(value = "Listar estoque pelo id")
    @RequestMapping(value = "/stock/{id}", method = RequestMethod.GET)
    public ResponseEntity<Stock> getStockById(@PathVariable(name = "id") long id) throws NotFoundException {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado com o id :: " + id));
        return new ResponseEntity<Stock>(stock, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar estoques")
    @RequestMapping(value = "/stock", method = RequestMethod.GET)
    public Page<Stock> getAllStock(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return stockRepository.findAll(PageRequest.of(offset, limit));
    }

}
