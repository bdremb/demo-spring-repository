package ru.learn.skill.demo.spring.flux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learn.skill.demo.spring.flux.entity.SubItem;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubItemModel {

    private String name;
    private BigDecimal price;

    public static SubItemModel from(SubItem subItem) {
        return new SubItemModel(subItem.getName(), subItem.getPrice());
    }

}
