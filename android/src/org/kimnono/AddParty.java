package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_party);

        player1 = (EditText) findViewById(R.id.player1);
        player2 = (EditText) findViewById(R.id.player2);
        player3 = (EditText) findViewById(R.id.player3);
        player4 = (EditText) findViewById(R.id.player4);
        player5 = (EditText) findViewById(R.id.player5);

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });

    }

    private void onSaveButtonClicked() {

        List<String> players = new ArrayList<String>();
        players.add(player1.getText().toString());
        players.add(player2.getText().toString());
        players.add(player3.getText().toString());
        players.add(player4.getText().toString());
        String player5Name = player5.getText().toString();
        if (player5Name != null && !"".equals(player5Name.trim())) {
            players.add(player5Name);
        }

        PlayerBoard board = new PlayerBoard();
        board.newParty(players);

        Intent intent = new Intent(this, PartyBoard.class);
        intent.putExtra(PartyBoard.BOARD, board);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "Partie créée",
                Toast.LENGTH_SHORT).show();

    }

}
