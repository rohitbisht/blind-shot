package org.company.fingerprint.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.company.fingerprint.distribution.Reply;
import org.company.fingerprint.distribution.Request;

import sourceafis.simple.*;

public class FingerprintManager implements IRequestProcessor
{	
	/**
	 * @uml.property  name="fingerprintProvider"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	FingerprintProvider fingerprintProvider;
	/**
	 * @uml.property  name="storedFingerprints"
	 */
	List<FingerprintData> storedFingerprints;
	
	/**
	 * @uml.property  name="fingerprintsLoaded"
	 */
	boolean fingerprintsLoaded = false;
	
	
	public FingerprintManager(FingerprintProvider provider)
	{
		fingerprintProvider = provider;
	}
	
	public void LoadFingerprints() throws IOException
	{
		fingerprintProvider.Open();
		storedFingerprints = fingerprintProvider.GetFingerprints();
		fingerprintsLoaded = true;
		
	}
	
	public List<FingerprintData> FindMatches(FingerprintData fp, Boolean returnOnFirstMatch) throws IOException
	{
		if(!fingerprintsLoaded)
			LoadFingerprints();
		
		ArrayList<FingerprintData> matches = new ArrayList<FingerprintData>();
		
		for(FingerprintData sfp : storedFingerprints)
		{
			if(MatchMinutia(sfp, fp))
			{
				matches.add(sfp);
				
				if(returnOnFirstMatch)
				{
					break;
				}
			}
		}
		return matches;
	}
	
	boolean MatchMinutia(Fingerprint fp1, Fingerprint fp2)
	{		
		AfisEngine engine = new AfisEngine();
		Person p1 = new Person(fp1);
		Person p2 = new Person(fp2);
		float match =  engine.verify(p1,p2);
		return match > 0.0;	
	}

	@Override
	public Reply ProcessRequest(Request request) {
		// TODO Auto-generated method stub
		Reply reply = new Reply(request);	
		ArrayList<FingerprintData> fps = new ArrayList<FingerprintData>();
		
		reply.Data = request.Data;
		return reply;
	}

	public void Close()
	{
		
	
	}
	
}
