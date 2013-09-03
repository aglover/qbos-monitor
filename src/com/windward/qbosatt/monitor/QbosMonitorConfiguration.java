package com.windward.qbosatt.monitor;

import com.realops.foundation.adapterframework.configuration.AdapterConfigurationException;
import com.realops.foundation.adapterframework.configuration.BaseAdapterConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * Created with IntelliJ IDEA.
 * User: aglover
 * Date: 8/27/13
 * Time: 11:59 AM
 */
public class QbosMonitorConfiguration extends BaseAdapterConfiguration {

    private final String AWS_KEY = "aws-key";
    private final String AWS_SECRET = "aws-secret";
    private final String AWS_QUEUE_NAME = "aws-queue-name";

    public QbosMonitorConfiguration(String id, Hashtable defaults)  {
        super(id, defaults);
        addValidKeys(getRequiredKeySet());
//        addRequiredKeys(getRequiredKeySet());
    }

    public QbosMonitorConfiguration(String id, Hashtable defaults, Set validKeys, Set requiredKeys) throws AdapterConfigurationException {
        super(id, defaults, validKeys, requiredKeys);
        addValidKeys(getRequiredKeySet());
//        addRequiredKeys(getRequiredKeySet());
    }

    public QbosMonitorConfiguration(String adapterId)  {
        super(adapterId);
        addValidKeys(getRequiredKeySet());
//        addRequiredKeys(getRequiredKeySet());
    }

    public String getAWSKey(){
        return getProperty(AWS_KEY);
    }

    public String getAWSSecret(){
        return getProperty(AWS_SECRET);
    }

    public String getAWSQueueName(){
        return getProperty(AWS_QUEUE_NAME);
    }

    private Set<String> getRequiredKeySet() {
        return unmodifiableSet(new HashSet<String>(Arrays.asList(AWS_KEY, AWS_SECRET, AWS_QUEUE_NAME)));
    }

}
