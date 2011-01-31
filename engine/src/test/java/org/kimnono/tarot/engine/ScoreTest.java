package org.kimnono.tarot.engine;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class ScoreTest extends TestCase {

    public void testSimpleParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien", "Florian");

        Game game = new Game();

        // Quel joueur a pris ?
        game.setTaker("Arno");

        // Quel contrat ?
        game.setContract(Contract.GARDE);

        // A 5 : Avec qui ? tout seul ou l'un des 4 autres noms

        // Quel contrat ?
        game.setHolders(Holders.TWO);

        // Résultat du joueur ?
        game.setScore(49.0);

        board.gameEnded(game);

        Assert.assertEquals(66, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int)board.getScores().get("Florian"));

    }

}
