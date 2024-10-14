package dev.hoainamtd.dto.response;

import org.springframework.http.HttpStatusCode;

public class ResponseFailure extends ResponseSuccess {

    public ResponseFailure(HttpStatusCode status, String message) {
        super(status, message);
    }
}
