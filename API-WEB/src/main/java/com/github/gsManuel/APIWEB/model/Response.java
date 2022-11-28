package com.github.gsManuel.APIWEB.model;

public class Response {
    private int statusCode;
    private String message;

    public Response(int code, String s) {
        this.statusCode= code;
        this.message=s;
    }


    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
