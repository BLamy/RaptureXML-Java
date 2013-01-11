package test;

import RaptureXML.RXMLElement;
import junit.framework.TestCase;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 3:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTests extends TestCase
{
    String simplifiedXML_;
    String attributedXML_;
    String interruptedTextXML_;
    String cdataXML_;
    @Before
    public void setUp() throws Exception
    {
        simplifiedXML_ = ""+
            "<shapes>"+
                "<square>Square</square>"+
                "<triangle>Triangle</triangle>"+
                "<circle>Circle</circle>"+
            "</shapes>";

        attributedXML_ = ""+
            "<shapes>"+
                "<square name=\"Square\" />"+
                "<triangle name=\"Triangle\" />"+
                "<circle name=\"Circle\" />"+
            "</shapes>";
        interruptedTextXML_ = "<top><a>this</a>is<a>interrupted</a>text<a></a></top>";
        cdataXML_ = "<top><![CDATA[this]]><![CDATA[is]]><![CDATA[cdata]]></top>";
    }


    public void testInterruptedText()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(interruptedTextXML_);
        assertEquals("", "thisisinterruptedtext", rxml.text());
    }

    public void testCDataText()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(cdataXML_);
        assertEquals("", "thisiscdata", rxml.text());
    }

    public void testTags()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        final int[] arr = {0};
        rxml.iterate("*", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                if (arr[0] == 0) {
                    assertEquals("", "square", element.tag());
                    assertEquals("", "Square", element.text());
                } else if (arr[0] == 1) {
                    assertEquals("", "triangle", element.tag());
                    assertEquals("", "Triangle", element.text());
                } else if (arr[0] == 2) {
                    assertEquals("", "circle", element.tag());
                    assertEquals("", "Circle", element.text());
                }
                arr[0]++;
            }
        });
        assertEquals("", 3, arr[0]);
    }

    public void testAttributes()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(attributedXML_);
        final int[] arr = {0};
        rxml.iterate("*", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                if (arr[0] == 0) {
                    assertEquals("", "Square", element.attribute("name"));
                } else if (arr[0] == 1) {
                    assertEquals("", "Triangle", element.attribute("name"));
                } else if (arr[0] == 2) {
                    assertEquals("", "Circle", element.attribute("name"));
                }

                arr[0]++;
            }
        });
        assertEquals("", 3, arr[0]);
    }
}
