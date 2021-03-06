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

    private static Logger LOGGER = Logger.getLogger(QbosMonitorConfiguration.class);
    private final String AWS_KEY = "aws-key";
    private final String AWS_SECRET = "aws-secret";
    private final String AWS_QUEUE_NAME = "aws-queue-name";
    private final String SLEEP_TIME = "sleep-time";

    public QbosMonitorConfiguration(String id, Hashtable defaults) {
        super(id, defaults);
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
        addValidKey(SLEEP_TIME);
    }

    public QbosMonitorConfiguration(String id, Hashtable defaults, Set validKeys, Set requiredKeys) throws AdapterConfigurationException {
        super(id, defaults, validKeys, requiredKeys);
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
        addValidKey(SLEEP_TIME);
    }

    public QbosMonitorConfiguration(String adapterId) {
        super(adapterId);
        addValidKey(AWS_KEY);
        addValidKey(AWS_SECRET);
        addValidKey(AWS_QUEUE_NAME);
        addValidKey(SLEEP_TIME);
    }

    public long getSleepTime(){
        return getLongProperty(SLEEP_TIME).longValue();
    }

    public String getAWSKey() {
        return getProperty(AWS_KEY);
    }

    public String getAWSSecret() {
        return getProperty(AWS_SECRET);
    }

    public String getAWSQueueName() {
        return getProperty(AWS_QUEUE_NAME);
    }

    private Set<String> getRequiredKeySet() {
        return unmodifiableSet(new HashSet<String>(Arrays.asList(AWS_KEY, AWS_SECRET, AWS_QUEUE_NAME)));
    }

}
