package org.company.fingerprint.distribution;

import java.io.Serializable;


public class DistributionMessageReply implements Serializable
{
    public String Server;
    public long OperationId;
    public Object Result;
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5187707729950192216L;

    public String GetServer()
    {
        return Server;
    }
    
    public long GetOperationId()
    {
        return OperationId;
    }
}
