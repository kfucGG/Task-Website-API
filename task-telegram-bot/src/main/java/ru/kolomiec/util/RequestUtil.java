package ru.kolomiec.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import javax.ws.rs.BadRequestException;
import java.io.IOException;

public class RequestUtil {

    public String tryConvertResponseBodyToString(ResponseBody responseBody) {
        String responseInStringFormat = null;
        try {
            responseInStringFormat = responseBody.string();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseInStringFormat;
    }
    public Response tryRequest(Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new BadRequestException();
    }
}
