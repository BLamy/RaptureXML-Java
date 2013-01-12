package test;

import junit.framework.TestCase;
import RaptureXML.RXMLElement;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/11/13
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class WildcardTests extends TestCase
{

    public void testEndingWildcard() throws Exception
    {
        File file = new File(WildcardTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        final int arr[] = {0};

        rxml.iterate("players.*", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 10, arr[0]);
    }

    public void testMidstreamWildcard() throws Exception
    {
        File file = new File(WildcardTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        final int arr[] = {0};

        rxml.iterate("players.*.name", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 10, arr[0]);

        // count the tags that have a position
        arr[0] = 0;
        rxml.iterate("players.*.position", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 9, arr[0]);
    }
}
