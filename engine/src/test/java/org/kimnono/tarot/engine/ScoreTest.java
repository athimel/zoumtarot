package org.kimnono.tarot.engine;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class ScoreTest extends TestCase {

    public void test4PlayerParty() throws Exception {

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

        Assert.assertEquals(198, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int)board.getScores().get("Florian"));

        Assert.assertTrue(board.isGameCoherent());

    }

    public void test4PlayerPartyScoreCumul() throws Exception {

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

        Assert.assertEquals(198, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isGameCoherent());

        game = new Game();
        game.setNominalCase("Yannick", Contract.PRISE, Holders.ONE, 39);
        board.gameEnded(game);

        Assert.assertEquals(235, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-177, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(-29, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-29, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isGameCoherent());

        game = new Game();
        game.setNominalCase("Julien", Contract.GARDE_SANS, Holders.THREE, 37);
        board.gameEnded(game);

        Assert.assertEquals(131, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-281, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(283, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-133, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isGameCoherent());

        game = new Game();
        game.setNominalCase("Florian", Contract.GARDE_CONTRE, Holders.NONE, 45);
        board.gameEnded(game);

        Assert.assertEquals(347, (int)board.getScores().get("Arno"));
        Assert.assertEquals(-65, (int)board.getScores().get("Yannick"));
        Assert.assertEquals(499, (int)board.getScores().get("Julien"));
        Assert.assertEquals(-781, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isGameCoherent());

    }

}
