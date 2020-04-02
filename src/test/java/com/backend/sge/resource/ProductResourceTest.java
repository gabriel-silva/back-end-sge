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

import com.backend.sge.model.*;
import com.backend.sge.repository.CategoryRepository;
import com.backend.sge.repository.MeasurementUnitRepository;
import com.backend.sge.repository.ProviderRepository;
import com.backend.sge.validation.*;
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
import com.backend.sge.repository.ProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductResource.class})
public class ProductResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private ProductResource productResource;

    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private MeasurementUnitRepository measurementUnitRepository;

    @MockBean
    private ProviderRepository providerRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
    }

    @Test
    public void createProduct() throws Exception {

        ProductValidation productValidation = new ProductValidation();
        productValidation.setIdCategory((long) 1);
        productValidation.setIdMeasurementUnit((long) 1);
        productValidation.setIdProvider((long) 1);
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        CategoryValidation categoryValidation = new CategoryValidation();
        categoryValidation.setName("Bebidas");

        Category category = new Category();
        category.setId(productValidation.getIdCategory());
        category.setName(categoryValidation.getName());

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));

        MeasurementUnitValidation measurementUnitValidation = new MeasurementUnitValidation();
        measurementUnitValidation.setName("UND");

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setId(productValidation.getIdMeasurementUnit());
        measurementUnit.setName(measurementUnitValidation.getName());

        when(measurementUnitRepository.save(any(MeasurementUnit.class))).thenReturn(measurementUnit);
        when(measurementUnitRepository.findById((long) 1)).thenReturn(Optional.of(measurementUnit));

        ProviderValidation providerValidation = new ProviderValidation();
        providerValidation.setName("COCA COLA INDUSTRIAS LTDA");
        providerValidation.setCnpj("45.997.418/0001-53");
        providerValidation.setPhone("(21) 3300-3639");
        providerValidation.setCellPhone("(21) 99933-3639");

        AddressValidation addressValidation = new AddressValidation();
        addressValidation.setCep("22250-040");
        addressValidation.setCity("Rio de Janeiro");
        addressValidation.setComplement("Andar 12 Parte");
        addressValidation.setNeighborhood("Botafogo");
        addressValidation.setNumber(374);
        addressValidation.setPublicPlace("PR de Botafogo");
        addressValidation.setState("RJ");

        providerValidation.setAddressValidation(addressValidation);

        Provider provider = new Provider();
        provider.setId(productValidation.getIdProvider());
        provider.setName(providerValidation.getName());
        provider.setCnpj(providerValidation.getCnpj());
        provider.setPhone(providerValidation.getPhone());
        provider.setCellPhone(providerValidation.getCellPhone());

        Address address = new Address();
        address.setCep(providerValidation.getAddressValidation().getCep());
        address.setCity(providerValidation.getAddressValidation().getCity());
        address.setComplement(providerValidation.getAddressValidation().getComplement());
        address.setNeighborhood(providerValidation.getAddressValidation().getNeighborhood());
        address.setNumber(providerValidation.getAddressValidation().getNumber());
        address.setPublicPlace(providerValidation.getAddressValidation().getPublicPlace());
        address.setState(providerValidation.getAddressValidation().getState());

        provider.setAddress(address);

        when(providerRepository.save(any(Provider.class))).thenReturn(provider);
        when(providerRepository.findById((long) 1)).thenReturn(Optional.of(provider));

        Product product = new Product();
        product.setIdCategory(category.getId());
        product.setIdMeasurementUnit(measurementUnit.getId());
        product.setIdProvider(provider.getId());
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
//                .andExpect(jsonPath("$.category.id", is(1)))
//                .andExpect(jsonPath("$.category.name", is("Bebidas")))
//                .andExpect(jsonPath("$.measurementUnit.id", is(1)))
//                .andExpect(jsonPath("$.measurementUnit.name", is("UND")))
//                .andExpect(jsonPath("$.provider.id", is(1)))
//                .andExpect(jsonPath("$.provider.name", is("COCA COLA INDUSTRIAS LTDA")))
//                .andExpect(jsonPath("$.provider.cnpj", is("45.997.418/0001-53")))
//                .andExpect(jsonPath("$.provider.phone", is("(21) 3300-3639")))
//                .andExpect(jsonPath("$.provider.cellPhone", is("(21) 99933-3639")))
//                .andExpect(jsonPath("$.provider.address.cep", is("22250-040")))
//                .andExpect(jsonPath("$.provider.address.city", is("Rio de Janeiro")))
//                .andExpect(jsonPath("$.provider.address.complement", is("Andar 12 Parte")))
//                .andExpect(jsonPath("$.provider.address.neighborhood", is("Botafogo")))
//                .andExpect(jsonPath("$.provider.address.number", is(374)))
//                .andExpect(jsonPath("$.provider.address.publicPlace", is("PR de Botafogo")))
//                .andExpect(jsonPath("$.provider.address.state", is("RJ")));

        verify(productRepository).save(any(Product.class));

    }

    @Test
    public void updateProduct() throws Exception {

        ProductValidation productValidation = new ProductValidation();
        productValidation.setIdCategory((long) 1);
        productValidation.setIdMeasurementUnit((long) 1);
        productValidation.setIdProvider((long) 1);
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        CategoryValidation categoryValidation = new CategoryValidation();
        categoryValidation.setName("Bebidas");

        Category category = new Category();
        category.setId(productValidation.getIdCategory());
        category.setName(categoryValidation.getName());

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));

        MeasurementUnitValidation measurementUnitValidation = new MeasurementUnitValidation();
        measurementUnitValidation.setName("UND");

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setId(productValidation.getIdMeasurementUnit());
        measurementUnit.setName(measurementUnitValidation.getName());

        when(measurementUnitRepository.save(any(MeasurementUnit.class))).thenReturn(measurementUnit);
        when(measurementUnitRepository.findById((long) 1)).thenReturn(Optional.of(measurementUnit));

        ProviderValidation providerValidation = new ProviderValidation();
        providerValidation.setName("COCA COLA INDUSTRIAS LTDA");
        providerValidation.setCnpj("45.997.418/0001-53");
        providerValidation.setPhone("(21) 3300-3639");
        providerValidation.setCellPhone("(21) 99933-3639");

        AddressValidation addressValidation = new AddressValidation();
        addressValidation.setCep("22250-040");
        addressValidation.setCity("Rio de Janeiro");
        addressValidation.setComplement("Andar 12 Parte");
        addressValidation.setNeighborhood("Botafogo");
        addressValidation.setNumber(374);
        addressValidation.setPublicPlace("PR de Botafogo");
        addressValidation.setState("RJ");

        providerValidation.setAddressValidation(addressValidation);

        Provider provider = new Provider();
        provider.setId(productValidation.getIdProvider());
        provider.setName(providerValidation.getName());
        provider.setCnpj(providerValidation.getCnpj());
        provider.setPhone(providerValidation.getPhone());
        provider.setCellPhone(providerValidation.getCellPhone());

        Address address = new Address();
        address.setCep(providerValidation.getAddressValidation().getCep());
        address.setCity(providerValidation.getAddressValidation().getCity());
        address.setComplement(providerValidation.getAddressValidation().getComplement());
        address.setNeighborhood(providerValidation.getAddressValidation().getNeighborhood());
        address.setNumber(providerValidation.getAddressValidation().getNumber());
        address.setPublicPlace(providerValidation.getAddressValidation().getPublicPlace());
        address.setState(providerValidation.getAddressValidation().getState());

        provider.setAddress(address);

        when(providerRepository.save(any(Provider.class))).thenReturn(provider);
        when(providerRepository.findById((long) 1)).thenReturn(Optional.of(provider));

        Product product = new Product();
        product.setIdCategory(category.getId());
        product.setIdMeasurementUnit(measurementUnit.getId());
        product.setIdProvider(provider.getId());
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
//                .andExpect(jsonPath("$.category.id", is(1)))
//                .andExpect(jsonPath("$.category.name", is("Bebidas")))
//                .andExpect(jsonPath("$.measurementUnit.id", is(1)))
//                .andExpect(jsonPath("$.measurementUnit.name", is("UND")))
//                .andExpect(jsonPath("$.provider.id", is(1)))
//                .andExpect(jsonPath("$.provider.name", is("COCA COLA INDUSTRIAS LTDA")))
//                .andExpect(jsonPath("$.provider.cnpj", is("45.997.418/0001-53")))
//                .andExpect(jsonPath("$.provider.phone", is("(21) 3300-3639")))
//                .andExpect(jsonPath("$.provider.cellPhone", is("(21) 99933-3639")))
//                .andExpect(jsonPath("$.provider.address.cep", is("22250-040")))
//                .andExpect(jsonPath("$.provider.address.city", is("Rio de Janeiro")))
//                .andExpect(jsonPath("$.provider.address.complement", is("Andar 12 Parte")))
//                .andExpect(jsonPath("$.provider.address.neighborhood", is("Botafogo")))
//                .andExpect(jsonPath("$.provider.address.number", is(374)))
//                .andExpect(jsonPath("$.provider.address.publicPlace", is("PR de Botafogo")))
//                .andExpect(jsonPath("$.provider.address.state", is("RJ")));

    }

    @Test
    public void getProductById() throws Exception {

        ProductValidation productValidation = new ProductValidation();
        productValidation.setIdCategory((long) 1);
        productValidation.setIdMeasurementUnit((long) 1);
        productValidation.setIdProvider((long) 1);
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        CategoryValidation categoryValidation = new CategoryValidation();
        categoryValidation.setName("Bebidas");

        Category category = new Category();
        category.setId(productValidation.getIdCategory());
        category.setName(categoryValidation.getName());

        MeasurementUnitValidation measurementUnitValidation = new MeasurementUnitValidation();
        measurementUnitValidation.setName("UND");

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setId(productValidation.getIdMeasurementUnit());
        measurementUnit.setName(measurementUnitValidation.getName());

        ProviderValidation providerValidation = new ProviderValidation();
        providerValidation.setName("COCA COLA INDUSTRIAS LTDA");
        providerValidation.setCnpj("45.997.418/0001-53");
        providerValidation.setPhone("(21) 3300-3639");
        providerValidation.setCellPhone("(21) 99933-3639");

        AddressValidation addressValidation = new AddressValidation();
        addressValidation.setCep("22250-040");
        addressValidation.setCity("Rio de Janeiro");
        addressValidation.setComplement("Andar 12 Parte");
        addressValidation.setNeighborhood("Botafogo");
        addressValidation.setNumber(374);
        addressValidation.setPublicPlace("PR de Botafogo");
        addressValidation.setState("RJ");

        providerValidation.setAddressValidation(addressValidation);

        Provider provider = new Provider();
        provider.setId(productValidation.getIdProvider());
        provider.setName(providerValidation.getName());
        provider.setCnpj(providerValidation.getCnpj());
        provider.setPhone(providerValidation.getPhone());
        provider.setCellPhone(providerValidation.getCellPhone());

        Address address = new Address();
        address.setCep(providerValidation.getAddressValidation().getCep());
        address.setCity(providerValidation.getAddressValidation().getCity());
        address.setComplement(providerValidation.getAddressValidation().getComplement());
        address.setNeighborhood(providerValidation.getAddressValidation().getNeighborhood());
        address.setNumber(providerValidation.getAddressValidation().getNumber());
        address.setPublicPlace(providerValidation.getAddressValidation().getPublicPlace());
        address.setState(providerValidation.getAddressValidation().getState());

        provider.setAddress(address);

        Product product = new Product();
        product.setId((long) 1);
        product.setIdCategory(category.getId());
        product.setIdMeasurementUnit(measurementUnit.getId());
        product.setIdProvider(provider.getId());
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
//                .andExpect(jsonPath("$.category.id", is(1)))
//                .andExpect(jsonPath("$.category.name", is("Bebidas")))
//                .andExpect(jsonPath("$.measurementUnit.id", is(1)))
//                .andExpect(jsonPath("$.measurementUnit.name", is("UND")))
//                .andExpect(jsonPath("$.provider.id", is(1)))
//                .andExpect(jsonPath("$.provider.name", is("COCA COLA INDUSTRIAS LTDA")))
//                .andExpect(jsonPath("$.provider.cnpj", is("45.997.418/0001-53")))
//                .andExpect(jsonPath("$.provider.phone", is("(21) 3300-3639")))
//                .andExpect(jsonPath("$.provider.cellPhone", is("(21) 99933-3639")))
//                .andExpect(jsonPath("$.provider.address.cep", is("22250-040")))
//                .andExpect(jsonPath("$.provider.address.city", is("Rio de Janeiro")))
//                .andExpect(jsonPath("$.provider.address.complement", is("Andar 12 Parte")))
//                .andExpect(jsonPath("$.provider.address.neighborhood", is("Botafogo")))
//                .andExpect(jsonPath("$.provider.address.number", is(374)))
//                .andExpect(jsonPath("$.provider.address.publicPlace", is("PR de Botafogo")))
//                .andExpect(jsonPath("$.provider.address.state", is("RJ")));

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