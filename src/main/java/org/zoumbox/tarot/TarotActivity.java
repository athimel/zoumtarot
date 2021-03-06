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
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.zoumbox.tarot.engine.PlayerBoard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activité parente, elle permet la sauvegarde/chargement des parties.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public abstract class TarotActivity extends Activity {

    public static final String BOARD_FILENAME_1_2_PREFIX = "org.zoumbox.tarot-1.2-party-";
    public static final String BOARD_FILENAME_1_2_SUFFIX = ".json";
    public static final String BOARD_FILENAME_1_2 = BOARD_FILENAME_1_2_PREFIX + "%d" + BOARD_FILENAME_1_2_SUFFIX;
    public static final Pattern BOARD_FILENAME_1_2_PATTERN = Pattern.compile(BOARD_FILENAME_1_2_PREFIX + "[0-9]*" + BOARD_FILENAME_1_2_SUFFIX);

    protected List<String> getSavedFileNames() {
        String[] files = fileList();
        List<String> filesList = Lists.newArrayList(files);

        List<String> result = Lists.newArrayList();
        result.addAll(Collections2.filter(filesList, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                Matcher matcher = BOARD_FILENAME_1_2_PATTERN.matcher(input);
                boolean result = matcher.matches();
                return result;
            }
        }));

        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String file1, String file2) {
                return file1.compareTo(file2);
            }
        });

        return result;
    }

    protected List<PlayerBoard> loadBoards() {

        List<String> filesList = getSavedFileNames();
        List<PlayerBoard> result = Lists.newArrayList();

        if (!filesList.isEmpty()) {
            for (String fileName : filesList) {
                try {
                    PlayerBoard board = loadBoardFromFile(fileName);
                    result.add(board);
                } catch (FileNotFoundException fnfe) {
                    Log.w(getClass().getSimpleName(), "Unable to load board: " + fileName, fnfe);
                }
            }
        }

        return result;
    }

    private PlayerBoard loadBoardFromFile(String fileName) throws FileNotFoundException {

        Log.i(getClass().getSimpleName(), "Loading board from file: " + fileName);

        FileInputStream is = null;
        PlayerBoard result = null;
        try {
            is = openFileInput(fileName);
            InputStreamReader reader = new InputStreamReader(is);
            result = new Gson().fromJson(reader, PlayerBoard.class);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Error while closing the board file", e);
                }
            }
        }

        return result;
    }

    private void saveBoardToFile(PlayerBoard board) throws IOException {

        String fileName = String.format(BOARD_FILENAME_1_2, board.getCreationDate());
        Log.i(getClass().getSimpleName(), "Saving board to file: " + fileName);

        FileOutputStream os = null;
        try {
            os = openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            String json = new Gson().toJson(board);
            os.write(json.getBytes());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Error while closing the board file", e);
                }
            }
        }

    }

    protected void saveBoard(PlayerBoard board) {

        try {
            saveBoardToFile(board);
        } catch (IOException ioe) {
            Log.w(getClass().getSimpleName(), "Unable to save board", ioe);
        }

    }

    protected void removeBoard(int index) throws IOException {

        List<String> fileNames = getSavedFileNames();
        String fileName = fileNames.get(index);

        Log.i(getClass().getSimpleName(), "Removing board: " + fileName);
        deleteFile(fileName);
    }

    protected int clearBoards() throws IOException {

        List<String> fileNames = getSavedFileNames();
        int result = fileNames.size();

        for (String fileName : fileNames) {
            deleteFile(fileName);
        }
        return result;
    }

    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), message, 5).show();
    }

}
