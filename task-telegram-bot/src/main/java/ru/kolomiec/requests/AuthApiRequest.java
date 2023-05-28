package ru.kolomiec.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.dto.PersonDTO;

@Slf4j
public class AuthApiRequest extends AbstractApiRequest{


    public AuthToken registrationNewPersonOnApi(PersonDTO personDTO) {
        log.info("registration person on api username is: " + personDTO.getUsername());
        Request request = new Request.Builder()
                .url(getBaseApiUrl() + "/auth/registration")
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(personDTO)))
                .build();

        return retrieveTokenFromResponseBody(requestUtil.tryRequest(request).body());
    }
    public AuthToken loginPersonOnApi(PersonDTO personDTO) {
        log.info("login person on api username is: " + personDTO.getUsername());
        Request request = new Request.Builder()
                .url(getBaseApiUrl() + "/auth/login")
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(personDTO)))
                .build();
        return retrieveTokenFromResponseBody(requestUtil.tryRequest(request).body());
    }

    private AuthToken retrieveTokenFromResponseBody(ResponseBody responseBody) {
        log.info("retireve token from api response");
        try {
            return new ObjectMapper().readValue(requestUtil.tryConvertResponseBodyToString(responseBody), AuthToken.class);
        } catch (Exception e) {
            log.info("retireve token is failure");
            throw new RuntimeException(e.getMessage());
        }
    }

}
