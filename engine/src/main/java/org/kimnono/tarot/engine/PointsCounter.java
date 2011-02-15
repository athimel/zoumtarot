package org.kimnono.tarot.engine;

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
