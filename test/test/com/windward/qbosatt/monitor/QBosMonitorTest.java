package test.com.windward.qbosatt.monitor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.b50.sqs.MessageReceivedCallback;
import com.b50.sqs.MessageSentCallback;
import com.b50.sqs.SQSAdapter;
import com.windward.qbosatt.monitor.QBosMonitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: aglover
 * Date: 8/30/13
 * Time: 1:56 PM
 */
public class QBosMonitorTest {
    @Test
    public void testMonitor() throws Exception {
        AmazonSQSClient mockClient = mock(AmazonSQSClient.class);
        MockSQSAdapter adapter = new MockSQSAdapter(mockClient, "test_queue");
        QBosMonitor monitor = new QBosMonitor();
        monitor.setSQSAdapter(adapter);
        monitor.initialize(null);
        Thread t = new Thread(monitor, "TestSampleAdapter");
        t.start();
        Thread.sleep(2000);
        monitor.shutdown();
        assertEquals("callback wasn't invoked", true, adapter.wasInvoked());
    }

    /**
     * Created with IntelliJ IDEA.
     * User: aglover
     * Date: 8/30/13
     * Time: 2:26 PM
     */
    public static class MockSQSAdapter extends SQSAdapter {

        private boolean invoked = false;

        public MockSQSAdapter(AmazonSQS sqs, String queueURL) {
            super(sqs, queueURL);

        }

        public MockSQSAdapter(String awsKey, String awsSecret, String queueName) {
            super(null, awsSecret, queueName);
        }

        public boolean wasInvoked() {
            return invoked;
        }

        @Override
        public void receive(MessageReceivedCallback callback) {
            if (callback != null) {
                invoked = true;
            }
        }

        @Override
        public void send(String message) {

        }

        @Override
        public void send(String message, MessageSentCallback callback) {
        }
    }
}
