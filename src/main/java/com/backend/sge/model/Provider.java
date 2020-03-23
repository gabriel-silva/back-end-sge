package com.backend.sge.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_provider")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Provider implements Serializable {

    @ApiModelProperty(value = "Código")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Nome do fornecedor")
    private String name;

    @ApiModelProperty(value = "Cnpj")
    private String cnpj;

    @ApiModelProperty(value = "Celular")
    private String cellPhone;

    @ApiModelProperty(value = "Telefone Fixo")
    private String phone;

    @ApiModelProperty(value = "Endereço do fornecedor")
    @Embedded
    private Address address;

}
