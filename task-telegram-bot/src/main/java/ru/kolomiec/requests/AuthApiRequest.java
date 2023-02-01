package ru.kolomiec.requests;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.dto.PersonDTO;

import javax.ws.rs.BadRequestException;
import java.io.IOException;

public class AuthApiRequest {

    private static final String BASE_URL = "http://localhost:7070/api";
    private final MediaType applicationJsonMediaType = MediaType.parse("application/json");

    public AuthToken registrationNewPersonOnApi(PersonDTO personDTO) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/registration")
                .post(RequestBody.create(applicationJsonMediaType, personDTOToJSON(personDTO)))
                .build();
        return retrieveTokenFromResponseBody(tryRequest(request));
    }
    public AuthToken loginPersonOnApi(PersonDTO personDTO) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/login")
                .post(RequestBody.create(applicationJsonMediaType, personDTOToJSON(personDTO)))
                .build();

        return retrieveTokenFromResponseBody(tryRequest(request));
    }

    private ResponseBody tryRequest(Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            return httpClient.newCall(request).execute().body();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new BadRequestException();
    }
    private AuthToken retrieveTokenFromResponseBody(ResponseBody responseBody) {
        try {
            return new Gson().fromJson(responseBody.string(), AuthToken.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("can not retrieve token from response body");
    }
    private String personDTOToJSON(PersonDTO personDTO) {
        return new Gson().toJson(personDTO);
    }

}
