package com.example.bookstore;

import io.grpc.Status;
import jakarta.persistence.EntityExistsException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleEntityExists(EntityExistsException e) {
        return Status.ALREADY_EXISTS.withDescription(e.getMessage()).withCause(e);
    }
}
