package org.zoumbox.tarot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
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
        Log.i(getClass().getSimpleName(), "getItem(" + index + ") = " + board);
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
            Log.i(getClass().getSimpleName(), "creating a PartyCellRenderedView object");
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
                row.setPadding(10, 0, 10, 0);

                playerNamesTV = new TextView(context);

                row.addView(playerNamesTV);
                addView(row);
            }

            {
                TableRow row = new TableRow(context);
                row.setPadding(20, 0, 20, 0);

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

            int count = board.getGames().size();

            String format = "%s   -   %d tour";
            if (count > 1) {
                format += "s";
            }
            format += " ";
            String message = String.format(format, date, count);

            SpannableString str = SpannableString.valueOf(message);
            str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, str.length(), 0);
            detailsTV.setText(str);
        }
    }

}
