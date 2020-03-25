package com.backend.sge.resource;

import com.backend.sge.exception.BadRequestException;
import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Product;
import com.backend.sge.model.ProductOutput;
import com.backend.sge.repository.ProductOutputRepository;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.repository.StockRepository;
import com.backend.sge.validation.ProductOutputValidation;
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
public class ProductOutputResource {

    @Autowired
    private ProductOutputRepository productOutputRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @ApiOperation(value = "Cadastrar saída de produtos")
    @RequestMapping(value = "/productOutput", method = RequestMethod.POST)
    public ResponseEntity<ProductOutput> createProductOutput(@Valid @RequestBody ProductOutputValidation productOutputValidation) throws NotFoundException, BadRequestException {

        //busca produto pelo id passado no productOutputValidation
        Product product = productRepository.findByStatusIsTrueAndId(productOutputValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productOutputValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productOutputValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null && sumStockByIdProduct > 0) {
            Integer subStockQtdProductOutputQtd = (sumStockByIdProduct - productOutputValidation.getQtd());

            //verifica se o máximo do estoque em produto é maior que o somatório da quantidade do estoque mais a quantidade passada pelo o usuário
            if (subStockQtdProductOutputQtd >= product.getMinStock()) {

                // criação do objeto de saída de produto e inserção na tabela
                ProductOutput productOutput = new ProductOutput();
                productOutput.setProduct(product);
                productOutput.setQtd(productOutputValidation.getQtd());
                productOutput.setUnitaryValue(productOutputValidation.getUnitaryValue());

                ProductOutput responseProductOutput = productOutputRepository.save(productOutput);
                return new ResponseEntity<ProductOutput>(responseProductOutput, HttpStatus.CREATED);

            } else {
                throw new BadRequestException("Quantidade do estoque excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Atualizar saída de produtos")
    @RequestMapping(value = "/productOutput/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductOutput> updateProductOutput(@PathVariable(value = "id") long id,
                                                            @Valid @RequestBody ProductOutputValidation productOutputValidation) throws NotFoundException, BadRequestException {

        //busca saída de produto pelo id passado via paramentro na requisição
        ProductOutput productOutput = productOutputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));

        //busca produto pelo id passado no productOutputValidation
        Product product = productRepository.findByStatusIsTrueAndId(productOutputValidation.getIdProduct())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com o id :: " + productOutputValidation.getIdProduct()));

        //somatório do estoque pelo o id do produto
        Integer sumStockByIdProduct = stockRepository.sumStockByIdProduct(productOutputValidation.getIdProduct());

        //caso o sumStockByIdProduct for diferente de nulo
        if (sumStockByIdProduct != null && sumStockByIdProduct > 0) {

            //verifica se máximo do estoque é igual ou que a quantidade informada pelo o usuário
            if (productOutputValidation.getQtd() >= product.getMinStock()) {

                // criação do objeto de saída de produto e inserção na tabela
                productOutput.setId(id);
                productOutput.setProduct(product);
                productOutput.setQtd(productOutputValidation.getQtd());
                productOutput.setUnitaryValue(productOutputValidation.getUnitaryValue());

                ProductOutput responseProductOutput = productOutputRepository.save(productOutput);
                return new ResponseEntity<ProductOutput>(responseProductOutput, HttpStatus.NO_CONTENT);

            } else {
                throw new BadRequestException("Quantidade do estoque excedida!");
            }

        } else {
            throw new BadRequestException("Não há estoque!");
        }

    }

    @ApiOperation(value = "Deletar saída de produtos")
    @RequestMapping(value = "/productOutput/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductOutput> deleteProductOutput(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductOutput productOutput = productOutputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));
        productOutputRepository.delete(productOutput);
        return new ResponseEntity<ProductOutput>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar saída de produtos pelo id")
    @RequestMapping(value = "/productOutput/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductOutput> getProductOutputById(@PathVariable(name = "id") long id) throws NotFoundException {
        ProductOutput productOutput = productOutputRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saída de Produto não encontrado com o id :: " + id));
        return new ResponseEntity<ProductOutput>(productOutput, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar saída de produtos")
    @RequestMapping(value = "/productOutput", method = RequestMethod.GET)
    public Page<ProductOutput> getAllProductOutput(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productOutputRepository.findAll(PageRequest.of(offset, limit));
    }

}
