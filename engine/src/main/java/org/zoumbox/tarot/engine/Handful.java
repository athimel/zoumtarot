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
 * Représente une poignée : aucune, simple, double ou triple.
 * A chaque poignée est associé un nombre de points.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
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
