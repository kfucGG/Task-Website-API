package ru.kolomiec.requests;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.ws.rs.BadRequestException;
import java.io.IOException;

abstract public class AbstractApiRequest {

    protected final String BASE_API_URL = "http://localhost:7070/api";
    protected final MediaType applicationJsonMediaType = MediaType.parse("application/json");

    public Response tryRequest(Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new BadRequestException();
    }

    public String tryConvertResponseBodyToString(ResponseBody responseBody) {
        String responseInStringFormat = null;
        try {
            responseInStringFormat = responseBody.string();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseInStringFormat;
    }
    public String entityToJson(Object source) {
        return new Gson().toJson(source);
    }
}
