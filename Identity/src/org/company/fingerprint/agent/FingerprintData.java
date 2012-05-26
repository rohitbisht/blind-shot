package org.company.fingerprint.agent;

import org.apache.hadoop.hbase.util.Bytes;
import sourceafis.simple.*;

public class FingerprintData extends Fingerprint{
	
	/**
	 * @uml.property  name="id"
	 */
	byte[] id;
	/**
	 * @uml.property  name="tempM" multiplicity="(0 -1)" dimension="1"
	 */
	byte[] tempM;
	
	public FingerprintData(String id, byte[] minutia)
	{
		this(Bytes.toBytes(id), minutia);
	}
	
	public FingerprintData(byte[] id, byte[] minutia)
	{
		this.id = id;
		//this.setIsoTemplate(minutia);
		tempM = minutia;
	}
	
	public FingerprintData(String id) 
	{
		this(id, null);
	}
	
	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public byte[] getId()
	{
		return id;
	}
	
	public byte[] getMinutia()
	{
		//return this.getIsoTemplate();
		return this.tempM;
	}
	
}
