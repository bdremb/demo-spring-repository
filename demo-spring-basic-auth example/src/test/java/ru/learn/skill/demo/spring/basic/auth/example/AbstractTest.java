package ru.learn.skill.demo.spring.basic.auth.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.learn.skill.demo.spring.basic.auth.example.entity.Role;
import ru.learn.skill.demo.spring.basic.auth.example.entity.RoleType;
import ru.learn.skill.demo.spring.basic.auth.example.entity.User;
import ru.learn.skill.demo.spring.basic.auth.example.repository.UserRepository;
import ru.learn.skill.demo.spring.basic.auth.example.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public class AbstractTest {

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        userService.createNewAccount(
                User.builder()
                        .username("testUser")
                        .password("12345")
                        .build(),
                Role.from(RoleType.ROLE_USER)
        );

        userService.createNewAccount(
                User.builder()
                        .username("testAdmin")
                        .password("55555")
                        .build(),
                Role.from(RoleType.ROLE_ADMIN)
        );
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

}
