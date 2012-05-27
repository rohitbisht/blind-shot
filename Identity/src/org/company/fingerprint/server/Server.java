package org.company.fingerprint.server;

import java.io.IOException;

import org.company.fingerprint.agent.Request;
import org.company.fingerprint.distribution.HBaseDistributionController;
import org.company.fingerprint.distribution.IDistributionController;
import org.company.fingerprint.transport.IDuplexDistributionChannel;
import org.company.fingerprint.transport.RedisBasedDistributionChannel;

public class Server {
    
    IDistributionController distributionController;
    
	public void Start() throws Exception
	{
	    IDuplexDistributionChannel channel = new RedisBasedDistributionChannel();
		distributionController = new HBaseDistributionController("Users", channel);
		distributionController.Start();		
	}
	
	public void Stop()
	{
		distributionController.Stop();
	}
	
	public void Send(Request request) throws Exception
	{
	    distributionController.Execute(request);
	}
	
}
