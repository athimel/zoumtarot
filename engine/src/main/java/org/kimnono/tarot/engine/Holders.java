package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public enum Holders {

    NONE(56),
    ONE(51),
    TWO(41),
    THREE(36);

    double target;

    Holders(double target) {
        this.target = target;
    }

}
