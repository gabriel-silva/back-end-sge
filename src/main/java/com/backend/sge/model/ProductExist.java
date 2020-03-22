package com.backend.sge.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tb_product_exist")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductExist implements Serializable {

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

    @ApiModelProperty(value = "Data da saída")
    @CreationTimestamp
    private Timestamp dateExist;

}
