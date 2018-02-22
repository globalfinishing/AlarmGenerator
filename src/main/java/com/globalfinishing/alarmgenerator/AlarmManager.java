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

import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Generate alarms
 *
 */
public class AlarmManager {

    private final TriggerFactory _triggerFactory = new TriggerFactory();

    private final Map<String, Trigger> _triggers = new LinkedHashMap<>();
    private final List<Message> _messages = new ArrayList<>();

    private final MessageFactory faultMessageFactory;
    private final MessageFactory warningMessageFactory;
    private final MessageFactory spareMessageFactory;
    private final MessageFactory defaultMessageFactory;

    private int alarmNumber;
    private boolean alarmNums = true;

    public AlarmManager() {
        faultMessageFactory = new MessageFactory();
        faultMessageFactory.setBackcolor(0xFF0000); // red
        faultMessageFactory.setForecolor(0x000000); // black

        warningMessageFactory = new MessageFactory();
        warningMessageFactory.setBackcolor(0xFFA500); // orange
        warningMessageFactory.setForecolor(0x000000); // black

        spareMessageFactory = new MessageFactory();
        spareMessageFactory.setBackcolor(0x0000FF); // blue
        spareMessageFactory.setForecolor(0xFFFFFF); // white

        defaultMessageFactory = new MessageFactory();
        defaultMessageFactory.setBackcolor(0xFFFFFF); // white
        defaultMessageFactory.setForecolor(0x000000); // black
    }

    /**
     * enable or disable alarm numbering
     * 
     * @param alarmNums
     */
    public void setAlarmNums(boolean alarmNums) {
        this.alarmNums = alarmNums;
    }

    /**
     * Generate alarms for all tags in a program
     * 
     * @param program
     *            The program
     */
    public void writeAlarms(Program program) {
        alarmNumber = Math.max(alarmNumber, program.getAlarmNumber());
        Log.pushContext("Program:" + program.getName());
        program.getTags().forEach(t -> {
            writeAlarmsImpl(t, false);
        });
        Log.popContext();
    }

    /**
     * Write out all the alarms in a tag
     * 
     * @param tag
     *            The tag to write alarms from
     * @param alarmFlag
     *            Alarm enable flag from meta-comments
     */
    private void writeAlarmsImpl(Tag tag, boolean alarmFlag) {
        Log.pushContext(tag.getName());

        // See if this tag has bit tags as children. If not, recursively process
        // the child tags
        boolean flag = alarmFlag || tag.getMeta().isAlarm(false);
        boolean wasAlarm[] = { false };

        tag.getTags().forEach(t -> {
            boolean isCandidate = t.isAlarmCandidate();
            if (flag && isCandidate) {
                addAlarm(tag, t);
                wasAlarm[0] = true;
            } else if (!isCandidate) {
                writeAlarmsImpl(t, flag);
            }
        });
        if (wasAlarm[0]) {
            alarmNumber += 32;
        }

        Log.popContext();
    }

    /**
     * Add a new alarm
     * 
     * @param tagName
     *            The name of the tag the alarm goes to.
     * @param msgPrefix
     *            Prefix to the alarm message
     * @param parsedDescription
     *            Alarm message, including description and meta data
     */
    private void addAlarm(Tag triggerTag, Tag alarmTag) {

        Meta meta = alarmTag.getMeta();

        if (meta.getStripped().length() < 2) {
            Log.warning("Skipping alarm with less than 2 characters: %s", meta.getString());
            return;
        }

        // Select the correct message factory
        MessageFactory factory;
        switch (meta.getStripped().charAt(0)) {
        case 'f':
            factory = faultMessageFactory;
            break;

        case 'w':
            factory = warningMessageFactory;
            break;

        case 's':
            factory = spareMessageFactory;
            break;

        case 'x':
            factory = defaultMessageFactory;
            break;

        default:
            Log.warning("Skipping alarm with unknown prefix: %s", meta.getString());
            return;
        }

        // Create the new alarm message
        String id = "M" + Integer.toString(_messages.size() + 1);

        Trigger trigger = getTrigger(triggerTag.getFullName());

        StringBuilder descBuilder = new StringBuilder();
        if (alarmNums) {
            descBuilder.append("#");
            descBuilder.append(alarmNumber + alarmTag.getBitNumber());
            descBuilder.append(": ");
        }
        if (!triggerTag.getFullDesc().isEmpty()) {
            descBuilder.append(triggerTag.getFullDesc());
            descBuilder.append(" - ");
        }
        descBuilder.append(meta.getStripped().substring(1).trim());
        String description = descBuilder.toString();

        Message message = factory.newMessage(id, trigger, alarmTag.getBitNumber() + 1, description, meta);
        _messages.add(message);

        Log.info("Adding: %s", alarmTag.getFullName() + ": " + description);
    }

    /**
     * Write all the alarms to XML.
     * 
     * @param fileName
     *            Name of the file to write to
     * @throws Exception
     *             XML exceptions
     */
    public void writeXML(String fileName) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("alarms");
        root.setAttribute("version", "1.0");
        root.setAttribute("product", "GFSAlarmGenerator");
        root.setAttribute("id", "Alarms");
        doc.appendChild(root);

        Element alarm = doc.createElement("alarm");
        alarm.setAttribute("history-size", "10000");
        alarm.setAttribute("display-name", "");
        alarm.setAttribute("hold-time", "250");
        alarm.setAttribute("max-update-rate", "1.00");
        alarm.setAttribute("embedded-server-update-rate", "1.00");
        alarm.setAttribute("silence-tag", "");
        alarm.setAttribute("remote-silence-exp", "");
        alarm.setAttribute("remote-ack-all-exp", "");
        alarm.setAttribute("status-reset-tag", "");
        alarm.setAttribute("remote-status-reset-exp", "");
        alarm.setAttribute("close-display-tag", "");
        alarm.setAttribute("remote-close-display-exp", "");
        root.appendChild(alarm);

        Element triggers = doc.createElement("triggers");
        alarm.appendChild(triggers);
        _triggers.forEach((k, v) -> triggers.appendChild(v.toElement(doc)));

        Element messages = doc.createElement("messages");
        alarm.appendChild(messages);
        _messages.forEach(t -> messages.appendChild(t.toElement(doc)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

    /**
     * Write all the alarms to CSV.
     *
     * @param fileName
     *            Name of the file to write to
     * @throws Exception
     *             Throws I/O exceptions
     */
    public void writeCSV(String fileName) throws Exception {
        try (Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("TRIGGER,BIT,MESSAGE,BGCOLOR,FGCOLOR\n");
            for (Message msg : _messages) {
                msg.toCSV(writer);
            }
        }
    }

    /**
     * Get a trigger based on its expression
     * 
     * @param exp
     * @return
     */
    private Trigger getTrigger(String exp) {
        Trigger trigger = _triggers.get(exp);
        if (trigger == null) {
            String id = "T" + Integer.toString(_triggers.size() + 1);
            trigger = _triggerFactory.newTrigger(id, exp);
            _triggers.put(exp, trigger);
        }
        return trigger;
    }

}
