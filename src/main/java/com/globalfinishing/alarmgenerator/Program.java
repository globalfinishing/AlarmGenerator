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
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represent Studio 5000 program tags. Controller tags use a program name of
 * "$CONTROLLER$"
 */
public class Program {

    public final static String CONTROLLER = "$CONTROLLER$";
    private final String name;
    private final Meta meta;
    private final List<Tag> tags = new ArrayList<>();
    private final Controller controller;

    /**
     * Create a new program tags object. Use ProgramTags.CONTROLLER for controller
     * tags
     * 
     * @param name
     *            The name of the program
     * @param desc
     *            The program description
     */
    public Program(String name, Meta meta, Controller controller) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");
        if (meta == null)
            throw new IllegalArgumentException("meta cannot be null");
        if (controller == null)
            throw new IllegalArgumentException("controller cannot be null");

        this.name = name;
        this.meta = meta;
        this.controller = controller;

        if (meta.getAlarmNumber(-1) == -1) {
            Log.warning("No alarm number defined");
        }
    }

    /**
     * Add a new tag
     * 
     * @param tag
     *            The new tag
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Get the alarm number.
     * 
     * @return Alarm number
     */
    public int getAlarmNumber() {
        return meta.getAlarmNumber(0);
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    /**
     * Attach tags to their AOIs
     * 
     * @param aois
     *            Map of all available AOIs
     */
    public void attachAOIs(Map<String, AOITags> aois) {
        tags.forEach(t -> {
            AOITags aoi = aois.get(t.getType().toUpperCase());
            if (aoi != null) {
                aoi.getTags().forEach(t2 -> t.addTag(new Tag(t2)));
            }
            t.compile(this);
        });
        tags.sort((a, b) -> a.compareTo(b));
    }

    /**
     * Get the program name.
     * 
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Given a PLC shortcut name and a tag name, add the correct Factory Talk tag
     * prefix and suffix to the tag.
     * 
     * @param shortcutName
     *            PLC Shortcut name to use
     * @param tagName
     *            Name of the tag
     * @return Fully decorated tag name, suitable for use in Factory Talk
     */
    public String decorateTagName(String tagName) {
        StringBuilder builder = new StringBuilder();
        if (CONTROLLER.equals(getName())) {
            builder.append("{[");
            builder.append(controller.getShortcutName());
            builder.append("]");
        } else {
            builder.append("{::[");
            builder.append(controller.getShortcutName());
            builder.append("]Program:");
            builder.append(getName());
            builder.append(".");
        }
        builder.append(tagName);
        builder.append("}");
        return builder.toString();
    }
}
