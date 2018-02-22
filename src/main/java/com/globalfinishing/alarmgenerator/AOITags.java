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

/**
 * AOITags Container
 */
public class AOITags {

    private final String name;
    private final Collection<Tag> tags = new ArrayList<>();

    public AOITags(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public Collection<Tag> getTags() {
        return tags;
    }
}
