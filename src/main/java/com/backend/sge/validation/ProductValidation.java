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
public class ProductValidation {

    @NotNull(message = "código da categoria é obrigatório")
    @PositiveOrZero(message = "código da categoria não pode ser negativo")
    private Long idCategory;

    @NotNull(message = "código do fornecedor é obrigatório")
    @PositiveOrZero(message = "código do fornecedor não pode ser negativo")
    private Long idProvider;

    @NotNull(message = "código da unidade de medida é obrigatório")
    @PositiveOrZero(message = "código da unidade de medida não pode ser negativo")
    private Long idMeasurementUnit;

    @NotBlank(message = "nome do produto é obrigatório")
    @Size(min = 3, max = 255, message = "nome do produto deve ter no mínino 3 caracteres e no máximo 255 caracteres")
    private String name;

    @NotNull(message = "estoque mínimo é obrigatório")
    @PositiveOrZero(message = "estoque mínimo não pode ser negativo")
    private Integer minStock;

    @NotNull(message = "estoque máximo é obrigatório")
    @PositiveOrZero(message = "estoque máximo não pode ser negativo")
    private Integer maxStock;

    @NotNull(message = "status é obrigatório")
    private Boolean status;

}