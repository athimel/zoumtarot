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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class AddParty extends TarotActivity {

    EditText player1;
    EditText player2;
    EditText player3;
    EditText player4;
    EditText player5;
    Button saveButton;

    public static final String BOARD = "board";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_party);

        int type = InputType.TYPE_CLASS_TEXT;  // 1st letter capital ?

        player1 = (EditText) findViewById(R.id.player1);
        player1.setInputType(type);
        player2 = (EditText) findViewById(R.id.player2);
        player2.setInputType(type);
        player3 = (EditText) findViewById(R.id.player3);
        player3.setInputType(type);
        player4 = (EditText) findViewById(R.id.player4);
        player4.setInputType(type);
        player5 = (EditText) findViewById(R.id.player5);
        player5.setInputType(type);

        // load players for edition
        PlayerBoard board =
                (PlayerBoard) getIntent().getSerializableExtra(BOARD);
        if (board != null) {
            String[] players = board.getPlayers();
            if (players.length > 0) {
                player1.setText(players[0]);
            }
            if (players.length > 1) {
                player2.setText(players[1]);
            }
            if (players.length > 2) {
                player3.setText(players[2]);
            }
            if (players.length > 3) {
                player4.setText(players[3]);
            } else {
                player4.setEnabled(false);
            }
            if (players.length > 4) {
                player5.setText(players[4]);
            } else {
                player5.setEnabled(false);
            }
        }

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });

    }

    protected boolean safeAddPlayer(List<String> players, TextView... views) {
        for (TextView view : views) {
            String playerName = view.getText().toString();
            if (playerName != null && !"".equals(playerName.trim())) {
                playerName = playerName.trim();
                if (players.contains(playerName)) {
                    return false;
                }
                players.add(playerName);
            }
        }
        return true;
    }

    private void onSaveButtonClicked() {

        List<String> players = new ArrayList<String>();
        boolean safeAddResult = safeAddPlayer(players, player1, player2, player3, player4, player5);


        if (!safeAddResult) {
            showToast("Vous ne pouvez indiquer 2 joueurs avec le même nom");
        } else if (players.size() < 3) {
            showToast("Seules les parties à 3, 4 et 5 joueurs sont supportées pour le moment");
        } else {

            PlayerBoard board =
                    (PlayerBoard) getIntent().getSerializableExtra(BOARD);
            String message;
            if (board != null) {
                try {
                    board.replacePlayers(players);

                    Intent intent = new Intent();
                    intent.putExtra(PartyBoard.BOARD, board);
                    setResult(RESULT_OK, intent);
                    finish();

                    message = "Noms des joueurs modifiés";
                } catch (UnsupportedOperationException uoe) {
                    message = String.format("Erreur: %s", uoe.getMessage());
                }

            } else {
                board = new PlayerBoard();
                board.newParty(players);

                Intent intent = new Intent(this, PartyBoard.class);
                intent.putExtra(PartyBoard.BOARD, board);
                int index = getIntent().getIntExtra(PartyBoard.BOARD_INDEX, -1);
                intent.putExtra(PartyBoard.BOARD_INDEX, index);
                startActivity(intent);

                message = "Partie créée";
            }

            showToast(message);
        }

    }

}
