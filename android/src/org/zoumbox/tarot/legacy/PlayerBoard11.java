/*
 * #%L
 * ZoumTarot :: android
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
package org.zoumbox.tarot.legacy;

import org.zoumbox.tarot.engine.Deal;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PlayerBoard11 extends PlayerBoard {

    private static final long serialVersionUID = -2231617148598930878L;

    protected LinkedList<Deal> games = new LinkedList<Deal>();

    public LinkedList<Deal> getGames() {
        return games;
    }

    public void setGames(LinkedList<Deal> games) {
        this.games = games;
    }

    @Override
    public List<Deal> getDeals() {
        return getGames();
    }
}
