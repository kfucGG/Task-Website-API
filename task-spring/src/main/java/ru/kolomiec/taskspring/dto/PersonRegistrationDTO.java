package ru.kolomiec.taskspring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.kolomiec.taskspring.entity.Person;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonRegistrationDTO implements Serializable {

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Size(min = 4, max = 60, message = "Username by 4 to 60 symbols")
    private String username;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Size(min = 4, max = 60, message = "Password by 4 to 60 symbols")
    private String password;

    public Person toPerson() {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        return person;
    }
}
