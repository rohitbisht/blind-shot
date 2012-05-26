package org.company.fingerprint.agent;

public class IdentityServer {
	
	FingerprintManager manager;
	IClientsHandler clientsHandler;
	
	public void Start() throws Exception
	{
		manager = new FingerprintManager(new FingerprintProvider());
		clientsHandler = ClientsHandlerFactory.GetClientsHandler(RedisBasedClientsHandler.class.getName());
		clientsHandler.Open(manager);		
	}
	
	public void Stop()
	{
		clientsHandler.Close();
		manager.Close();
	}
}
