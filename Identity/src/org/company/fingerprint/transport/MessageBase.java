package org.company.fingerprint.transport;

public class MessageBase
{
    private static long nextId=1;
    private long id;
    
    public MessageBase()
    {
        id = nextId++ ;
    }
    
    public long getId()
    {
        return id;
    }
}
