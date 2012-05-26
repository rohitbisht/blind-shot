package org.company.fingerprint;

import java.io.Serializable;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1813503618622141070L;
    private static long nextId = 0;
    
    public Request(){
    	nextId++;
    	requestId = nextId;
    }
    
	/**
	 * @uml.property  name="requestId"
	 */
	public long requestId;
	/**
	 * @uml.property  name="fingerprintData"
	 * @uml.associationEnd  readOnly="true"
	 */
	public FingerprintData fingerprintData;
	
}



