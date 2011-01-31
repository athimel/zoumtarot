package org.kimnono;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.kimnono.tarot.engine.PlayerBoard;

import java.awt.font.LayoutPath;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PartyBoard extends Activity {

    protected TableRow getHeadRow(PlayerBoard board) {
        TableRow row = new TableRow(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (String playerName : board.getScores().keySet()) {
            TextView textView = new TextView(this);
            textView.setText(playerName);
            LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(textView);

            row.addView(textView);
        }
        return row;
    }

    protected TableRow getScoreLine(int ... scores) {
        TableRow row = new TableRow(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.gravity = Gravity.LEFT;
        for (int score : scores) {
            TextView textView = new TextView(this);
            textView.setText("" + score);
            row.addView(textView, params);
        }
        return row;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        PlayerBoard board = new PlayerBoard();
        board.newParty("Kévin", "Florian", "Yannick", "Julien", "Corentin");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        GridView grid = (GridView)findViewById(R.id.gridview);
        grid.setAdapter(new );

        TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        params.width = TableRow.LayoutParams.FILL_PARENT;
        TableRow header = getHeadRow(board);
        table.addView(header, params);

//        View separator = new View(this);
//        params = new TableLayout.LayoutParams();
//        params.weight = 2;
//        separator.setBackgroundColor(123);
//        table.addView(separator, params);

        table.addView(getScoreLine(180, -90, -90, 90, -90), params);
        table.addView(getScoreLine(80, -40, -140, 140, -40), params);
        table.addView(getScoreLine(130, 10, -90, 90, -140), params);
        table.addView(getScoreLine(220, -80, -270, 180, -50), params);
        table.addView(getScoreLine(390, 90, -610, 350, -220), params);

    }

}
