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
import com.backend.sge.model.Product;
import com.backend.sge.repository.ProductRepository;
import com.backend.sge.validation.ProductValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductResource.class})
public class ProductResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private ProductResource productResource;

    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
    }

    @Test
    public void createProduct() throws Exception {

        ProductValidation productValidation = new ProductValidation();
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        Product product = new Product();
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());

        when(productRepository.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/product")
                .content(objectMapper.writeValueAsString(productValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.minStock", is(0)))
                .andExpect(jsonPath("$.maxStock", is(100)))
                .andExpect(jsonPath("$.status", is(true)));

        verify(productRepository).save(any(Product.class));

    }

    @Test
    public void updateProduct() throws Exception {

        ProductValidation productValidation = new ProductValidation();
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        Product product = new Product();
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());

        when(productRepository.findById((long) 1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/product/1")
                .content(objectMapper.writeValueAsString(productValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.minStock", is(0)))
                .andExpect(jsonPath("$.maxStock", is(100)))
                .andExpect(jsonPath("$.status", is(true)));

    }

    @Test
    public void getProductById() throws Exception {

        Product product = new Product();
        product.setId((long) 1);
        product.setName("Coca-Cola 2LT");
        product.setMinStock(0);
        product.setMaxStock(100);
        product.setStatus(true);

        when(productRepository.findById((long) 1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.minStock", is(0)))
                .andExpect(jsonPath("$.maxStock", is(100)))
                .andExpect(jsonPath("$.status", is(true)));

        verify(productRepository).findById((long) 1);

    }

    @Test
    public void getProductByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/product/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProduct() throws Exception {

        Product product = new Product();
        product.setId((long) 1);
        product.setName("Coca-Cola 2LT");
        product.setMinStock(0);
        product.setMaxStock(100);
        product.setStatus(true);

        when(productRepository.findById((long) 1)).thenReturn(Optional.of(product));

        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isNoContent());

        verify(productRepository, times(1)).delete(product);

    }

}