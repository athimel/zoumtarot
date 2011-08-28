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
import android.widget.TableRow;
import com.google.common.collect.Lists;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Deal;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.Oudlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Activité pour l'ajout et l'édition d'une donne.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class AddDeal extends TarotActivity {

    public static final String PLAYERS = "players";
    public static final String DEAL = "deal"; //optional
    public static final String INDEX = "index"; //optional

    public static final double MAX_SCORE = 91d;
    public static final double MIN_SLAM_SCORE = 60d; // Cas d'une garde contre avec dans le chien : 4 rois + 21 + 1 et l'excuse dans les mains d'un défenseur. Soit 6*45+4 = 31 points

    protected Spinner taker;
    protected Spinner secondTaker;
    protected Spinner contract;
    protected RadioGroup holders;
    protected EditText score;
    protected CheckBox isDefenseScore;
    protected Spinner handful;
    protected CheckBox oneIsLast;
    protected CheckBox forDefense;
    protected CheckBox slamAnnounced;
    protected CheckBox slamRealized;

    protected List<String> getPlayers() {
        Serializable serializable = getIntent().getSerializableExtra(PLAYERS);
        List<String> result = (ArrayList<String>) serializable;
        return result;
    }

    protected List<String> getContracts() {
        List<String> result = Lists.newArrayListWithCapacity(Contract.values().length);
        for (Contract contract : Contract.values()) {
            String contractString = contract.toString();
            String pretty = Tools.toPrettyPrint(contractString);
            result.add(pretty);
        }
        return result;
    }

    protected List<String> getHandfuls(boolean is3playersGame, boolean is5playersGame) {
        List<String> result = Lists.newArrayListWithCapacity(Handful.values().length);
        result.add(getString(R.string.handful_none));
        int handfulSimple = 10;
        int handfulDouble = 13;
        int handfulTriple = 15;
        if (is5playersGame) {
            handfulSimple = 8;
            handfulDouble = 10;
            handfulTriple = 13;
        }
        if (is3playersGame) {
            handfulSimple = 13;
            handfulDouble = 15;
            handfulTriple = 18;
        }
        result.add(getString(R.string.handful_simple, handfulSimple));
        result.add(getString(R.string.handful_double, handfulDouble));
        result.add(getString(R.string.handful_triple, handfulTriple));
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        List<String> players = getPlayers();
        boolean is3playersGame = players.size() == 3;
        boolean is5playersGame = players.size() == 5;

        setContentView(R.layout.new_deal);

        taker = (Spinner) findViewById(R.id.taker);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
        taker.setAdapter(adapter);

        if (is5playersGame) {
            secondTaker = (Spinner) findViewById(R.id.secondTaker);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
            secondTaker.setAdapter(adapter);
        } else {
            TableRow secondTakerRow = (TableRow) findViewById(R.id.secondTakerRow);
            secondTakerRow.setVisibility(View.INVISIBLE);
            secondTakerRow.removeAllViews();
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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getHandfuls(is3playersGame, is5playersGame));
        handful.setAdapter(adapter);

        oneIsLast = (CheckBox) findViewById(R.id.oneIsLast);
        forDefense = (CheckBox) findViewById(R.id.forDefense);

        slamAnnounced = (CheckBox) findViewById(R.id.slamAnnounced);
        slamRealized = (CheckBox) findViewById(R.id.slamRealized);

        final int index = getIntent().getIntExtra(INDEX, -1);

        if (index != -1) {
            Deal deal = (Deal) getIntent().getSerializableExtra(DEAL);

            int takerIndex = players.indexOf(deal.getTaker());
            taker.setSelection(takerIndex);

            if (is5playersGame) {
                int secondTakerIndex = players.indexOf(deal.getSecondTaker());
                secondTaker.setSelection(secondTakerIndex);
            }

            int contractIndex = getContracts().indexOf(Tools.toPrettyPrint(deal.getContract().toString()));
            contract.setSelection(contractIndex);

            switch (deal.getOudlers()) {
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

            double dealScore = deal.getScore();
            String text = String.format("%.1f", dealScore);
            if (dealScore == Math.round(dealScore)) { // Ends with '.0' or ',0'
                text = text.substring(0, text.length() - 2);
            }
            text = text.replace(',', '.');
            score.setText(text);

            int handfulIndex;
            switch (deal.getHandful()) {
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

            if (deal.getOneIsLast() != 0) {
                oneIsLast.setChecked(true);
                if (Deal.ONE_IS_LAST_DEFENSE == deal.getOneIsLast()) {
                    forDefense.setChecked(true);
                }
            }

            slamAnnounced.setChecked(deal.isSlamAnnounced());
            slamRealized.setChecked(deal.isSlamRealized());
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

        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSaveButtonClicked(index);
            }
        });

    }

    private void onSaveButtonClicked(int index) {

        Deal deal = new Deal();

        deal.setTaker(taker.getSelectedItem().toString());

        List<String> players = getPlayers();
        boolean is5playersGame = players.size() == 5;
        if (is5playersGame) {
            deal.setSecondTaker(secondTaker.getSelectedItem().toString());
        }

        Contract dealContract = getContractValue(contract);
        deal.setContract(dealContract);

        Oudlers dealOudlers;
        switch (holders.getCheckedRadioButtonId()) {
            case R.id.c_b_0:
                dealOudlers = Oudlers.NONE;
                break;
            case R.id.c_b_1:
                dealOudlers = Oudlers.ONE;
                break;
            case R.id.c_b_2:
                dealOudlers = Oudlers.TWO;
                break;
            default:
                dealOudlers = Oudlers.THREE;
                break;
        }
        deal.setOudlers(dealOudlers);

        String scoreText = score.getText().toString();
        double dealScore = -1.0;
        try {
            dealScore = Double.parseDouble(scoreText);
        } catch (NumberFormatException nfe) {
            // Not valid, validation will fail
        }
        if (isDefenseScore.isChecked()) {
            dealScore = MAX_SCORE - dealScore;
        }
        deal.setScore(dealScore);

        Handful dealHandful = getHandfulValue(handful);
        deal.setHandful(dealHandful);

        int dealOneIsLast = 0;
        if (oneIsLast.isChecked()) {
            dealOneIsLast = Deal.ONE_IS_LAST_TAKER;
            if (forDefense.isChecked()) {
                dealOneIsLast = Deal.ONE_IS_LAST_DEFENSE;
            }
        }
        deal.setOneIsLast(dealOneIsLast);

        boolean dealSlamAnnounced = slamAnnounced.isChecked();
        deal.setSlamAnnounced(dealSlamAnnounced);
        boolean dealSlamRealized = slamRealized.isChecked();
        dealSlamRealized |= dealScore == MAX_SCORE;
        deal.setSlamRealized(dealSlamRealized);

        // Read is over, now validate
        String validationMessage = validate(deal, players.size());

        if (validationMessage == null) {
            Intent intent = new Intent();
            intent.putExtra(PartyBoard.DEAL, deal);
            intent.putExtra(PartyBoard.DEAL_INDEX, index);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showToast(validationMessage);
        }

    }

