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
public class CategoryValidation {

    @NotBlank(message = "nome da categoria é obrigatório")
    @Size(min = 3, max = 50, message = "nome da categoria deve ter no mínimo 3 caracteres e no máximo 50 caracteres")
    private String name;

}
