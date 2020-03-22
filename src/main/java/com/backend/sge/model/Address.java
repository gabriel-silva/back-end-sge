package com.backend.sge.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {
    @ApiModelProperty(value = "Logradouro")
    private String publicPlace;

    @ApiModelProperty(value = "Número")
    private Integer number;

    @ApiModelProperty(value = "Complemento")
    private String complement;

    @ApiModelProperty(value = "Bairro")
    private String neighborhood;

    @ApiModelProperty(value = "Cep")
    private String cep;

    @ApiModelProperty(value = "Cidade")
    private String city;

    @ApiModelProperty(value = "Estado")
    private String state;
}