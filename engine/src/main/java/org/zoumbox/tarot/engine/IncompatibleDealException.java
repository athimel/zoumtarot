package org.zoumbox.tarot.engine;

/**
 * Created by thimel on 27/12/13.
 */
public class IncompatibleDealException extends RuntimeException {

    private static final long serialVersionUID = 1554302050697890384L;

    public IncompatibleDealException() {
    }

    public IncompatibleDealException(String s) {
        super(s);
    }

    public IncompatibleDealException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IncompatibleDealException(Throwable throwable) {
        super(throwable);
    }

    public IncompatibleDealException(String s, Throwable throwable, boolean b, boolean b2) {
        super(s, throwable, b, b2);
    }

}
