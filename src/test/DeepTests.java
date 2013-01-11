package test;

import RaptureXML.RXMLElement;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 2:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeepTests extends TestCase
{
    public void testQuery() throws Exception
    {
        File file = new File(DeepChildrenTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        final int[] arr = {0};//Nasty hack to allow me to incriment a int inside an inner class

        // Count the players
        rxml.iterate("players.player", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 9, arr[0]);


        // count the first player's name
        arr[0] = 0;
        rxml.iterate("players.player.name", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 1, arr[0]);

        // count the coaches
        arr[0] = 0;
        rxml.iterate("players.coach", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 1, arr[0]);



    }
}
