package com.backend.sge.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductEntryValidation {

    @NotNull(message = "código do produto é obrigatório")
    @PositiveOrZero(message = "código do produto não pode ser negativo")
    private Long idProduct;

    @NotNull(message = "quantidade é obrigatório")
    @PositiveOrZero(message = "quantidade não pode ser negativo")
    private Integer qtd;

    @NotNull(message = "valor unitário é obrigatório")
    private Double unitaryValue;

}