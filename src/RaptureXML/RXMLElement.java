package RaptureXML;
// ================================================================================================
// RXMLElement
// Fast processing of XML files
//
// ================================================================================================
// Created by Brett Lamy
// Largely based off of John Blanco's Objective-c RXMLElement
// ================================================================================================
//  Copyright (c) 2012 Brett Lamy
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ================================================================================================
////  Copyright (c) 2011 John Blanco
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ================================================================================================
//

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class RXMLElement {
	private Document _doc;
	private Node _node;

	public interface Block{
		void block(RXMLElement element);
	}

	//==========================================================
	// Constructors
	//==========================================================
	public RXMLElement()
	{
	}

	public RXMLElement(String xmlString)
	{
        if (xmlString == null || xmlString.equals(""))
            return;
	    try
	    {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
	    	DocumentBuilder builder = factory.newDocumentBuilder();
			_doc = builder.parse(new InputSource(new StringReader(xmlString)));
		}
	    catch (Exception e)
		{
//			e.printStackTrace();
		}
        if (_doc != null)
        {
        	_node = _doc.getDocumentElement();
        	if (_node == null)
        	{
        		_doc = null;
        	}
        }
	}

	public RXMLElement(File file)
	{
		String xmlString = null;
    	DataInputStream in = null;
    	try {
    		byte[] buffer = new byte[(int)file.length()];
    		in = new DataInputStream(new FileInputStream(file));
    		in.readFully(buffer);
    		xmlString = new String(buffer);
    	} catch (IOException e) {
//    		e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) { /* ignore it */
            }
        }

    	try
	    {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
			_doc = builder.parse(new InputSource(new StringReader(xmlString)));
		}
	    catch (Exception e)
		{
			e.printStackTrace();
		}
        if (_doc != null)
        {
        	_node = _doc.getDocumentElement();
        	if (_node == null)
        	{
        		_doc = null;
        	}
        }
	}

	public RXMLElement(Node node)
	{
		_doc = null;
		_node = node;
	}


	//==========================================================
	// Static Constructors
	//==========================================================
	public static RXMLElement elementFromXMLString(String xmlString)
	{
		return new RXMLElement(xmlString);
	}

	public static RXMLElement elementFromFile(File file)
	{
		return new RXMLElement(file);
	}

	public static RXMLElement elementFromNode(Node node)
	{
		return new RXMLElement(node);
	}


	//==========================================================
	// Accessors
	//==========================================================
	public String description()
	{
		return this.text();
	}

	public String tag()
	{
		return _node.getNodeName();
	}

	public String text()
	{
		if (_node == null)
			return "";
		Node cur = _node;
        NodeList children = cur.getChildNodes();
        StringBuilder value = new StringBuilder();
        for (int i=0; i < children.getLength(); i++)//Must loop through every item and append them on to one another
        {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                node = node.getFirstChild();
            if (node != null)
                value.append(node.getNodeValue());
        }

        /* If value is empty then it may just be that the element had no children but may contain a value */
        if (value.toString().equals("") && cur.getNodeValue() != null &&!cur.getNodeValue().equals(""))
            return cur.getNodeValue();

        return value.toString();
	}

	public int textAsInt()
	{
		return Integer.parseInt((this.text().equals("")) ? "0" : this.text());
	}

	public double textAsDouble()
	{
		return Double.parseDouble(this.text());
	}

	public String attribute(String attName)
	{
        Node node = _node.getAttributes().getNamedItem(attName);
        if (node == null)
            return "";
		String attribute = node.getNodeValue();
		if (attribute == null)
            return "";
        return attribute;
	}

	public String attribute(String attName, String nameSpace)
	{
		String attribute = _node.getAttributes().getNamedItemNS(nameSpace, attName).getNodeValue();
		return (attribute == null) ? "" : attribute;
	}

	public int attributeAsInt(String attName)
	{
		return Integer.parseInt(this.attribute(attName));
	}

	public int attributeAsInt(String attName, String nameSpace)
	{
		return Integer.parseInt(this.attribute(attName, nameSpace));
	}

	public double attributeAsDouble(String attName)
	{
		return Double.parseDouble(this.attribute(attName));
	}

	public double attributeAsDouble(String attName, String nameSpace)
	{
		return Double.parseDouble(this.attribute(attName, nameSpace));
	}

	public boolean isValid()
	{
		return _node != null;
	}


	//==========================================================
	// Public methods
	//==========================================================
	public RXMLElement child(String tag)
	{
		if (_node == null)
			return new RXMLElement();
		String[] components = tag.split("\\.");
		Node cur = _node;

		// navigate down
		for (String itag : components)
		{
			if (itag.equals("*"))
			{
				cur = cur.getFirstChild();
				while (cur != null && cur.getNodeType() != Node.ELEMENT_NODE)
                    cur = cur.getNextSibling();
			}
			else
			{
				cur = cur.getFirstChild();
				while (cur != null)
				{
					if (cur.getNodeName().equals(itag))
						break;
					cur = cur.getNextSibling();
				}
			}
 			if (cur == null)
				break;
		}

		if (cur != null)
			return RXMLElement.elementFromNode(cur);
		return new RXMLElement();
	}

	public RXMLElement child(String tag, String namespace)
	{
		if (_node == null)
			return new RXMLElement();
		String[] components = tag.split("\\.");
		Node cur = _node;

		// navigate down
		for (String itag : components)
		{
			if (itag.equals("*"))
			{
				cur = cur.getFirstChild();
				while (cur != null && cur.getNodeType() != Node.ELEMENT_NODE && !cur.getNamespaceURI().equals(namespace))
                    cur = cur.getNextSibling();
			}
			else
			{
				cur = cur.getFirstChild();
				while (cur != null)
				{
					if (cur.getNodeType() == Node.ELEMENT_NODE && !cur.getNodeName().equals(itag)  && !cur.getNamespaceURI().equals(namespace))
						break;
					cur = cur.getNextSibling();
				}
			}
			if (cur == null)
				break;
		}

		if (cur != null)
			return RXMLElement.elementFromNode(cur.getFirstChild());
		return new RXMLElement();
	}

	public ArrayList<RXMLElement> children(String tag)
	{
		ArrayList<RXMLElement> children = new ArrayList<RXMLElement>();
		Node cur = _node.getFirstChild();
		while (cur != null)
		{
			if (cur.getNodeName().equals(tag))
				children.add(RXMLElement.elementFromNode(cur));
			cur = cur.getNextSibling();
		}
		return children;
	}

	public ArrayList<RXMLElement> children(String tag, String namespace)
	{
		ArrayList<RXMLElement> children = new ArrayList<RXMLElement>();
		Node cur = _node.getFirstChild();
		while (cur != null)
		{
			if (cur.getNodeName().replaceFirst("ns:","").equals(tag) && cur.getNamespaceURI().equals(namespace))
				children.add(RXMLElement.elementFromNode(cur));
			cur = cur.getNextSibling();
		}
		return children;
	}

	public ArrayList<RXMLElement> childrenWithRootXPath(String query)
	{
		if (query == null)
			return new ArrayList<RXMLElement>();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList nodes = null;
		try {
			nodes = (NodeList)xpath.evaluate(query, _node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (nodes == null)
			return new ArrayList<RXMLElement>();

		ArrayList<RXMLElement> resultNodes = new ArrayList<RXMLElement>();

		Node cur = _node.getFirstChild();
		while (cur != null)
		{
			RXMLElement element = RXMLElement.elementFromNode(cur);
			if (element != null)
				resultNodes.add(element);
            cur = cur.getNextSibling();
		}
		return resultNodes;
	}

	public void iterate(String query, Block blk)
	{
		if (query == null)
			return;

		String[] components = query.split("\\.");
		Node cur = _node;

		// Navigate down
		for (int i = 0; i < components.length; ++i)
		{
			String iTagName = components[i];

			if (iTagName.equals("*"))
			{
				cur = cur.getFirstChild();

				// different behavior depending on if this is the end of the query or midstream
				if (i < (components.length - 1))
				{
					do {
						if (cur.getNodeType() == Node.ELEMENT_NODE)
						{
							RXMLElement element = RXMLElement.elementFromNode(cur);
							int lengthOfSubStringArray = components.length - i - 1;
		    				String[] subArray = new String[lengthOfSubStringArray];
		    				System.arraycopy(components, i + 1, subArray, 0, lengthOfSubStringArray);
		    				String restOfQuery = implode(subArray, ".");
		    				element.iterate(restOfQuery, blk);
						}
                        cur = cur.getNextSibling();
					} while (cur != null);
				}
			}
			else
			{
				cur = cur.getFirstChild();
				while (cur != null)
				{
					if (cur.getNodeType() == Node.ELEMENT_NODE && cur.getNodeName().equals(iTagName))
						break;
					cur = cur.getNextSibling();
				}
			}
			if (cur == null)
				break;
		}

		if (cur != null)
		{
			// enumerate
    		String childTagName = (components.length > 0) ? components[components.length-1] : null;

    		do {
    			if (cur.getNodeType() == Node.ELEMENT_NODE)
    			{
    				RXMLElement element = RXMLElement.elementFromNode(cur);
    				blk.block(element);
    			}

    			if (childTagName.equals("*"))
    			{
    				cur = cur.getNextSibling();
    			}
    			else
    			{
    				while ((cur = cur.getNextSibling()) != null)
    				{
    					if (cur.getNodeType() == Node.ELEMENT_NODE && cur.getNodeName().equals(childTagName))
    						break;
    				}
    			}
    		} while (cur != null);
		}
	}

	public void iterateWithRootXPath(String query, Block blk)
	{
		ArrayList<RXMLElement> children = this.childrenWithRootXPath(query);
		this.iterateElements(children, blk);
	}

	public void iterateElements(ArrayList<RXMLElement> elements, Block blk)
	{
		for (RXMLElement element : elements)
		{
			blk.block(element);
		}
	}


	//=================================================
	// Random Utility method
	//=================================================
	public static String implode(String[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}

}
