package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MeasurementUnitValidation {

    @NotBlank(message = "nome da unidade de medida é obrigatório")
    @Size(min = 3, max = 15, message = "nome da unidade de medida deve ter no mínimo 3 caracteres e no máximo 15 caracteres")
    private String name;

}