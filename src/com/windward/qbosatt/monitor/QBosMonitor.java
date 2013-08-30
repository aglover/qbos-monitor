package com.windward.qbosatt.monitor;

import com.b50.sqs.MessageReceivedCallback;
import com.b50.sqs.SQSAdapter;
import com.realops.common.xml.InvalidXMLFormatException;
import com.realops.common.xml.XML;
import com.realops.foundation.adapterframework.AbstractMonitorAdapter;
import com.realops.foundation.adapterframework.AdapterException;
import com.realops.foundation.adapterframework.AdapterManager;

import java.io.StringReader;

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
        continueToMonitor = false;
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
                    try {
                        sendEvent("QBos", XML.read(new StringReader(body)));
                    } catch (InvalidXMLFormatException e) {
                        //TODO: how does one handle an error when parsing XML inside a monitor?
                        e.printStackTrace();
                    }
                }
            });

            try {
                Thread.sleep(60000); //1 minute
            } catch (InterruptedException e) {
                //bad issue, bail out
                continueToMonitor = false;
            }
        }
    }
}
