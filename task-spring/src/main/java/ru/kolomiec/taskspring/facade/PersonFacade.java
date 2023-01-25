package ru.kolomiec.taskspring.facade;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.entity.Person;

@Component
@AllArgsConstructor
public class PersonFacade {

    private final ModelMapper modelMapper;


    public Person fromRegistrationDTOtoPerson(PersonRegistrationDTO registrationPerson) {
        return modelMapper.map(registrationPerson, Person.class);
    }

    public PersonRegistrationDTO fromPersonToRegistrationDTO(Person person) {
        return modelMapper.map(person, PersonRegistrationDTO.class);
    }
}
