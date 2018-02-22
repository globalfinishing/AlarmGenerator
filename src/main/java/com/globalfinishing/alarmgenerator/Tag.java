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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tag implements Comparable<Tag> {

    private final String name;
    private String fullName = null;
    /** Set after calling compile () */
    private String fullDesc = null;
    /** Set after calling compile () */
    private Meta meta = null;
    private final String type;
    private Map<String, Meta> comments = null;
    private final Map<String, Tag> tags = new LinkedHashMap<>();

    /**
     * Make a new typeless tag
     * 
     * @param name
     *            Tag name
     */
    public Tag(String name) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");

        this.name = name;
        this.type = "";
    }

    /**
     * Make a new tag with a known type
     * 
     * @param name
     *            Tag name
     * @param meta
     *            Tag description
     * @param type
     *            Tag type
     */
    public Tag(String name, Meta meta, String type) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");
        if (meta == null)
            throw new IllegalArgumentException("desc cannot be null");
        if (type == null)
            throw new IllegalArgumentException("type cannot be null");

        this.name = name;
        this.meta = meta;
        this.type = type;
    }

    /**
     * Copy a tag
     * 
     * @param tag
     */
    public Tag(Tag tag) {
        this.name = tag.name;
        this.meta = tag.meta;
        this.type = tag.type;

        if (tag.comments == null) {
            this.comments = null;
        } else {
            this.comments = new HashMap<>(tag.comments);
        }

        tag.tags.forEach((k, v) -> {
            tags.put(k, new Tag(v));
        });
    }

    /**
     * Apply all the tag comments to the child tag descriptions.
     */
    public void compile(Program program) {
        pushComments();
        updateFullName(program, null);
        updateDescription(null);
        sortTags();
    }

    /**
     * Push the comment list down to tag descriptions.
     * 
     * Messages have the following format .TAG.subtag.subsubtag.digit
     * 
     */
    private void pushComments() {
        tags.values().forEach(Tag::pushComments);
        if (comments != null) {
            comments.forEach((operand, comment) -> {
                String[] parts = operand.split("[.]");
                pushOperandChain(parts, 1, comment); // Start at index 1 as 0 is always empty
            });
        }
        comments = null;
    }

    /**
     * Push the operands down the description chain. The operands array is a path to
     * follow down the tag tree. If the tags do not exist, they will be created. The
     * comment will be set on the last node in the tree.
     * 
     * @param operands
     *            Path to a tag
     * @param index
     *            Current index in a path.
     * @param comment
     *            The comment to add to the requested tag
     */
    private void pushOperandChain(String[] operands, int index, Meta comment) {
        if (index < operands.length) {
            String op = operands[index];
            Tag tag = getTag(op);
            if (tag == null) {
                tag = new Tag(op);
                addTag(tag);
            }
            tag.pushOperandChain(operands, index + 1, comment);
        } else {
            meta = comment;
        }
    }

    /**
     * Calculate or update the full tag name for each tag recursively
     * 
     * @param program
     *            The program from which this tag belongs
     * @param prefix
     *            The name of the parent tag, or null if there is no parent
     */
    private void updateFullName(Program program, String prefix) {
        String undecoratedName = Util.smartJoin(".", prefix, name);
        fullName = program.decorateTagName(undecoratedName);
        tags.values().forEach(t -> t.updateFullName(program, undecoratedName));
    }

    /**
     * Update the description field
     */
    private void updateDescription(String pre) {
        fullDesc = Util.smartJoin(" - ", pre, meta.getStripped());
        tags.values().forEach(t -> t.updateDescription(fullDesc));
    }

    /**
     * Sort all child tags
     */
    private void sortTags() {
        if (tags.isEmpty()) {
            return;
        }

        tags.values().forEach(t -> t.sortTags());

        List<Tag> t = new ArrayList<Tag>();
        tags.values().forEach(t::add);
        tags.clear();
        t.stream().sorted().forEach(this::addTag);
    }

    /*
     * Debugging
     */
    public void Print() {
        Print(0);
    }

    /*
     * Debugging
     */
    private void Print(int lvl) {
        String space = "";
        for (int i = 0; i < lvl; i++) {
            space += "    ";
        }
        System.out.println(space + name + ":");
        space += "    ";
        System.out.println(space + "desc: " + meta);
        System.out.println(space + "type: " + type);

        System.out.println(space + "comments:");
        String space2 = space + "    ";
        if (comments != null) {
            comments.forEach((operand, comment) -> {
                System.out.println(space2 + operand + ": " + comment);
            });
        }

        System.out.println(space + "tags:");
        tags.values().stream().forEach((t) -> {
            t.Print(lvl + 2);
        });
    }

    /**
     * Check if this tag is a valid alarm candidate
     * 
     * @return True if this tag looks like an alarm
     */
    public boolean isAlarmCandidate() {
        // We cannot be an alarm if there are sub-tags.
        if (!tags.isEmpty()) {
            return false;
        }
        try {
            // All digital alarms end with a bit number between 0 and 31.
            int num = Integer.parseInt(name);
            if (num >= 0 && num < 32) {
                return true;
            }
        } catch (NumberFormatException e) {
            // pass
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        if (meta == null) {
            return "";
        } else {
            return meta.getStripped();
        }
    }

    /**
     * Get the full name of this tag. Returns null if this tag has not been compiled
     * with the compile method.
     * 
     * @return Full tag name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get the full description of this tag. Returns null if this tag has not been
     * compiled with the compile method.
     * 
     * @return Full tag description
     */
    public String getFullDesc() {
        return fullDesc;
    }

    public Meta getMeta() {
        return meta;
    }

    public String getType() {
        return type;
    }

    public Tag getTag(String name) {
        if (tags == null) {
            return null;
        } else {
            return tags.get(name.toUpperCase());
        }
    }

    public void addTag(Tag t) {
        tags.put(t.getName().toUpperCase(), t);
    }

    public Collection<Tag> getTags() {
        return tags.values();
    }

    public void addComment(String operand, Meta comment) {
        if (comments == null) {
            comments = new HashMap<>();
        }
        comments.put(operand, comment);
    }

    /**
     * Return the bit number of this tag if isAlarmCandidate is true. If
     * isAlarmCandidate is false, this may throw an exception.
     * 
     * @return The bit number.
     */
    public int getBitNumber() {
        return Integer.parseInt(name);
    }

    @Override
    public int compareTo(Tag b) {
        try {
            return Integer.parseInt(this.getName()) - Integer.parseInt(b.getName());
        } catch (NumberFormatException e) {
            return this.getName().compareToIgnoreCase(b.getName());
        }
    }

}
