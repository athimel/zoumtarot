package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public enum Handful {

    NONE(0),
    SIMPLE(20),
    DOUBLE(30),
    TRIPLE(40);

    private double value;

    public double getValue() {
        return value;
    }

    Handful(double value) {
        this.value = value;
    }

}
