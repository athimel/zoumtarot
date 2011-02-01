package org.kimnono.tarot.engine;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PlayerBoard implements Serializable {

    private static final long serialVersionUID = -2231617148598930878L;

    protected LinkedHashMap<String, Integer> scores = new LinkedHashMap<String, Integer>();
    protected LinkedList<Game> games = new LinkedList<Game>();

    public void newParty(Collection<String> players) {
        scores.clear();
        games.clear();
        int playersCount = 0;
        if (players != null) {
            playersCount = players.size();
        }
        if (playersCount == 0 || (playersCount != 4 && playersCount != 5)) {
            String message =
                    "Only 4 and 5 players are supported for the moment. You gave %d player names.";
            throw new UnsupportedOperationException(String.format(message, playersCount));
        }
        for (String player : players) {
            scores.put(player, 0);
        }
    }

    public void newParty(String... players) {
        scores.clear();
        games.clear();
        int playersCount = 0;
        if (players != null) {
            playersCount = players.length;
        }
        if (playersCount == 0 || (playersCount != 4 && playersCount != 5)) {
            String message =
                    "Only 4 and 5 players are supported for the moment. You gave %d player names.";
            throw new UnsupportedOperationException(String.format(message, playersCount));
        }
        for (String player : players) {
            scores.put(player, 0);
        }
    }

    public void gameEnded(Game game) {
        if (isGameComplete(game)) {
            games.add(game);

            int scoreSeed = getScoreSeed(game);
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                String playerName = entry.getKey();
                Integer playerScore = entry.getValue();

                int playerPartyScore = scoreSeed;
                boolean isTaker = game.isTaker(playerName);
                boolean isSecondTaker = game.isSecondTaker(playerName);
                if (!isTaker && !isSecondTaker) { // N'a pas pris, on inverse le score
                    playerPartyScore *= -1;
                }

                // Cas particulier du score du preneur
                if (isTaker) {
                    // A 4 joueurs, coeff=3
                    int takerCoeff = 3;

                    if (isA5PlayersGame()) {
                        // A 5 joueurs, ca dépend de s'il est tout seul ou pas
                        takerCoeff = game.isTakerAlone() ? 4 : 2;
                    }
                    playerPartyScore *= takerCoeff;
                }
                playerScore += playerPartyScore;
                entry.setValue(playerScore);
            }
        }
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public List<Game> getGames() {
        return games;
    }

    public boolean isGameComplete(Game game) {
        return true; // TODO Arno 28/01/2011
    }

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

    public boolean isScoreCoherent() {
        int total = 0;
        for (Integer score : scores.values()) {
            total += score;
        }
        boolean result = (total == 0);
        return result;
    }

    public boolean isA5PlayersGame() {
        boolean result = (scores.size() == 5);
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

}
