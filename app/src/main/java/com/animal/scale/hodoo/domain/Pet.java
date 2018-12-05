package com.animal.scale.hodoo.domain;

import lombok.Data;

@Data
public class Pet {

    private int petIdx;

    private String petGroupCode;

    private int basic;

    private int disease;

    private int physical;

    private int weight;

    private String createDate;
}
