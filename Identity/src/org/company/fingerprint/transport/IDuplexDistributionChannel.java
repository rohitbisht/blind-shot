package org.company.fingerprint.transport;

import java.util.List;

/*
 * This Interface is responsible for  sending messages to all the specified servers.
 * The messages are received on the callback registered. The callback also gets the remote servers name.
 * It is the responsibility of the Client to co-relate the sent and received messages.
 * The Send will guarantee that the message is received by all the receivers or return an error message containing all the 
 * servers with errors and respected errors.
*/
public interface IDuplexDistributionChannel
{
    void Open();
    void Close();
    void SendToMultipleServers(List<String> serverList, Object message) throws Exception;
    void RegisterReceive(IDistributionChannelReceiveCallback callback);
}
