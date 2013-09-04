package com.windward.qbosatt.monitor;

import com.realops.foundation.adapterframework.configuration.AdapterConfigurationException;
import com.realops.foundation.adapterframework.configuration.BaseAdapterConfiguration;
import org.apache.log4j.Logger;

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
    private static Logger LOGGER = Logger.getLogger(QbosMonitorConfiguration.class);

    public QbosMonitorConfiguration(String id, Hashtable defaults)  {
        super(id, defaults);
//        addValidKeys(getRequiredKeySet());
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
//        addRequiredKeys(getRequiredKeySet());
    }

    public QbosMonitorConfiguration(String id, Hashtable defaults, Set validKeys, Set requiredKeys) throws AdapterConfigurationException {
        super(id, defaults, validKeys, requiredKeys);
//        addValidKeys(getRequiredKeySet());
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
//        addRequiredKeys(getRequiredKeySet());
    }

    public QbosMonitorConfiguration(String adapterId)  {
        super(adapterId);
//        addValidKeys(getRequiredKeySet());
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
//        addRequiredKeys(getRequiredKeySet());
    }

    @Override
    public void setProperty(String key, String value){
        LOGGER.error("Set property " +key+ " vlaue " +value);
        super.setProperty(key, value);
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
