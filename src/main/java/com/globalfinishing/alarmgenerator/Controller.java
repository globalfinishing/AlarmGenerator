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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Represent a PLC Controller. All programs, tags, and AOIs are stored here.
 * Each L5X file contains one controller.
 */
public class Controller {
    private String shortcutName;
    private List<Program> programs = new ArrayList<>();
    private Map<String, AOITags> aois = new HashMap<>();

    public Controller(String shortcutName, String fileName) throws Exception {
        this.shortcutName = shortcutName;
        Log.pushContext(shortcutName);

        // Setup XML
        DocumentBuilderFactory docFactory;
        DocumentBuilder docBuilder;
        docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setCoalescing(true);
        docBuilder = docFactory.newDocumentBuilder();
        Log.info("Loading XML Document");
        Document doc = docBuilder.parse(new File(fileName));
        Log.info("Loading Complete");

        TagFactory tagFactory = new TagFactory();

        // Load AOIs
        Log.info("Reading AOIs");
        tagFactory.readAOIs(doc, this::addAOI);
        Log.info("Reading AOIs complete");

        // Load all programs
        Log.info("Reading Programs");
        tagFactory.readPrograms(doc, this, programs::add);
        Log.info("Reading Programs Complete");

        Log.info("Attaching AOIs");
        programs.forEach((p) -> {
            p.attachAOIs(aois);
        });
        Log.info("Attaching AOIs complete");

        Log.popContext();
    }

    /**
     * Get the shortcut name of this controller
     * 
     * @return
     */
    public String getShortcutName() {
        return shortcutName;
    }

    public Collection<Program> getPrograms() {
        return programs;
    }

    /**
     * Get an AOI by name.
     * 
     * @param name
     * @return
     */
    public AOITags getAOI(String name) {
        return aois.get(name.toUpperCase());
    }

    private void addAOI(AOITags aoi) {
        aois.put(aoi.getName().toUpperCase(), aoi);
    }
}
