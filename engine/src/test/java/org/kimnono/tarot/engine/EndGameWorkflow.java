package org.kimnono.tarot.engine;

import org.kimnono.tarot.engine.Contract;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.Holders;
import org.kimnono.tarot.engine.PlayerBoard;

/**
 * @author Arnaud Thimel <arno@kimnono.org>
 */
public class EndGameWorkflow {

    public static void main(String[] args) {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien", "Florian");

        Game game = new Game();

        System.out.println("Quel joueur a pris ?");
        game.setTaker("Arno");

        System.out.println("Quel contrat ?");
        game.setContract(Contract.GARDE);

        // A 5 : Avec qui ? tout seul ou l'un des 4 autres noms

        System.out.println("Quel contrat ?");
        game.setHolders(Holders.TWO);

        System.out.println("Résultat du joueur ?"); // Prévoir la saisie de l'équipe adverse
        game.setScore(49.0);

        board.gameEnded(game);
        System.out.println(board.getScores());
    }

}
