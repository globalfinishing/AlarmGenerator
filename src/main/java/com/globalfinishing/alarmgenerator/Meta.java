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

/*
 * Store tag description and meta-data in the description field
 */
public class Meta {

    private String str;
    private String stripped;

    private Boolean isTrendTag;

    private Integer alarmNumber;
    private Boolean isAlarm;

    private Integer backcolor;
    private Integer forecolor;
    private Boolean audio;
    private Boolean display;
    private Boolean print;
    private Boolean message_to_tag;

    public Meta(String comment) {
        MetaParser m = new MetaParser(comment);
        str = comment;
        stripped = m.getStripped();

        for (String key : m.paramSet()) {
            switch (key) {
            case "trendtag":
                isTrendTag = true;
                break;
            case "alarmnum":
                alarmNumber = getInteger(m, "alarmnum");
                break;
            case "alarms":
                isAlarm = true;
                break;
            case "backcolor":
                backcolor = getInteger(m, "backcolor");
                break;
            case "forecolor":
                forecolor = getInteger(m, "forecolor");
                break;
            case "audio":
                audio = getBoolean(m, "audio");
                break;
            case "display":
                display = getBoolean(m, "display");
                break;
            case "print":
                print = getBoolean(m, "print");
                break;
            case "message-to-tag":
                message_to_tag = getBoolean(m, "message-to-tag");
                break;
            default:
                Log.warning("Unknown key: \"%s\"", key);
            }
        }
    }

    public String getString() {
        return str;
    }

    public String getStripped() {
        return stripped;
    }

    public boolean isTrendTag(boolean def) {
        if (isTrendTag == null) {
            return def;
        } else {
            return isTrendTag;
        }
    }

    public boolean isAlarm(boolean def) {
        if (isAlarm == null) {
            return def;
        } else {
            return isAlarm;
        }
    }

    public int getAlarmNumber(int def) {
        if (alarmNumber == null) {
            return def;
        } else {
            return alarmNumber;
        }
    }

    public int getBackColor(int def) {
        if (backcolor == null) {
            return def;
        } else {
            return backcolor;
        }
    }

    public int getForeColor(int def) {
        if (forecolor == null) {
            return def;
        } else {
            return forecolor;
        }
    }

    public boolean getAudio(boolean def) {
        if (audio == null) {
            return def;
        } else {
            return audio;
        }
    }

    public boolean getDisplay(boolean def) {
        if (display == null) {
            return def;
        } else {
            return display;
        }
    }

    public boolean getPrint(boolean def) {
        if (print == null) {
            return def;
        } else {
            return print;
        }
    }

    public boolean getMessageToTag(boolean def) {
        if (message_to_tag == null) {
            return def;
        } else {
            return message_to_tag;
        }
    }

    private Integer getInteger(MetaParser m, String name) {
        String value = m.getParam(name);
        try {
            return Integer.decode(value);
        } catch (NumberFormatException e) {
            Log.warning("Invalid \"%s\": %s", name, value);
        }
        return null;
    }

    private Boolean getBoolean(MetaParser m, String name) {
        String value = m.getParam(name);
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            Log.warning("Invalid \"%s\": %s", name, value);
        }
        return null;
    }
}
