package com.backend.sge.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Product implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Categoria")
    private Long idCategory;

    @ApiModelProperty(value = "Unidade de medida")
    private Long idMeasurementUnit;

    @ApiModelProperty(value = "Fornecedor")
    private Long idProvider;

    @ApiModelProperty(value = "Nome do produto")
    private String name;

    @ApiModelProperty(value = "Estoque mínimo")
    private Integer minStock;

    @ApiModelProperty(value = "Estoque máximo")
    private Integer maxStock;

    @ApiModelProperty(value = "Status do produto - ativado/desativado")
    private Boolean status;

}
