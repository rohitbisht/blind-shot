package org.company.fingerprint;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.company.fingerprint.transport.RedisBasedDistributionChannel;
import org.junit.Test;

public class RedisBasedDistributionChannelTest 
{

    @Test
    public void testSendToMultipleServers() throws Exception
    {
        RedisBasedDistributionChannel c = new RedisBasedDistributionChannel(RedisBasedDistributionChannel.RequestChannel, RedisBasedDistributionChannel.ReplyChannel);
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
