package com.backend.sge.resource;

import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Category;
import com.backend.sge.repository.CategoryRepository;
import com.backend.sge.validation.CategoryValidation;
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
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @ApiOperation(value = "Cadastrar categoria")
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryValidation categoryValidation) {
        Category category = new Category();
        category.setName(categoryValidation.getName());
        Category responseCategory = categoryRepository.save(category);
        return new ResponseEntity<Category>(responseCategory, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualizar categoria")
    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@PathVariable(value = "id") long id,
                                                   @Valid @RequestBody CategoryValidation categoryValidation) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com o id :: " + id));
        category.setId(id);
        category.setName(categoryValidation.getName());
        Category responseCategory = categoryRepository.save(category);
        return new ResponseEntity<Category>(responseCategory, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar categoria")
    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable(name = "id") long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com o id :: " + id));
        categoryRepository.delete(category);
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar categoria pelo id")
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable(name = "id") long id) throws NotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com o id :: " + id));
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar categorias")
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Page<Category> getAllCategory(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return categoryRepository.findAll(PageRequest.of(offset, limit));
    }

}
