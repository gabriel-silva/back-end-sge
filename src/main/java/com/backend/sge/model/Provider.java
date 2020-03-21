package com.backend.sge.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tb_provider")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String cnpj;
    private String stateRegistration;
    private String cellPhone;
    private String phone;

    @Embedded
    private Address address;
}
