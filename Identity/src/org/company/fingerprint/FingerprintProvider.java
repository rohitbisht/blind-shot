package org.company.fingerprint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class FingerprintProvider {
	
	static final String fingerprintTableName="Users";
	static final String fingerprintColumnFamily="details";
	static final String fingerprintColmnnMinutia="m";
	
	static byte[] columnFamilyBytes = Bytes.toBytes(fingerprintColumnFamily);
	static byte[] minutiaColumnBytes = Bytes.toBytes(fingerprintColmnnMinutia);
	
	
	/**
	 * @uml.property  name="conf"
	 * @uml.associationEnd  
	 */
	HBaseConfiguration conf;
	
	public List<FingerprintData> GetFingerprints() throws IOException
	{
		ArrayList<FingerprintData> fps = new ArrayList<FingerprintData>();
		
		Scan scan = new Scan();
		
		scan.addColumn(columnFamilyBytes, minutiaColumnBytes);		
		HTable table = new HTable(conf, fingerprintTableName);		
		ResultScanner scanner = table.getScanner(scan);
				
		for(Result rs = scanner.next(); rs!=null; rs= scanner.next())
		{	
			FingerprintData fp  = new FingerprintData(rs.getRow(), rs.getValue(columnFamilyBytes, minutiaColumnBytes));
			fps.add(fp);
			
	    //get all the fingerprints from db store
		}
		
		scanner.close();
		return fps;
	}
	
	
	
	public void Open()
	{		 
		//@SuppressWarnings("deprecation")	
		this.conf = new HBaseConfiguration();
		
	}
}
