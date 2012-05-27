package org.company.fingerprint.agent;

import org.company.fingerprint.distribution.Reply;
import org.company.fingerprint.distribution.Request;

public interface IRequestProcessor {
	public Reply ProcessRequest(Request request);
}
