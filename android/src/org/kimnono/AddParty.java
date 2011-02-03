package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.kimnono.tarot.engine.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class AddParty extends Activity {

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

        player1 = (EditText) findViewById(R.id.player1);
        player2 = (EditText) findViewById(R.id.player2);
        player3 = (EditText) findViewById(R.id.player3);
        player4 = (EditText) findViewById(R.id.player4);
        player5 = (EditText) findViewById(R.id.player5);

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
            }
            if (players.length > 4) {
                player5.setText(players[4]);
            }
        }

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });

    }

    protected void safeAddPlayer(List<String> players, TextView... views) {
        for (TextView view : views) {
            String playerName = view.getText().toString();
            if (playerName != null && !"".equals(playerName.trim())) {
                players.add(playerName);
            }
        }
    }

    private void onSaveButtonClicked() {

        List<String> players = new ArrayList<String>();
        safeAddPlayer(players, player1, player2, player3, player4, player5);

        if (players.size() < 4) {
            Toast.makeText(getApplicationContext(),
                    "Seules les parties à 4 et 5 joueurs sont supportées pour le moment",
                    Toast.LENGTH_LONG).show();
        } else {

            PlayerBoard board =
                    (PlayerBoard) getIntent().getSerializableExtra(BOARD);
            String message;
            if (board != null) {
                board.replacePlayers(players);

                message = "Noms des joueurs modifiés";
            } else {
                board = new PlayerBoard();
                board.newParty(players);

                message = "Partie créée";
            }

            Intent intent = new Intent(this, PartyBoard.class);
            intent.putExtra(PartyBoard.BOARD, board);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
        }

    }

}
