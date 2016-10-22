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
 * Classe utilitaire chargée du calcul des points.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class PointsCounter {

    public static int getScoreSeed(Deal deal) {

        double score = deal.score - deal.getOudlers().getTarget();
        boolean gameWon = score >= 0.0;
        if (gameWon) {
            score = Math.round(score); // Arrondi toujours à l'absolu supérieur
            score += 25;
        } else {
            score = Math.round(score - 0.1); // Pour forcer l'arrondi à l'absolu supérieur
            score -= 25;
        }

        int contractCoeff = deal.getContract().getValue();
        score *= contractCoeff;

        // Announcements : Handful. Always counts for the wining team
        if (gameWon) {
            score += deal.getHandful().getValue();
        } else {
            score -= deal.getHandful().getValue();
        }

        // Announcements : One Is Last. Counts for the team realizing it
        score += deal.getOneIsLast() * 10 * contractCoeff;

        // Announcements : Slam
        double slamScore = 0d;
        if (deal.isSlamAnnounced() && deal.isSlamRealized()) {
            slamScore = 400d;
        } else if (!deal.isSlamAnnounced() && deal.isSlamRealized()) {
            slamScore = 200d;
        } else if (deal.isSlamAnnounced() && !deal.isSlamRealized()) {
            // need to check if the deal is won
            if (gameWon) {
                slamScore = -200d; // points for the defense
            } else {
                slamScore = 200d;
            }
        }
        score += slamScore;

        Long scoreRounded = Math.round(score);
        int result = scoreRounded.intValue();
        return result;
    }

    public static int getPlayerDealScore(PlayerBoard board, Deal deal, String playerName) {

        int playerPartyScore = getScoreSeed(deal);
        boolean isTaker = deal.isTaker(playerName);
        boolean isSecondTaker = deal.isSecondTaker(playerName);
        boolean isExcludedPlayer = deal.isExcludedPlayer(playerName);

        if (isExcludedPlayer) {
            playerPartyScore = 0;
        } else {

            if (!isTaker && !isSecondTaker) { // N'a pas pris, on inverse le score
                playerPartyScore *= -1;
            }

            // Cas particulier du score du preneur
            if (isTaker) {
                // Par défaut, 4 joueurs, coeff=3
                int takerCoeff = 3;

                if (board.isA3PlayersGame()) {
                    // À 3 joueurs, le coeff n'est que de 2
                    takerCoeff = 2;
                } else if (board.isA5PlayersGame() || board.isA6PlayersGame()) {
                    // À 5 ou 6 joueurs, ça dépend de s'il est tout seul ou pas
                    takerCoeff = deal.isTakerAlone() ? 4 : 2;
                }
                playerPartyScore *= takerCoeff;
            }
        }
        return playerPartyScore;

    }

}
