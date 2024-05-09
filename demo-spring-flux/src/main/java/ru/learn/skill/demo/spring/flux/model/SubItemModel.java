package ru.learn.skill.demo.spring.flux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubItemModel {

    private String name;
    private BigDecimal price;
}
