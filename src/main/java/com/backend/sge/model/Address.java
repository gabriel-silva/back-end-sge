package com.backend.sge.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {
    private String publicPlace;
    private Integer number;
    private String complement;
    private String neighborhood;
    private String cep;
    private String city;
    private String state;
}