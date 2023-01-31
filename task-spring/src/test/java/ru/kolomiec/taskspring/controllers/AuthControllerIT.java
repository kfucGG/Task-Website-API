package ru.kolomiec.taskspring.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import ru.kolomiec.taskspring.IntegrationTestBase;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:/sqlQuery/drop-person-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sqlQuery/create-person-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthControllerIT extends ControllerIntegrationTestBase {

    private final PersonRegistrationDTO personRegistrationDTO = new PersonRegistrationDTO("test", "test");

    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(post("/api/auth/registration")
                    .content(objectMapper.writeValueAsString(personRegistrationDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        makeRequestForRegistrationNewPerson(personRegistrationDTO);
        JwtRequest loginRequest = new JwtRequest(personRegistrationDTO.getUsername(), personRegistrationDTO.getPassword());
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWithDefunctUsername() throws Exception {
        makeRequestForRegistrationNewPerson(personRegistrationDTO);
        JwtRequest loginRequest = new JwtRequest("Defunct username", "test");
        mockMvc.perform(post("/api/auth/login")
                    .content(objectMapper.writeValueAsString(loginRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        makeRequestForRegistrationNewPerson(personRegistrationDTO);
        JwtRequest loginRequest = new JwtRequest(personRegistrationDTO.getUsername(), "incorrect password");
        mockMvc.perform(post("/api/auth/login")
                    .content(objectMapper.writeValueAsString(loginRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
