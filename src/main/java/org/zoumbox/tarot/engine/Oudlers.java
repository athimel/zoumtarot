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
 * Nombre de bouts du preneur : aucun, un, deux ou trois.
 * A chaque nombre de bouts est associé une nombre de points à
 * réaliser par le preneur pour remporter la donne.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
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
