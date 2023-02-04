package ru.kolomiec.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import ru.kolomiec.util.RequestUtil;

abstract public class AbstractApiRequest {

    protected final String BASE_API_URL = "http://localhost:7070/api";
    protected final MediaType applicationJsonMediaType = MediaType.parse("application/json");
    protected final RequestUtil requestUtil = new RequestUtil();

    public String entityToJson(Object source) {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        String objectInJson = null;
        try {
            objectInJson = objectMapper.writeValueAsString(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectInJson;
    }
}
