package org.company.fingerprint.distribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.company.fingerprint.agent.FingerprintData;
import org.company.fingerprint.transport.RedisBasedDistributionChannel;

public class Reply implements Serializable {

	/**
	 * 
	 */
	
	static long nextReplyId = 1;
	private static final long serialVersionUID = -4421455936497697459L;
	
	
	public Reply(Request request)
	{
		RequestId = request.Id;
		ReplyId = nextReplyId++;	
		Server = RedisBasedDistributionChannel.GetServer();
	}
	
	/**
	 * @uml.property  name="requestId"
	 */
	public long RequestId;
	/**
	 * @uml.property  name="replyId"
	 */
	public long ReplyId;
	/**
	 * @uml.property  name="instanceId"
	 */
	public long InstanceId;
	/**
	 * @uml.property  name="fingerprints"
	 */
	public Object Data;
	
	//TODO:This does not belong here
	public String Server;

}
