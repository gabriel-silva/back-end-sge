package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AddressValidation {

    @NotBlank(message = "logradouro é obrigatório")
    @Size(min = 3, max = 255, message = "logradouro deve ser igual ou superior a 3 caracteres e menor que 255 caracteres")
    private String publicPlace;

    @NotNull(message = "número é obrigatório")
    @PositiveOrZero(message = "número não pode ser negativo")
    private Integer number;

    @Size(max = 255, message = "complemento não deve ter mais que 255 caracteres")
    private String complement;

    @NotBlank(message = "bairro é obrigatório")
    @Size(max = 255, message = "bairro não deve ter mais que 255 caracteres")
    private String neighborhood;

    @Pattern(regexp = "(\\d{5}-\\d{3})", message = "99999-999")
    @NotBlank(message = "cep é obrigatório")
    @Size(min = 9, max = 9, message = "cep deve ter 9 caracteres")
    private String cep;

    @NotBlank(message = "cidade é obrigatória")
    @Size(min = 3, max = 50, message = "cidade deve ter no mínino 3 caracteres e no máximo 50 caracteres")
    private String city;

    @NotBlank(message = "estado é obrigatório")
    @Size(min = 2, max = 2, message = "estado deve ter 2 caracteres")
    private String state;

}