package org.zoumbox.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartiesList extends TarotActivity {

    public static final int DISPLAY_BOARD = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int partiesCount = resetList();
        if (partiesCount == 0) {
            String newParty = getResources().getString(R.string.new_party);
            String message = String.format(
                    "Pour créer une partie, appuyez sur '%s'", newParty);
            showToast(message);
        }
    }

    protected int resetList() {

        final List<PlayerBoard> boards = getParties();

        ListView listView = (ListView) findViewById(R.id.listview);

        PartiesListAdapter listAdapter = new PartiesListAdapter(this, boards);
        listView.setAdapter(listAdapter);

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PartiesList.this, PartyBoard.class);
                PlayerBoard board = boards.get(position);
                intent.putExtra(PartyBoard.BOARD, board);
                intent.putExtra(PartyBoard.BOARD_INDEX, position);
                startActivityForResult(intent, DISPLAY_BOARD);
            }

        });

        registerForContextMenu(listView);

        int result = boards != null ? boards.size() : 0;
        return result;
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

            showToast(message);

            resetList();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showToast(String.format("Unable to remove board: %s", ioe.getMessage()));
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
            case R.id.qr_code_market:
                Intent intent_market = new Intent(this, QRCodeMarket.class);
                startActivity(intent_market);
                return true;
            case R.id.qr_code_version:
                Intent intent_version = new Intent(this, QRCodeVersion.class);
                startActivity(intent_version);
                return true;
            case R.id.credits:
                // TODO AThimel 16/05/11 Implement credits page
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
