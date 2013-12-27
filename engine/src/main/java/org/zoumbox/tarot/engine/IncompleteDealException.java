package org.zoumbox.tarot.engine;

/**
 * Created by thimel on 27/12/13.
 */
public class IncompleteDealException extends RuntimeException {

    private static final long serialVersionUID = -8476913472751032202L;

    public IncompleteDealException() {
    }

    public IncompleteDealException(String s) {
        super(s);
    }

    public IncompleteDealException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IncompleteDealException(Throwable throwable) {
        super(throwable);
    }

    public IncompleteDealException(String s, Throwable throwable, boolean b, boolean b2) {
        super(s, throwable, b, b2);
    }
}
