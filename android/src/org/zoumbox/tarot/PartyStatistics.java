/*
 * #%L
 * ZoumTarot :: android
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
package org.zoumbox.tarot;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Statistics;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Activité pour l'ajout et l'édition d'une donne.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class PartyStatistics extends TarotActivity {

    public static final String STATISTICS = "party_statistics";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Map<String, Statistics> statistics = getStatistics();

        setContentView(R.layout.statistics);

        TableLayout table = (TableLayout) findViewById(R.id.list_statistics);

        for (Map.Entry<String, Statistics> entry : statistics.entrySet()) {

            Statistics playerStats = entry.getValue();

            {
                String playerName = entry.getKey();
                addRow(table, Tools.bold(playerName));
            }

            {
                Double takerPercent = playerStats.getTakerPercent();
                String taker = "n/c";
                if (takerPercent != null) {
                    taker = String.format("%.0f%s (%.0f/%.0f)", takerPercent, "%", playerStats.getTakerCount(), playerStats.getDealCount());
                }

                addRow(table, "   Preneur : " + taker);
            }

            {
                Double successPercent = playerStats.getSuccessPercent();
                String success = "n/c";
                if (successPercent != null) {
                    success = String.format("%.0f%s (%.0f/%.0f)", successPercent, "%", playerStats.getSuccessCount(), playerStats.getTakerCount());
                }

                addRow(table, "   Réussite : " + success);
            }

            {
                Set<Contract> contracts = playerStats.getFavoriteContract();
                String favorite = "n/c";

                if (!contracts.isEmpty()) {
                    Iterable<String> contractsStrings = Iterables.transform(contracts, new Function<Contract, String>() {
                        @Override
                        public String apply(Contract contract) {
                            String contractString = contract.toString();
                            String pretty = Tools.toPrettyPrint(contractString);
                            return pretty;
                        }
                    });
                    favorite = Joiner.on(", ").join(contractsStrings);
                }

                addRow(table, "   Contrat préféré : " + favorite);
            }

            {
                addRow(table, "");
            }

        }
    }

    protected void addRow(TableLayout table, CharSequence text) {
        TableRow row = new TableRow(this);
        table.addView(row);

        TextView view = new TextView(this);
        view.setTextColor(Color.BLACK);
        view.setText(text);
        row.addView(view);

    }
    private Map<String, Statistics> getStatistics() {
        Serializable serializable = getIntent().getSerializableExtra(STATISTICS);
        Map<String, Statistics> result = (Map<String, Statistics>) serializable;
        return result;
    }

}
