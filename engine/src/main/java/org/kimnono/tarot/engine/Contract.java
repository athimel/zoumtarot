package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <arno@kimnono.org>
 */
public enum Contract {

    PRISE(1),
    GARDE(2),
    GARDE_SANS(4),
    GARDE_CONTRE(6);

    protected int value;

    public int getValue() {
        return value;
    }

    Contract(int value) {
        this.value = value;
    }

    public String toShortString() {
        switch (this) {
            case PRISE:
                return "P";
            case GARDE:
                return "G";
            case GARDE_SANS:
                return "GS";
            default:
                return "GC";
        }
    }

}
