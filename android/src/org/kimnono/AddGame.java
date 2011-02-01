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
import org.kimnono.tarot.engine.Contract;
import org.kimnono.tarot.engine.Game;
import org.kimnono.tarot.engine.Holders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class AddGame extends Activity {

    public static final String PLAYERS = "players";
    public static final String GAME = "game"; //optional
    public static final String INDEX = "index"; //optional

    protected Spinner taker;
    protected Spinner secondTaker;
    protected Spinner contract;
    protected RadioGroup holders;
    protected EditText score;
    protected Button saveButton;

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
        score.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final int index = getIntent().getIntExtra(INDEX, -1);

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked(index);
            }
        });

    }

    private void onSaveButtonClicked(int index) {

        // TODO AThimel 01/02/2011 Manage edit mode and index

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
