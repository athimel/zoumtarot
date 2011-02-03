package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.PlayerBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class PartyBoard extends Activity {

    protected PlayerBoard board;

    public static final String BOARD = "board";
    public static final String GAME = "game";
    public static final String INDEX = "index";

    public static final int NEW_GAME = 0;
    public static final int EDIT_GAME = 1;
    public static final int EDIT_PLAYERS = 2;

    /**
     * From a PlayerBoard, generate the rows (including the header)
     *
     * @param board the board containing data
     * @return a list of HashMap with player names and stats
     */
    protected ArrayList<HashMap<String, String>> getLines(PlayerBoard board) {

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();
        List<String> players = new ArrayList<String>();

        for (String player : board.getScores().keySet()) {
            map.put(player, player);
            players.add(player);
        }
        result.add(map);

        PlayerBoard copy = board.cloneForNewParty();

        for (Game game : board.getGames()) {

            copy.gameEnded(game);

            map = new HashMap<String, String>();
            for (String player : players) {
                String format = "%d%s";
                Integer score = copy.getScores().get(player);
                String contractMark = "";
                String secondTakerMark = "";
                String wonMark = "";
                if (game.isTaker(player)) {
                    contractMark = game.getContract().toShortString();
                    wonMark = game.isWon() ? "\u2191" : "\u2193"; // up or down arrow
                }
                if (game.isSecondTaker(player)) {
                    secondTakerMark = "\u002A"; // star
                }
                String suffix =
                        String.format(" %s%s%s", contractMark, secondTakerMark, wonMark);
                map.put(player, String.format(format, score, suffix));
            }
            result.add(map);

        }

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        ListView listView = resetList((PlayerBoard) getIntent().getSerializableExtra(BOARD));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                PlayerBoard board = PartyBoard.this.board;
                if (position == 0) {
                    Intent intent = new Intent(PartyBoard.this, AddParty.class);
                    intent.putExtra(AddParty.BOARD, board);
                    startActivityForResult(intent, EDIT_PLAYERS);
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

        ArrayList<HashMap<String, String>> lines = getLines(board);

        String[] players = board.getPlayers();
        SimpleAdapter adapter;
        if (players.length == 5) {
            adapter = new SimpleAdapter(this, lines, R.layout.party_line_5players,
                    players,
                    new int[]{R.id.player1, R.id.player2, R.id.player3, R.id.player4, R.id.player5});
        } else {
            adapter = new SimpleAdapter(this, lines, R.layout.party_line,
                    players,
                    new int[]{R.id.player1, R.id.player2, R.id.player3, R.id.player4});
        }
        list.setAdapter(adapter);

        return list;
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
            case R.id.new_game:
                Intent intent = new Intent(this, AddGame.class);
                ArrayList<String> players = new ArrayList<String>(this.board.getScores().keySet());
                intent.putExtra(AddGame.PLAYERS, players);
                startActivityForResult(intent, NEW_GAME);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_GAME || requestCode == EDIT_GAME) {
                Game game = (Game) data.getSerializableExtra(GAME);

                PlayerBoard board = this.board;
                if (requestCode == EDIT_GAME) {
                    int replaceIndex = data.getIntExtra(INDEX, -1);
                    List<Game> games = board.getGames();
                    if (replaceIndex < 0 || replaceIndex >= games.size()) {
                        Toast.makeText(getApplicationContext(), "WTF!?? index:" + replaceIndex + " size:" + games.size(),
                                Toast.LENGTH_LONG).show();
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
                    who = String.format("%s et %s totalisent", game.getTaker(), game.getSecondTaker());
                } else {
                    who = String.format("%s totalise", game.getTaker());
                }
                double target = game.getHolders().getTarget();
                double score = game.getScore();
                double diff = Math.abs(target - score);

                String message = String.format("%s %s points pour %.0f : tour %s de %s points.",
                        who, toString(score), target,
                        game.isWon() ? "gagné" : "perdu", toString(diff));

                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();

                if (!board.isScoreCoherent()) {
                    Toast.makeText(getApplicationContext(), "ERROR: Score is not coherent !",
                            Toast.LENGTH_LONG).show();
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

}
