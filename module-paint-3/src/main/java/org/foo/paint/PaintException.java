package org.foo.paint;

public class PaintException extends RuntimeException {

    public PaintException(Exception cause) {
        super(cause);
    }

    public PaintException(String message) {
        super(message);
    }
}
