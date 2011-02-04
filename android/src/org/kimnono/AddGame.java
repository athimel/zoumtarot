package org.kimnono;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
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

        List<String> players = getPlayers();
        boolean is5playersGame = players.size() == 5;
        if (is5playersGame) {
            setContentView(R.layout.new_game_5players);
        } else {
            setContentView(R.layout.new_game);
        }

        taker = (Spinner) findViewById(R.id.taker);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
        taker.setAdapter(adapter);

        if (is5playersGame) {
            secondTaker = (Spinner) findViewById(R.id.secondTaker);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
            secondTaker.setAdapter(adapter);
        }

        contract = (Spinner) findViewById(R.id.contract);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getContracts());
        contract.setAdapter(adapter);

        holders = (RadioGroup) findViewById(R.id.holders);

        score = (EditText) findViewById(R.id.score);
        score.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final int index = getIntent().getIntExtra(INDEX, -1);

        if (index != -1) {
            Game game = (Game) getIntent().getSerializableExtra(GAME);

            int takerIndex = players.indexOf(game.getTaker());
            taker.setSelection(takerIndex);

            if (is5playersGame) {
                int secondTakerIndex = players.indexOf(game.getSecondTaker());
                secondTaker.setSelection(secondTakerIndex);
            }

            int contractIndex = getContracts().indexOf(game.getContract().toString());
            contract.setSelection(contractIndex);

            switch (game.getHolders()) {
                case NONE:
                    ((RadioButton) findViewById(R.id.c_b_0)).setChecked(true);
                    break;
                case ONE:
                    ((RadioButton) findViewById(R.id.c_b_1)).setChecked(true);
                    break;
                case TWO:
                    ((RadioButton) findViewById(R.id.c_b_2)).setChecked(true);
                    break;
                default:
                    ((RadioButton) findViewById(R.id.c_b_3)).setChecked(true);
                    break;
            }

            double gameScore = game.getScore();
            String text = String.format("%.1f", gameScore);
            if (gameScore == Math.round(gameScore)) { // Ends with '.0' or ',0'
                text = text.substring(0, text.length() - 2);
            }
            score.setText(text);
        }

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked(index);
            }
        });

    }

    private void onSaveButtonClicked(int index) {

        Game game = new Game();

        game.setTaker(taker.getSelectedItem().toString());

        List<String> players = getPlayers();
        boolean is5playersGame = players.size() == 5;
        if (is5playersGame) {
            game.setSecondTaker(secondTaker.getSelectedItem().toString());
        }

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

        String validationMessage = validate(game, players.size());

        if (validationMessage == null) {
            Intent intent = new Intent();
            intent.putExtra(PartyBoard.GAME, game);
            intent.putExtra(PartyBoard.INDEX, index);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), validationMessage,
                    Toast.LENGTH_LONG).show();
        }

    }

    protected String validate(Game game, int playerCount) {
        if (game.getTaker() == null) {
            return "Vous devez indiquer qui a pris";
        }
        if (game.getContract() == null) {
            return "Vous devez indiquer le contrat";
        }
        if (game.getHolders() == null) {
            return "Vous devez indiquer combien de bouts a le preneur";
        }
        double score = game.getScore();
        if (score < 0 || score > 91) {
            return "Le score doit être compris entre 0 et 91";
        }
        if (score != Math.round(score)) { // multiple de '0.0'
            if (playerCount == 5) {
                if ((score + 0.5) != Math.round(score + 0.5)) { // multiple de '0.5'
                    return "À 5 joueurs, le score doit être un multiple de 0.5";
                }
            } else {
                return "À 4 joueurs, le score est forcément un nombre entier";
            }
        }
        double minimalScore = 0.5 * playerCount;
        if ((score > 0.0 && score < minimalScore) || (score > (91.0 - minimalScore) && score < 91.0)) {
            return String.format("%.1f n'est pas un score possible à %d joueurs", score, playerCount);
        }

        return null; // nothing went wrong
    }

}
