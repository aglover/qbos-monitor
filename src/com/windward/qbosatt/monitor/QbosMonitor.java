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
    private long sleepTime = 60000; // default to 1 minute

    @Override
    public void initialize(AdapterManager aAdapterManager) {
        super.initialize(aAdapterManager);
        configuration = getConfiguration();
        continueToMonitor = true;
        ahoy = null;
        long iSleep = 0;
        try { iSleep = configuration.getLongProperty("sleep-time"); } catch (Exception e) {}
        if (iSleep > 0) {
            sleepTime = iSleep;
        }

        setState(StateEnum.STARTING);
        LOGGER.debug("QbosMonitor has initialized");
    }

    public boolean isConfigured() {
        return (configuration != null) ? true : false;
    }

    @Override
    public void shutdown() throws AdapterException {
        continueToMonitor = false;
        ahoy = null;
        setState(StateEnum.STOPPING);
        LOGGER.debug("QbosMonitor has shutdown");
    }

    public String getAdapterConfigurationClassName() {
        return QbosMonitorConfiguration.class.getName();
    }

    @Override
    public void run() {
        try {
            setState(StateEnum.RUNNING);
            ahoy = getSQSAdapter();
            while (continueToMonitor && getState() == StateEnum.RUNNING) {
                ahoy.receive(new MessageReceivedCallback() {
                    @Override
                    public void onReceive(String id, String body) {
                        try {
                            sendEvent("QBos", XML.read(new StringReader(body)));
                        } catch (InvalidXMLFormatException e) {
                            XML event = new XML("event");
                            event.addChild("invalid-xml").setText(body);
                            sendEvent("qbos", event);
                        }
                    }
                });

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.warn("InterruptedException caught, but not setting a fault");
                }
            }
        } catch (Exception e) {
            handleException("Exception in run()", e);
        }
        //at the end of run, reset ahoy
        ahoy = null;
    }

    private void handleException(String msg, Exception e) {
        LOGGER.error(msg, e);
        continueToMonitor = false;
        setState(StateEnum.FAULT);
    }

    private SQSAdapter getSQSAdapter() {
        try {
            if (ahoy == null) {
                String queueName = configuration.getProperty("aws-queue-name");
                String awsKey = configuration.getProperty("aws-key");
                String awsSecret = configuration.getProperty("aws-secret");
                ahoy = new SQSAdapter(awsKey, awsSecret, queueName);
            }
        } catch (Exception e) {
            handleException("QbosMonitor Exception in getSQSAdapter", e);
        }
        return ahoy;
    }

    public void setSQSAdapter(SQSAdapter instance) {
        ahoy = instance;
    }
}
