package com.example.mithraapplication.MithraAppServerEvents;

public interface HandleFileDownloadResponse {

    void fileDownloadedSuccessfully(byte[] message);
    void fileDownloadFailure(String message);
}
