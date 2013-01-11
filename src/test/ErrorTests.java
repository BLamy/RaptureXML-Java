package test;

import junit.framework.TestCase;
import RaptureXML.RXMLElement;
/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 3:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorTests extends TestCase
{

    String simplifiedXML_;
    String badXML_;

    @org.junit.Before
    public void setUp() throws Exception
    {
        simplifiedXML_ = "\n" +
                "        <shapes>\n" +
                "            <square>Square</square>\n" +
                "            <triangle>Triangle</triangle>\n" +
                "            <circle>Circle</circle>\n" +
                "        </shapes>";
        badXML_ = "</xml";
    }

    public void testBadXML()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(badXML_); //Will throw an exception but be caught internally
        assertFalse(rxml.isValid());
    }

    public void testMissingTag()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        RXMLElement hexagon = rxml.child("hexagon");
        assertFalse(hexagon.isValid());
    }

    public void testMissingTagIteration()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        final int[] arr = {0};//Nasty hack to allow me to incriment a int inside an inner class
        rxml.iterate("hexagon", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                arr[0]++;
            }
        });
        assertEquals("", 0, arr[0]);
    }

    public void testMissingAttribute()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        String missingName = rxml.attribute("name");
        assertEquals("", "", missingName);
    }


}
