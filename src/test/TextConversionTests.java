package test;

import junit.framework.TestCase;
import org.junit.Before;
import RaptureXML.RXMLElement;
/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 4:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextConversionTests extends TestCase
{
    String simplifiedXML_;
    String attributedXML_;
    @Before
    public void setUp() throws Exception
    {
        simplifiedXML_ = ""+
                "<shapes>"+
                    "<square>"+
                        "<id>1</id>"+
                        "<name>Square</name>"+
                    "</square>"+
                    "<triangle>"+
                        "<id>2.5</id>"+
                        "<name>Triangle</name>"+
                    "</triangle>"+
                "</shapes>";

        attributedXML_ = ""+
                "<shapes>"+
                    "<square id=\"1\">"+
                        "<name>Square</name>"+
                    "</square>"+
                    "<triangle id=\"2.5\">"+
                        "<name>Triangle</name>"+
                    "</triangle>"+
                "</shapes>";
    }

    public void testIntTags()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(simplifiedXML_);
        final int arr[] = {0};
        rxml.iterate("*", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                if (arr[0] == 0)
                    assertEquals("", 1, element.child("id").textAsInt());
                else if(arr[0] == 1)
                    assertEquals(2.5, 0.01, element.child("id").textAsDouble());
                arr[0]++;
            }

        });

    }

    public void testIntAttributes()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(attributedXML_);
        final int arr[] = {0};
        rxml.iterate("*", new RXMLElement.Block()
        {
            @Override
            public void block(RXMLElement element)
            {
                if (arr[0] == 0)
                    assertEquals("", 1, element.attributeAsInt("id"));
                else if(arr[0] == 1)
                    assertEquals(2.5, 0.01, element.attributeAsDouble("id"));
                else if(arr[0] == 2)
                    assertEquals("", 3, element.attributeAsInt("id"));
                arr[0]++;
            }
        });
    }
}
