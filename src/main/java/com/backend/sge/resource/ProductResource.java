package com.backend.sge.resource;

import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.validation.ProductValidation;
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
public class ProductResource {

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation(value = "Cadastrar produto")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductValidation productValidation) {
        Product product = new Product();
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());
        Product responseProduct = productRepository.save(product);
        return new ResponseEntity<Product>(responseProduct, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualizar produto")
    @RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") long id,
                                                 @Valid @RequestBody ProductValidation productValidation) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: $id"));
        product.setId(id);
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());
        Product responseProduct = productRepository.save(product);
        return new ResponseEntity<Product>(responseProduct, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar produto")
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable(name = "id") long id) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: $id"));
        productRepository.delete(product);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar produto pelo id")
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable(name = "id") long id) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: $id"));
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar produtos")
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Page<Product> getAllCategories(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productRepository.findAll(PageRequest.of(offset, limit));
    }

}
