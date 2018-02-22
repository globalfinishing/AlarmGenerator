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

public class Main {

    public static void main(String[] argv) {
        if (argv.length == 0) {
            GUI.run();
            return;
        }

        Generator g = new Generator();
        for (int i = 0; i < argv.length; i++) {
            switch (argv[i]) {
            case "--L5X":
                assertUsage(i + 2 < argv.length);
                g.addL5X(argv[i + 1], argv[i + 2]);
                i += 2;
                break;

            case "--out":
                assertUsage(i + 1 < argv.length);
                g.setXMLfile(argv[i + 1]);
                i++;
                break;

            case "--csv":
                assertUsage(i + 1 < argv.length);
                g.setCSVFile(argv[i + 1]);
                i++;
                break;

            case "--trend":
                assertUsage(i + 1 < argv.length);
                g.setTrendFile(argv[i + 1]);
                i++;
                break;

            case "--log-level":
                assertUsage(i + 1 < argv.length);
                setLogLevel(argv[i + 1]);
                i++;
                break;

            case "--alarmnums":
                g.setAlarmNums(true);
                break;

            case "--no-alarmnums":
                g.setAlarmNums(false);
                break;

            case "--help":
                showUsage();
                return;

            case "--version":
                System.out.println(Util.getApplicationTitle());
                return;

            default:
                Log.severe("Unknown parameter: %s", argv[i]);
                showUsage();
                return;
            }
        }

        try {
            g.run();
        } catch (Exception ex) {
            Log.severe(Util.printStackTrace(ex));
            System.exit(1);
        }
    }

    /**
     * Print out the command line usage to stdout
     */
    public static void showUsage() {
        // System.out.println("================================================================================");
        System.out.println("usage: java -jar alarmgenerator.jar [OPTION...] --L5X <shortcut> <path>");
        System.out.println("       [--L5X <shortcut <path>]... --out <path>");
        System.out.println("");
        System.out.println("OPTIONS");
        System.out.println("");
        System.out.println("--help                  Shows this help.");
        System.out.println("");
        System.out.println("--version               Show version number.");
        System.out.println("");
        System.out.println("--L5X <shortcut> <path> Import alarms from an L5X document. The <shortcut> name");
        System.out.println("                        will be used for all generated tags. <path> is the name");
        System.out.println("                        of the file to load");
        System.out.println("");
        System.out.println("--out <path>            Select the desired xml output file");
        System.out.println("");
        System.out.println("--trend <path>          Output trend tags to the given <path>");
        System.out.println("");
        System.out.println("--csv <path>            Select the desired csv output file");
        System.out.println("");
        System.out.println("--alarmnums             Enable alarm numbers (default).");
        System.out.println("");
        System.out.println("--no-alarmnums          Disable alarm numbers");
        System.out.println("");
        System.out.println("--log-level <level>     Set the log level. The log level must be one of: FINE,");
        System.out.println("                        INFO, WARNING, or SEVERE. The default is WARNING.");
        // System.out.println("================================================================================");
    }

    /**
     * If the assertion is false, print out the usage and exit the program with exit
     * code 1. If the assertion is true, nothing happens.
     * 
     * @param assertion
     *            Show usage and exit if false.
     */
    private static void assertUsage(boolean assertion) {
        if (!assertion) {
            showUsage();
            System.exit(1);
        }
    }

    private static void setLogLevel(String level) {
        switch (level.toUpperCase()) {
        case "FINE":
            Log.setLevel(Log.Level.Fine);
            break;

        case "INFO":
            Log.setLevel(Log.Level.Info);

        case "WARNING":
            Log.setLevel(Log.Level.Warning);
            break;

        case "SEVERE":
            Log.setLevel(Log.Level.Severe);
            break;

        default:
            System.out.println(String.format("Unknown log level: %s", level));
            assertUsage(false);
        }
    }
}
