package com.backend.sge.resource;

import com.backend.sge.exception.BadRequestException;
import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.model.ProductInput;
import com.backend.sge.repository.ProductInputRepository;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.repository.StockRepository;
import com.backend.sge.validation.ProductInputValidation;
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
public class ProductInputResource {

    @Autowired
    private ProductInputRepository productInputRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @ApiOperation(value = "Cadastrar entrada de produtos")
    @RequestMapping(value = "/productInput", method = RequestMethod.POST)
    public ResponseEntity<ProductInput> createProductInput(@Valid @RequestBody ProductInputValidation productInputValidation) throws NotFoundException, BadRequestException {

        //busca produto pelo id passado no productInputValidation
        Product product = productRepository.findByStatusIsTrueAndId(productInputValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productInputValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productInputValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {
            Integer sumStockQtdProductInputQtd = (sumStockByIdProduct + productInputValidation.getQtd());

            //verifica se o máximo do estoque em produto é maior que o somatório da quantidade do estoque mais a quantidade passada pelo o usuário
            if (product.getMaxStock() >= sumStockQtdProductInputQtd) {

                // criação do objeto de entrada de produto e inserção na tabela
                ProductInput productInput = new ProductInput();
                productInput.setProduct(product);
                productInput.setQtd(productInputValidation.getQtd());
                productInput.setUnitaryValue(productInputValidation.getUnitaryValue());

                ProductInput responseProductInput = productInputRepository.save(productInput);
                return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.CREATED);

            } else {
                throw new BadRequestException("Quantidade máxima de produtos excedida!");
            }

        } else {
            // criação do objeto de entrada de produto e inserção na tabela
            ProductInput productInput = new ProductInput();
            productInput.setProduct(product);
            productInput.setQtd(productInputValidation.getQtd());
            productInput.setUnitaryValue(productInputValidation.getUnitaryValue());

            ProductInput responseProductInput = productInputRepository.save(productInput);
            return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.CREATED);

        }

    }

    @ApiOperation(value = "Atualizar entrada de produtos")
    @RequestMapping(value = "/productInput/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductInput> updateProductInput(@PathVariable(value = "id") long id,
                                                           @Valid @RequestBody ProductInputValidation productInputValidation) throws NotFoundException, BadRequestException {

        //busca entrada de produto pelo id passado via paramentro na requisição
        ProductInput productInput = productInputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));

        //busca produto pelo id passado no productInputValidation
        Product product = productRepository.findByStatusIsTrueAndId(productInputValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productInputValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productInputValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {

           //verifica se máximo do estoque é igual ou que a quantidade informada pelo o usuário
            if (product.getMaxStock() >= productInputValidation.getQtd()) {

                // criação do objeto de entrada de produto e inserção na tabela
                productInput.setId(id);
                productInput.setProduct(product);
                productInput.setQtd(productInputValidation.getQtd());
                productInput.setUnitaryValue(productInputValidation.getUnitaryValue());

                ProductInput responseProductInput = productInputRepository.save(productInput);
                return new ResponseEntity<ProductInput>(responseProductInput, HttpStatus.NO_CONTENT);

            } else {
                throw new BadRequestException("Quantidade máxima de produtos excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Deletar entrada de produtos")
    @RequestMapping(value = "/productInput/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductInput> deleteProductInput(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductInput productInput = productInputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));
        productInputRepository.delete(productInput);
        return new ResponseEntity<ProductInput>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar entrada de produtos pelo id")
    @RequestMapping(value = "/productInput/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductInput> getProductInputById(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductInput productInput = productInputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrada de Produto não encontrado com o id :: " + id));
        return new ResponseEntity<ProductInput>(productInput, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar entrada de produtos")
    @RequestMapping(value = "/productInput", method = RequestMethod.GET)
    public Page<ProductInput> getAllProductInput(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productInputRepository.findAll(PageRequest.of(offset, limit));
    }

}
