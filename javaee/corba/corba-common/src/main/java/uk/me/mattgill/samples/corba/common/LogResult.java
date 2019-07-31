package uk.me.mattgill.samples.corba.common;

import java.io.Serializable;

public class LogResult implements Serializable {

    private static final long serialVersionUID = -3115001044370460052L;

    private final boolean success;
    private final String message;

    public LogResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("LogResult: {success = '%b', message = '%s'}", success, message);
    }
}