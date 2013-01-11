package test;

import RaptureXML.RXMLElement;
import junit.framework.TestCase;
import org.junit.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeepChildrenTests extends TestCase
{

    public void testQuery() throws Exception
    {
        File file = new File(DeepChildrenTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        final int[] arr = {0};//Nasty hack to allow me to incriment a int inside an inner class

        // Count the players
        RXMLElement players = rxml.child("players");
        ArrayList <RXMLElement> children = players.children("player");
        rxml.iterateElements(children, new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 9, arr[0]);
    }
    public void testDeepChildQuery() throws Exception
    {
        File file = new File(DeepChildrenTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        // count the players
        RXMLElement coachingYears = rxml.child("players.coach.experience.years");
        assertEquals("", 1, coachingYears.textAsInt());
    }

    public void testDeepChildQueryWithWildcard() throws Exception
    {
        File file = new File(DeepChildrenTests.class.getResource("players.xml").toURI());
        RXMLElement rxml = RXMLElement.elementFromFile(file);
        // count the players
        RXMLElement coachingYears = rxml.child("players.coach.experience.teams.*");

        // first team returned
        assertEquals("", 53, coachingYears.textAsInt());
    }
}
