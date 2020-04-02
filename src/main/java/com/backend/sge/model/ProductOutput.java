package com.backend.sge.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tb_product_output")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ProductOutput implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ApiModelProperty(value = "Produto")
//    @ManyToOne
//    @JoinColumn(name = "id_product", referencedColumnName = "id")
//    private Product product;

    @ApiModelProperty(value = "id do produto")
    private Long idProduct;

    @ApiModelProperty(value = "Quantidade do estoque")
    private Integer qtd;

    @ApiModelProperty(value = "Valor unitário")
    private Double unitaryValue;

    @ApiModelProperty(value = "Data de criação")
    @CreationTimestamp
    private Timestamp createdAt;

    @ApiModelProperty(value = "Data de atualização")
    @UpdateTimestamp
    private Timestamp updatedAt;

}
