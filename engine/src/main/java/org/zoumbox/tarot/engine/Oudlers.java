package org.zoumbox.tarot.engine;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public enum Oudlers {

    NONE(56),
    ONE(51),
    TWO(41),
    THREE(36);

    protected double target;

    public double getTarget() {
        return target;
    }

    Oudlers(double target) {
        this.target = target;
    }

}
