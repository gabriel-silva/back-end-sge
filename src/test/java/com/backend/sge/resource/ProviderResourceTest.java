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

import com.backend.sge.model.Address;
import com.backend.sge.validation.AddressValidation;
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
import com.backend.sge.model.Provider;
import com.backend.sge.repository.ProviderRepository;
import com.backend.sge.validation.ProviderValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProviderResource.class})
public class ProviderResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private ProviderResource providerResource;

    private MockMvc mockMvc;

    @MockBean
    private ProviderRepository providerRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(providerResource).build();
    }

    @Test
    public void createProvider() throws Exception {

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

        mockMvc.perform(post("/api/provider")
                .content(objectMapper.writeValueAsString(providerValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("COCA COLA INDUSTRIAS LTDA")))
                .andExpect(jsonPath("$.cnpj", is("45.997.418/0001-53")))
                .andExpect(jsonPath("$.phone", is("(21) 3300-3639")))
                .andExpect(jsonPath("$.cellPhone", is("(21) 99933-3639")))
                .andExpect(jsonPath("$.address.cep", is("22250-040")))
                .andExpect(jsonPath("$.address.city", is("Rio de Janeiro")))
                .andExpect(jsonPath("$.address.complement", is("Andar 12 Parte")))
                .andExpect(jsonPath("$.address.neighborhood", is("Botafogo")))
                .andExpect(jsonPath("$.address.number", is(374)))
                .andExpect(jsonPath("$.address.publicPlace", is("PR de Botafogo")))
                .andExpect(jsonPath("$.address.state", is("RJ")));

        verify(providerRepository).save(any(Provider.class));

    }

    @Test
    public void updateProvider() throws Exception {

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

        when(providerRepository.findById((long) 1)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        mockMvc.perform(put("/api/provider/1")
                .content(objectMapper.writeValueAsString(providerValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("COCA COLA INDUSTRIAS LTDA")))
                .andExpect(jsonPath("$.cnpj", is("45.997.418/0001-53")))
                .andExpect(jsonPath("$.phone", is("(21) 3300-3639")))
                .andExpect(jsonPath("$.cellPhone", is("(21) 99933-3639")))
                .andExpect(jsonPath("$.address.cep", is("22250-040")))
                .andExpect(jsonPath("$.address.city", is("Rio de Janeiro")))
                .andExpect(jsonPath("$.address.complement", is("Andar 12 Parte")))
                .andExpect(jsonPath("$.address.neighborhood", is("Botafogo")))
                .andExpect(jsonPath("$.address.number", is(374)))
                .andExpect(jsonPath("$.address.publicPlace", is("PR de Botafogo")))
                .andExpect(jsonPath("$.address.state", is("RJ")));

    }

    @Test
    public void getProviderById() throws Exception {

        Provider provider = new Provider();
        provider.setId((long) 1);
        provider.setName("COCA COLA INDUSTRIAS LTDA");
        provider.setCnpj("45.997.418/0001-53");
        provider.setPhone("(21) 3300-3639");
        provider.setCellPhone("(21) 99933-3639");

        Address address = new Address();
        address.setCep("22250-040");
        address.setCity("Rio de Janeiro");
        address.setComplement("Andar 12 Parte");
        address.setNeighborhood("Botafogo");
        address.setNumber(374);
        address.setPublicPlace("PR de Botafogo");
        address.setState("RJ");

        provider.setAddress(address);

        when(providerRepository.findById((long) 1)).thenReturn(Optional.of(provider));

        mockMvc.perform(get("/api/provider/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("COCA COLA INDUSTRIAS LTDA")))
                .andExpect(jsonPath("$.cnpj", is("45.997.418/0001-53")))
                .andExpect(jsonPath("$.phone", is("(21) 3300-3639")))
                .andExpect(jsonPath("$.cellPhone", is("(21) 99933-3639")))
                .andExpect(jsonPath("$.address.cep", is("22250-040")))
                .andExpect(jsonPath("$.address.city", is("Rio de Janeiro")))
                .andExpect(jsonPath("$.address.complement", is("Andar 12 Parte")))
                .andExpect(jsonPath("$.address.neighborhood", is("Botafogo")))
                .andExpect(jsonPath("$.address.number", is(374)))
                .andExpect(jsonPath("$.address.publicPlace", is("PR de Botafogo")))
                .andExpect(jsonPath("$.address.state", is("RJ")));

        verify(providerRepository).findById((long) 1);

    }

    @Test
    public void getProviderByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/provider/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProvider() throws Exception {

        Provider provider = new Provider();
        provider.setId((long) 1);
        provider.setName("COCA COLA INDUSTRIAS LTDA");
        provider.setCnpj("45.997.418/0001-53");
        provider.setPhone("(21) 3300-3639");
        provider.setCellPhone("(21) 99933-3639");

        Address address = new Address();
        address.setCep("22250-040");
        address.setCity("Rio de Janeiro");
        address.setComplement("Andar 12 Parte");
        address.setNeighborhood("Botafogo");
        address.setNumber(374);
        address.setPublicPlace("PR de Botafogo");
        address.setState("RJ");

        provider.setAddress(address);

        when(providerRepository.findById((long) 1)).thenReturn(Optional.of(provider));

        mockMvc.perform(delete("/api/provider/1"))
                .andExpect(status().isNoContent());

        verify(providerRepository, times(1)).delete(provider);

    }

}