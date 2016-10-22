/*
 * #%L
 * ZoumTarot :: engine
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Zoumbox.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.zoumbox.tarot.engine;

/**
 * Représente un contrat : prise, garde, garde sans et garde contre.
 * A chaque contrat est associé un coefficient utilisé dans le calcul des points.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
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
