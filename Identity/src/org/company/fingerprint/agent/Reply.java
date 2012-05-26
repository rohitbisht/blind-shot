package org.company.fingerprint.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reply implements Serializable {

	/**
	 * 
	 */
	
	static long nextReplyId = 1;
	private static final long serialVersionUID = -4421455936497697459L;
	
	public Reply(Request request)
	{
		requestId = request.requestId;
		replyId = nextReplyId++;
		fingerprints = new ArrayList<FingerprintData>();
	}
	
	/**
	 * @uml.property  name="requestId"
	 */
	public long requestId;
	/**
	 * @uml.property  name="replyId"
	 */
	public long replyId;
	/**
	 * @uml.property  name="instanceId"
	 */
	public long instanceId;
	/**
	 * @uml.property  name="fingerprints"
	 */
	public List<FingerprintData> fingerprints; 

}
