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

    public Statistics(int dealCount) {
        this.dealCount = dealCount;
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
