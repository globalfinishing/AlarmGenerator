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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generate tags from XML
 *
 */
public class TagFactory {

    private final YPath aoiPath = new YPath(
            "RSLogix5000Content/Controller/AddOnInstructionDefinitions/AddOnInstructionDefinition");
    private final YPath controllerPath = new YPath("RSLogix5000Content/Controller");
    private final YPath programPath = new YPath("RSLogix5000Content/Controller/Programs/Program");
    private final YPath tagsPath = new YPath("Tags/Tag");
    private final YPath parameterPath = new YPath("Parameters/Parameter");
    private final YPath descriptionPath = new YPath("Description");
    private final YPath commentPath = new YPath("Comments/Comment");

    /**
     * Read all AOIs
     * 
     * @param n
     *            Studio5000 XML document
     * @param consumer
     *            Callback to receive each AOITags
     */
    public void readAOIs(Document n, Consumer<AOITags> consumer) {
        aoiPath.for_each(n, x -> {
            AOITags aoi;
            String name = x.getAttribute("Name");
            Log.pushContext("AOI:" + name);
            aoi = new AOITags(name);
            parameterPath.for_each(x, y -> aoi.addTag(readTag(y)));
            consumer.accept(aoi);
            Log.popContext();
        });
    }

    /**
     * Read all program tags for each program
     * 
     * @param n
     *            Root document
     * @param consumer
     *            Call-back to consume each program
     */
    public void readPrograms(Document n, Controller controller, Consumer<Program> consumer) {
        controllerPath.for_each(n, controllerNode -> {
            String[] desc = { "" };
            descriptionPath.for_each(controllerNode, x -> desc[0] = x.getTextContent().trim());

            Log.pushContext("Controller Description:");
            Program p = new Program(Program.CONTROLLER, new Meta(desc[0]), controller);
            Log.popContext();

            Log.pushContext("Controller:");
            tagsPath.for_each(controllerNode, y -> p.addTag(readTag(y)));
            Log.popContext();

            consumer.accept(p);
        });

        programPath.for_each(n, program -> {
            String name = program.getAttribute("Name");
            String[] desc = { "" };
            descriptionPath.for_each(program, x -> desc[0] = x.getTextContent().trim());

            Log.pushContext("Program Description:" + name);
            Program p = new Program(name, new Meta(desc[0]), controller);
            Log.popContext();

            Log.pushContext("Program:" + name);
            tagsPath.for_each(program, y -> p.addTag(readTag(y)));
            Log.popContext();

            consumer.accept(p);
        });
    }

    /**
     * Read a tag
     * 
     * @param n
     *            Parameter, or otherwise compatible element
     * @return A new tag
     */
    public Tag readTag(Element n) {
        String name = n.getAttribute("Name");
        Log.pushContext(name);
        String desc[] = { "" };
        descriptionPath.for_each(n, x -> desc[0] = x.getTextContent().trim());
        String type = n.getAttribute("DataType");
        Tag t = new Tag(name, new Meta(desc[0]), type);

        commentPath.for_each(n, comment -> {
            String op = comment.getAttribute("Operand");
            String com = comment.getTextContent().trim();
            Log.pushContext(op.substring(1));
            t.addComment(op, new Meta(com));
            Log.popContext();
        });
        Log.popContext();
        return t;
    }
}
