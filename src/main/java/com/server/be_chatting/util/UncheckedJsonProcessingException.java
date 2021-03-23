package com.server.be_chatting.util;

import java.io.UncheckedIOException;

import com.fasterxml.jackson.core.JsonProcessingException;


public class UncheckedJsonProcessingException extends UncheckedIOException {

    public UncheckedJsonProcessingException(JsonProcessingException cause) {
        super(cause.getMessage(), cause);
    }
}
