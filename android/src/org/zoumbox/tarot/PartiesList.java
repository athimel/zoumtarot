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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.io.IOException;
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
            String newParty = getString(R.string.new_party);
            String message = getString(R.string.parties_new, newParty);
            showToast(message);
        }
    }

    protected int resetList() {

        final List<PlayerBoard> boards = loadBoards();

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
                startActivityForResult(intent, DISPLAY_BOARD);
            }

        });

        registerForContextMenu(listView);

        int result = boards != null ? boards.size() : 0;
        return result;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview) {
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

    public void onNewPartyButtonClicked(View target) {
        Intent intent = new Intent(this, AddParty.class);
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
                LayoutInflater inflater = (LayoutInflater)
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.credits, null, false);
                final PopupWindow credits = new PopupWindow(
                        popupView,
                        this.getWindowManager().getDefaultDisplay().getWidth() - 75,
                        this.getWindowManager().getDefaultDisplay().getHeight() - 100,
                        true);
                // The code below assumes that the root container has an id called 'main'
                credits.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);

                Button btnExitInfo = (Button) popupView.findViewById(R.id.credits_close);
                btnExitInfo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        credits.dismiss();
                    }
                });
                return true;
            case R.id.general_statistics:
                // TODO AThimel 16/05/11 Implement credits page - cf http://dev.zoumbox.org/issues/55
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
