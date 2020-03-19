package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CategoryValidation {

    @PositiveOrZero(message = "id não pode ser negativo")
    private long id;

    @NotNull(message = "categoria é obrigatória")
    @Size(min = 3, max = 50, message = "categoria deve ser igual ou superior a 3 caracteres e menor que 50 caracteres")
    private String name;

}
