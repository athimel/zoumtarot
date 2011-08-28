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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Représente le tableau des scores. C'est cet objet qui liste les joueurs,
 * ainsi que les donnes.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class PlayerBoard implements Serializable {

    private static final long serialVersionUID = -2231617148598930878L;

    protected LinkedHashMap<String, Integer> scores = new LinkedHashMap<String, Integer>();
    protected LinkedList<Deal> deals = new LinkedList<Deal>();

    protected long creationDate = -1;

    public void newParty(Collection<String> players) {
        ArrayList<String> playersCopy = Lists.newArrayList(players); // To be sure we're not working on the same list
        clear();
        initPlayers(playersCopy);
        initDate();
    }

    private void initDate() {
        if (creationDate == -1) {
            creationDate = System.currentTimeMillis();
        }
    }

    public void newParty(String... players) {
        newParty(Arrays.asList(players));
    }

    protected void clear() {
        scores.clear();
        deals.clear();
    }

    protected void initPlayers(List<String> players) {
        int playersCount = 0;
        if (players != null) {
            playersCount = players.size();
        }
        if (playersCount == 0 || (playersCount != 3 && playersCount != 4 && playersCount != 5)) {
            String message =
                    "Only 3, 4 and 5 players are supported for the moment. You gave %d player names.";
            throw new UnsupportedOperationException(String.format(message, playersCount));
        }
        for (String player : players) {
            scores.put(player.trim(), 0);
        }
    }

    public void dealEnded(Deal deal) {
        if (isDealComplete(deal)) {
            deals.add(deal);
            deal.initDate();

            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                String playerName = entry.getKey();
                Integer playerScore = entry.getValue();

                int playerPartyScore = PointsCounter.getPlayerDealScore(this, deal, playerName);
                playerScore += playerPartyScore;
                entry.setValue(playerScore);
            }
        }
    }

    public long getCreationDate() {
        return creationDate;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public boolean isDealComplete(Deal deal) {
        return true; // TODO Arno 28/01/2011
    }

    public boolean isScoreCoherent() {
        int total = 0;
        for (Integer score : scores.values()) {
            total += score;
        }
        boolean result = (total == 0);
        return result;
    }

    public int getPlayersCount() {
        int result = scores.size();
        return result;
    }

    public boolean isA5PlayersGame() {
        boolean result = (scores.size() == 5);
        return result;
    }

    public boolean isA3PlayersGame() {
        boolean result = (scores.size() == 3);
        return result;
    }

    public PlayerBoard cloneForNewParty() {
        PlayerBoard result = new PlayerBoard();
        result.newParty(scores.keySet());
        return result;
    }

    public String[] getPlayers() {
        String[] result = new String[scores.size()];
        result = scores.keySet().toArray(result);
        return result;
    }

    public void replaceDeal(int index, Deal newDeal) {
        deals.remove(index);
        deals.add(index, newDeal);
        resetScore();
    }

    protected void resetScore() {
        String[] players = getPlayers();
        List<Deal> gamesCopy = new ArrayList<Deal>(deals);
        newParty(players);
        for (Deal deal : gamesCopy) {
            dealEnded(deal);
        }
    }

    public void replacePlayers(List<String> players) {
        String[] inPlacePlayers = getPlayers();
        if (inPlacePlayers.length != players.size()) {
            throw new UnsupportedOperationException("It is not possible to modify player count");
        }
        String key = "__replaceBy#" + System.currentTimeMillis() + "#";
        // Rename in two phase to avoid player name collision during renaming
        for (int i = 0; i < inPlacePlayers.length; i++) {
            renamePlayer(inPlacePlayers[i], key + i);
        }
        for (int i = 0; i < inPlacePlayers.length; i++) {
            renamePlayer(key + i, players.get(i));
        }
    }

    protected void renamePlayer(String from, String to) {
        Integer score = scores.get(from);
        scores.remove(from);
        scores.put(to, score);

        for (Deal deal : deals) {
            if (deal.isTaker(from)) {
                deal.setTaker(to);
            }
            if (deal.isSecondTaker(from)) {
                deal.setSecondTaker(to);
            }
        }
    }

    public long getDuration() {
        long result = -1;
        if (deals != null && !deals.isEmpty() && creationDate > 0) {
            Deal lastDeal = deals.getLast();
            long lastGameDate = lastDeal.getDate();
            if (lastGameDate != -1) {
                result = lastGameDate - creationDate;
            }
        }
        return result;
    }

    public static PlayerBoard cloneIt(PlayerBoard board) {
        PlayerBoard result = new PlayerBoard();
        result.creationDate = board.getCreationDate();
        result.deals = Lists.newLinkedList(board.getDeals());
        result.scores = Maps.newLinkedHashMap(board.getScores());
        return result;
    }

}
