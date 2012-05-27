package org.company.fingerprint.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;
import org.company.fingerprint.agent.IRequestProcessor;
import org.company.fingerprint.agent.Reply;
import org.company.fingerprint.agent.Request;
import org.company.fingerprint.distribution.DistributionMessageReply;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisBasedDistributionChannel extends BinaryJedisPubSub implements IDuplexDistributionChannel
{
    
    static byte[] replyChannel = Bytes.toBytes("identity.fingerprint.reply");
    static byte[] requestChannel = Bytes.toBytes("identity.fingerprint.request");
    
    Thread subscriptionThread;
    Jedis subscriptionJedisClient;
    IDistributionChannelReceiveCallback receiverCallback;
    
    /**
     * @uml.property  name="requestProcessor"
     * @uml.associationEnd  multiplicity="(1 1)"
     */
    IRequestProcessor requestProcessor;
    
    /**
     * @uml.property  name="pool"
     * @uml.associationEnd  multiplicity="(0 -1)" elementType="redis.clients.jedis.Jedis"
     */
    JedisPool pool;
    
    
    
    public RedisBasedDistributionChannel()
    {
        
    }
    
    public void Open()
    {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Subscribe(RedisBasedDistributionChannel.replyChannel);
    }
    
    public void Close()
    {
        if(null!=subscriptionJedisClient)
        {
            subscriptionJedisClient.disconnect();
            
        }
    }
    
    void Subscribe(final byte[] channel)
    {   
        final BinaryJedisPubSub pubSub = this;
        subscriptionJedisClient = pool.getResource();
        
        subscriptionThread = new Thread( new Runnable(){
            @Override
            public void run() {             
                subscriptionJedisClient.subscribe(pubSub, channel);
            }
            
        });
        
        subscriptionThread.start();     
    }
    
    @Override
    public void SendToMultipleServers(List<String> serverList, Object message) throws Exception
    {
        //Using Redis, a single publish should send the msg to multiple servers. 
        //TODO: Implement ACK mechanism
        Message wireMessage = new Message();
        wireMessage.Data = message;
        wireMessage.Sender = "server";
                
        pool.getResource().publish(requestChannel, Serialize(message));
        
    }
    
    byte[] Serialize(Object object) throws Exception
    {
        if( !(object instanceof Serializable))
        {
            throw new Exception("Not a serliazble message");
        }       
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ObjectOutputStream ostream = new ObjectOutputStream(outStream);
        ostream.writeObject(object);
        
        byte[] data = outStream.toByteArray();
        ostream.close();
        outStream.close();
        
        return data;
    }
    
    Object DeSerialize(byte[] data) throws Exception
    {
        
    
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);

        ObjectInputStream inputStream;
        
            inputStream = new ObjectInputStream(inStream);
            Object request = inputStream.readObject();
            inputStream.close();
            inStream.close();
            
        return request;
            
    }
    

    @Override
    public void RegisterReceive(IDistributionChannelReceiveCallback callback)
    {   
        receiverCallback = callback;
    }

    
    void OnReceiveMessage(Message message)
    {       
        if(null != receiverCallback)
        {
            receiverCallback.Callback(message.Sender, message.Data);
        }
    }

    @Override
    public void onMessage(byte[] channel, byte[] message)
    {        
        try
        {
            Object incoming = DeSerialize(message);
            if(incoming instanceof Message)
            {
                OnReceiveMessage((Message) incoming);
            }
            else
            {
                System.out.println("\nRedisBasedDistribution: Unknown message received");
            }
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onPMessage(byte[] arg0, byte[] arg1, byte[] arg2)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPSubscribe(byte[] arg0, int arg1)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPUnsubscribe(byte[] arg0, int arg1)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSubscribe(byte[] arg0, int arg1)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onUnsubscribe(byte[] arg0, int arg1)
    {
        // TODO Auto-generated method stub
        
    }

}
