package ru.kolomiec.taskspring.util.validators;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.services.interfaces.PersonService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniqueUsernameValidator implements Validator {

    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonRegistrationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonRegistrationDTO targetPerson = (PersonRegistrationDTO) target;
        log.info("validate(): validating unique registration person username with username: " +
                targetPerson.getUsername());
        try {
            Person byUsername = personService.findByUsername(targetPerson.getUsername());
        } catch (UsernameNotFoundException ignore) {
            log.info("validate(): person with username:" + targetPerson.getUsername() +
                    " passed validation cause person with such username does not exists");
            return;
        }
        errors.rejectValue("username", String.valueOf(HttpStatus.CONFLICT.value()));
    }
}
