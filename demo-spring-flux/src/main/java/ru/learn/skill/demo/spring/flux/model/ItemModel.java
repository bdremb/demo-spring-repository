package ru.learn.skill.demo.spring.flux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {

    private String id;
    private String name;
    private Integer count;
    private List<SubItemModel> subItems;
}
