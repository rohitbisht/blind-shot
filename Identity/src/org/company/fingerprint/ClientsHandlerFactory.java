package org.company.fingerprint;

public class ClientsHandlerFactory {
	public static IClientsHandler GetClientsHandler(String name) throws Exception
	{
		if( name ==  RedisBasedClientsHandler.class.getName())
				return new RedisBasedClientsHandler();
		else
				throw new Exception("Invalid clients handler");
			
	}
}
