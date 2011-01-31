package org.kimnono.tarot.engine;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class Game {

    protected String taker;

    protected String secondTaker;

    protected Contract contract;

    protected Holders holders;

    protected double score;

    public void setNominalCase(
            String taker, Contract contract, Holders holders, double score) {
        setTaker(taker);
        setContract(contract);
        setHolders(holders);
        setScore(score);
    }

    public void set5PlayersCase(
            String taker, String secondTaker, Contract contract,
            Holders holders, double score) {
        setTaker(taker);
        setSecondTaker(secondTaker);
        setContract(contract);
        setHolders(holders);
        setScore(score);
    }

    public String getTaker() {
        return taker;
    }

    public void setTaker(String taker) {
        this.taker = taker;
    }

    public String getSecondTaker() {
        return secondTaker;
    }

    public void setSecondTaker(String secondTaker) {
        this.secondTaker = secondTaker;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Holders getHolders() {
        return holders;
    }

    public void setHolders(Holders holders) {
        this.holders = holders;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isTaker(String player) {
        boolean result = taker.equals(player);
        return result;
    }

    public boolean isSecondTaker(String player) {
        boolean result = secondTaker != null && secondTaker.equals(player);
        return result;
    }

    public boolean isTakerAlone() {
        boolean result = (secondTaker == null || taker.equals(secondTaker));
        return result;
    }

}
