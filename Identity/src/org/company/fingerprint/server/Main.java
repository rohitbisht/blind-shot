package org.company.fingerprint.server;

import org.apache.hadoop.hbase.util.Bytes;
import org.company.fingerprint.agent.FingerprintData;
import org.company.fingerprint.distribution.Request;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        server.Start();
        //System.out.print("\nServer Started. Press any key to stop");
        Request req = new Request();
        req.Data = new FingerprintData("10");
        
        Object o = server.Send(req);
        System.out.printf("\n Done : %d", Bytes.toStringBinary((byte[]) o));
        
    }
}
