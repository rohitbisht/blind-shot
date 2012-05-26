package org.company.fingerprint.distribution;

import java.util.HashMap;


public interface ISynchronizedOperationCompletionCallback
{

    void Invoke(HashMap<String, Object> completedMap);

}
