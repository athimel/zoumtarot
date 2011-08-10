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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.common.collect.Lists;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Game;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.Oudlers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class AddGame extends TarotActivity {

    public static final String PLAYERS = "players";
    public static final String GAME = "game"; //optional
    public static final String INDEX = "index"; //optional

    public static final int MAX_SCORE = 91;

    protected Spinner taker;
    protected Spinner secondTaker;
    protected Spinner contract;
    protected RadioGroup holders;
    protected EditText score;
    protected CheckBox isDefenseScore;
    protected Spinner handful;
    protected CheckBox oneIsLast;
    protected CheckBox forDefense;
    protected Button saveButton;

    protected List<String> getPlayers() {
        return (ArrayList<String>) getIntent().getSerializableExtra(PLAYERS);
    }

    protected List<String> getContracts() {
        List<String> result = new ArrayList<String>(Contract.values().length);
        for (Contract contract : Contract.values()) {
            result.add(Tools.toPrettyPrint(contract.toString()));
        }
        return result;
    }

    protected List<String> getHandfuls(boolean is5playersGame) {
        List<String> result = Lists.newArrayListWithCapacity(Handful.values().length);
        result.add(getString(R.string.handful_none));
        result.add(getString(R.string.handful_simple, is5playersGame ? 8 : 10));
        result.add(getString(R.string.handful_double, is5playersGame ? 10 : 13));
        result.add(getString(R.string.handful_triple, is5playersGame ? 13 : 15));
        return result;
    }
//
//    protected String formatFromId(int id, Object ... args) {
//        CharSequence text = getText(id);
//        String result = text.toString();
//        if (args != null) {
//            result = String.format(result, args);
//        }
//        return result;
//    }

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
        contract.setSelection(1); // Set default to 'Garde'

        holders = (RadioGroup) findViewById(R.id.oudlers);

        score = (EditText) findViewById(R.id.score);
        score.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        isDefenseScore = (CheckBox) findViewById(R.id.isDefenseScore);

        // Announcements
        handful = (Spinner) findViewById(R.id.handful);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getHandfuls(is5playersGame));
        handful.setAdapter(adapter);

        oneIsLast = (CheckBox) findViewById(R.id.oneIsLast);
        forDefense = (CheckBox) findViewById(R.id.forDefense);

        final int index = getIntent().getIntExtra(INDEX, -1);

        if (index != -1) {
            Game game = (Game) getIntent().getSerializableExtra(GAME);

            int takerIndex = players.indexOf(game.getTaker());
            taker.setSelection(takerIndex);

            if (is5playersGame) {
                int secondTakerIndex = players.indexOf(game.getSecondTaker());
                secondTaker.setSelection(secondTakerIndex);
            }

            int contractIndex = getContracts().indexOf(Tools.toPrettyPrint(game.getContract().toString()));
            contract.setSelection(contractIndex);

            switch (game.getOudlers()) {
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
            text = text.replace(',', '.');
            score.setText(text);

            int handfulIndex;
            switch (game.getHandful()) {
                case SIMPLE:
                    handfulIndex = 1;
                    break;
                case DOUBLE:
                    handfulIndex = 2;
                    break;
                case TRIPLE:
                    handfulIndex = 3;
                    break;
                case NONE:
                default:
                    handfulIndex = 0;
                    break;
            }
            handful.setSelection(handfulIndex);

            if (game.getOneIsLast() != 0) {
                oneIsLast.setChecked(true);
                if (Game.ONE_IS_LAST_DEFENSE == game.getOneIsLast()) {
                    forDefense.setChecked(true);
                }
            }
        }

        forDefense.setEnabled(oneIsLast.isChecked());
        oneIsLast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    forDefense.setEnabled(true);
                } else {
                    forDefense.setChecked(false);
                    forDefense.setEnabled(false);
                }
            }

        });

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

        Contract gameContract = getContractValue(contract);
        game.setContract(gameContract);

        Oudlers gameOudlers;
        switch (holders.getCheckedRadioButtonId()) {
            case R.id.c_b_0:
                gameOudlers = Oudlers.NONE;
                break;
            case R.id.c_b_1:
                gameOudlers = Oudlers.ONE;
                break;
            case R.id.c_b_2:
                gameOudlers = Oudlers.TWO;
                break;
            default:
                gameOudlers = Oudlers.THREE;
                break;
        }
        game.setOudlers(gameOudlers);

        String scoreText = score.getText().toString();
        double gameScore = -1.0;
        try {
            gameScore = Double.parseDouble(scoreText);
        } catch (NumberFormatException nfe) {
            // Not valid, validation will fail
        }
        if (isDefenseScore.isChecked()) {
            gameScore = MAX_SCORE - gameScore;
        }
        game.setScore(gameScore);

        Handful gameHandful = getHandfulValue(handful);
        game.setHandful(gameHandful);

        int gameOneIsLast = 0;
        if (oneIsLast.isChecked()) {
            gameOneIsLast = Game.ONE_IS_LAST_TAKER;
            if (forDefense.isChecked()) {
                gameOneIsLast = Game.ONE_IS_LAST_DEFENSE;
            }
        }
        game.setOneIsLast(gameOneIsLast);

        // Read is over, now validate
        String validationMessage = validate(game, players.size());

        if (validationMessage == null) {
            Intent intent = new Intent();
            intent.putExtra(PartyBoard.GAME, game);
            intent.putExtra(PartyBoard.GAME_INDEX, index);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showToast(validationMessage);
        }

    }

    protected Contract getContractValue(Spinner contract) {
        String contractText = contract.getSelectedItem().toString();
        Contract gameContract = Contract.PRISE;
        for (Contract contractValue : Contract.values()) {
            if (Tools.toPrettyPrint(contractValue.toString()).equals(contractText)) {
                gameContract = contractValue;
                break;
            }
        }
        return gameContract;
    }

    protected Handful getHandfulValue(Spinner handful) {
        String handfulText = handful.getSelectedItem().toString().toLowerCase();
        Handful gameHandful = Handful.NONE;
        for (Handful handfulValue : Handful.values()) {
            if (handfulText.startsWith(handfulValue.toString().toLowerCase())) {
                gameHandful = handfulValue;
                break;
            }
        }
        return gameHandful;
    }

    protected String validate(Game game, int playerCount) {
        if (game.getTaker() == null) {
            return getString(R.string.game_no_taker);
        }
        if (game.getContract() == null) {
            return getString(R.string.game_no_contract);
        }
        if (game.getOudlers() == null) {
            return getString(R.string.game_no_oudlers);
        }
        double score = game.getScore();
        if (score < 0 || score > 91) {
            return getString(R.string.game_score_range);
        }
        if (score != Math.round(score)) { // multiple de '0.0'
            if (playerCount == 5) {
                if ((score + 0.5) != Math.round(score + 0.5)) { // multiple de '0.5'
                    return getString(R.string.game_score_5players);
                }
            } else {
                return getString(R.string.game_score_4players);
            }
        }
        double minimalScore = 0.5 * playerCount;
        if ((score > 0.0 && score < minimalScore) || (score > (91.0 - minimalScore) && score < 91.0)) {
            return String.format("%.1f n'est pas un score possible à %d joueurs", score, playerCount); //FIXME AThimel 10/08/2011 #29 : Do for several params messages
        }

        return null; // nothing went wrong
    }

}
