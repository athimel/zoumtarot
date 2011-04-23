package org.zoumbox.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Game;
import org.zoumbox.tarot.engine.Oudlers;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartiesList extends TarotActivity {

    public static final int DISPLAY_BOARD = 0;

    protected List<PlayerBoard> loadDemoDatas() {
        List<PlayerBoard> result = new ArrayList<PlayerBoard>();

        PlayerBoard board = new PlayerBoard();
        board.newParty("Corentin", "Yannick", "Kévin", "Julien", "Florian");
        Game game = new Game();
        game.set5PlayersCase("Kévin", "Julien", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Kévin", "Yannick", Contract.GARDE, Oudlers.TWO, 31);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Corentin", "Julien", Contract.GARDE, Oudlers.THREE, 26);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Florian", Contract.GARDE_SANS, Oudlers.NONE, 46);
        board.gameEnded(game);
        game = new Game();
        game.set5PlayersCase("Yannick", "Corentin", Contract.GARDE_CONTRE, Oudlers.NONE, 46);

        result.add(board);

        board = new PlayerBoard();
        board.newParty("Jean", "Estelle", "Arno", "Matthieu");
        game = new Game();
        game.setNominalCase("Arno", Contract.GARDE_SANS, Oudlers.ONE, 61);
        board.gameEnded(game);
        game = new Game();
        game.setNominalCase("Jean", Contract.GARDE, Oudlers.TWO, 31);
        board.gameEnded(game);
        game = new Game();
        game.setNominalCase("Matthieu", Contract.GARDE, Oudlers.THREE, 26);
        board.gameEnded(game);

        result.add(board);

        return result;
    }

    protected static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    removeBoard(position);

                    Toast.makeText(getApplicationContext(), "Partie supprimée",
                            Toast.LENGTH_LONG).show();

                    resetList();
                    return true;
                } catch (Exception eee) {

                    eee.printStackTrace();

                    Toast.makeText(getApplicationContext(),
                            String.format("Unable to remove board: %s", eee.getMessage()),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.new_party:
//                Intent intent = new Intent(this, AddParty.class);
//                intent.putExtra(PartyBoard.BOARD_INDEX, getParties().size());
//                startActivityForResult(intent, DISPLAY_BOARD);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == DISPLAY_BOARD && resultCode == RESULT_CANCELED) {
            resetList();
        }
    }

}
