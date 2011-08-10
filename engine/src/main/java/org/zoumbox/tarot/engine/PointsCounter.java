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
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PointsCounter {

    public static int getScoreSeed(Game game) {

        double score = game.score - game.getOudlers().getTarget();
        boolean gameWon = score >= 0.0;
        if (gameWon) {
            score = Math.round(score); // Arrondi toujours à l'absolu supérieur
            score += 25;
        } else {
            score = Math.round(score - 0.1); // Pour forcer l'arrondi à l'absolu supérieur
            score -= 25;
        }

        int contractCoeff = game.getContract().getValue();
        score *= contractCoeff;

        // Announcements : Handful. Always counts for the wining team
        if (gameWon) {
            score += game.getHandful().getValue();
        } else {
            score -= game.getHandful().getValue();
        }

        // Announcements : One Is Last. Counts for the team realizing it
        score += game.getOneIsLast() * 10 * contractCoeff;

        // Announcements : Slam
        double slamScore = 0d;
        if (game.isSlamAnnounced() && game.isSlamRealized()) {
            slamScore = 400d;
        } else if (!game.isSlamAnnounced() && game.isSlamRealized()) {
            slamScore = 200d;
        } else if (game.isSlamAnnounced() && !game.isSlamRealized()) {
            // need to check if the game is won
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

    public static int getPlayerGameScore(PlayerBoard board, Game game, String playerName) {

        int playerPartyScore = getScoreSeed(game);
        boolean isTaker = game.isTaker(playerName);
        boolean isSecondTaker = game.isSecondTaker(playerName);
        if (!isTaker && !isSecondTaker) { // N'a pas pris, on inverse le score
            playerPartyScore *= -1;
        }

        // Cas particulier du score du preneur
        if (isTaker) {
            // Par défaut, 4 joueurs, coeff=3
            int takerCoeff = 3;

            if (board.isA3PlayersGame()) {
                // A 3 joueurs, le coeff n'est que de 2
                takerCoeff = 2;
            } else if (board.isA5PlayersGame()) {
                // A 5 joueurs, ca dépend de s'il est tout seul ou pas
                takerCoeff = game.isTakerAlone() ? 4 : 2;
            }
            playerPartyScore *= takerCoeff;
        }
        return playerPartyScore;

    }

}
