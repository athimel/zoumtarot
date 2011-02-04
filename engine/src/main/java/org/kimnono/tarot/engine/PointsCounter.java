package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PointsCounter {

    public static int getScoreSeed(Game game) {

        double score = game.score - game.getHolders().target;
        boolean gameWon = score >= 0.0;
        if (gameWon) {
            score = Math.round(score); // Arrondi toujours à l'absolu supérieur
            score += 25;
        } else {
            score = Math.round(score - 0.1); // Pour forcer l'arrondi à l'absolu supérieur
            score -= 25;
        }

        score *= game.getContract().value;

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
            // A 4 joueurs, coeff=3
            int takerCoeff = 3;

            if (board.isA5PlayersGame()) {
                // A 5 joueurs, ca dépend de s'il est tout seul ou pas
                takerCoeff = game.isTakerAlone() ? 4 : 2;
            }
            playerPartyScore *= takerCoeff;
        }
        return playerPartyScore;

    }

}
