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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.zoumbox.tarot.engine.Contract;
import org.zoumbox.tarot.engine.Game;
import org.zoumbox.tarot.engine.Handful;
import org.zoumbox.tarot.engine.Oudlers;
import org.zoumbox.tarot.engine.PlayerBoard;

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

    public static final String BOARDS_FILENAME = "org.zoumbox.tarot-1.1-parties.xml";

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
        result.alias("game", Game.class);
        result.alias("contract", Contract.class);
        result.alias("oudlers", Oudlers.class);
        result.alias("handful", Handful.class);
        result.registerConverter(new SingleValueEnumConverter(Contract.class));
        result.registerConverter(new SingleValueEnumConverter(Oudlers.class));
        result.registerConverter(new SingleValueEnumConverter(Handful.class));

        return result;
    }

    protected List<PlayerBoard> loadBoards() throws FileNotFoundException {

        XStream stream = getXStream();

        List<PlayerBoard> result;

        FileInputStream is = openFileInput(BOARDS_FILENAME);
        result = (List<PlayerBoard>) stream.fromXML(is);

        return result;
    }

    protected void saveBoards(List<PlayerBoard> boards) throws IOException {

        XStream stream = getXStream();

        FileOutputStream os = openFileOutput(BOARDS_FILENAME, Context.MODE_PRIVATE);
        String xml = stream.toXML(boards);
        os.write(xml.getBytes());

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

}
