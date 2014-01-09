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

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.zoumbox.tarot.engine.Deal;
import org.zoumbox.tarot.engine.PlayerBoard;
import org.zoumbox.tarot.engine.PointsCounter;

public class PartyBoardAdapter extends BaseAdapter {

    protected PlayerBoard board;
    protected int columnWidth;

    public PartyBoardAdapter(PlayerBoard board, int columnWidth) {
        super();
        this.board = board;
        this.columnWidth = columnWidth;
    }

    @Override
    public int getCount() {
        int result = 0;
        if (board != null) {
            result = board.getDeals().size();
        }
        return result;
    }

    @Override
    public Object getItem(int index) {
        Deal deal = board.getDeals().get(index);
        return deal;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        DealLineView line = (DealLineView) view;

        if (line == null) {
            // create the cell renderer
            line = new DealLineView(parent.getContext());
        }
        line.setData(board.getDeals().get(index));

        return line;
    }

    private class DealLineView extends LinearLayout {

        public DealLineView(Context context) {
            super(context);
            init();
        }

        private void init() {
            int nbPlayers = board.getPlayersCount();
            for (int i = 0; i < nbPlayers; i++) {
                TextView dealTV = (TextView) View.inflate(getContext(), R.layout.party_board_score, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, LayoutParams.WRAP_CONTENT);
                dealTV.setPadding(5, 0, 5, 0);
                addView(dealTV, params);
            }
        }

        public void setData(Deal deal) {
            for (int coll = 0; coll < board.getPlayersCount(); coll++) {
                TextView cell = (TextView) getChildAt(coll);
                String playerName = board.getPlayers().get(coll);

                int score = PointsCounter.getPlayerDealScore(board, deal, playerName);

                String contractMark = "";
                String secondTakerMark = "";
                String wonMark = "";

                if (deal.isTaker(playerName)) {
                    contractMark = deal.getContract().toShortString();
                    wonMark = deal.isWon() ? "\u2191" : "\u2193"; // up or down arrow
                }
                if (deal.isSecondTaker(playerName)) {
                    secondTakerMark = "\u002A"; // star
                }
                String suffix =
                        String.format(" %s%s%s", contractMark, secondTakerMark, wonMark);
                String text = String.format("%d%s", score, suffix);

                cell.setTextColor(Color.BLACK);
                cell.setText(text);
            }
        }
    }
}
