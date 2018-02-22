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

import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * Store a single alarm
 * 
 * example XML
 * 
 * <message id="M1" trigger-value="2" trigger="#T1" backcolor="#FF0000" forecolor="#000000"
 * audio="false" display="true" print="false" message-to-tag="false"
 * text="#1000 Module CP1 401 Slot 1 Fault"/>
 */
public class Message {

    private final String id;
    private final int trigger_value;
    private final Trigger trigger;
    private final int backcolor;
    private final int forecolor;
    private final boolean audio;
    private final boolean display;
    private final boolean print;
    private final boolean message_to_tag;
    private final String text;

    /**
     * @param id
     * @param trigger_value
     * @param trigger
     * @param backcolor
     * @param forecolor
     * @param audio
     * @param display
     * @param print
     * @param message_to_tag
     * @param text
     */
    public Message(String id, int trigger_value, Trigger trigger, int backcolor, int forecolor, boolean audio,
            boolean display, boolean print, boolean message_to_tag, String text) {
        super();
        this.id = id;
        this.trigger_value = trigger_value;
        this.trigger = trigger;
        this.backcolor = backcolor;
        this.forecolor = forecolor;
        this.audio = audio;
        this.display = display;
        this.print = print;
        this.message_to_tag = message_to_tag;
        this.text = text;
    }

    /**
     * Create a new Message Element representing this message
     *
     * @param doc
     *            Parent document
     * @return The Message Element
     */
    public Element toElement(Document doc) {
        Element e = doc.createElement("message");
        e.setAttribute("id", id);
        e.setAttribute("trigger-value", Integer.toString(trigger_value));
        e.setAttribute("trigger", "#" + trigger.getId());
        e.setAttribute("backcolor", String.format("#%06X", backcolor));
        e.setAttribute("forecolor", String.format("#%06X", forecolor));
        e.setAttribute("audio", Boolean.toString(audio));
        e.setAttribute("display", Boolean.toString(display));
        e.setAttribute("print", Boolean.toString(print));
        e.setAttribute("message-to-tag", Boolean.toString(message_to_tag));
        e.setAttribute("text", text);
        return e;
    }

    /**
     * Write this message out in CSV to the given writer
     *
     * @param writer
     *            Writer to write to
     * @throws Exception
     *             I/O Exceptions
     */
    public void toCSV(Writer writer) throws Exception {
        writer.write(trigger.getExp());
        writer.write(",");
        writer.write(Integer.toString(trigger_value));
        writer.write(",\"");
        writer.write(text.replace("\"", "\"\""));
        writer.write("\",");
        writer.write(String.format("#%06X", backcolor));
        writer.write(",");
        writer.write(String.format("#%06X", forecolor));
        writer.write("\n");
    }

}
