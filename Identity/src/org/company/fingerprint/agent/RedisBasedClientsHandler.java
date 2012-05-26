package org.company.fingerprint.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.hadoop.hbase.util.Bytes;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



public class RedisBasedClientsHandler extends BinaryJedisPubSub implements IClientsHandler
{
	static byte[] replyChannel = Bytes.toBytes("identity.fingerprint.reply");
	static byte[] requestChannel = Bytes.toBytes("identity.fingerprint.request");
	
	Thread subscriptionThread;
	Jedis subscriptionJedisClient;
	
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
	
	
	
	public RedisBasedClientsHandler()
	{
		Initialize();
	}

	public void Initialize()
	{
		pool = new JedisPool(new JedisPoolConfig(), "localhost");
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
	
	public void Publish(byte[] channel, byte[] data)
	{
		Jedis jedis = pool.getResource();
		jedis.publish(channel, data);
	}
	
	public void SendReply(Reply reply) throws IOException
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    ObjectOutputStream ostream = new ObjectOutputStream(outStream);
	    ostream.writeObject(reply);
	    Jedis jedis = this.pool.getResource();
	    jedis.publish(replyChannel, outStream.toByteArray());
	    
	    ostream.close();
	    outStream.close();
	}


	@Override
	public void Open(IRequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
		Subscribe(RedisBasedClientsHandler.requestChannel);
	}
	
	@Override
	public void onMessage(byte[] channel, byte[] message) {
		// TODO Auto-generated method stub
		ByteArrayInputStream inStream = new ByteArrayInputStream(message);

		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(inStream);
			Request request = (Request) inputStream.readObject();
			inputStream.close();
			inStream.close();
			
			Reply reply = requestProcessor.ProcessRequest(request);
			SendReply(reply);
			
		} catch (Exception e) {
			System.out.print(e.getMessage());

		}
	}
	
	
	@Override
	public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPSubscribe(byte[] pattern, int subscribedChannels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSubscribe(byte[] channel, int subscribedChannels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnsubscribe(byte[] channel, int subscribedChannels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Close() {
		// TODO Auto-generated method stub
		if(null != subscriptionThread)
		{
			subscriptionJedisClient.disconnect();
		}
	}
	
}
