package org.kimnono;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.kimnono.tarot.engine.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

public class PartiesList extends Activity {

    protected List<PlayerBoard> getParties() {
        List<PlayerBoard> result = new ArrayList<PlayerBoard>();
        PlayerBoard board = new PlayerBoard();
        board.newParty("Arno", "Yannick", "Kevin", "Julien");
        result.add(board);

        board = new PlayerBoard();
        board.newParty("Jean", "Estelle", "Florian", "Éric");
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
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_party:
                Toast.makeText(getApplicationContext(), "Nouvelle partie !",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
