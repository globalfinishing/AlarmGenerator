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

import java.util.Stack;
import java.util.function.Consumer;

/**
 * Manage the application logger
 */
public class Log {

    private static final Stack<String> contextStack = new Stack<>();
    private static Level level = Level.Warning;
    private static Consumer<String> eventConsumer;

    public enum Level {
        Severe, Warning, Info, Fine;
    }

    public static void pushContext(String context) {
        Log.contextStack.push(context);
    }

    public static String popContext() {
        return contextStack.pop();
    }

    public static String getContext() {
        return String.join(".", contextStack);
    }

    public static void setLevel(Level level) {
        Log.level = level;
    }

    public static Level getLevel() {
        return level;
    }

    public static void severe(String msg, Object... params) {
        if (level.compareTo(Level.Severe) >= 0) {
            doLog("SEVERE", msg, params);
        }
    }

    public static void warning(String msg, Object... params) {
        if (level.compareTo(Level.Warning) >= 0) {
            doLog("WARNING", msg, params);
        }
    }

    public static void info(String msg, Object... params) {
        if (level.compareTo(Level.Info) >= 0) {
            doLog("INFO", msg, params);
        }
    }

    public static void fine(String msg, Object... string) {
        if (level.compareTo(Level.Fine) >= 0) {
            doLog("FINE", msg, string);
        }
    }

    public static void setListener(Consumer<String> listener) {
        eventConsumer = listener;
    }

    private static void doLog(String pre, String msg, Object[] params) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(pre).append("] ");
        if (!contextStack.isEmpty()) {
            builder.append("[").append(getContext()).append("] ");
        }
        builder.append(String.format(msg, params));
        builder.append("\n");
        String s = builder.toString();
        System.out.print(s);
        if (eventConsumer != null) {
            eventConsumer.accept(s);
        }
    }
}
