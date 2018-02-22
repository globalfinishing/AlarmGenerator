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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Util {

    /**
     * Read META-INF to display the program version number and title
     * 
     * @return
     */
    public static String getApplicationTitle() {
        try {
            String title = Util.class.getPackage().getImplementationTitle();
            String version = Util.class.getPackage().getImplementationVersion();
            if (title != null && version != null) {
                return title + " - v" + version;
            }

        } catch (Exception ex) {
            Log.warning(printStackTrace(ex));
        }
        return "Alarm Generator v?.?";
    }

    /**
     * Join strings together. Null or empty strings will be removed.
     * 
     * @param delimiter
     *            The delimiter to place between strings
     * @param parts
     *            The strings to join
     * @return
     */
    public static String smartJoin(String delimiter, String... parts) {
        return Arrays.stream(parts).filter(p -> p != null && !p.isEmpty()).collect(Collectors.joining(delimiter));
    }

    /**
     * Helper function to extract the full diagnostic message from an exception.
     * 
     * @param t
     *            Throwable
     * @return Throwable description and full stack trace
     */
    public static String printStackTrace(Throwable t) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        printWriter.flush();
        return t.getMessage() + "\n" + writer.toString();
    }
}
