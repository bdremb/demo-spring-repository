package ru.learn.skill.demo.spring.flux.controller;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.learn.skill.demo.spring.flux.AbstractTest;
import ru.learn.skill.demo.spring.flux.model.ItemModel;
import ru.learn.skill.demo.spring.flux.model.SubItemModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemControllerTest extends AbstractTest {

    @Test
    void whenGetAllItems_thenReturnListOfItemsFromDatabase() {
        var expected = List.of(
                new ItemModel(FIRST_ITEM_ID, "Name 1", 10, Collections.emptyList()),
                new ItemModel(SECOND_ITEM_ID, "Name 2", 20, List.of(
                        new SubItemModel("SubItem 1", BigDecimal.valueOf(1001)),
                        new SubItemModel("SubItem 2", BigDecimal.valueOf(2001))
                ))
        );

        webTestClient.get().uri("/api/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ItemModel.class)
                .hasSize(2)
                .contains(expected.toArray(ItemModel[]::new));
    }

    @Test
    void whenGetItemById_thenReturnItemById() {
        var expected = new ItemModel(SECOND_ITEM_ID, "Name 2", 20, List.of(
                new SubItemModel("SubItem 1", BigDecimal.valueOf(1001)),
                new SubItemModel("SubItem 2", BigDecimal.valueOf(2001))
        ));

        webTestClient.get().uri("/api/v1/items/{id}", SECOND_ITEM_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemModel.class)
                .isEqualTo(expected);
    }

    //11:16
    @Test
    void whenGetItemByName_thenReturnItemByName() {
        var expectedData = new ItemModel(FIRST_ITEM_ID, "Name 1", 10, Collections.emptyList());

        webTestClient.get().uri("/api/v1/items/by-name?name=Name 1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemModel.class)
                .isEqualTo(expectedData);
    }

    @Test
    void whenCreateItem_thenReturnNewItemWithIdAndPublishEvent() {
        StepVerifier.create(itemRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        ItemModel requestModel = new ItemModel();
        requestModel.setName("Test Item");
        requestModel.setCount(5);
        requestModel.setSubItems(Collections.emptyList());

        webTestClient.post().uri("/api/v1/items")
                .body(Mono.just(requestModel), ItemModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemModel.class)
                .value(responseModel -> assertNotNull(responseModel.getId()));

        StepVerifier.create(itemRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();

        webTestClient.get().uri("/api/v1/items/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ServerSentEvent<ItemModel>>() {
                })
                .getResponseBody()
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(serverSentEvent -> {
                    var data = serverSentEvent.data();
                    assertNotNull(data);
                    assertNotNull(data.getId());
                    assertEquals("Test Item", data.getName());
                })
                .thenCancel()
                .verify();
    }

    @Test
    void whenUpdateItem_thenReturnUpdatedItem() {
        ItemModel requestModel = new ItemModel();
        requestModel.setName("New Item Name");

        webTestClient.put().uri("/api/v1/items/{id}", FIRST_ITEM_ID)
                .body(Mono.just(requestModel), ItemModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemModel.class)
                .value(responseModel -> {
                    assertEquals("New Item Name", responseModel.getName());
                });

        StepVerifier.create(itemRepository.findItemByName("Name 1"))
                .expectNextCount(0L)
                .verifyComplete();

        StepVerifier.create(itemRepository.findItemByName("New Item Name"))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void whenDeleteById_thenRemoveItemFromDatabase() {
        webTestClient.delete().uri("/api/v1/items/{id}", FIRST_ITEM_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(itemRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }

}
