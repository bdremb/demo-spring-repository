package ru.learn.skill.demo.spring.flux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learn.skill.demo.spring.flux.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {

    private String id;
    private String name;
    private Integer count;
    private List<SubItemModel> subItems;

    public static ItemModel from(Item item) {
        var model = new ItemModel();
        model.setId(item.getId());
        model.setName(item.getName());
        model.setCount(item.getCount());

        if(item.getSubItems() != null) {
            model.setSubItems(item.getSubItems().stream().map(SubItemModel::from).collect(Collectors.toList()));
        }

        return model;
    }

}
