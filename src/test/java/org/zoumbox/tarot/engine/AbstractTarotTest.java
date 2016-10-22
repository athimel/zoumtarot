package org.zoumbox.tarot.engine;

/**
 * @author Arnaud Thimel : thimel@codelutin.com
 */
public abstract class AbstractTarotTest {

    public Deal getNominalDeal(
            String taker, Contract contract, Oudlers oudlers, double score) {
        Deal result = new Deal();
        result.setTaker(taker);
        result.setContract(contract);
        result.setOudlers(oudlers);
        result.setScore(score);
        return result;
    }

    public Deal get5PlayersDeal(String taker, String secondTaker, Contract contract, Oudlers oudlers, double score) {
        Deal result = new Deal();
        result.setTaker(taker);
        result.setSecondTaker(secondTaker);
        result.setContract(contract);
        result.setOudlers(oudlers);
        result.setScore(score);
        return result;
    }

    public Deal get6PlayersDeal(String taker, String secondTaker, String excludedPlayer, Contract contract,
                                Oudlers oudlers, double score) {
        Deal result = new Deal();
        result.setTaker(taker);
        result.setSecondTaker(secondTaker);
        result.setExcludedPlayer(excludedPlayer);
        result.setContract(contract);
        result.setOudlers(oudlers);
        result.setScore(score);
        return result;
    }

}
