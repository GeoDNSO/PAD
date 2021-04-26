package es.ucm.fdi.tieryourlikes.model;

public class ApiResponse<T> {

    private T object;
    private ResponseStatus responseStatus;
    private String error;

    public ApiResponse(T object, ResponseStatus responseStatus, String error) {
        this.object = object;
        this.responseStatus = responseStatus;
        this.error = error;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
