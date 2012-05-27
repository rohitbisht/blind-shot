/**
 * 
 */
package org.company.fingerprint.distribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.commons.lang.NotImplementedException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HTable;
import org.company.fingerprint.transport.IDistributionChannelReceiveCallback;
import org.company.fingerprint.transport.IDuplexDistributionChannel;

/**
 * @author rohit
 *
 */
public class HBaseDistributionController implements IDistributionController
{
    Configuration configuration;
    String dataTableName;
    ArrayList<ServerName> regionServers;
    IDuplexDistributionChannel channel;
    
    /**
     * @uml.property  name="synchnronizerBlocks"
     * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" aggregation="shared" inverse="hBaseDistributionController:org.company.fingerprint.distribution.SynchnronizerBlock"
     */
    private ArrayList<SynchnronizerBlock> synchnronizerBlocks;
    
    public HBaseDistributionController(String dataTableName) throws IOException
    {
        configuration = HBaseConfiguration.create();
        this.dataTableName = dataTableName;
        
        Initialize();
    }
    
    void Initialize() throws IOException
    {
        RefrestServersList();
        InitializeDistributedChannel();
    }
    
    void InitializeDistributedChannel()
    {
        throw new NotImplementedException();
    }
    
    void RegisterReceiver()
    {
        channel.RegisterReceive(new IDistributionChannelReceiveCallback() {
            
            @Override
            public void Callback(String remoteServer, Object message) 
            {
                try
                {
                    if(message instanceof DistributionMessageReply)
                    {
                        DistributionMessageReply replyMessage = (DistributionMessageReply) message;
                        OnReceive(remoteServer, replyMessage);
                    }
                    else
                    {
                        throw new Exception("Invalid message");
                    }
                    
                }
                catch(Exception e)
                {
                    System.out.printf("Distributed receive: exception occurred : %s", e.getMessage());
                }                
            }
        });
    }
    
    
    public void RefrestServersList() throws IOException
    {        
        HTable table = new HTable(this.dataTableName) ;
        NavigableMap<HRegionInfo, ServerName> map = table.getRegionLocations();
        
        HashMap<ServerName, String> hashMap = new HashMap<ServerName, String>();
        
        for(Entry<HRegionInfo, ServerName> pair : map.entrySet() )
        {
            System.out.printf("%s : %s \n", pair.getKey().getRegionNameAsString(), pair.getValue().getServerName());
            hashMap.put(pair.getValue(),"x");
        }
        
        this.regionServers = new ArrayList<ServerName> (hashMap.keySet());
        
    }
    
    /* (non-Javadoc)
     * @see org.company.fingerprint.distribution.IDistributionController#Execute(java.lang.Object)
     */
    @Override
    public Object Execute(Object args) throws Exception
    {        
        ArrayList<String> servers = new ArrayList<String>();
        for(ServerName s : regionServers)
        {
            servers.add(s.getHostname());
        }
        
        //FIXME : set the correct operation id
        SynchnronizerBlock sblock = new SynchnronizerBlock(1, servers);
        
        //FIXME : threadsafe
        synchnronizerBlocks.add(sblock);
        channel.SendToMultipleServers(servers, args);
        sblock.wait();
        
        //FIXME : Threadsafe
        synchnronizerBlocks.remove(sblock);
        return sblock.GetResults();
    }
    
    public void OnReceive(String Server, DistributionMessageReply message) throws Exception
    {
        SynchnronizerBlock syncBlock = FindSyncBlock(message.OperationId);
        if(null == syncBlock)
        {
            System.out.print("Invalid (out of band, or late) incoming reply");
            return;
        }
        syncBlock.AddResult(message);
    }
    
    public SynchnronizerBlock FindSyncBlock(long operationId)
    {
        for(SynchnronizerBlock sb : synchnronizerBlocks)
        {
            if(sb.OperationId == operationId)
                return sb;
        }
        
        return null;
    }

	
}
