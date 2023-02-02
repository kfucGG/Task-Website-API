package ru.kolomiec.requests;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import ru.kolomiec.util.RequestUtil;

abstract public class AbstractApiRequest {

    protected final String BASE_API_URL = "http://localhost:7070/api";
    protected final MediaType applicationJsonMediaType = MediaType.parse("application/json");
    protected final RequestUtil requestUtil = new RequestUtil();

    public String entityToJson(Object source) {
        return new Gson().toJson(source);
    }
}
