package org.kimnono.tarot.engine;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PlayerBoard {

    protected Map<String, Integer> scores = new LinkedHashMap<String, Integer>();
    protected List<Game> games = new LinkedList<Game>();

    public void newParty(String ... players) {
        scores.clear();
        games.clear();
        if (players == null || players.length != 4) {
            throw new UnsupportedOperationException(
                    "Only 4 players are supported for the moment");
        }
        for (String player : players) {
            scores.put(player, 0);
        }
    }

    public void gameEnded(Game game) {
        if (isGameComplete(game)) {
            games.add(game);

            int scoreSeed = getScoreSeed(game);
            for (Map.Entry<String, Integer> entry: scores.entrySet()) {
                String playerName = entry.getKey();
                Integer playerScore = entry.getValue();

                int playerPartyScore = scoreSeed;
                if (!game.isTaker(playerName)) { // N'a pas pris, on inverse le score
                    playerPartyScore *= -1;
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
        if (score >= 0.0) {
            score += 25;
        } else {
            score -= 25;
        }
        score *= game.getContract().value;

        Long scoreRounded = Math.round(score);
        int result = scoreRounded.intValue();
        return result;
    }

}
