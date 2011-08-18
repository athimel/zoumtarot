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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.common.base.Joiner;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PartiesListAdapter extends BaseAdapter {

    private List<PlayerBoard> boards;
    private Activity context;

    public PartiesListAdapter(Activity context, List<PlayerBoard> boards) {
        // save the activity/context ref
        this.context = context;
        this.boards = boards;
    }

    @Override
    public int getCount() {
        int result = boards != null ? boards.size() : 0;
        return result;
    }

    @Override
    public Object getItem(int index) {
        PlayerBoard board = boards.get(index);
        return board;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View cellRenderer, ViewGroup parent) {
        PartyCellRenderedView cellRendererView;

        if (cellRenderer == null) {
            // create the cell renderer
            cellRendererView = new PartyCellRenderedView(this.context);
        } else {
            cellRendererView = (PartyCellRenderedView) cellRenderer;
        }

        // update the cell renderer, and handle selection state
        cellRendererView.display(index);

        return cellRendererView;
    }

    private class PartyCellRenderedView extends TableLayout {

        public static final String PATTERN = "dd MMMM yyyy à HH:mm";

        TextView playerNamesTV;
        TextView detailsTV;

        public PartyCellRenderedView(Context context) {
            super(context);
            init();
        }

        private void init() {

            {
                TableRow row = new TableRow(context);
                row.setPadding(10, 0, 0, 0);

                playerNamesTV = new TextView(context);

                row.addView(playerNamesTV);
                addView(row);
            }

            {
                TableRow row = new TableRow(context);
                row.setPadding(15, 0, 0, 0);

                detailsTV = new TextView(context);

                row.addView(detailsTV);
                addView(row);
            }

            playerNamesTV.setTextColor(Color.BLACK);
            detailsTV.setTextColor(Color.GRAY);

        }

        public void display(int index) {
            PlayerBoard board = (PlayerBoard) getItem(index);

            String names = Joiner.on(", ").join(board.getPlayers());
            playerNamesTV.setText(names);

            long creationDate = board.getCreationDate();
            DateFormat df = new SimpleDateFormat(PATTERN);
            String date = creationDate > 0 ? df.format(new Date(creationDate)) : "n/c";

            int count = board.getDeals().size();

            long duration = board.getDuration();
            String durationStr = "n/c";
            if (duration > 999) { // At least 1 second
                long seconds = duration / 1000;
                durationStr = String.format("%ds", seconds);

                if (seconds > 99) {
                    long minutes = seconds / 60;
                    durationStr = String.format("%dmin", minutes);

                    if (minutes > 60) {
                        long hours = minutes / 60;
                        minutes -= (hours * 60);
                        durationStr = String.format("%dh %dmin", hours, minutes);
                    }
                }

            }

            String format = "%s  -  %d tour  -  %s";
            if (count > 1) {
                format = "%s  -  %d tours  -  %s";
            }
            format += " ";
            String message = String.format(format, date, count, durationStr);

            SpannableString str = SpannableString.valueOf(message);
            str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, str.length(), 0);
            detailsTV.setText(str);
        }
    }

}
