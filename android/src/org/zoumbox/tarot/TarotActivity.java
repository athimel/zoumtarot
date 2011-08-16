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
import android.widget.Toast;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Deal;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.Oudlers;
import org.zoumbox.tarot.engine.PlayerBoard;
import org.zoumbox.tarot.legacy.PlayerBoard11;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public abstract class TarotActivity extends Activity {

    public static final String BOARDS_FILENAME_1_1 = "org.zoumbox.tarot-1.1-parties.xml";
    public static final String BOARDS_FILENAME_1_2 = "org.zoumbox.tarot-1.2-parties.xml";

    public class SingleValueEnumConverter extends AbstractSingleValueConverter {
        private final Class enumType;

        public SingleValueEnumConverter(Class type) {
            this.enumType = type;
        }

        public boolean canConvert(Class c) {
            return c.equals(enumType);
        }

        public Object fromString(String value) {
            return Enum.valueOf(enumType, value);
        }
    }

    protected XStream getXStream() {
        XStream result = new XStream();
        result.alias("board", PlayerBoard.class);
        result.alias("deal", Deal.class);
        result.alias("contract", Contract.class);
        result.alias("oudlers", Oudlers.class);
        result.alias("handful", Handful.class);
        result.registerConverter(new SingleValueEnumConverter(Contract.class));
        result.registerConverter(new SingleValueEnumConverter(Oudlers.class));
        result.registerConverter(new SingleValueEnumConverter(Handful.class));

        return result;
    }

    protected List<PlayerBoard> loadBoards() throws FileNotFoundException {

        // In case application has just been updated
        migrate11File();

        XStream stream = getXStream();

        List<PlayerBoard> result = null;
        FileInputStream is = null;
        try {
            is = openFileInput(BOARDS_FILENAME_1_2);
            result = (List<PlayerBoard>) stream.fromXML(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    protected void saveBoards(List<PlayerBoard> boards) throws IOException {

        XStream stream = getXStream();

        FileOutputStream os = null;
        try {
            os = openFileOutput(BOARDS_FILENAME_1_2, Context.MODE_WORLD_READABLE);
            String xml = stream.toXML(boards);
            os.write(xml.getBytes());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    protected void saveBoard(int index, PlayerBoard board) throws IOException {

        List<PlayerBoard> boards = null;
        try {
            boards = loadBoards();
        } catch (FileNotFoundException fnfe) {
            // This might append if this is the first time
        }

        if (boards == null) {
            boards = new ArrayList<PlayerBoard>();
        }
        if (index != -1 || index == boards.size()) {
            if (index != boards.size()) {
                boards.remove(index);
            }
            boards.add(index, board);
        } else {
            boards.add(board);
        }

        saveBoards(boards);
    }

    protected void removeBoard(int index) throws IOException {

        List<PlayerBoard> boards = null;
        try {
            boards = loadBoards();
        } catch (FileNotFoundException fnfe) {
            // This might append if this is the first time
        }

        if (boards != null && index < boards.size()) {
            boards.remove(index);
            saveBoards(boards);
        }

    }

    protected int clearBoards() throws IOException {

        int result = 0;
        try {
            List<PlayerBoard> boards = loadBoards();
            result = boards.size();
            boards.clear();
            saveBoards(boards);
        } catch (FileNotFoundException fnfe) {
            // This might append if this is the first time
        }
        return result;
    }

    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), message, 5).show();
    }

    ////////////////////////////
    // 1.1 Retro compatibility  //
    ////////////////////////////

    private void migrate11File() {
        String[] files = fileList();
        try {
            if (files != null) {
                for (String file : files) {
                    System.out.println("File found: " + file);
                    if (BOARDS_FILENAME_1_1.equals(file)) {
                        List<PlayerBoard11> playerBoard11s = loadBoards11(file);
                        List<PlayerBoard> newList = Lists.newArrayList();
                        for (PlayerBoard11 board11 : playerBoard11s) {
                            System.out.println("Migrate board: " + board11.getScores());
                            PlayerBoard board = PlayerBoard.cloneIt(board11);
                            newList.add(board);
                        }
                        saveBoards(newList);
                        deleteFile(file);
                    }
                }
            }
        } catch (Exception eee) {
            eee.printStackTrace();
        }

    }

    protected XStream getXStream11() {
        XStream result = new XStream();
        result.alias("board", PlayerBoard11.class);
        result.alias("game", Deal.class);
        result.alias("contract", Contract.class);
        result.alias("oudlers", Oudlers.class);
        result.alias("handful", Handful.class);
        result.registerConverter(new SingleValueEnumConverter(Contract.class));
        result.registerConverter(new SingleValueEnumConverter(Oudlers.class));
        result.registerConverter(new SingleValueEnumConverter(Handful.class));

        return result;
    }

    protected List<PlayerBoard11> loadBoards11(String fileName) {

        XStream stream = getXStream11();

        List<PlayerBoard11> result = null;

        FileInputStream is = null;
        try {
            is = openFileInput(fileName);
            result = (List<PlayerBoard11>) stream.fromXML(is);
        } catch (Exception eee) {
            eee.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
