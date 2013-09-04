package com.windward.qbosatt.monitor;

import com.b50.sqs.MessageReceivedCallback;
import com.b50.sqs.SQSAdapter;
import com.realops.common.enumeration.StateEnum;
import com.realops.common.xml.InvalidXMLFormatException;
import com.realops.common.xml.XML;
import com.realops.foundation.adapterframework.AbstractMonitorAdapter;
import com.realops.foundation.adapterframework.AdapterException;
import com.realops.foundation.adapterframework.AdapterManager;
import com.realops.foundation.adapterframework.configuration.BaseAdapterConfiguration;
import org.apache.log4j.Logger;

import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: aglover
 * Date: 8/27/13
 * Time: 11:20 AM
 */
public class QbosMonitor extends AbstractMonitorAdapter {

    private static Logger LOGGER = Logger.getLogger(QbosMonitor.class);
    private BaseAdapterConfiguration configuration;
    private boolean continueToMonitor;
    private SQSAdapter ahoy;

    @Override
    public void initialize(AdapterManager aAdapterManager) {
        super.initialize(aAdapterManager);
        configuration = getConfiguration();
        continueToMonitor = true;
        setState(StateEnum.STARTING);
        LOGGER.error("QbosMonitor has initialized");
    }

    public boolean isConfigured() {
        return (configuration != null) ? true : false;
    }

    @Override
    public void shutdown() throws AdapterException {
        continueToMonitor = false;
        setState(StateEnum.STOPPING);
        LOGGER.error("QbosMonitor has shutdown");
    }

    public String getAdapterConfigurationClassName() {
        return QbosMonitorConfiguration.class.getName();
    }

    @Override
    public void run() {
        try {
            LOGGER.error("QbosMonitor run method invoked");
            setState(StateEnum.RUNNING);
            ahoy = getSqsAdapter();
            LOGGER.error("Ahoy instance is " + ahoy + " continueToMonitor is " + continueToMonitor
                    + " and state is " + getState().toString());
            while (continueToMonitor && getState() == StateEnum.RUNNING) {
                LOGGER.error("QbosMonitor is in while loop, about to invoke ahoy.receive");
                ahoy.receive(new MessageReceivedCallback() {
                    @Override
                    public void onReceive(String id, String body) {
                        try {
                            LOGGER.error("onReceive callback invoked! About to send event for this body: " + body);
                            sendEvent("QBos", XML.read(new StringReader(body)));
                        } catch (InvalidXMLFormatException e) {
                            //TODO: how does one handle an error when parsing XML inside a monitor?
                            LOGGER.error("QbosMonitor InvalidXMLFormatException! ", e);
                        }
                    }
                });

                try {
                    Thread.sleep(60000); //1 minute
                } catch (InterruptedException e) {
                    LOGGER.error("Exception in run() -- bad news", e);
                    continueToMonitor = false;
                    setState(StateEnum.FAULT);
                }
            }
        } catch (Exception e) {
            LOGGER.error("QbosMonitor Exception! ", e);
        }
    }

    public void setSQSAdapter(SQSAdapter instance) {
        ahoy = instance;
    }

    private SQSAdapter getSqsAdapter() {
        try {
            if (ahoy == null) {
                String queueName = configuration.getProperty("aws-queue-name");
                String awsKey = configuration.getProperty("aws-key");
                String awsSecret = configuration.getProperty("aws-secret");
                ahoy = new SQSAdapter(awsKey, awsSecret, queueName);
            }
        } catch (Exception e) {
            LOGGER.error("QbosMonitor Exception connecting to SQS: " + e.getLocalizedMessage(), e);
            continueToMonitor = false;
            setState(StateEnum.FAULT);
        }
        return ahoy;
    }
}
