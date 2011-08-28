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

import java.io.Serializable;

/**
 * Représente une donne : ensemble des éléments qui constitue un tour.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class Deal implements Serializable {

    private static final long serialVersionUID = -3415746288521792447L;

    public static final int ONE_IS_LAST_TAKER = 1;
    public static final int ONE_IS_LAST_DEFENSE = -1;

    ////////////////////
    // Technical fields //
    ////////////////////

    protected long date = -1;

    ////////////////////
    // Nominal fields //
    ////////////////////

    protected String taker;

    protected String secondTaker;

    protected Contract contract;

    protected Oudlers oudlers;

    protected double score;

    ////////////////////
    // Announcements  //
    ////////////////////

    protected Handful handful = Handful.NONE;

    protected boolean slamRealized = false;

    protected boolean slamAnnounced = false;

    protected int oneIsLast = 0; // 1 means for the takers ; -1 means for the defense

    public void setNominalCase(
            String taker, Contract contract, Oudlers oudlers, double score) {
        setTaker(taker);
        setContract(contract);
        setOudlers(oudlers);
        setScore(score);
    }

    public void set5PlayersCase(
            String taker, String secondTaker, Contract contract,
            Oudlers oudlers, double score) {
        setTaker(taker);
        setSecondTaker(secondTaker);
        setContract(contract);
        setOudlers(oudlers);
        setScore(score);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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

    public Oudlers getOudlers() {
        return oudlers;
    }

    public void setOudlers(Oudlers oudlers) {
        this.oudlers = oudlers;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Handful getHandful() {
        return handful;
    }

    public void setHandful(Handful handful) {
        this.handful = handful;
    }

    public boolean isSlamRealized() {
        return slamRealized;
    }

    public void setSlamRealized(boolean slamRealized) {
        this.slamRealized = slamRealized;
    }

    public boolean isSlamAnnounced() {
        return slamAnnounced;
    }

    public void setSlamAnnounced(boolean slamAnnounced) {
        this.slamAnnounced = slamAnnounced;
    }

    public int getOneIsLast() {
        return oneIsLast;
    }

    public void setOneIsLast(int oneIsLast) {
        this.oneIsLast = oneIsLast;
    }

    public boolean isWon() {
        boolean result = score >= oudlers.target;
        return result;
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

    public void initDate() {
        if (date == -1) { // init only once
            date = System.currentTimeMillis();
        }
    }

}
