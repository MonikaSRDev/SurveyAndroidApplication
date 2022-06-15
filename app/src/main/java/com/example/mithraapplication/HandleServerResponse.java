package com.example.mithraapplication;

public interface HandleServerResponse {
    void responseReceivedSuccessfully(String message);

    void responseReceivedFailure(String message);
}
