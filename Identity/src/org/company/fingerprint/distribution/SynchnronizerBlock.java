package org.company.fingerprint.distribution;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;



/**
 * @author rohit
 *SynchronizerBlock is used to synchronize between the inbound operation and the result from the distributed operation execution.
 *TODO: Make thread safe.
 */
public class SynchnronizerBlock
{
    private HashMap<String, Object> completedMap;
    public long OperationId;
    public ISynchronizedOperationCompletionCallback Callback;
    public List<String> ServerList;
    
    public SynchnronizerBlock(long operationId, ISynchronizedOperationCompletionCallback callback)
    {
        //TODO null checks
        this.OperationId = operationId;
        this.Callback = callback;
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
         
         CallbackIfAllRepliesReceived();
         
    }
    
    void CallbackIfAllRepliesReceived()
    {
        for(String server : this.ServerList)
        {
            if(!completedMap.containsKey(server))
            {
                return;
            }
        }
        
        this.Callback.Invoke(completedMap);
    }
}
