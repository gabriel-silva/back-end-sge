package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

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

    @NotBlank(message = "cep é obrigatório")
    @Size(min = 9, max = 9, message = "cep não deve ter mais que 9 caracteres")
    private String cep;

    @NotBlank(message = "cidade é obrigatória")
    @Size(min = 3, max = 50, message = "cidade deve ser igual ou superior a 3 caracteres e menor que 50 caracteres")
    private String city;

    @NotBlank(message = "estado é obrigatório")
    @Size(max = 2, message = "estado não deve ter mais que 2 caracteres")
    private String state;

}