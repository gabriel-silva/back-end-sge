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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_provider")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
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

    @ApiModelProperty(value = "Data de criação")
    @CreationTimestamp
    private Timestamp createdAt;

    @ApiModelProperty(value = "Data de atualização")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ApiModelProperty(value = "Endereço do fornecedor")
    @Embedded
    private Address address;

//    @ApiModelProperty(value = "Produtos")
//    @OneToMany(mappedBy = "provider")
//    private List<Product> products = new ArrayList<>();

}
