package com.windward.qbosatt.monitor;

import com.b50.sqs.MessageReceivedCallback;
import com.b50.sqs.SQSAdapter;
import com.realops.foundation.adapterframework.AbstractMonitorAdapter;
import com.realops.foundation.adapterframework.AdapterException;
import com.realops.foundation.adapterframework.AdapterManager;

/**
 * Created with IntelliJ IDEA.
 * User: aglover
 * Date: 8/27/13
 * Time: 11:20 AM
 */
public class QBosMonitor extends AbstractMonitorAdapter {

    private QBosMonitorConfiguration configuration;
    private boolean continueToMonitor;

    @Override
    public void initialize(AdapterManager aAdapterManager) {
        configuration = (QBosMonitorConfiguration) getConfiguration();
        continueToMonitor = true;
    }

    @Override
    public void shutdown() throws AdapterException {

    }

    @Override
    public void run() {

        String queueName = configuration.getAWSQueueName();
        String awsKey = configuration.getAWSKey();
        String awsSecret = configuration.getAWSSecret();

        SQSAdapter ahoy = new SQSAdapter(awsKey, awsSecret, queueName);

        while (continueToMonitor) {
            ahoy.receive(new MessageReceivedCallback() {
                @Override
                public void onReceive(String id, String body) {

                }
            });

            try{
                Thread.sleep(60000); //1 minute
            } catch (InterruptedException e) {
                //bad issue, bail out
                continueToMonitor = false;
            }
        }
    }
}
