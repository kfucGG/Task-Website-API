package ru.kolomiec.taskspring.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonRegistrationDTO {

    private String username;
    private String password;
}
