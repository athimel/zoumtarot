package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.kimnono.tarot.engine.Contract;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.Holders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class AddGame extends Activity {

    public static final String PLAYERS = "players";

    Spinner taker;
    Spinner secondTaker;
    Spinner contract;
    RadioGroup holders;
    EditText score;
    Button saveButton;

    protected List<String> getPlayers() {
        return (ArrayList<String>) getIntent().getSerializableExtra(PLAYERS);
    }

    protected List<String> getContracts() {
        List<String> result = new ArrayList<String>(Contract.values().length);
        for (Contract contract : Contract.values()) {
            result.add(contract.toString());
        }
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);

        List<String> players = getPlayers();

        taker = (Spinner) findViewById(R.id.taker);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
        taker.setAdapter(adapter);
        secondTaker = (Spinner) findViewById(R.id.secondTaker);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
        secondTaker.setAdapter(adapter);

        contract = (Spinner) findViewById(R.id.contract);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getContracts());
        contract.setAdapter(adapter);

        holders = (RadioGroup) findViewById(R.id.holders);

        score = (EditText) findViewById(R.id.score);
        score.setInputType(InputType.TYPE_CLASS_NUMBER);
        score.setText("0");

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });

    }

    private void onSaveButtonClicked() {

        Game game = new Game();

        game.setTaker(taker.getSelectedItem().toString());

        game.setSecondTaker(secondTaker.getSelectedItem().toString());

        Contract gameContract = Contract.valueOf(contract.getSelectedItem().toString());
        game.setContract(gameContract);

        Holders gameHolders;
        switch (holders.getCheckedRadioButtonId()) {
            case R.id.c_b_0:
                gameHolders = Holders.NONE;
                break;
            case R.id.c_b_1:
            gameHolders = Holders.ONE;
                break;
            case R.id.c_b_2:
                gameHolders = Holders.TWO;
                break;
            default:
                gameHolders = Holders.THREE;
                break;
        }
        game.setHolders(gameHolders);

        double gameScore = Double.parseDouble(score.getText().toString());
        game.setScore(gameScore);

        Intent intent = new Intent();
        intent.putExtra(PartyBoard.GAME, game);
        setResult(RESULT_OK, intent);
        finish();

    }

}
