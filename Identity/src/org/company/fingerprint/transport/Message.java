package org.company.fingerprint.transport;

import java.io.Serializable;

public class Message extends MessageBase implements Serializable
{    
    /**
     * 
     */
    private static final long serialVersionUID = -5957750779969287140L;
    public String Sender;
    public Object Data;
}
