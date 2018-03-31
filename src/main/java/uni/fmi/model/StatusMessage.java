package uni.fmi.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatusMessage {
    private int status;
    private String message;

    StatusMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + status;
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StatusMessage)) {
            return false;
        }
        StatusMessage sm = (StatusMessage) obj;
        return sm.getStatus() == status
                && sm.getMessage().equals(message);
    }

    @Override
    public String toString() {
        return String.format("[Status: %s; Message: %s]",
                status, message);
    }
}
