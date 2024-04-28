package com.example.spring.demo.spring.integration.app;

import com.example.spring.demo.spring.integration.app.model.EntityModel;
import com.example.spring.demo.spring.integration.app.model.UpsertEntityRequest;
import com.example.spring.demo.spring.integration.app.repository.DatabaseEntityRepository;
import com.example.spring.demo.spring.integration.app.service.DatabaseEntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.redis.testcontainers.RedisContainer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@Sql("classpath:db/init.sql")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractTest {

    public static final UUID UPDATED_ID = UUID.fromString("adefec47-dee5-44a6-be03-5fad3052eeb4");

    public static final Instant ENTITY_DATE = Instant.parse("2100-01-01T00:00:00Z");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DatabaseEntityService databaseEntityService;

    @Autowired
    protected DatabaseEntityRepository databaseEntityRepository;

    @RegisterExtension
    protected static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    protected static PostgreSQLContainer postgreSQLContainer;

    @Container
    protected static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.12"))
            .withExposedPorts(6379)
            .withReuse(true);

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registeredProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());

        registry.add("app.integration.base-url", wireMockServer::baseUrl);
    }

    @BeforeEach
    public void before() throws Exception {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();

        stubClient();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    private void stubClient() throws Exception {
        List<EntityModel> findAllResponseBody = new ArrayList<>();
        findAllResponseBody.add(new EntityModel(UUID.randomUUID(), "Entity 1", Instant.now()));
        findAllResponseBody.add(new EntityModel(UUID.randomUUID(), "Entity 2", Instant.now()));
        wireMockServer.stubFor(WireMock.get("/api/v1/entity")
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(findAllResponseBody))
                                .withStatus(200)
                ));

        EntityModel findByNameResponseBody = new EntityModel(UUID.randomUUID(), "someEntity", ENTITY_DATE);
        wireMockServer.stubFor(WireMock.get("/api/v1/entity" + findByNameResponseBody.getName())
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(findByNameResponseBody))
                                .withStatus(200)
                ));

        UpsertEntityRequest createEntityRequest = new UpsertEntityRequest();
        createEntityRequest.setName("newEntity");
        EntityModel createResponseBody = new EntityModel(UUID.randomUUID(), "newEntity", ENTITY_DATE);
        wireMockServer.stubFor(WireMock.post("/api/v1/entity")
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(createEntityRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(createResponseBody))
                        .withStatus(201)));

        UpsertEntityRequest updateRequest = new UpsertEntityRequest();
        updateRequest.setName("updatedName");
        EntityModel updateResponseBody = new EntityModel(UPDATED_ID, "updatedName", ENTITY_DATE);
        wireMockServer.stubFor(WireMock.put("/api/v1/entity/" + UPDATED_ID)
//                .withRequestBody(equalToJson(objectMapper.writeValueAsString(updateRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(updateResponseBody))
                        .withStatus(200)));

        wireMockServer.stubFor(WireMock.delete("/api/v1/entity/" + UPDATED_ID)
                .willReturn(aResponse().withStatus(204)));
    }

}
