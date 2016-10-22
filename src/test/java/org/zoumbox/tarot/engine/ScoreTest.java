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

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class ScoreTest extends AbstractTarotTest {

    @Test
    public void test3PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien");

        Deal deal = new Deal();

        // Quel joueur a pris ?
        deal.setTaker("Arno");

        // Quel contrat ?
        deal.setContract(Contract.GARDE);

        // Quel contrat ?
        deal.setOudlers(Oudlers.TWO);

        // Résultat du joueur ?
        deal.setScore(49.0);

        board.dealEnded(deal);

        Assert.assertEquals(132, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int) board.getScores().get("Julien"));

        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void test4PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien", "Florian");

        Deal deal = new Deal();

        // Quel joueur a pris ?
        deal.setTaker("Arno");

        // Quel contrat ?
        deal.setContract(Contract.GARDE);

        // Quel contrat ?
        deal.setOudlers(Oudlers.TWO);

        // Résultat du joueur ?
        deal.setScore(49.0);

        board.dealEnded(deal);

        Assert.assertEquals(198, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int) board.getScores().get("Florian"));

        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void test4PlayersPartyScoreCumul() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien", "Florian");

        Deal deal = new Deal();

        // Quel joueur a pris ?
        deal.setTaker("Arno");

        // Quel contrat ?
        deal.setContract(Contract.GARDE);

        // Quel contrat ?
        deal.setOudlers(Oudlers.TWO);

        // Résultat du joueur ?
        deal.setScore(49.0);

        board.dealEnded(deal);

        Assert.assertEquals(198, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-66, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-66, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-66, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = getNominalDeal("Yannick", Contract.PRISE, Oudlers.ONE, 39);
        board.dealEnded(deal);

        Assert.assertEquals(235, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-177, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-29, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-29, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = getNominalDeal("Julien", Contract.GARDE_SANS, Oudlers.THREE, 37);
        board.dealEnded(deal);

        Assert.assertEquals(131, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-281, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(283, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-133, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = getNominalDeal("Florian", Contract.GARDE_CONTRE, Oudlers.NONE, 45);
        board.dealEnded(deal);

        Assert.assertEquals(347, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-65, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(499, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-781, (int) board.getScores().get("Florian"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void test4PlayersPartyScoreCumulBogus() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Jean", "Estelle", "Arno", "Matthieu");
        Deal deal = getNominalDeal("Arno", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.dealEnded(deal);
        Assert.assertTrue(board.isScoreCoherent());
        deal = getNominalDeal("Jean", Contract.GARDE, Oudlers.TWO, 31);
        board.dealEnded(deal);
        Assert.assertTrue(board.isScoreCoherent());
        deal = getNominalDeal("Matthieu", Contract.GARDE_CONTRE, Oudlers.THREE, 26);
        board.dealEnded(deal);
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void test5PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = get5PlayersDeal("Kevin", "Julien", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.dealEnded(deal);

        Assert.assertEquals(280, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-140, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Kevin", "Yannick", Contract.GARDE, Oudlers.TWO, 31);
        board.dealEnded(deal);

        Assert.assertEquals(140, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-70, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-210, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(210, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-70, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Corentin", "Julien", Contract.GARDE, Oudlers.THREE, 26);
        board.dealEnded(deal);

        Assert.assertEquals(210, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-210, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Yannick", "Florian", Contract.GARDE_SANS, Oudlers.NONE, 46);
        board.dealEnded(deal);

        Assert.assertEquals(350, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-420, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(280, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-70, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Yannick", "Corentin", Contract.GARDE_CONTRE, Oudlers.NONE, 46);
        board.dealEnded(deal);

        Assert.assertEquals(560, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(70, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-840, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(490, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-280, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }

    @Test
    public void test5PlayersPartyTakerAlone() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = get5PlayersDeal("Kevin", "Kevin", Contract.GARDE_SANS, Oudlers.ONE, 41);
        board.dealEnded(deal);

        Assert.assertEquals(-560, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(140, (int) board.getScores().get("Florian"));
        Assert.assertEquals(140, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(140, (int) board.getScores().get("Julien"));
        Assert.assertEquals(140, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Yannick", "Yannick", Contract.GARDE_CONTRE, Oudlers.THREE, 52);
        board.dealEnded(deal);

        Assert.assertEquals(-806, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-106, (int) board.getScores().get("Florian"));
        Assert.assertEquals(1124, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-106, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-106, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }

    @Test
    public void test5PlayersPartyRound() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 41.5);
        board.dealEnded(deal);

        Assert.assertEquals(52, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(26, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-26, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-26, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-26, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 40.5);
        board.dealEnded(deal);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());
    }

    @Test
    public void test6PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Sylvain", "Nelly", "Seb", "Marine", "Arno", "Charlotte");

        Deal deal = get6PlayersDeal("Arno", "Arno", "Charlotte", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.dealEnded(deal);

        Assert.assertEquals(560, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-140, (int) board.getScores().get("Sylvain"));
        Assert.assertEquals(-140, (int) board.getScores().get("Nelly"));
        Assert.assertEquals(-140, (int) board.getScores().get("Seb"));
        Assert.assertEquals(-140, (int) board.getScores().get("Marine"));
        Assert.assertEquals(0, (int) board.getScores().get("Charlotte"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get6PlayersDeal("Nelly", "Marine", "Sylvain", Contract.GARDE_SANS, Oudlers.ONE, 78);
        board.dealEnded(deal);

        Assert.assertEquals(352, (int) board.getScores().get("Arno"));
        Assert.assertEquals(-140, (int) board.getScores().get("Sylvain"));
        Assert.assertEquals(276, (int) board.getScores().get("Nelly"));
        Assert.assertEquals(-348, (int) board.getScores().get("Seb"));
        Assert.assertEquals(68, (int) board.getScores().get("Marine"));
        Assert.assertEquals(-208, (int) board.getScores().get("Charlotte"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testReplaceDeal() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 41.5);
        board.dealEnded(deal);

        Assert.assertEquals(52, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(26, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-26, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-26, (int) board.getScores().get("Julien"));
        Assert.assertEquals(-26, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 40.5);
        board.dealEnded(deal);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 40.5);
        board.replaceDeal(0, deal);


        Assert.assertEquals(-104, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(-52, (int) board.getScores().get("Florian"));
        Assert.assertEquals(52, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(52, (int) board.getScores().get("Julien"));
        Assert.assertEquals(52, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

        deal = get5PlayersDeal("Kevin", "Florian", Contract.PRISE, Oudlers.TWO, 41.5);
        board.replaceDeal(1, deal);

        Assert.assertEquals(0, (int) board.getScores().get("Kevin"));
        Assert.assertEquals(0, (int) board.getScores().get("Florian"));
        Assert.assertEquals(0, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(0, (int) board.getScores().get("Julien"));
        Assert.assertEquals(0, (int) board.getScores().get("Corentin"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testPlayersRenaming() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = get5PlayersDeal("Kevin", "Julien", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.dealEnded(deal);
        deal = get5PlayersDeal("Kevin", "Yannick", Contract.GARDE, Oudlers.TWO, 31);
        board.dealEnded(deal);
        deal = get5PlayersDeal("Corentin", "Julien", Contract.GARDE, Oudlers.THREE, 26);
        board.dealEnded(deal);
        deal = get5PlayersDeal("Yannick", "Florian", Contract.GARDE_SANS, Oudlers.NONE, 46);
        board.dealEnded(deal);
        deal = get5PlayersDeal("Yannick", "Corentin", Contract.GARDE_CONTRE, Oudlers.NONE, 46);
        board.dealEnded(deal);

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

    @Test
    public void testOfficialExample1() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien");

        Deal deal = getNominalDeal("Kévin", Contract.GARDE, Oudlers.TWO, 49);
        deal.setHandful(Handful.SIMPLE);
        deal.setOneIsLast(Deal.ONE_IS_LAST_TAKER);
        board.dealEnded(deal);

        Assert.assertEquals(318, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(-106, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-106, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-106, (int) board.getScores().get("Julien"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testOfficialExample2() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien");

        Deal deal = getNominalDeal("Kévin", Contract.GARDE_SANS, Oudlers.TWO, 45);
        deal.setOneIsLast(Deal.ONE_IS_LAST_DEFENSE);
        board.dealEnded(deal);

        Assert.assertEquals(228, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(-76, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-76, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-76, (int) board.getScores().get("Julien"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testOfficialExample3() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien");

        Deal deal = getNominalDeal("Kévin", Contract.PRISE, Oudlers.TWO, 34);
        deal.setHandful(Handful.SIMPLE);
        deal.setOneIsLast(Deal.ONE_IS_LAST_TAKER);
        board.dealEnded(deal);

        Assert.assertEquals(-126, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(42, (int) board.getScores().get("Florian"));
        Assert.assertEquals(42, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(42, (int) board.getScores().get("Julien"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testOfficialExample4() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien");

        Deal deal = getNominalDeal("Kévin", Contract.GARDE, Oudlers.TWO, 52);
        deal.setHandful(Handful.SIMPLE);
        board.dealEnded(deal);

        Assert.assertEquals(276, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(-92, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-92, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-92, (int) board.getScores().get("Julien"));
        Assert.assertTrue(board.isScoreCoherent());

    }

    @Test
    public void testOfficialExample5() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien");

        Deal deal = getNominalDeal("Kévin", Contract.GARDE, Oudlers.TWO, 87);
        deal.setHandful(Handful.SIMPLE);
        deal.setOneIsLast(Deal.ONE_IS_LAST_TAKER);
        deal.setSlamAnnounced(true);
        deal.setSlamRealized(true);
        board.dealEnded(deal);

        Assert.assertEquals(1746, (int) board.getScores().get("Kévin"));
        Assert.assertEquals(-582, (int) board.getScores().get("Florian"));
        Assert.assertEquals(-582, (int) board.getScores().get("Yannick"));
        Assert.assertEquals(-582, (int) board.getScores().get("Julien"));
        Assert.assertTrue(board.isScoreCoherent());

    }

}
