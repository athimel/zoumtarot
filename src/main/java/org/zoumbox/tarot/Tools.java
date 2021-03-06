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

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;

/**
 * Classe utilitaire.
 *
 * @author Arnaud Thimel <arno@zoumbox.org>
 */
public class Tools {

    public static String toPrettyPrint(String string) {
        String result = string.toLowerCase();
        result = result.replaceAll("_", " ");
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    protected static CharSequence italic(String text) {
        SpannableString str = SpannableString.valueOf(text + " ");
        str.setSpan(new StyleSpan(Typeface.ITALIC), 0, str.length(), 0);
        return str;
    }

    protected static CharSequence bold(String text) {
        SpannableString str = SpannableString.valueOf(text);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);
        return str;
    }

    protected static CharSequence bold_italic(String text) {
        SpannableString str = SpannableString.valueOf(text + " ");
        str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, str.length(), 0);
        return str;
    }

}
