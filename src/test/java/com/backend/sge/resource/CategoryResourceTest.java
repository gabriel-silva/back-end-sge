package com.backend.sge.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.sge.model.Category;
import com.backend.sge.repository.CategoryRepository;
import com.backend.sge.validation.CategoryValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CategoryResource.class})
public class CategoryResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private CategoryResource categoryResource;

    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(categoryResource).build();
    }

    @Test
    public void createCategory() throws Exception {

        CategoryValidation categoryValidation = new CategoryValidation();
        categoryValidation.setName("Bebidas");

        Category category = new Category();
        category.setName(categoryValidation.getName());

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/category")
                .content(objectMapper.writeValueAsString(categoryValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Bebidas")));

        verify(categoryRepository).save(any(Category.class));

    }

    @Test
    public void updateCategory() throws Exception {

        CategoryValidation categoryValidation = new CategoryValidation();
        categoryValidation.setName("Bebidas");

        Category category = new Category();
        category.setName(categoryValidation.getName());

        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/api/category/1")
                .content(objectMapper.writeValueAsString(categoryValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bebidas")));

    }

    @Test
    public void getCategoryById() throws Exception {

        Category category = new Category();
        category.setId((long) 1);
        category.setName("Bebidas");

        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/category/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bebidas")));

        verify(categoryRepository).findById((long) 1);

    }

    @Test
    public void getCategoryByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/category/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCategory() throws Exception {

        Category category = new Category();
        category.setId((long) 1);
        category.setName("Bebidas");

        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));

        mockMvc.perform(delete("/api/category/1"))
                .andExpect(status().isNoContent());

        verify(categoryRepository, times(1)).delete(category);

    }

}