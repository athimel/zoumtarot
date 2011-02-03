package org.kimnono.tarot.engine;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class ScoreTest extends TestCase {

    public void test4PlayersParty() throws Exception {

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

        Assert.assertEquals(198, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int) board.getScores().get("Florian"));

        Assert.assertTrue(board.isScoreCoherent());

    }

    public void test4PlayersPartyScoreCumul() throws Exception {

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

        Assert.assertEquals(198, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.setNominalCase("Yannick", Contract.PRISE, Holders.ONE, 39);
        board.gameEnded(game);

        Assert.assertEquals(235, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-177, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-29, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-29, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.setNominalCase("Julien", Contract.GARDE_SANS, Holders.THREE, 37);
        board.gameEnded(game);

        Assert.assertEquals(131, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-281, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(283, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-133, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.setNominalCase("Florian", Contract.GARDE_CONTRE, Holders.NONE, 45);
        board.gameEnded(game);

        Assert.assertEquals(347, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-65, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(499, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-781, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    public void test4PlayersPartyScoreCumulBogus() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Jean", "Estelle", "Arno", "Matthieu");
        Game game = new Game();
        game.setNominalCase("Arno", Contract.GARDE_SANS, Holders.ONE, 61);
        board.gameEnded(game);
        Assert.assertTrue(board.isScoreCoherent());
        game = new Game();
        game.setNominalCase("Jean", Contract.GARDE, Holders.TWO, 31);
        board.gameEnded(game);
        Assert.assertTrue(board.isScoreCoherent());
        game = new Game();
        game.setNominalCase("Matthieu", Contract.GARDE_CONTRE, Holders.THREE, 26);
        board.gameEnded(game);
        Assert.assertTrue(board.isScoreCoherent());

    }

    public void test5PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Game game = new Game();
        game.set5PlayersCase("Kevin", "Julien", Contract.GARDE_SANS, Holders.ONE, 61);
        board.gameEnded(game);

        Assert.assertEquals(280, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-140, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Kevin", "Yannick", Contract.GARDE, Holders.TWO, 31);
        board.gameEnded(game);

        Assert.assertEquals(140, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-70, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-210, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(210, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-70, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Corentin", "Julien", Contract.GARDE, Holders.THREE, 26);
        board.gameEnded(game);

        Assert.assertEquals(210, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-210, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Yannick", "Florian", Contract.GARDE_SANS, Holders.NONE, 46);
        board.gameEnded(game);

        Assert.assertEquals(350, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-420, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(280, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-70, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Yannick", "Corentin", Contract.GARDE_CONTRE, Holders.NONE, 46);
        board.gameEnded(game);

        Assert.assertEquals(560, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(70, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-840, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(490, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-280, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }

    public void test5PlayersPartyTakerAlone() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Game game = new Game();
        game.set5PlayersCase("Kevin", "Kevin", Contract.GARDE_SANS, Holders.ONE, 41);
        board.gameEnded(game);

        Assert.assertEquals(-560, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(140, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Yannick", null, Contract.GARDE_CONTRE, Holders.THREE, 52);
        board.gameEnded(game);

        Assert.assertEquals(-806, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-106, (int) board.getScores().get("Florian"));
        Assert.assertEquals(1124, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-106, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-106, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }

    public void test5PlayersPartyRound() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Game game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 41.5);
        board.gameEnded(game);

        Assert.assertEquals(52, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(26, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-26, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-26, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-26, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 40.5);
        board.gameEnded(game);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }


    public void testReplaceGame() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Game game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 41.5);
        board.gameEnded(game);

        Assert.assertEquals(52, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(26, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-26, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-26, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-26, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 40.5);
        board.gameEnded(game);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 40.5);
        board.replaceGame(0, game);


        Assert.assertEquals(-104, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-52, (int) board.getScores().get("Florian"));
        Assert.assertEquals(52, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(52, (int) board.getScores().get("Julien"));
        Assert.assertEquals(52, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        game = new Game();
        game.set5PlayersCase("Kevin", "Florian", Contract.PRISE, Holders.TWO, 41.5);
        board.replaceGame(1, game);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

    }


    public void testPlayersRenaming() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Game game = new Game();
        game.set5PlayersCase("Kevin", "Julien", Contract.GARDE_SANS, Holders.ONE, 61);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Kevin", "Yannick", Contract.GARDE, Holders.TWO, 31);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Corentin", "Julien", Contract.GARDE, Holders.THREE, 26);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Florian", Contract.GARDE_SANS, Holders.NONE, 46);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Corentin", Contract.GARDE_CONTRE, Holders.NONE, 46);
        board.gameEnded(game);

        Assert.assertEquals(560, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(70, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-840, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(490, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-280, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        board.replacePlayers(Arrays.asList("Kévin", "Éric", "Toto", "Corentin", "Arno"));

        Assert.assertEquals(560, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(70, (int) board.getScores().get("Éric"));
        Assert.assertEquals(-840, (int) board.getScores().get("Toto"));
        Assert.assertEquals(490, (int) board.getScores().get("Corentin"));
        Assert.assertEquals(-280, (int) board.getScores().get("Arno"));
        Assert.assertTrue(board.isScoreCoherent());

    }

}
