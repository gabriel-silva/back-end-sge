package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProviderValidation {

    @NotBlank(message = "nome do fornecedor é obrigatório")
    @Size(min = 3, max = 255, message = "categoria deve ser igual ou superior a 3 caracteres e menor que 255 caracteres")
    private String name;

    @NotBlank(message = "cnpj é obrigatório")
    @Size(min = 18, max = 18, message = "cnpj deve ser igual a 18 caracteres")
    private String cnpj;

    @NotBlank(message = "telefone fixo é obrigatório")
    @Size(min = 14, max = 14, message = "telefone fixo deve ser igual a 14 caracteres")
    private String phone;

    @Size(min = 15, max = 15, message = "celular deve ser igual a 15 caracteres")
    private String cellPhone;

    @Valid
    @Embedded
    private AddressValidation addressValidation;

}