//    protected void checkSlamRealized() {
//
//        String scoreText = score.getText().toString();
//        try {
//            double dealScore = Double.parseDouble(scoreText);
//            if (isDefenseScore.isChecked()) {
//                dealScore = MAX_SCORE - dealScore;
//            }
//            if (dealScore == MAX_SCORE) {
//                slamRealized.setChecked(true);
//            }
//            if (dealScore == 0) {
//                slamRealized.setChecked(false);
//            }
//        } catch (NumberFormatException nfe) {
//            // Not valid, do not change anything
//        }
//    }

    protected Contract getContractValue(Spinner contract) {
        String contractText = contract.getSelectedItem().toString();
        Contract dealContract = Contract.PRISE;
        for (Contract contractValue : Contract.values()) {
            if (Tools.toPrettyPrint(contractValue.toString()).equals(contractText)) {
                dealContract = contractValue;
                break;
            }
        }
        return dealContract;
    }

    protected Handful getHandfulValue(Spinner handful) {
        String handfulText = handful.getSelectedItem().toString().toLowerCase();
        Handful dealHandful = Handful.NONE;
        for (Handful handfulValue : Handful.values()) {
            if (handfulText.startsWith(handfulValue.toString().toLowerCase())) {
                dealHandful = handfulValue;
                break;
            }
        }
        return dealHandful;
    }

    protected String validate(Deal deal, int playerCount) {
        if (deal.getTaker() == null) {
            return getString(R.string.deal_no_taker);
        }
        if (deal.getContract() == null) {
            return getString(R.string.deal_no_contract);
        }
        Oudlers oudlers = deal.getOudlers();
        if (oudlers == null) {
            return getString(R.string.deal_no_oudlers);
        }
        double score = deal.getScore();
        if (score < 0 || score > MAX_SCORE) {
            return getString(R.string.deal_score_range);
        }
        if (score != Math.round(score)) { // multiple de '0.0'
            if (playerCount == 5) {
                if ((score + 0.5) != Math.round(score + 0.5)) { // multiple de '0.5'
                    return getString(R.string.deal_score_5players);
                }
            } else {
                return getString(R.string.deal_score_4players);
            }
        }
        double minimalScore = 0.5 * playerCount;
        if ((score > 0.0 && score < minimalScore) || (score > (MAX_SCORE - minimalScore) && score < MAX_SCORE)) {
            return getString(R.string.deal_score_validity, score, playerCount);
        }

        if (score == MAX_SCORE && !Oudlers.THREE.equals(oudlers)) {
            return getString(R.string.deal_score_max_oudlers);
        }

        if (deal.isSlamRealized() && score < MIN_SLAM_SCORE) {
            return getString(R.string.deal_score_min_slam);
        }

        return null; // nothing went wrong
    }

}
