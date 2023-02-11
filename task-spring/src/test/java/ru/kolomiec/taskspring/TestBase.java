package ru.kolomiec.taskspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.kolomiec.taskspring.initialize.PostgreSQLContainerInit;



@SpringBootTest()
@ContextConfiguration(initializers = {PostgreSQLContainerInit.InitializeApplicationContextWithDBProperties.class})
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
public class TestBase {

    @Autowired
    public MockMvc mockMvc;

    public final String BASE_URL = "http://localhost:7070/api";
    @Autowired
    public ObjectMapper objectMapper;
    @BeforeAll
    public static void init() {
        PostgreSQLContainerInit.container.start();
    }
}
