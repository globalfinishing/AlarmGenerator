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

import java.util.function.Consumer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * YPath is a stripped down XPath that will be over 1,000,000 times faster.
 * 
 * YPath can only process the '/' command and only finds elements
 * 
 */
public class YPath {

    /**
     * Store the path to traverse.
     */
    private final String[] path;

    /**
     * Create a new optimized Y-Path.
     * 
     * @param path
     *            Y-Path expression
     */
    public YPath(String path) {
        this.path = path.split("/");
    }

    /**
     * Evaluate a path given an element. Reentrant and thread safe.
     * 
     * @param path
     *            Y-Path path
     * @param n
     *            Element to start at
     * @param consumer
     *            Call back for each found element
     */
    public static void for_each(String path, Node n, Consumer<Element> consumer) {
        new YPath(path).for_each(n, consumer);
    }

    /**
     * Evaluate this YPath. Reentrant and thread safe.
     * 
     * @param n
     *            Element to start at
     * @param consumer
     *            Call back for each found element
     */
    public void for_each(Node n, Consumer<Element> consumer) {
        forEach(n, 0, consumer);
    }

    /**
     * Implementation of for_each. Reentrant and thread safe.
     * 
     * @param n
     *            The starting node
     * @param index
     *            The current position on the Y-Path
     * @param consumer
     *            Call back for each found element
     */
    private void forEach(Node n, int index, Consumer<Element> consumer) {
        if (index == path.length && n.getNodeType() == Node.ELEMENT_NODE) {
            consumer.accept((Element) n);
        } else {
            for (Node c = n.getFirstChild(); c != null; c = c.getNextSibling()) {
                if (c.getNodeType() == Node.ELEMENT_NODE && path[index].equals(c.getNodeName())) {
                    forEach(c, index + 1, consumer);
                }
            }
        }
    }
}
