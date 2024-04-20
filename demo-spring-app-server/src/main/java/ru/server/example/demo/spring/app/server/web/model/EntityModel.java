package ru.server.example.demo.spring.app.server.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EntityModel {
    private Long id;
    private String name;
    private Integer age;
    @Builder.Default
    private List<EntityModel> models = new ArrayList<>();
}
