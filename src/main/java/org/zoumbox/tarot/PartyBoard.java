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

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.zoumbox.tarot.engine.Deal;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.PlayerBoard;
import org.zoumbox.tarot.engine.PointsCounter;
import org.zoumbox.tarot.engine.Statistics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tableau des scores
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class PartyBoard extends TarotActivity {

    protected PlayerBoard board;
    protected int columnWidth;
    public static final String BOARD = "board";
    public static final String DEAL = "deal";
    public static final String DEAL_INDEX = "deal-index";
    public static final int NEW_DEAL = 0;
    public static final int EDIT_DEAL = 1;
    public static final int EDIT_PLAYERS = 2;
    protected static final int LEGEND_DIALOG = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        this.board = (PlayerBoard) getIntent().getSerializableExtra(BOARD);

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        columnWidth = Math.round((float) screenWidth / board.getPlayersCount());

        createPlayerNames();
        createTotals();
        updateList();

        ListView list = (ListView) findViewById(R.id.party_board_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartyBoard.this, AddDeal.class);
                ArrayList<String> players = new ArrayList<String>(board.getScores().keySet());
                intent.putExtra(AddDeal.PLAYERS, players);
                intent.putExtra(AddDeal.DEAL, board.getDeals().get(position));
                intent.putExtra(AddDeal.INDEX, position);
                startActivityForResult(intent, EDIT_DEAL);
            }
        });
    }

    protected void createPlayerNames() {
        LinearLayout playerNames = (LinearLayout) findViewById(R.id.party_board_player_names);
        int playersCount = board.getPlayersCount();

        for (int index = 0; index < playersCount; index++) {
            TextView playerNameTV = (TextView) View.inflate(this, R.layout.party_board_player_name, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, LayoutParams.WRAP_CONTENT);
            playerNames.addView(playerNameTV, params);

        }

        playerNames.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartyBoard.this, AddParty.class);
                intent.putExtra(AddParty.BOARD, board);
                startActivityForResult(intent, EDIT_PLAYERS);
            }
        });

        updatePlayers();
    }

    protected void createTotals() {
        LinearLayout totals = (LinearLayout) findViewById(R.id.party_board_totals);
        int playersCount = board.getPlayersCount();

        for (int index = 0; index < playersCount; index++) {
            TextView totalTV = (TextView) View.inflate(this, R.layout.party_board_total, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, LayoutParams.WRAP_CONTENT);
            totals.addView(totalTV, params);
        }
    }

    @Override
    protected void saveBoard(PlayerBoard board) {
        this.board = board;
        super.saveBoard(board);
    }

    protected void updatePlayers() {
        LinearLayout playerNames = (LinearLayout) findViewById(R.id.party_board_player_names);
        int playersCount = board.getPlayersCount();

        for (int index = 0; index < playersCount; index++) {
            String playerName = board.getPlayers().get(index);
            TextView playerNameTV = (TextView) playerNames.getChildAt(index);
            playerNameTV.setText(playerName);
        }
    }

    protected void updateList() {
        ListView list = (ListView) findViewById(R.id.party_board_list);
        PartyBoardAdapter adapter = new PartyBoardAdapter(board, columnWidth);
        list.setAdapter(adapter);

        updateTotals();
    }

    protected void updateTotals() {
        LinearLayout totals = (LinearLayout) findViewById(R.id.party_board_totals);
        int playersCount = board.getPlayersCount();
        Map<String, Integer> scores = board.getScores();

        for (int playerIndex = 0; playerIndex < playersCount; playerIndex++) {
            TextView totalTV = (TextView) totals.getChildAt(playerIndex);

            String playerName = board.getPlayers().get(playerIndex);
            int score = scores.get(playerName);
            String total = String.format("%d", score);
            totalTV.setText(total);

            int color;
            if (isMax(score)) {
                color = getResources().getColor(R.color.best_score);
            } else if (isMin(score)) {
                color = getResources().getColor(R.color.worst_score);
            } else {
                color = getResources().getColor(R.color.score);
            }
            totalTV.setTextColor(color);
        }
    }

    public void onNewDealButtonClicked(View target) {
        Intent intent = new Intent(this, AddDeal.class);
        ArrayList<String> players = new ArrayList<String>(this.board.getScores().keySet());
        intent.putExtra(AddDeal.PLAYERS, players);
        startActivityForResult(intent, NEW_DEAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_DEAL || requestCode == EDIT_DEAL) {
                Deal deal = (Deal) data.getSerializableExtra(DEAL);

                PlayerBoard board = this.board;
                if (requestCode == EDIT_DEAL) {
                    int replaceIndex = data.getIntExtra(DEAL_INDEX, -1);
                    List<Deal> deals = board.getDeals();
                    if (replaceIndex < 0 || replaceIndex >= deals.size()) {
                        showToast("WTF!?? index:" + replaceIndex + " size:" + deals.size());
                    } else {
                        board.replaceDeal(replaceIndex, deal);
                    }
                } else {
                    board.dealEnded(deal);
                }

                saveBoard(board);
                updateList();

                boolean is5PlayersGame = (board.getPlayers().size() == 5 && !deal.isTakerAlone());

                String who;
                if (is5PlayersGame) {
                    who = getString(R.string.deal_who_5players, deal.getTaker(), deal.getSecondTaker());
                } else {
                    who = getString(R.string.deal_who, deal.getTaker());
                }
                double target = deal.getOudlers().getTarget();
                double score = deal.getScore();
                double diff = Math.abs(target - score);

                String dealConclusion;
                if (diff == 0d) {
                    dealConclusion = getString(R.string.deal_just_done);
                } else {
                    dealConclusion = getString(R.string.deal_conclusion, deal.isWon() ? getString(R.string.deal_won) : getString(R.string.deal_lost), toString(diff));
                }
                String conclusion = getString(R.string.deal_end, who, toString(score), target, dealConclusion);

                // Announcements
                if (deal.isSlamAnnounced() && deal.isSlamRealized()) {
                    conclusion += "\n";
                    conclusion += getString(R.string.deal_slam_announced_and_realized);
                } else if (deal.isSlamAnnounced() && !deal.isSlamRealized()) {
                    conclusion += "\n";
                    conclusion += getString(R.string.deal_slam_announced);
                } else if (!deal.isSlamAnnounced() && deal.isSlamRealized()) {
                    conclusion += "\n";
                    conclusion += getString(R.string.deal_slam_realized);
                }
                if (!Handful.NONE.equals(deal.getHandful())) {
                    String handfulText = Tools.toPrettyPrint(deal.getHandful().toString());
                    conclusion += "\n";
                    conclusion += getString(R.string.deal_handful, handfulText);
                }
                if (deal.getOneIsLast() != 0) {
                    conclusion += "\n";
                    conclusion += getString(R.string.deal_one_is_last, Deal.ONE_IS_LAST_TAKER == deal.getOneIsLast() ? getString(R.string.deal_attack) : getString(R.string.deal_defense));
                }
                showToast(conclusion);

                String onePlayerFormat = getString(R.string.deal_score_one_player_format);
                String otherPlayersFormat = getString(R.string.deal_score_others_format);
                String taker = deal.getTaker();
                String scoreInformation = String.format(onePlayerFormat, taker, PointsCounter.getPlayerDealScore(board, deal, taker));
                if (is5PlayersGame && !deal.isTakerAlone()) {
                    String secondTaker = deal.getSecondTaker();
                    scoreInformation += "\n";
                    scoreInformation += String.format(onePlayerFormat, secondTaker, PointsCounter.getPlayerDealScore(board, deal, secondTaker));
                }
                scoreInformation += "\n";
                scoreInformation += String.format(otherPlayersFormat, PointsCounter.getScoreSeed(deal) * -1);

                showToast(scoreInformation);

                if (!board.isScoreCoherent()) {
                    showToast("ERROR: Score is not coherent !");
                }

            } else if (requestCode == EDIT_PLAYERS) {
                PlayerBoard playerBoard = (PlayerBoard) data.getSerializableExtra(BOARD);
                saveBoard(playerBoard);
                updatePlayers();
            }
        }
    }

    protected String toString(double number) {
        String result = String.format("%.1f", number);
        if (result.endsWith(".0") || result.endsWith(",0")) {
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.legend:
                showDialog(LEGEND_DIALOG);
                return true;

            case R.id.party_statistics:
                LinkedHashMap<String, Statistics> statistics = board.getStatistics();
                Intent intent = new Intent(this, PartyStatistics.class);
                intent.putExtra(PartyStatistics.STATISTICS, statistics);
                startActivity(intent);
                return true;

            case R.id.rules:
                Uri uri = Uri.parse("http://dev.zoumbox.org/maven-sites/zoumtarot/regles.html");
                Intent rulesView = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(rulesView);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == LEGEND_DIALOG) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.legend);
            dialog.setTitle(R.string.legend);
            Button btnExitInfo = (Button) dialog.findViewById(R.id.legend_close);
            btnExitInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return dialog;
        }
        return super.onCreateDialog(id);
    }

    protected boolean isMax(int score) {
        int max = Integer.MIN_VALUE;
        for (int current : board.getScores().values()) {
            max = Math.max(max, current);
        }
        boolean result = (max == score);
        return result;
    }

    protected boolean isMin(int score) {
        int min = Integer.MAX_VALUE;
        for (int current : board.getScores().values()) {
            min = Math.min(min, current);
        }
        boolean result = (min == score);
        return result;
    }
}
