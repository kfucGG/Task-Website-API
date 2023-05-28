package ru.kolomiec.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.ws.rs.BadRequestException;
import java.io.IOException;


@Slf4j
public class RequestUtil {

    public String tryConvertResponseBodyToString(ResponseBody responseBody) {
        log.info("converting responseBody to String");
        String responseInStringFormat = null;
        try {
            responseInStringFormat = responseBody.string();
        } catch (IOException ex) {
            log.info(ex.getMessage());
            ex.printStackTrace();
        }
        return responseInStringFormat;
    }
    public Response tryRequest(Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        log.info("making request on api");
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.CONFLICT_409.getStatusCode()) {
                log.info("server response that already had this resource throwing IllegalArgException");
                throw new IllegalArgumentException();
            }
            return response;
        } catch (IOException ex) {
            log.info(ex.getMessage());
        }
        throw new BadRequestException();
    }
}
