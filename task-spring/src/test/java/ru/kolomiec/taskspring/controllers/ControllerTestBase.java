package ru.kolomiec.taskspring.controllers;

import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import ru.kolomiec.taskspring.TestBase;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup(value = {
        @Sql(scripts = "classpath:/sqlQuery/drop-table-person.sql"),
        @Sql(scripts = "classpath:/sqlQuery/create-table-person.sql"),
        @Sql(scripts = "classpath:/sqlQuery/drop-table-task.sql"),
        @Sql(scripts = "classpath:/sqlQuery/create-table-task.sql")
})
public class ControllerTestBase extends TestBase {

    public final PersonRegistrationDTO testPerson = new PersonRegistrationDTO("testPerson", "testPerson");

    public MvcResult registerPerson(PersonRegistrationDTO personRegistrationDTO) throws Exception {
        return mockMvc.perform(post(BASE_URL + "/auth/registration")
                        .content(objectMapper.writeValueAsString(testPerson))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
    }
}
