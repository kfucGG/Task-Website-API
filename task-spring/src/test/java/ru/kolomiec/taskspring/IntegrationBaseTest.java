package ru.kolomiec.taskspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.kolomiec.taskspring.initialize.PostgreSQLContainerInit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
//@ContextConfiguration(initializers = {PostgreSQLContainerInit.InitializeApplicationContextWithDBProperties.class})
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
public class IntegrationBaseTest {
//
//    @Autowired
//    public MockMvc mockMvc;
//
//    @Autowired
//    public ObjectMapper objectMapper;
//
//    @BeforeAll
//    public static void init() {
//        PostgreSQLContainerInit.container.start();
//    }
//
//    @AfterAll
//    public static void shutdown() {
//        PostgreSQLContainerInit.container.stop();
//    }
}
