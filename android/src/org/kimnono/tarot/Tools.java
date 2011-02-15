package org.kimnono.tarot;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class Tools {

    public static String toPrettyPrint(String string) {
        String result = string.toLowerCase();
        result = result.replaceAll("_", " ");
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

}
