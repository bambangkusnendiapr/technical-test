package com.enigma.technicaltest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WebResponse<T> {
    private String message;

    private T data;

    public WebResponse(String message) {
        this.message = message;
    }
}
