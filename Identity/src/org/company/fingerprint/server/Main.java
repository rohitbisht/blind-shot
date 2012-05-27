package org.company.fingerprint.server;

import org.company.fingerprint.agent.FingerprintData;
import org.company.fingerprint.agent.Request;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        server.Start();
        //System.out.print("\nServer Started. Press any key to stop");
        Request req = new Request();
        req.fingerprintData = new FingerprintData("10");
        
        server.Send(req);
        
    }
}
