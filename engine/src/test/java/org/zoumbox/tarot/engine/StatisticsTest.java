package org.zoumbox.tarot.engine;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class StatisticsTest {

    @Test
    public void test3PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien");

        Deal deal = new Deal();
        deal.setNominalCase("Yannick", Contract.GARDE, Oudlers.TWO, 42);
        board.dealEnded(deal);

        Map<String, Statistics> statistics = board.getStatistics();
        Statistics yannick = statistics.get("Yannick");
        Assert.assertEquals(100d, yannick.getTakerPercent());
        Assert.assertEquals(100d, yannick.getSuccessPercent());
        Assert.assertEquals(Contract.GARDE, yannick.getFavoriteContract().iterator().next());

    }


    @Test
    public void test4PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Julien", "Florian");

        Deal deal = new Deal();
        deal.setNominalCase("Arno", Contract.GARDE, Oudlers.TWO, 49.0);
        board.dealEnded(deal);

        deal = new Deal();
        deal.setNominalCase("Arno", Contract.PRISE, Oudlers.TWO, 49.0);
        board.dealEnded(deal);

        deal = new Deal();
        deal.setNominalCase("Julien", Contract.GARDE_CONTRE, Oudlers.TWO, 49.0);
        board.dealEnded(deal);


        deal = new Deal();
        deal.setNominalCase("Julien", Contract.GARDE_CONTRE, Oudlers.TWO, 40.0);
        board.dealEnded(deal);


        deal = new Deal();
        deal.setNominalCase("Julien", Contract.GARDE, Oudlers.TWO, 49.0);
        board.dealEnded(deal);

        deal = new Deal();
        deal.setNominalCase("Yannick", Contract.GARDE, Oudlers.TWO, 42);
        board.dealEnded(deal);

        Map<String, Statistics> statistics = board.getStatistics();
        Statistics yannick = statistics.get("Yannick");
        Assert.assertEquals(16.67d, yannick.getTakerPercent(), 0.0049d);
        Assert.assertEquals(100d, yannick.getSuccessPercent(), 0.0049d);
        Assert.assertEquals(Contract.GARDE, yannick.getFavoriteContract().iterator().next());

        Statistics arno = statistics.get("Arno");
        Assert.assertEquals(33.33d, arno.getTakerPercent(), 0.0049d);
        Assert.assertEquals(100d, arno.getSuccessPercent(), 0.0049d);
        Assert.assertTrue(arno.getFavoriteContract().contains(Contract.GARDE));
        Assert.assertTrue(arno.getFavoriteContract().contains(Contract.PRISE));

        Statistics julien = statistics.get("Julien");
        Assert.assertEquals(50d, julien.getTakerPercent(), 0.0049d);
        Assert.assertEquals(66.67d, julien.getSuccessPercent(), 0.0049d);
        Assert.assertEquals(Contract.GARDE_CONTRE, julien.getFavoriteContract().iterator().next());

        Statistics florian = statistics.get("Florian");
        Assert.assertEquals(0d, florian.getTakerPercent(), 0.0049d);
        Assert.assertNull(florian.getSuccessPercent());
        Assert.assertTrue(florian.getFavoriteContract().isEmpty());

    }

    @Test
    public void test5PlayersParty() throws Exception {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kevin", "Florian", "Yannick", "Julien", "Corentin");

        Deal deal = new Deal();
        deal.set5PlayersCase("Kevin", "Julien", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.dealEnded(deal);

        deal = new Deal();
        deal.set5PlayersCase("Kevin", "Yannick", Contract.GARDE, Oudlers.TWO, 31);
        board.dealEnded(deal);

        deal = new Deal();
        deal.set5PlayersCase("Corentin", "Julien", Contract.GARDE, Oudlers.THREE, 26);
        board.dealEnded(deal);

        deal = new Deal();
        deal.set5PlayersCase("Yannick", "Florian", Contract.GARDE_SANS, Oudlers.NONE, 46);
        board.dealEnded(deal);

        deal = new Deal();
        deal.set5PlayersCase("Yannick", "Corentin", Contract.GARDE_CONTRE, Oudlers.NONE, 46);
        board.dealEnded(deal);

        Map<String, Statistics> statistics = board.getStatistics();
        Statistics yannick = statistics.get("Yannick");
        Assert.assertEquals(40d, yannick.getTakerPercent(), 0.0049d);
        Assert.assertEquals(0d, yannick.getSuccessPercent(), 0.0049d);
        Assert.assertTrue(yannick.getFavoriteContract().contains(Contract.GARDE_CONTRE));
        Assert.assertTrue(yannick.getFavoriteContract().contains(Contract.GARDE_SANS));

        Statistics florian = statistics.get("Florian");
        Assert.assertEquals(0d, florian.getTakerPercent(), 0.0049d);
        Assert.assertNull(florian.getSuccessPercent());
        Assert.assertTrue(florian.getFavoriteContract().isEmpty());

        Statistics julien = statistics.get("Julien");
        Assert.assertEquals(0d, julien.getTakerPercent(), 0.0049d);
        Assert.assertNull(julien.getSuccessPercent());
        Assert.assertTrue(julien.getFavoriteContract().isEmpty());

        Statistics corentin = statistics.get("Corentin");
        Assert.assertEquals(20d, corentin.getTakerPercent(), 0.0049d);
        Assert.assertEquals(0d, corentin.getSuccessPercent(), 0.0049d);
        Assert.assertEquals(Contract.GARDE, corentin.getFavoriteContract().iterator().next());

        Statistics kevin = statistics.get("Kevin");
        Assert.assertEquals(40d, kevin.getTakerPercent(), 0.0049d);
        Assert.assertEquals(50d, kevin.getSuccessPercent(), 0.0049d);
        Assert.assertTrue(kevin.getFavoriteContract().contains(Contract.GARDE));
        Assert.assertTrue(kevin.getFavoriteContract().contains(Contract.GARDE_SANS));
    }

}
