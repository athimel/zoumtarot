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

import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class StatisticsHelper {

    public static LinkedHashMap<String, Statistics> getStatistics(List<PlayerBoard> boards) {
        LinkedHashMap<String, Statistics> result = Maps.newLinkedHashMap();
        for (PlayerBoard board : boards) {

            List<Deal> deals = board.getDeals();
            int dealCount = deals.size();
            for (String player : board.getScores().keySet()) {
                Statistics statistics = getStatistics(result, player);
                statistics.addDealCount(dealCount);
            }

            for (Deal deal : deals) {
                String taker = deal.getTaker();
                Statistics statistics = result.get(taker);
                Contract contract = deal.getContract();
                boolean won = deal.isWon();
                statistics.newPartyTaken(contract, won);
            }
        }
        return result;
    }

    protected static Statistics getStatistics(Map<String, Statistics> statisticsMap, String playerName) {
        String loweredPlayerName = playerName.toLowerCase();
        Statistics result = statisticsMap.get(loweredPlayerName);
        if (result == null) {
            result = new Statistics();
            result.setPlayerName(playerName);
            statisticsMap.put(loweredPlayerName, result);
        }
        return result;
    }

}
