package ru.kolomiec.taskspring.controllers;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.kolomiec.taskspring.IntegrationBaseTest;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ControllerIntegrationTestBase extends IntegrationBaseTest {
//
//    public MvcResult makeRequestForRegistrationNewPerson(PersonRegistrationDTO personRegistrationDTO) throws Exception {
//        return mockMvc.perform(post("/api/auth/registration")
//                        .content(objectMapper.writeValueAsString(personRegistrationDTO))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//    }
//
//    public JwtResponse deserializeStringToJwtResponse(String json) throws Exception {
//        return objectMapper.readValue(json, JwtResponse.class);
//    }
//
//    public String makeRequestForRegistrationAndReturnToken(PersonRegistrationDTO personRegistrationDTO) throws Exception {
//        String response = makeRequestForRegistrationNewPerson(personRegistrationDTO).getResponse().getContentAsString();
//        return deserializeStringToJwtResponse(response).getToken();
//    }

}
