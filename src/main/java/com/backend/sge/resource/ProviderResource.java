package com.backend.sge.resource;

import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.Address;
import com.backend.sge.model.Provider;
import com.backend.sge.repository.ProviderRepository;
import com.backend.sge.validation.ProviderValidation;
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
public class ProviderResource {

    @Autowired
    private ProviderRepository providerRepository;

    @ApiOperation(value = "Cadastrar fornecedor")
    @RequestMapping(value = "/provider", method = RequestMethod.POST)
    public ResponseEntity<Provider> createProvider(@Valid @RequestBody ProviderValidation providerValidation) {
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

        Provider responseProvider = providerRepository.save(provider);
        return new ResponseEntity<Provider>(responseProvider, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualizar fornecedor")
    @RequestMapping(value = "/provider/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Provider> updateProvider(@PathVariable(value = "id") long id,
                                                   @Valid @RequestBody ProviderValidation providerValidation) throws NotFoundException {
        Provider provider = providerRepository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrado com o id :: $id"));

        provider.setId(id);
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

        Provider responseProvider = providerRepository.save(provider);
        return new ResponseEntity<Provider>(responseProvider, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar fornecedor")
    @RequestMapping(value = "/provider/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Provider> deleteProvider(@PathVariable(name = "id") long id) throws NotFoundException {
        Provider provider = providerRepository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrado com o id :: $id"));
        providerRepository.delete(provider);
        return new ResponseEntity<Provider>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar fornecedor pelo id")
    @RequestMapping(value = "/provider/{id}", method = RequestMethod.GET)
    public ResponseEntity<Provider> getProviderById(@PathVariable(name = "id") long id) throws NotFoundException {
        Provider provider = providerRepository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrado com o id :: $id"));
        return new ResponseEntity<Provider>(provider, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar fornecedores")
    @RequestMapping(value = "/provider", method = RequestMethod.GET)
    public Page<Provider> getAllCategories(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return providerRepository.findAll(PageRequest.of(offset, limit));
    }

}
