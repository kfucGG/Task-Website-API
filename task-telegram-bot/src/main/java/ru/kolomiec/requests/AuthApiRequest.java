package ru.kolomiec.requests;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.dto.PersonDTO;
import ru.kolomiec.util.RequestUtil;

public class AuthApiRequest extends AbstractApiRequest{


    public AuthToken registrationNewPersonOnApi(PersonDTO personDTO) {
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/auth/registration")
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(personDTO)))
                .build();
        return retrieveTokenFromResponseBody(requestUtil.tryRequest(request).body());
    }
    public AuthToken loginPersonOnApi(PersonDTO personDTO) {
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/auth/login")
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(personDTO)))
                .build();
        return retrieveTokenFromResponseBody(requestUtil.tryRequest(request).body());
    }

    private AuthToken retrieveTokenFromResponseBody(ResponseBody responseBody) {
        RequestUtil requestUtil = new RequestUtil();
        return new Gson().fromJson(requestUtil.tryConvertResponseBodyToString(responseBody), AuthToken.class);
    }

}
