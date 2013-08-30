package test.com.windward.qbosatt.monitor;

import com.realops.common.xml.XML;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: aglover
 * Date: 8/30/13
 * Time: 11:43 AM
 */
public class XMLParsingTest {
    @Test
    public void testParsingRawString() throws Exception {
        String rawXML = "<Event>\n" +
                "    <QSI>dm2q</QSI>\n" +
                "    <ClassID>18272</ClassID>\n" +
                "    <RecordID>526252</RecordID>\n" +
                "    <Lifecycle>approved</Lifecycle>\n" +
                "</Event>";
        XML xml = XML.read(new StringReader(rawXML));

        assertNotNull(xml);
    }
}
