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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.zoumbox.tarot.engine.Game;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.PlayerBoard;
import org.zoumbox.tarot.engine.PointsCounter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PartyBoard extends TarotActivity {

    protected PlayerBoard board;

    public static final String BOARD = "board";
    public static final String BOARD_INDEX = "board-index"; // optional
    public static final String GAME = "game";
    public static final String GAME_INDEX = "game-index";

    public static final int NEW_GAME = 0;
    public static final int EDIT_GAME = 1;
    public static final int EDIT_PLAYERS = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        ListView listView = resetList((PlayerBoard) getIntent().getSerializableExtra(BOARD));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                PlayerBoard board = PartyBoard.this.board;

                boolean isTotal = (position > board.getGames().size());

                if (position == 0) {
                    Intent intent = new Intent(PartyBoard.this, AddParty.class);
                    intent.putExtra(AddParty.BOARD, board);
                    startActivityForResult(intent, EDIT_PLAYERS);
                } else if (isTotal) {
                    // Nothing to do
                } else {
                    Intent intent = new Intent(PartyBoard.this, AddGame.class);
                    ArrayList<String> players = new ArrayList<String>(board.getScores().keySet());
                    intent.putExtra(AddGame.PLAYERS, players);
                    int index = position - 1;
                    intent.putExtra(AddGame.GAME, board.getGames().get(index));
                    intent.putExtra(AddGame.INDEX, index);
                    startActivityForResult(intent, EDIT_GAME);
                }
            }
        });
    }

    private ListView resetList() {
        return resetList(this.board);
    }

    private ListView resetList(PlayerBoard board) {
        this.board = board;
        ListView list = (ListView) findViewById(R.id.party_board_list);

        PartyBoardAdapter adapter = new PartyBoardAdapter(this, board);
        list.setAdapter(adapter);

        try {
            int index = getIntent().getIntExtra(BOARD_INDEX, -1);
            saveBoard(index, board);
        } catch (Exception eee) {

            eee.printStackTrace();

            showToast(String.format("Unable to save board: %s", eee.getMessage()));

        }

        return list;
    }

    public void onNewGameButtonClicked(View target) {
        Intent intent = new Intent(this, AddGame.class);
        ArrayList<String> players = new ArrayList<String>(this.board.getScores().keySet());
        intent.putExtra(AddGame.PLAYERS, players);
        startActivityForResult(intent, NEW_GAME);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_GAME || requestCode == EDIT_GAME) {
                Game game = (Game) data.getSerializableExtra(GAME);

                PlayerBoard board = this.board;
                if (requestCode == EDIT_GAME) {
                    int replaceIndex = data.getIntExtra(GAME_INDEX, -1);
                    List<Game> games = board.getGames();
                    if (replaceIndex < 0 || replaceIndex >= games.size()) {
                        showToast("WTF!?? index:" + replaceIndex + " size:" + games.size());
                    } else {
                        board.replaceGame(replaceIndex, game);
                    }
                } else {
                    board.gameEnded(game);
                }

                resetList();

                boolean is5PlayersGame = (board.getPlayers().length == 5 && !game.isTakerAlone());

                String who;
                if (is5PlayersGame) {
                    who = getString(R.string.game_who_5players, game.getTaker(), game.getSecondTaker());
                } else {
                    who = getString(R.string.game_who, game.getTaker());
                }
                double target = game.getOudlers().getTarget();
                double score = game.getScore();
                double diff = Math.abs(target - score);

                String gameConclusion;
                if (diff == 0d) {
                    gameConclusion = getString(R.string.game_just_done);
                } else {
                    gameConclusion = getString(R.string.game_conclusion, game.isWon() ? getString(R.string.game_won) : getString(R.string.game_lost), toString(diff));
                }
                String message = getString(R.string.game_end, who, toString(score), target, gameConclusion);

                // Announcements
                if (!Handful.NONE.equals(game.getHandful())) {
                    String handfulText = Tools.toPrettyPrint(game.getHandful().toString());
                    message += "\n" + getString(R.string.game_handful, handfulText);
                }
                if (game.getOneIsLast() != 0) {
                    message += "\n" + getString(R.string.game_one_is_last, Game.ONE_IS_LAST_TAKER == game.getOneIsLast() ? getString(R.string.game_attack) : getString(R.string.game_defense));
                }

                showToast(message);

                String onePlayerFormat = getString(R.string.game_score_one_player_format);
                String otherPlayersFormat = getString(R.string.game_score_others_format);
                String taker = game.getTaker();
                String text = String.format(onePlayerFormat, taker, PointsCounter.getPlayerGameScore(board, game, taker));
                if (is5PlayersGame && !game.isTakerAlone()) {
                    String secondTaker = game.getSecondTaker();
                    text += "\n";
                    text += String.format(onePlayerFormat, secondTaker, PointsCounter.getPlayerGameScore(board, game, secondTaker));
                }
                text += "\n";
                text += String.format(otherPlayersFormat, PointsCounter.getScoreSeed(game) * -1);

                showToast(text);

                if (!board.isScoreCoherent()) {
                    showToast("ERROR: Score is not coherent !");
                }
            } else if (requestCode == EDIT_PLAYERS) {
                resetList((PlayerBoard) data.getSerializableExtra(BOARD));
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
            case R.id.party_statistics:
                // TODO AThimel 16/05/11 Implement credits page - cf http://dev.zoumbox.org/issues/49
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
