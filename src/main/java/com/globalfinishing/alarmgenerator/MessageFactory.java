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

public class MessageFactory {
    private int backcolor = 0xFF0000;
    private int forecolor = 0x000000;
    private boolean audio = false;
    private boolean display = true;
    private boolean print = false;
    private boolean message_to_tag = false;

    public Message newMessage(String id, Trigger trigger, int trigger_value, String msg, Meta meta) {
        Message message = new Message(
            id,
            trigger_value,
            trigger,
            meta.getBackColor(backcolor),
            meta.getForeColor(forecolor),
            meta.getAudio(audio),
            meta.getDisplay(display),
            meta.getPrint(print),
            meta.getMessageToTag(message_to_tag),
            msg
            );
        return message;
    }

    public int getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(int backcolor) {
        this.backcolor = backcolor;
    }

    public int getForecolor() {
        return forecolor;
    }

    public void setForecolor(int forecolor) {
        this.forecolor = forecolor;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isMessage_to_tag() {
        return message_to_tag;
    }

    public void setMessage_to_tag(boolean message_to_tag) {
        this.message_to_tag = message_to_tag;
    }
}
