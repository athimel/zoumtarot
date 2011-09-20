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

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class Statistics implements Serializable {

    private static final long serialVersionUID = -5137349116741438713L;

    protected Double takerPercent;
    protected Double successPercent;
    protected Set<Contract> favoriteContract;

    protected double dealCount = 0d;

    protected double takerCount = 0d;
    protected double successCount = 0d;
    protected Multiset<Contract> contracts = HashMultiset.create();

    public void addDealCount(int dealCount) {
        this.dealCount += dealCount;
    }

    public void newPartyTaken(Contract contract, boolean won) {
        takerCount++;
        if (won) {
            successCount++;
        }
        contracts.add(contract);
    }

    public Double getTakerPercent() {
        if (takerPercent == null && dealCount != 0d) {
            takerPercent = 100d * takerCount / dealCount;
        }
        return takerPercent;
    }

    public Double getSuccessPercent() {
        if (successPercent == null && takerCount != 0d) {
            successPercent = 100d * successCount / takerCount;
        }
        return successPercent;
    }

    public Set<Contract> getFavoriteContract() {
        if (favoriteContract == null) {
            favoriteContract = Sets.newLinkedHashSet();
            int maxOccurrence = 0;
            for (Multiset.Entry<Contract> entry : contracts.entrySet()) {
                int count = entry.getCount();
                if (count > maxOccurrence) {
                    favoriteContract.clear();
                    maxOccurrence = count;
                }
                if (count == maxOccurrence) {
                    Contract contract = entry.getElement();
                    favoriteContract.add(contract);
                }
            }
        }
        return favoriteContract;
    }

    public double getDealCount() {
        return dealCount;
    }

    public double getTakerCount() {
        return takerCount;
    }

    public double getSuccessCount() {
        return successCount;
    }
}
