package uni.fmi.model;

public class StatusMessageBuilder {
    private int status;
    private String message;

    public StatusMessageBuilder status(int status) {
        this.status = status;
        return this;
    }

    public StatusMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public StatusMessage build() {
        return new StatusMessage(status, message);
    }
}