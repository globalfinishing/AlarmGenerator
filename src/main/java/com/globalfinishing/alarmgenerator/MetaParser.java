/*******************************************************************************

    Alarm Generator

    Copyright (C) 2018 Global Finishing Solutions, LLC.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.

*******************************************************************************/
package com.globalfinishing.alarmgenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Read tag descriptions and extract meta-data
 */
public class MetaParser {

    private String str;
    private String stripped;
    private Map<String, String> params = new HashMap<>();

    /**
     * Parse a string
     * 
     * @param string
     */
    public MetaParser(String string) {
        str = string;
        StringBuilder builder = new StringBuilder();
        int startPos;
        int stopPos = 0;

        while (true) {
            startPos = string.indexOf("(*", stopPos);
            if (startPos < 0) {
                builder.append(string.substring(stopPos));
                break;
            }
            builder.append(string.substring(stopPos, startPos));

            stopPos = string.indexOf("*)", startPos + 2);
            if (stopPos < 0) {
                Log.warning("Unterminated meta string.");
                stopPos = string.length();
            }

            parseMetaComment(string.substring(startPos + 2, stopPos));
            stopPos += 2;
        }

        stripped = builder.toString().trim();
    }

    /**
     * Get the original unparsed string.
     * 
     * @return Original string
     */
    public String getString() {
        return str;
    }

    /**
     * Get the string with the meta data stripped out.
     * 
     * @return Unparsed string
     */
    public String getStripped() {
        return stripped;
    }

    /**
     * Return the requested parameter, or null if it was not found.
     * 
     * @param param
     *            Parameter name
     * @return Requested param, or null
     */
    public String getParam(String param) {
        return params.get(param);
    }

    /**
     * Return a set of all param names.
     * 
     * @return Key set
     */
    public Set<String> paramSet() {
        return params.keySet();
    }

    /**
     * Parse the contents of a meta string
     * 
     * This parses the following grammar.
     * 
     * line = key_value_pair * key_value_pair = key ('=' value )? key = ident value
     * = ident
     * 
     * @param line
     *            Line to parse
     */
    private void parseMetaComment(String line) {
        Tokenizer t = new Tokenizer(line);

        while (true) {
            String k;
            String v = null;

            if (t.acceptEOF()) {
                break;
            }

            // read key
            k = t.expectIdent();
            if (k == null) {
                break;
            }

            // read optional '=' value
            if (t.acceptEqual()) {
                v = t.expectIdent();
            }

            // Store the result
            params.put(k, v);
        }
    }

    /**
     * Custom tokenizer that can read identifiers and "=" tokens
     */
    private class Tokenizer {
        private final String s;
        private int pos;

        /*
         * Make a new tokenizer
         */
        public Tokenizer(String s) {
            this.s = s;
            this.pos = 0;
        }

        /**
         * Read the next identifier, or return null. An identifier has any character
         * that is not whitespace or an equal operator.
         */
        public String expectIdent() {
            eatWhite();
            StringBuilder buffer = new StringBuilder();

            while (pos < s.length()) {
                char c = s.charAt(pos);
                if (Character.isWhitespace(c) || c == '=') {
                    break;
                } else if (c == '"') {
                    pos++;
                    nextQuotedIdent(buffer);
                } else {
                    buffer.append(c);
                    pos++;
                }
            }
            if (buffer.length() == 0) {
                Log.warning("Expected an identifier on col %d, counting from \"(*\"", pos);
                return null;
            } else {
                return buffer.toString();
            }
        }

        /**
         * Read the next equal operator, or return null.
         * 
         * @return Returns the next equal sign, or null.
         */
        public boolean acceptEqual() {
            eatWhite();
            if (pos < s.length()) {
                char c = s.charAt(pos);
                if (c == '=') {
                    pos++;
                    return true;
                }
            }
            return false;
        }

        /**
         * Return true if we are at the end of the file/line
         * 
         * @return True for EOF
         */
        public boolean acceptEOF() {
            eatWhite();
            return pos >= s.length();
        }

        /**
         * Remove whitespace.
         */
        private void eatWhite() {
            while (pos < s.length()) {
                char c = s.charAt(pos);
                if (Character.isWhitespace(c)) {
                    pos++;
                } else {
                    break;
                }
            }
        }

        /**
         * Read quoted text and put it in the buffer. Assume that the first quote has
         * been read.
         * 
         * @param buffer
         *            Put text into the buffer
         */
        private void nextQuotedIdent(StringBuilder buffer) {
            while (pos < s.length()) {
                char c = s.charAt(pos);
                if (c == '"') {
                    pos++;
                    return;
                } else {
                    pos++;
                    buffer.append(c);
                }
            }
            Log.warning("Unterminated quote in meta section.");
        }
    }
}
