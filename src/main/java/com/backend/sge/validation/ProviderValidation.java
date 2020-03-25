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
    @Size(min = 3, max = 255, message = "nome do fornecedor deve ter no mínimo 3 caracteres ou no máximo 255 caracteres")
    private String name;

    @Pattern(regexp = "(\\d{2}.\\d{3}.\\d{3}\\/\\d{4}-\\d{2})", message = "99.999.999/9999-99")
    @NotBlank(message = "cnpj é obrigatório")
    @Size(min = 18, max = 18, message = "cnpj deve ter 18 caracteres")
    private String cnpj;

    @Pattern(regexp = "(\\(\\d{2}\\)\\s)?(\\d{4}\\-\\d{4})", message = "(99) 9999-9999")
    @NotBlank(message = "telefone fixo é obrigatório")
    @Size(min = 14, max = 14, message = "telefone fixo deve ter 14 caracteres")
    private String phone;

    @Pattern(regexp = "(\\(\\d{2}\\)\\s)?(\\d{5}\\-\\d{4})", message = "(99) 99999-9999")
    @Size(min = 15, max = 15, message = "celular deve ter 15 caracteres")
    private String cellPhone;

    @Valid
    @Embedded
    private AddressValidation addressValidation;

}
