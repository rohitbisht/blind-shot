package org.company.fingerprint.distribution;

public interface IDistributionController
{
    void Start() throws Exception;
    void Stop();
    Object Execute(Object args) throws Exception;
    
}
