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

import java.util.ArrayList;
import java.util.List;

/**
 * Read Studio5000 XML files and generate alarm xml files for Factory Talk View
 * Machine Edition
 *
 */
public class Generator {

    private List<String> shortcuts = new ArrayList<>();
    private List<String> l5xfiles = new ArrayList<>();
    private String xmlFile = null;
    private String trendFile = null;
    private String csvFile = null;
    private boolean alarmNums = true;

    public void run() throws Exception {

        if (shortcuts.isEmpty()) {
            Log.severe("No input file selected!");
            return;
        }

        List<Controller> controllers = new ArrayList<>();
        List<Program> programList = new ArrayList<>();

        for (int i = 0; i < shortcuts.size(); i++) {
            Controller c = new Controller(shortcuts.get(i), l5xfiles.get(i));
            controllers.add(c);
            programList.addAll(c.getPrograms());
        }

        programList.sort((p, q) -> p.getAlarmNumber() - q.getAlarmNumber());

        if (xmlFile != null || csvFile != null) {
            AlarmManager alarmManager = new AlarmManager();
            alarmManager.setAlarmNums(alarmNums);
            Log.info("Generating alarms");
            programList.forEach((p) -> {
                alarmManager.writeAlarms(p);
            });
            Log.info("Generating alarms complete");

            if (xmlFile != null) {
                Log.info("Writing output xml");
                alarmManager.writeXML(xmlFile);
                Log.info("Writing output xml complete");
            }

            if (csvFile != null) {
                Log.info("Writing output csv");
                alarmManager.writeCSV(csvFile);
                Log.info("Writing output csv complete");
            }
        }

        if (trendFile != null) {
            Log.info("Generating trend tags");
            TrendManager trendManager = new TrendManager();
            programList.forEach(p -> trendManager.addProgram(p));
            trendManager.writeFile(trendFile);
            Log.info("Generating trend tags complete");
        }
    }

    public void addL5X(String shortcut, String fileName) {
        shortcuts.add(shortcut);
        l5xfiles.add(fileName);
    }

    public void setAlarmNums(boolean alarmNums) {
        this.alarmNums = alarmNums;
    }

    public void setXMLfile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void setCSVFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public void setTrendFile(String trendFile) {
        this.trendFile = trendFile;
    }
}
