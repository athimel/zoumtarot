package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.kimnono.tarot.engine.Contract;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.Holders;
import org.kimnono.tarot.engine.PlayerBoard;

import java.lang.reflect.Array;
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
                map.put(player, "" + copy.getScores().get(player));
            }
            result.add(map);

        }

        return result;
    }

    protected PlayerBoard getBoard() {

        PlayerBoard result = (PlayerBoard)getIntent().getSerializableExtra(BOARD);

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_board);

        resetList();
    }

    private void resetList() {
        ListView list = (ListView) findViewById(R.id.party_board_list);

        PlayerBoard board = getBoard();

        ArrayList<HashMap<String, String>> lines = getLines(board);

        SimpleAdapter adapter = new SimpleAdapter(this, lines, R.layout.party_line,
                board.getPlayers(),
                new int[]{R.id.player1, R.id.player2, R.id.player3, R.id.player4, R.id.player5});
        list.setAdapter(adapter);
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
                Game game = (Game)data.getSerializableExtra(GAME);
                getBoard().gameEnded(game);

                resetList();

                String message = "%s et %s totalisent %.1f points pour %.0f : %s";
                message = String.format(message,
                        game.getTaker(), game.getSecondTaker(), game.getScore(), game.getHolders().getTarget(),
                        game.isWon() ? "gagné!": "perdu :(");
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
