package com.backend.sge.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_stock")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Stock implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Código do produto")
    private Long idProduct;

    @ApiModelProperty(value = "Quantidade do estoque")
    private Integer qtd;

    @ApiModelProperty(value = "Valor unitário")
    private Double unitaryValue;

}