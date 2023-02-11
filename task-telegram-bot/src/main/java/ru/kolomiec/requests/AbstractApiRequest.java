package ru.kolomiec.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.*;
import ru.kolomiec.util.RequestUtil;

abstract public class AbstractApiRequest {

    protected final MediaType applicationJsonMediaType = MediaType.parse("application/json");
    protected final RequestUtil requestUtil = new RequestUtil();

    public String getBaseApiUrl() {
        if (System.getenv("FROM_DOCKER") != null) {
            return "http://task-spring:7070/api";
        }
        return "http://localhost:7070/api";
    }
    public String entityToJson(Object source) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).findAndRegisterModules();
        String objectInJson = null;
        try {
            objectInJson = objectMapper.writeValueAsString(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectInJson;
    }
}
