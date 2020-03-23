package com.backend.sge.resource;

import com.backend.sge.exception.BadRequestException;
import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.model.ProductExist;
import com.backend.sge.repository.ProductExistRepository;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.repository.StockRepository;
import com.backend.sge.validation.ProductExistValidation;
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
public class ProductExistResource {

    @Autowired
    private ProductExistRepository productExistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @ApiOperation(value = "Cadastrar saída de produtos")
    @RequestMapping(value = "/productExist", method = RequestMethod.POST)
    public ResponseEntity<ProductExist> createProductExist(@Valid @RequestBody ProductExistValidation productExistValidation) throws NotFoundException, BadRequestException {

        //busca produto pelo id passado no productExistValidation
        Product product = productRepository.findByStatusIsTrueAndId(productExistValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productExistValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productExistValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {
            Integer subStockQtdProductExistQtd = (sumStockByIdProduct - productExistValidation.getQtd());

            //verifica se o máximo do estoque em produto é maior que o somatório da quantidade do estoque mais a quantidade passada pelo o usuário
            if (subStockQtdProductExistQtd >= product.getMinStock()) {

                // criação do objeto de saída de produto e inserção na tabela
                ProductExist productExist = new ProductExist();
                productExist.setProduct(product);
                productExist.setQtd(productExistValidation.getQtd());
                productExist.setUnitaryValue(productExistValidation.getUnitaryValue());

                ProductExist responseProductExist = productExistRepository.save(productExist);
                return new ResponseEntity<ProductExist>(responseProductExist, HttpStatus.CREATED);

            } else {
                throw new BadRequestException("Quantidade do estoque excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Atualizar saída de produtos")
    @RequestMapping(value = "/productExist/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductExist> updateProductExist(@PathVariable(value = "id") long id,
                                                           @Valid @RequestBody ProductExistValidation productExistValidation) throws NotFoundException, BadRequestException {

        //busca saída de produto pelo id passado via paramentro na requisição
        ProductExist productExist = productExistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));

        //busca produto pelo id passado no productExistValidation
        Product product = productRepository.findByStatusIsTrueAndId(productExistValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productExistValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productExistValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null) {

            //verifica se máximo do estoque é igual ou que a quantidade informada pelo o usuário
            if (productExistValidation.getQtd() >= product.getMinStock()) {

                // criação do objeto de saída de produto e inserção na tabela
                productExist.setId(productExist.getId());
                productExist.setProduct(product);
                productExist.setQtd(productExistValidation.getQtd());
                productExist.setUnitaryValue(productExistValidation.getUnitaryValue());

                ProductExist responseProductExist = productExistRepository.save(productExist);
                return new ResponseEntity<ProductExist>(responseProductExist, HttpStatus.NO_CONTENT);

            } else {
                throw new BadRequestException("Quantidade do estoque excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Deletar saída de produtos")
    @RequestMapping(value = "/productExist/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductExist> deleteProductExist(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductExist productExist = productExistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));
        productExistRepository.delete(productExist);
        return new ResponseEntity<ProductExist>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar saída de produtos pelo id")
    @RequestMapping(value = "/productExist/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductExist> getProductExistById(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductExist productExist = productExistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));
        return new ResponseEntity<ProductExist>(productExist, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar saída de produtos")
    @RequestMapping(value = "/productExist", method = RequestMethod.GET)
    public Page<ProductExist> getAllCategories(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productExistRepository.findAll(PageRequest.of(offset, limit));
    }

}
