package org.company.fingerprint.agent;

public interface IClientsHandler {
	void Open(IRequestProcessor requestProcessor);
	void Close();
}
