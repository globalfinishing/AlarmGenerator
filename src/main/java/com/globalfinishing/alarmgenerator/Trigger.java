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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Trigger {

    private final String _id;
    private final String _type;
    private final int _ack_all_value;
    private final boolean _use_ack_all;
    private final String _ack_tag;
    private final String _exp;
    private final String _message_tag;
    private final String _message_handshake_exp;
    private final String _message_notification_tag;
    private final String _remote_ack_exp;
    private final String _remote_ack_handshake_tag;
    private final String _label;
    private final String _handshake_tag;

    /**
     * @param _id
     * @param _type
     * @param _ack_all_value
     * @param _use_ack_all
     * @param _ack_tag
     * @param _exp
     * @param _message_tag
     * @param _message_handshake_exp
     * @param _message_notification_tag
     * @param _remote_ack_exp
     * @param _remote_ack_handshake_tag
     * @param _label
     * @param _handshake_tag
     */
    public Trigger(String _id, String _type, int _ack_all_value, boolean _use_ack_all, String _ack_tag, String _exp,
            String _message_tag, String _message_handshake_exp, String _message_notification_tag,
            String _remote_ack_exp, String _remote_ack_handshake_tag, String _label, String _handshake_tag) {
        super();
        this._id = _id;
        this._type = _type;
        this._ack_all_value = _ack_all_value;
        this._use_ack_all = _use_ack_all;
        this._ack_tag = _ack_tag;
        this._exp = _exp;
        this._message_tag = _message_tag;
        this._message_handshake_exp = _message_handshake_exp;
        this._message_notification_tag = _message_notification_tag;
        this._remote_ack_exp = _remote_ack_exp;
        this._remote_ack_handshake_tag = _remote_ack_handshake_tag;
        this._label = _label;
        this._handshake_tag = _handshake_tag;
    }

    public String getId() {
        return _id;
    }

    public String getExp() {
        return _exp;
    }

    public Element toElement(Document doc) {
        Element e = doc.createElement("trigger");
        e.setAttribute("id", _id);
        e.setAttribute("type", _type);
        e.setAttribute("ack-all-value", Integer.toString(_ack_all_value));
        e.setAttribute("use-ack-all", Boolean.toString(_use_ack_all));
        e.setAttribute("ack-tag", _ack_tag);
        e.setAttribute("exp", _exp);
        e.setAttribute("message-tag", _message_tag);
        e.setAttribute("message-handshake-exp", _message_handshake_exp);
        e.setAttribute("message-notification-tag", _message_notification_tag);
        e.setAttribute("remote-ack-exp", _remote_ack_exp);
        e.setAttribute("remote-ack-handshake-tag", _remote_ack_handshake_tag);
        e.setAttribute("label", _label);
        e.setAttribute("handshake-tag", _handshake_tag);
        return e;
    }

}
