package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import junit.framework.Assert;
import org.kimnono.tarot.engine.Contract;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.Holders;
import org.kimnono.tarot.engine.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

public class PartiesList extends Activity {

    protected List<PlayerBoard> getParties() {
        List<PlayerBoard> result = new ArrayList<PlayerBoard>();


        PlayerBoard board = new PlayerBoard();
        board.newParty("Corentin", "Yannick", "Kévin", "Julien", "Florian");
        Game game = new Game();
        game.set5PlayersCase("Kévin", "Julien", Contract.GARDE_SANS, Holders.ONE, 61);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Kévin", "Yannick", Contract.GARDE, Holders.TWO, 31);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Corentin", "Julien", Contract.GARDE, Holders.THREE, 26);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Florian", Contract.GARDE_SANS, Holders.NONE, 46);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Corentin", Contract.GARDE_CONTRE, Holders.NONE, 46);

        result.add(board);

        board = new PlayerBoard();
        board.newParty("Jean", "Estelle", "Arno", "Éric", "Matthieu");
        game = new Game();
        game.set5PlayersCase("Arno", "Estelle", Contract.GARDE_SANS, Holders.ONE, 61);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Éric", "Jean", Contract.GARDE, Holders.TWO, 31);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Matthieu", "Matthieu", Contract.GARDE, Holders.THREE, 26);
        board.gameEnded(game);

        result.add(board);

        return result;
    }

    protected List<String> getPartiesAsString(List<PlayerBoard> boards) {
        List<String> result = new ArrayList<String>(boards.size());
        for (PlayerBoard board : boards) {
            String message = "%s - %d tour(s)";
            String players = board.getScores().keySet().toString();
            players = players.substring(1, players.length() - 1);
            message = String.format(message, players, board.getGames().size());
            result.add(message);
        }
        return result;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        List<PlayerBoard> parties = getParties();
        List<String> partiesAsString = getPartiesAsString(parties);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.party, partiesAsString));

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PartiesList.this, PartyBoard.class);
                PlayerBoard board = getParties().get(position);
                intent.putExtra(PartyBoard.BOARD, board);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_party:
                Intent i = new Intent(this, AddParty.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
