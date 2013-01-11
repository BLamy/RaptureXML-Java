package test;

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 1/10/13
 * Time: 12:49 AM
 * To change this template use File | Settings | File Templates.
 */
import RaptureXML.RXMLElement;

import java.util.ArrayList;

public class BoundaryTests extends junit.framework.TestCase
{
    String emptyXML_;
    String emptyTopTagXML_;
    String childXML_;
    String childrenXML_;
    String attributeXML_;
    String namespaceXML_;
    @org.junit.Before
    public void setUp() throws Exception
    {
        emptyXML_ = "";
        emptyTopTagXML_ = "<top></top>";
        childXML_ = "<top><empty_child></empty_child><text_child>foo</text_child></top>";
        childrenXML_ = "<top><child></child><child></child><child></child></top>";
        attributeXML_ = "<top foo=\"bar\"></top>";
        namespaceXML_ = "<ns:top xmlns:ns=\"*\" ns:foo=\"bar\" ns:one=\"1\"><ns:text>something</ns:text></ns:top>";
    }
    public void testEmptyXML()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(emptyXML_);
        assertFalse(rxml.isValid());
    }

    public void testEmptyTopTagXML()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(emptyTopTagXML_);
        assertTrue(rxml.isValid());
        assertEquals("text not blank", "", rxml.text());
        assertEquals("Arrays not equal", new ArrayList<RXMLElement>(), rxml.childrenWithRootXPath("*"));
    }

    public void testAttribute()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(attributeXML_);
        assertTrue(rxml.isValid());
        assertEquals("", "bar", rxml.attribute("foo"));
    }

    public void testNamespaceAttribute()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(namespaceXML_);
        assertTrue(rxml.isValid());
        assertEquals("", "bar", rxml.attribute("foo","*"));
        assertEquals("", 1, rxml.attributeAsInt("one","*"));
    }

    public void testChild()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(childXML_);
        assertTrue(rxml.isValid());
        assertEquals("Non empty child", "", rxml.child("empty_child").text());
        assertEquals("Child contains different text", "foo", rxml.child("text_child").text());
    }

    public void testNamespaceChild()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(namespaceXML_);
        assertTrue(rxml.isValid());
        assertEquals("", "something", rxml.child("*", "text").text());
    }

    public void testChildren()
    {
        RXMLElement rxml = RXMLElement.elementFromXMLString(childrenXML_);
        assertTrue(rxml.isValid());
        assertEquals("", 3, rxml.children("child").size());
    }

    public void testNamespaceChildren()
    {

        RXMLElement rxml = RXMLElement.elementFromXMLString(namespaceXML_);
        assertTrue(rxml.isValid());
        assertEquals("", 1, rxml.children("text", "*").size());
    }

}
