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
@Table(name = "tb_measurement_unit")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MeasurementUnit implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Nome da unidade de medida")
    private String name;

    @ApiModelProperty(value = "Produtos")
    @OneToMany(mappedBy = "measurementUnit")
    private List<Product> products = new ArrayList<>();

}
