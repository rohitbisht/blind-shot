package org.company.fingerprint.transport;


public interface IDistributionChannelReceiveCallback
{
    public void Callback(String remoteServer, Object message);
}
