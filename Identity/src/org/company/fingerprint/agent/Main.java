package org.company.fingerprint.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.hadoop.hbase.util.Bytes;
import org.company.fingerprint.distribution.Request;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
//			
		IdentityServer server = new IdentityServer();
		server.Start();
		System.out.print("Press any key to stop server");
		System.in.read();
		server.Stop();
	}
	
	public static void TrySendingRequest() throws IOException
	{
		byte[] temp = new byte[200];
		for (int i =0;i < temp.length; i++)			
		{
			temp[i]= (byte)i;
		}	
		
		
		Request req = new Request();
		req.Data = new FingerprintData("testone", temp);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
		
		objectOutStream.writeObject(req);
		byte[] data = outStream.toByteArray();
		
		objectOutStream.close();
		outStream.close();
		//r.Publish(requestChannel, data);
	
	}
}
