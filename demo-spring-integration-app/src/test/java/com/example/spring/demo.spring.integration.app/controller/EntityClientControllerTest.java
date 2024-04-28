package com.example.spring.demo.spring.integration.app.controller;

import com.example.spring.demo.spring.integration.app.AbstractTest;
import com.example.spring.demo.spring.integration.app.entity.DatabaseEntity;
import com.example.spring.demo.spring.integration.app.model.UpsertEntityRequest;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EntityClientControllerTest extends AbstractTest {

    @Test
    void whenGetAllEntities_thenReturnEntityList() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());

        String actualResponse = mockMvc.perform(get("/api/v1/client/entity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(databaseEntityService.findAll());

        assertFalse(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenGetEntityByName_thenReturnOneEntity() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());

        String actualResponse = mockMvc.perform(get("/api/v1/client/entity/testName1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(databaseEntityService.findByName("testName1"));

        assertFalse(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenCreateEntity_thenCreateEntityAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, databaseEntityRepository.count());

        mockMvc.perform(get("/api/v1/client/entity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        UpsertEntityRequest request = new UpsertEntityRequest();
        request.setName("newEntity");
        String actualResponse = mockMvc.perform(post("/api/v1/client/entity")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new DatabaseEntity(UUID.randomUUID(), "newEntity", ENTITY_DATE));

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(4, databaseEntityRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
    }

    @Test
    void whenUpdateEntity_thenUpdateEntityAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());

        mockMvc.perform(get("/api/v1/client/entity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        UpsertEntityRequest request = new UpsertEntityRequest();
        request.setName("updatedName");
        String actualResponse = mockMvc.perform(put("/api/v1/client/entity/{id}", UPDATED_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new DatabaseEntity(UUID.randomUUID(), "updatedName", ENTITY_DATE));

        assertTrue(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
    }

    @Test
    void whenDeleteEntityById_thenDeleteEntityByIdAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, databaseEntityRepository.count());

        mockMvc.perform(get("/api/v1/client/entity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(redisTemplate.keys("*").isEmpty());

        mockMvc.perform(delete("/api/v1/client/entity/{id}", UPDATED_ID))
                .andExpect(status().isNoContent());

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(2, databaseEntityRepository.count());
    }

}
