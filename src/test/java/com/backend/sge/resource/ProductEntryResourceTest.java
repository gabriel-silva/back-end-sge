package com.backend.sge.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
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
import com.backend.sge.repository.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductEntryResource.class})
public class ProductEntryResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private ProductEntryResource productEntryResource;

    private MockMvc mockMvc;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    private ProductEntryRepository productEntryRepository;

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
        mockMvc = MockMvcBuilders.standaloneSetup(productEntryResource).build();
    }

    @Test
    public void createProductEntry() throws Exception {

        ProductEntryValidation productEntryValidation = new ProductEntryValidation();
        productEntryValidation.setIdProduct((long) 1);
        productEntryValidation.setQtd(100);
        productEntryValidation.setUnitaryValue(2.50);

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
        product.setId(productEntryValidation.getIdProduct());
        product.setCategory(category);
        product.setMeasurementUnit(measurementUnit);
        product.setProvider(provider);
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findByStatusIsTrueAndId((long) 1)).thenReturn(Optional.of(product));

        ProductEntry productEntry = new ProductEntry();
        productEntry.setProduct(product);
        productEntry.setQtd(productEntryValidation.getQtd());
        productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

        when(productEntryRepository.save(any(ProductEntry.class))).thenReturn(productEntry);

        mockMvc.perform(post("/api/productEntry")
                .content(objectMapper.writeValueAsString(productEntryValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.qtd", is(100)))
                .andExpect(jsonPath("$.unitaryValue", is(2.50)))
                .andExpect(jsonPath("$.product.id", is(1)))
                .andExpect(jsonPath("$.product.category.id", is(1)))
                .andExpect(jsonPath("$.product.category.name", is("Bebidas")))
                .andExpect(jsonPath("$.product.measurementUnit.id", is(1)))
                .andExpect(jsonPath("$.product.measurementUnit.name", is("UND")))
                .andExpect(jsonPath("$.product.provider.id", is(1)))
                .andExpect(jsonPath("$.product.provider.name", is("COCA COLA INDUSTRIAS LTDA")))
                .andExpect(jsonPath("$.product.provider.cnpj", is("45.997.418/0001-53")))
                .andExpect(jsonPath("$.product.provider.phone", is("(21) 3300-3639")))
                .andExpect(jsonPath("$.product.provider.cellPhone", is("(21) 99933-3639")))
                .andExpect(jsonPath("$.product.provider.address.cep", is("22250-040")))
                .andExpect(jsonPath("$.product.provider.address.city", is("Rio de Janeiro")))
                .andExpect(jsonPath("$.product.provider.address.complement", is("Andar 12 Parte")))
                .andExpect(jsonPath("$.product.provider.address.neighborhood", is("Botafogo")))
                .andExpect(jsonPath("$.product.provider.address.number", is(374)))
                .andExpect(jsonPath("$.product.provider.address.publicPlace", is("PR de Botafogo")))
                .andExpect(jsonPath("$.product.provider.address.state", is("RJ")))
                .andExpect(jsonPath("$.product.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.product.minStock", is(0)))
                .andExpect(jsonPath("$.product.maxStock", is(100)))
                .andExpect(jsonPath("$.product.status", is(true)));

        verify(productEntryRepository).save(any(ProductEntry.class));

    }

    @Test
    public void updateProductEntry() throws Exception {

        ProductEntryValidation productEntryValidation = new ProductEntryValidation();
        productEntryValidation.setIdProduct((long) 1);
        productEntryValidation.setQtd(100);
        productEntryValidation.setUnitaryValue(2.50);

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
        product.setId(productEntryValidation.getIdProduct());
        product.setCategory(category);
        product.setMeasurementUnit(measurementUnit);
        product.setProvider(provider);
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findByStatusIsTrueAndId((long) 1)).thenReturn(Optional.of(product));

        ProductEntry productEntry = new ProductEntry();
        productEntry.setProduct(product);
        productEntry.setQtd(productEntryValidation.getQtd());
        productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

        when(productEntryRepository.findById((long) 1)).thenReturn(Optional.of(productEntry));
        when(productEntryRepository.save(any(ProductEntry.class))).thenReturn(productEntry);

        mockMvc.perform(put("/api/productEntry/1")
                .content(objectMapper.writeValueAsString(productEntryValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.qtd", is(100)))
                .andExpect(jsonPath("$.unitaryValue", is(2.50)))
                .andExpect(jsonPath("$.product.id", is(1)))
                .andExpect(jsonPath("$.product.category.id", is(1)))
                .andExpect(jsonPath("$.product.category.name", is("Bebidas")))
                .andExpect(jsonPath("$.product.measurementUnit.id", is(1)))
                .andExpect(jsonPath("$.product.measurementUnit.name", is("UND")))
                .andExpect(jsonPath("$.product.provider.id", is(1)))
                .andExpect(jsonPath("$.product.provider.name", is("COCA COLA INDUSTRIAS LTDA")))
                .andExpect(jsonPath("$.product.provider.cnpj", is("45.997.418/0001-53")))
                .andExpect(jsonPath("$.product.provider.phone", is("(21) 3300-3639")))
                .andExpect(jsonPath("$.product.provider.cellPhone", is("(21) 99933-3639")))
                .andExpect(jsonPath("$.product.provider.address.cep", is("22250-040")))
                .andExpect(jsonPath("$.product.provider.address.city", is("Rio de Janeiro")))
                .andExpect(jsonPath("$.product.provider.address.complement", is("Andar 12 Parte")))
                .andExpect(jsonPath("$.product.provider.address.neighborhood", is("Botafogo")))
                .andExpect(jsonPath("$.product.provider.address.number", is(374)))
                .andExpect(jsonPath("$.product.provider.address.publicPlace", is("PR de Botafogo")))
                .andExpect(jsonPath("$.product.provider.address.state", is("RJ")))
                .andExpect(jsonPath("$.product.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.product.minStock", is(0)))
                .andExpect(jsonPath("$.product.maxStock", is(100)))
                .andExpect(jsonPath("$.product.status", is(true)));

    }

    @Test
    public void getProductEntryById() throws Exception {

        ProductEntryValidation productEntryValidation = new ProductEntryValidation();
        productEntryValidation.setIdProduct((long) 1);
        productEntryValidation.setQtd(100);
        productEntryValidation.setUnitaryValue(2.50);

        ProductValidation productValidation = new ProductValidation();
        productValidation.setIdCategory((long) 1);
        productValidation.setIdMeasurementUnit((long) 1);
        productValidation.setIdProvider((long) 1);
        productValidation.setName("Coca-Cola 2LT");
        productValidation.setMinStock(0);
        productValidation.setMaxStock(100);
        productValidation.setStatus(true);

        Product product = new Product();
        product.setName(productValidation.getName());
        product.setMinStock(productValidation.getMinStock());
        product.setMaxStock(productValidation.getMaxStock());
        product.setStatus(productValidation.getStatus());

        ProductEntry productEntry = new ProductEntry();
        productEntry.setId((long) 1);
        productEntry.setProduct(product);
        productEntry.setQtd(productEntryValidation.getQtd());
        productEntry.setUnitaryValue(productEntryValidation.getUnitaryValue());

        when(productEntryRepository.findById((long) 1)).thenReturn(Optional.of(productEntry));

        mockMvc.perform(get("/api/productEntry/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.qtd", is(100)))
                .andExpect(jsonPath("$.unitaryValue", is(2.50)))
                .andExpect(jsonPath("$.product.name", is("Coca-Cola 2LT")))
                .andExpect(jsonPath("$.product.minStock", is(0)))
                .andExpect(jsonPath("$.product.maxStock", is(100)))
                .andExpect(jsonPath("$.product.status", is(true)));

        verify(productEntryRepository).findById((long) 1);

    }

    @Test
    public void getProductEntryByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/productEntry/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductEntry() throws Exception {

        ProductEntry productEntry = new ProductEntry();
        productEntry.setId((long) 1);
        productEntry.setQtd(100);
        productEntry.setUnitaryValue(2.50);

        when(productEntryRepository.findById((long) 1)).thenReturn(Optional.of(productEntry));

        mockMvc.perform(delete("/api/productEntry/1"))
                .andExpect(status().isNoContent());

        verify(productEntryRepository, times(1)).delete(productEntry);

    }

}