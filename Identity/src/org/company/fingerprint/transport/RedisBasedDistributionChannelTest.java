package org.company.fingerprint.transport;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class RedisBasedDistributionChannelTest extends
        RedisBasedDistributionChannel
{

    @Test
    public void testSendToMultipleServers() throws Exception
    {
        RedisBasedDistributionChannel c = new RedisBasedDistributionChannel();
        byte[] b = new byte[10];
        for(int i=0; i<b.length; i++)
        {
            b[i]= (byte) i;
        }
        
        ArrayList<String> servers = new ArrayList<String>();
        servers.add("server1");
        
        c.SendToMultipleServers(servers, b);
    }

    @Test
    public void testRegisterReceive()
    {
        
    }

}
