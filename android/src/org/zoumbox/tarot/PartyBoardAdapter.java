package org.zoumbox.tarot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.common.collect.Lists;
import org.zoumbox.tarot.engine.Game;
import org.zoumbox.tarot.engine.PlayerBoard;
import org.zoumbox.tarot.engine.PointsCounter;

import java.util.List;

public class PartyBoardAdapter extends BaseAdapter {

    private PlayerBoard board;
    private Activity context;

    public PartyBoardAdapter(Activity context, PlayerBoard board) {
        // save the activity/context ref
        this.context = context;
        this.board = board;
    }

    @Override
    public int getCount() {
        int result = 1; // player names
        result += board != null ? board.getGames().size() : 0;
        result += 1; // total
        return result;
    }

    @Override
    public Object getItem(int index) {
        Game game = board.getGames().get(index);
        return game;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View cellRenderer, ViewGroup parent) {
        GameCellRenderedView cellRendererView;

        if (cellRenderer == null) {
            // create the cell renderer
            Log.i(getClass().getSimpleName(), "creating a GameCellRenderedView object");
            cellRendererView = new GameCellRenderedView(this.context, board.getPlayersCount());
        } else {
            cellRendererView = (GameCellRenderedView) cellRenderer;
        }

        // update the cell renderer, and handle selection state
        cellRendererView.display(index);

        return cellRendererView;
    }

    private class GameCellRenderedView extends TableLayout {

        List<TextView> cells;

        public GameCellRenderedView(Context context, int nbPlayers) {
            super(context);
            init(nbPlayers);
        }

        private void init(int nbPlayers) {

            cells = Lists.newArrayListWithCapacity(nbPlayers);

            TableRow row = new TableRow(context);

            int totalWidth = context.getWindowManager().getDefaultDisplay().getWidth();
            for (int i = 0; i < nbPlayers; i++) {

                TextView cell = new TextView(context);
                cell.setWidth(totalWidth / nbPlayers);
                cell.setPadding(5, 0, 5, 0);
                cell.setGravity(Gravity.CENTER);

                cells.add(cell);
                row.addView(cell);
            }
            addView(row);

        }

        public void display(int index) {

            boolean isTotal = (index > board.getGames().size());
            int playersCount = board.getPlayersCount();

            if (index == 0) {
                for (int coll = 0; coll < playersCount; coll++) {
                    TextView cell = cells.get(coll);
                    cell.setTextColor(Color.BLACK);
                    String playerName = board.getPlayers()[coll];

                    SpannableString str = SpannableString.valueOf(playerName);
                    str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);

                    cell.setText(str);
                }

            } else if (isTotal) {
                for (int coll = 0; coll < playersCount; coll++) {
                    TextView cell = cells.get(coll);
                    String playerName = board.getPlayers()[coll];
                    int score = board.getScores().get(playerName);

                    String message = String.format("%d", score);

                    int style = Typeface.ITALIC;
                    if (isMax(score)) {
                        cell.setTextColor(Color.parseColor("#009400"));
                        style = Typeface.BOLD_ITALIC;
                    } else if (isMin(score)) {
                        cell.setTextColor(Color.RED);
                        style = Typeface.BOLD_ITALIC;
                    } else {
                        cell.setTextColor(Color.BLACK);
                    }

                    SpannableString str = SpannableString.valueOf(message);
                    str.setSpan(new StyleSpan(style), 0, str.length(), 0);

                    cell.setText(str);
                }
            } else {
                Game game = board.getGames().get(index - 1);
                for (int coll = 0; coll < playersCount; coll++) {
                    TextView cell = cells.get(coll);
                    String playerName = board.getPlayers()[coll];

                    int score = PointsCounter.getPlayerGameScore(board, game, playerName);

                    String contractMark = "";
                    String secondTakerMark = "";
                    String wonMark = "";

                    if (game.isTaker(playerName)) {
                        contractMark = game.getContract().toShortString();
                        wonMark = game.isWon() ? "\u2191" : "\u2193"; // up or down arrow
                    }
                    if (game.isSecondTaker(playerName)) {
                        secondTakerMark = "\u002A"; // star
                    }
                    String suffix =
                            String.format(" %s%s%s", contractMark, secondTakerMark, wonMark);
                    String text = String.format("%d%s", score, suffix);

                    cell.setTextColor(Color.parseColor("#868686"));
                    cell.setText(text);
                }

            }
        }

        private boolean isMax(int score) {
            int max = Integer.MIN_VALUE;
            for (int current : board.getScores().values()) {
                max = Math.max(max, current);
            }
            boolean result = (max == score);
            return result;
        }

        private boolean isMin(int score) {
            int min = Integer.MAX_VALUE;
            for (int current : board.getScores().values()) {
                min = Math.min(min, current);
            }
            boolean result = (min == score);
            return result;
        }
    }

}
