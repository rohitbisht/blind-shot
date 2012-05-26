package org.company.fingerprint;

public interface IClientsHandler {
	void Open(IRequestProcessor requestProcessor);
	void Close();
}
