package com.backend.sge.resource;

import com.backend.sge.exception.BadRequestException;
import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.model.ProductEntry;
import com.backend.sge.model.Stock;
import com.backend.sge.repository.ProductEntryRepository;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.repository.StockRepository;
import com.backend.sge.validation.ProductEntryValidation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductEntryResource {

    @Autowired
    private ProductEntryRepository productEntryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @ApiOperation(value = "Cadastrar entrada de produtos")
    @RequestMapping(value = "/productEntry", method = RequestMethod.POST)
    public ResponseEntity<ProductEntry> createProductEntry(@Valid @RequestBody ProductEntryValidation productEntryValidation) throws NotFoundException, BadRequestException {

        Product product = productRepository.findByStatusIsTrueAndId(productEntryValidation.getIdProduct()).orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: $id"));
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productEntryValidation.getIdProduct());

        if (sumStockByIdProduct != null) {
            Integer sumStockQtdProductEntryQtd = (sumStockByIdProduct + productEntryValidation.getQtd());
            if (product.getMaxStock() >= sumStockQtdProductEntryQtd) {

                ProductEntry productEntry = new ProductEntry();
                productEntry.setIdProduct(product.getId());
                productEntry.setQtd(productEntryValidation.getQtd());
                productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

                Stock stock = new Stock();
                stock.setIdProduct(product.getId());
                stock.setQtd(sumStockQtdProductEntryQtd);
                stock.setUnitaryValue(productEntryValidation.getUnitaryValue());
                stockRepository.save(stock);

                ProductEntry responseProductEntry = productEntryRepository.save(productEntry);
                return new ResponseEntity<ProductEntry>(responseProductEntry, HttpStatus.CREATED);

            } else {
                throw new BadRequestException("Quantidade máxima de produto excedida.");
            }
        } else {

            ProductEntry productEntry = new ProductEntry();
            productEntry.setIdProduct(product.getId());
            productEntry.setQtd(productEntryValidation.getQtd());
            productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

            Stock stock = new Stock();
            stock.setIdProduct(product.getId());
            stock.setQtd(productEntryValidation.getQtd());
            stock.setUnitaryValue(productEntryValidation.getUnitaryValue());
            stockRepository.save(stock);

            ProductEntry responseProductEntry = productEntryRepository.save(productEntry);
            return new ResponseEntity<ProductEntry>(responseProductEntry, HttpStatus.CREATED);
        }

    }

    @ApiOperation(value = "Atualizar entrada de produtos")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductEntry> updateProductEntry(@PathVariable(value = "id") long id,
                                                           @Valid @RequestBody ProductEntryValidation productEntryValidation) throws NotFoundException {
        ProductEntry productEntry = productEntryRepository.findById(id).orElseThrow(() -> new NotFoundException("id :: $id da Entrada de Produto não encontrado."));
        Product product = productRepository.findById(productEntryValidation.getIdProduct()).orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: $id"));
        productEntry.setIdProduct(product.getId());
        productEntry.setIdProduct(product.getId());
        productEntry.setQtd(productEntryValidation.getQtd());
        productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

        ProductEntry responseProductEntry = productEntryRepository.save(productEntry);
        return new ResponseEntity<ProductEntry>(responseProductEntry, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar entrada de produtos")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductEntry> deleteProductEntry(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductEntry productEntry = productEntryRepository.findById(id).orElseThrow(() -> new NotFoundException("id :: $id da Entrada de Produto não encontrado."));
        productEntryRepository.delete(productEntry);
        return new ResponseEntity<ProductEntry>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar entrada de produtos pelo id")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductEntry> getProductEntryById(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductEntry productEntry = productEntryRepository.findById(id).orElseThrow(() -> new NotFoundException("id :: $id da Entrada de Produto não encontrado."));
        return new ResponseEntity<ProductEntry>(productEntry, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar entrada de produtos")
    @RequestMapping(value = "/productEntry", method = RequestMethod.GET)
    public Page<ProductEntry> getAllCategories(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productEntryRepository.findAll(PageRequest.of(offset, limit));
    }

}
