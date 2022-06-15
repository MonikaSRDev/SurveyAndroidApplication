package com.example.mithraapplication.ModelClasses;

public class ServerErrorResponse extends JsonServerObject{
    private String exception;
    private String exc;
    private String _server_messages;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getExc() {
        return exc;
    }

    public void setExc(String exc) {
        this.exc = exc;
    }

    public String get_server_messages() {
        return _server_messages;
    }

    public void set_server_messages(String _server_messages) {
        this._server_messages = _server_messages;
    }
}
