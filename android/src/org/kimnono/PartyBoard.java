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

    public static final String BOARD = "board";
    public static final String GAME = "game";

    public static final int code = 0;

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

    protected PlayerBoard getBoard() {

        PlayerBoard result = (PlayerBoard) getIntent().getSerializableExtra(BOARD);

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        ListView listView = resetList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                Intent intent = new Intent(PartyBoard.this, AddGame.class);
//                ArrayList<String> players = new ArrayList<String>(getBoard().getScores().keySet());
//                intent.putExtra(AddGame.PLAYERS, players);
//                int index = position - 1;
//                intent.putExtra(AddGame.GAME, getBoard().getGames().get(index));
//                intent.putExtra(AddGame.INDEX, index);
//                startActivityForResult(intent, code);
                Toast.makeText(getApplicationContext(), "Édition non supportée pour le moment",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private ListView resetList() {
        ListView list = (ListView) findViewById(R.id.party_board_list);

        PlayerBoard board = getBoard();

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
                ArrayList<String> players = new ArrayList<String>(getBoard().getScores().keySet());
                intent.putExtra(AddGame.PLAYERS, players);
                startActivityForResult(intent, code);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == code) {
            if (resultCode == RESULT_OK) {
                Game game = (Game) data.getSerializableExtra(GAME);
                getBoard().gameEnded(game);

                resetList();

                String message;
                if (getBoard().getPlayers().length == 5) {
                    message = "%s et %s totalisent %.1f points pour %.0f : %s";
                    message = String.format(message,
                            game.getTaker(), game.getSecondTaker(), game.getScore(), game.getHolders().getTarget(),
                            game.isWon() ? "gagné!" : "perdu :(");
                } else {
                    message = "%s totalise %.1f points pour %.0f : %s";
                    message = String.format(message,
                            game.getTaker(), game.getScore(), game.getHolders().getTarget(),
                            game.isWon() ? "gagné!" : "perdu :(");
                }
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
