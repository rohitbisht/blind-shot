package org.company.fingerprint.distribution;

import java.io.Serializable;

import org.company.fingerprint.agent.FingerprintData;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1813503618622141070L;
    private static long nextId = 0;
    
    public Request(){
    	nextId++;
    	Id = nextId;
    }
    
	/**
	 * @uml.property  name="requestId"
	 */
	public long Id;
	/**
	 * @uml.property  name="fingerprintData"
	 * @uml.associationEnd  readOnly="true"
	 */
	public Object Data;
	
}



