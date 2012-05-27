package org.company.fingerprint.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.hadoop.hbase.util.Bytes;
import org.company.fingerprint.transport.IDistributionChannelReceiveCallback;
import org.company.fingerprint.transport.IDuplexDistributionChannel;
import org.company.fingerprint.transport.RedisBasedDistributionChannel;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



public class RedisBasedClientsHandler implements IClientsHandler, IDistributionChannelReceiveCallback
{
	
	IDuplexDistributionChannel channel;
	/**
	 * @uml.property  name="requestProcessor"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	IRequestProcessor requestProcessor;
	
	/**
	 * @uml.property  name="pool"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="redis.clients.jedis.Jedis"
	 */
	
	public RedisBasedClientsHandler()
	{
		Initialize();
	}

	public void Initialize()
	{
		channel = new RedisBasedDistributionChannel(RedisBasedDistributionChannel.ReplyChannel, RedisBasedDistributionChannel.RequestChannel);	
	}	
	
	
	public void SendReply(String server, Reply reply) throws Exception
	{   
	    ArrayList<String> servers = new ArrayList<String>();
	    servers.add(server);
		channel.SendToMultipleServers(servers, reply);
	}


	@Override
	public void Open(IRequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
		channel.RegisterReceive(this);
		channel.Open();
		
	}
	

	@Override
	public void Close() {
		// TODO Auto-generated method stub
		if(null != channel)
		{
			channel.Close();
		}
	}

    @Override
    public void Callback(String remoteServer, Object message) 
    {
        if(!(message instanceof Request)){
            System.out.println("RedisBasedClient: Invalid incoming message, not of type Request");
        }
        
        Request request = (Request) message;
        Reply reply = requestProcessor.ProcessRequest(request);
        try
        {
            SendReply(remoteServer, reply);
        } catch (Exception e)
        {
            System.out.printf("\nRedisbasedClient: Callback: Exception %s ", e.getMessage());
        }
        
    }	
}
