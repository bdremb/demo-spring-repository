package ru.learn.skill.demo.spring.flux.publisher;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.learn.skill.demo.spring.flux.model.ItemModel;

@Component
public class ItemUpdatesPublisher {

    private final Sinks.Many<ItemModel> itemModelUpdatesSink;

    public ItemUpdatesPublisher() {
        this.itemModelUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(ItemModel model) {
        itemModelUpdatesSink.tryEmitNext(model);
    }

    public Sinks.Many<ItemModel> getUpdatesSink() {
        return itemModelUpdatesSink;
    }

}
