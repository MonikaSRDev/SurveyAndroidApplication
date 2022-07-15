package com.example.mithraapplication;

public interface HandleFileDownloadResponse {

    void fileDownloadedSuccessfully(byte[] message);
    void fileDownloadFailure(String message);
}
