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

public class TriggerFactory {
    private String type = "bit";
    private int ack_all_value = 0;
    private boolean use_ack_all = false;
    private String ack_tag = "";
    private String message_tag = "";
    private String message_handshake_exp = "";
    private String message_notification_tag = "";
    private String remote_ack_exp = "";
    private String remote_ack_handshake_tag = "";
    private String handshake_tag = "";
    
    public Trigger newTrigger(String id, String exp) {
        String label = id;
        Trigger trigger = new Trigger(
            id,
            type,
            ack_all_value,
            use_ack_all,
            ack_tag,
            exp,
            message_tag,
            message_handshake_exp,
            message_notification_tag,
            remote_ack_exp,
            remote_ack_handshake_tag,
            label,
            handshake_tag
            );
        return trigger;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAck_all_value() {
        return ack_all_value;
    }

    public void setAck_all_value(int ack_all_value) {
        this.ack_all_value = ack_all_value;
    }

    public boolean isUse_ack_all() {
        return use_ack_all;
    }

    public void setUse_ack_all(boolean use_ack_all) {
        this.use_ack_all = use_ack_all;
    }

    public String getAck_tag() {
        return ack_tag;
    }

    public void setAck_tag(String ack_tag) {
        this.ack_tag = ack_tag;
    }

    public String getMessage_tag() {
        return message_tag;
    }

    public void setMessage_tag(String message_tag) {
        this.message_tag = message_tag;
    }

    public String getMessage_handshake_exp() {
        return message_handshake_exp;
    }

    public void setMessage_handshake_exp(String message_handshake_exp) {
        this.message_handshake_exp = message_handshake_exp;
    }

    public String getMessage_notification_tag() {
        return message_notification_tag;
    }

    public void setMessage_notification_tag(String message_notification_tag) {
        this.message_notification_tag = message_notification_tag;
    }

    public String getRemote_ack_exp() {
        return remote_ack_exp;
    }

    public void setRemote_ack_exp(String remote_ack_exp) {
        this.remote_ack_exp = remote_ack_exp;
    }

    public String getRemote_ack_handshake_tag() {
        return remote_ack_handshake_tag;
    }

    public void setRemote_ack_handshake_tag(String remote_ack_handshake_tag) {
        this.remote_ack_handshake_tag = remote_ack_handshake_tag;
    }

    public String getHandshake_tag() {
        return handshake_tag;
    }

    public void setHandshake_tag(String handshake_tag) {
        this.handshake_tag = handshake_tag;
    }
}
