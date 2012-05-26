package org.company.fingerprint.distribution;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;



/**
 * @author rohit
 *SynchronizerBlock is used to synchronize between the inbound operation and the result from the distributed operation execution.
 *Raises notifyall event on itself once all the results are received.
 *TODO: Make thread safe.
 */
public class SynchnronizerBlock
{
    private HashMap<String, Object> completedMap;
    public long OperationId;
    
    public List<String> ServerList;
    
    public SynchnronizerBlock(long operationId, List<String> servers)
    {
        //TODO null checks
        this.OperationId = operationId;
        this.ServerList = servers;
        completedMap = new HashMap<String, Object>();
    }
    
    public void AddResult(DistributionMessageReply reply) throws Exception
    {
         if(null == reply)
             throw new NullArgumentException("reply");
         if(reply.GetOperationId() != this.OperationId)
         {
             throw new Exception("SyncBlock:Invalid operation id sent.");
         }
         
         if(completedMap.containsKey(reply.Server))
         {
             throw new Exception("Duplicate message, the message from server is already received");             
         }
         
         completedMap.put(reply.Server, reply.Result );
         
         NotifyIfAllRepliesReceived();
         
    }
    
    void NotifyIfAllRepliesReceived()
    {
        for(String server : this.ServerList)
        {
            if(!completedMap.containsKey(server))
            {
                return;
            }
        }
        
        this.notifyAll();
    }
    
    public Object GetResults()
    {
        return completedMap;
    }
}
