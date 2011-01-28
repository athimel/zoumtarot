package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <arno@kimnono.org>
 */
public enum Contract {

    PRISE(1),
    GARDE(2),
    GARDE_SANS(4),
    GARDE_CONTRE(6);

    int value;

    Contract(int value) {
        this.value = value;
    }

}
