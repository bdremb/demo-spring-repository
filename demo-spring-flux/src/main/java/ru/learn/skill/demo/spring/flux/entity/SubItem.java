package ru.learn.skill.demo.spring.flux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learn.skill.demo.spring.flux.model.SubItemModel;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubItem {

    private String name;

    private BigDecimal price;

    public static SubItem from(SubItemModel model) {
        return new SubItem(model.getName(), model.getPrice());
    }

}
