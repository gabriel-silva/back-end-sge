package com.backend.sge.resource;

import com.backend.sge.exception.BadRequestException;
import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.model.ProductInput;
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
    public ResponseEntity<ProductInput> createProductEntry(@Valid @RequestBody ProductEntryValidation productEntryValidation) throws NotFoundException, BadRequestException {

        //busca produto pelo id passado no productEntryValidation
        Product product = productRepository.findByStatusIsTrueAndId(productEntryValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productEntryValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productEntryValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {
            Integer sumStockQtdProductEntryQtd = (sumStockByIdProduct + productEntryValidation.getQtd());

            //verifica se o máximo do estoque em produto é maior que o somatório da quantidade do estoque mais a quantidade passada pelo o usuário
            if (product.getMaxStock() >= sumStockQtdProductEntryQtd) {

                // criação do objeto de entrada de produto e inserção na tabela
                ProductInput productInput = new ProductInput();
                productInput.setProduct(product);
                productInput.setQtd(productEntryValidation.getQtd());
                productInput.setUnitaryValue(productEntryValidation.getUnitaryValue());

                ProductInput responseProductInput = productEntryRepository.save(productInput);
                return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.CREATED);

            } else {
                throw new BadRequestException("Quantidade máxima de produtos excedida!");
            }

        } else {
            // criação do objeto de entrada de produto e inserção na tabela
            ProductInput productInput = new ProductInput();
            productInput.setProduct(product);
            productInput.setQtd(productEntryValidation.getQtd());
            productInput.setUnitaryValue(productEntryValidation.getUnitaryValue());

            ProductInput responseProductInput = productEntryRepository.save(productInput);
            return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.CREATED);

        }

    }

    @ApiOperation(value = "Atualizar entrada de produtos")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductInput> updateProductEntry(@PathVariable(value = "id") long id,
                                                           @Valid @RequestBody ProductEntryValidation productEntryValidation) throws NotFoundException, BadRequestException {

        //busca entrada de produto pelo id passado via paramentro na requisição
        ProductInput productInput = productEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));

        //busca produto pelo id passado no productEntryValidation
        Product product = productRepository.findByStatusIsTrueAndId(productEntryValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productEntryValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productEntryValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {

           //verifica se máximo do estoque é igual ou que a quantidade informada pelo o usuário
            if (product.getMaxStock() >= productEntryValidation.getQtd()) {

                // criação do objeto de entrada de produto e inserção na tabela
                productInput.setId(id);
                productInput.setProduct(product);
                productInput.setQtd(productEntryValidation.getQtd());
                productInput.setUnitaryValue(productEntryValidation.getUnitaryValue());

                ProductInput responseProductInput = productEntryRepository.save(productInput);
                return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.NO_CONTENT);

            } else {
                throw new BadRequestException("Quantidade máxima de produtos excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Deletar entrada de produtos")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductInput> deleteProductEntry(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductInput productInput = productEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));
        productEntryRepository.delete(productInput);
        return new ResponseEntity<ProductInput>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar entrada de produtos pelo id")
    @RequestMapping(value = "/productEntry/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductInput> getProductEntryById(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductInput productInput = productEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));
        return new ResponseEntity<ProductInput>(productInput, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar entrada de produtos")
    @RequestMapping(value = "/productEntry", method = RequestMethod.GET)
    public Page<ProductInput> getAllCategories(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productEntryRepository.findAll(PageRequest.of(offset, limit));
    }

}
