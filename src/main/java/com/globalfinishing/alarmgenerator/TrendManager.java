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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Read all the trend tags from the L5X and print them out on one line suitable for Factory Talk Studio ME.
 *
 */
public class TrendManager {
    private List<String> trendTags = new ArrayList<>();
    

    /**
     * Add all the tags from a program into the trend list
     * 
     * @param program
     */
    public void addProgram(Program program) {
        ArrayDeque<Tag> tagList = new ArrayDeque<>(program.getTags());
        
        while (!tagList.isEmpty()) {
            Tag tag = tagList.pop();
            tagList.addAll(tag.getTags());
            if (tag.getMeta().isTrendTag(false)) {
                trendTags.add(tag.getFullName());
            }
        }
    }
    
    /**
     * Write all the trend data to a file.
     * 
     * @param fileName
     *            The file to write to
     */
    public void writeFile(String fileName) throws Exception {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
            if (!trendTags.isEmpty()) {
                out.write(trendTags.get(0));
            }
            for (int i = 1; i < trendTags.size(); i++) {
                out.write(' ');
                out.write(trendTags.get(i));
            }
            out.write('\n');
        }
    }

}
