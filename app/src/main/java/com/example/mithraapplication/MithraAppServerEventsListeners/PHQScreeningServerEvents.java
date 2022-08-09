package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface PHQScreeningServerEvents {

    void postScreeningDetails(String message);

    void updateScreeningStatus(String message);
}
