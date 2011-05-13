package org.zoumbox.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartiesList extends TarotActivity {

    public static final int DISPLAY_BOARD = 0;

    protected SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    protected List<String> getPartiesAsString(List<PlayerBoard> boards) {
        List<String> result = new ArrayList<String>();
        if (boards != null) {
            for (PlayerBoard board : boards) {
                String message = "%s\n%s - %d tour(s)";
                String players = board.getScores().keySet().toString();
                players = players.substring(1, players.length() - 1);

                long creationDate = board.getCreationDate();
                String date = creationDate > 0 ? df.format(new Date(creationDate)) : "n/c";

                message = String.format(message, players, date, board.getGames().size());
                result.add(message);
            }
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

        resetList();
    }

    protected void resetList() {

        final List<PlayerBoard> parties = getParties();
        List<String> partiesAsString = getPartiesAsString(parties);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.party, partiesAsString));

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PartiesList.this, PartyBoard.class);
                PlayerBoard board = parties.get(position);
                intent.putExtra(PartyBoard.BOARD, board);
                intent.putExtra(PartyBoard.BOARD_INDEX, position);
                startActivityForResult(intent, DISPLAY_BOARD);
            }

        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview) {
            menu.setHeaderTitle(getResources().getString(R.string.app_name));
            menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.remove_one));
            menu.add(Menu.NONE, 1, 1, getResources().getString(R.string.remove_all));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        try {
            String message = getResources().getString(R.string.party_removed);
            if (menuItemIndex == 1) {
                int nbPartiesRemoved = clearBoards();
                if (nbPartiesRemoved > 1) {
                    String str = getResources().getString(R.string.parties_removed);
                    message = String.format(str, nbPartiesRemoved);
                }
            } else {
                removeBoard(info.position);
            }

            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_LONG).show();

            resetList();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    String.format("Unable to remove board: %s", ioe.getMessage()),
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private List<PlayerBoard> getParties() {
        List<PlayerBoard> result;
        try {
            result = loadBoards();
        } catch (FileNotFoundException e) {
            System.out.println("No party found from disk");
            result = new ArrayList<PlayerBoard>();
        }
        return result;
    }

    public void onNewPartyButtonClicked(View target) {
        Intent intent = new Intent(this, AddParty.class);
        intent.putExtra(PartyBoard.BOARD_INDEX, getParties().size());
        startActivityForResult(intent, DISPLAY_BOARD);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == DISPLAY_BOARD && resultCode == RESULT_CANCELED) {
            resetList();
        }
    }

}
