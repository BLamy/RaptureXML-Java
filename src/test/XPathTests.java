package test;

import RaptureXML.RXMLElement;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/11/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class XPathTests extends TestCase
{
    String simplifiedXML_;
    String attributedXML_;
    String interruptedTextXML_;
    String cdataXML_;

    public void setUp()
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

    public void testBasicPath()
    {
        final int curr[] = {0};
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        rxml.iterateWithRootXPath("//circle", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                assertEquals("", "Circle", element.text());
                curr[0]++;
            }
        });
        assertEquals("", 1, curr[0]);
    }

    public void testAttributePath()
    {
        final int curr[] = {0};
        RXMLElement rxml = RXMLElement.elementFromXMLString(attributedXML_);
        rxml.iterateWithRootXPath("//circle[@name='Circle']", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                assertEquals("", "Circle", element.attribute("name"));
                curr[0]++;
            }
        });
        assertEquals("", 1, curr[0]);
    }
}
