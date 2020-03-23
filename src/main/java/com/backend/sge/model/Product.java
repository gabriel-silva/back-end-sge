package com.backend.sge.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Product implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Categoria")
    @ManyToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private Category category;

    @ApiModelProperty(value = "Unidade de medida")
    @ManyToOne
    @JoinColumn(name = "id_measurement_unit", referencedColumnName = "id")
    private MeasurementUnit measurementUnit;

    @ApiModelProperty(value = "Fornecedor")
    @ManyToOne
    @JoinColumn(name = "id_provider", referencedColumnName = "id")
    private Provider provider;

    @ApiModelProperty(value = "Nome do produto")
    private String name;

    @ApiModelProperty(value = "Estoque mínimo")
    private Integer minStock;

    @ApiModelProperty(value = "Estoque máximo")
    private Integer maxStock;

    @ApiModelProperty(value = "Status do produto - ativado/desativado")
    private Boolean status;

    @ApiModelProperty(value = "Entrada de Produtos")
    @OneToMany(mappedBy = "product")
    private List<ProductEntry> productEntries = new ArrayList<>();

    @ApiModelProperty(value = "Saida de Produtos")
    @OneToMany(mappedBy = "product")
    private List<ProductExist> productExists = new ArrayList<>();

}
