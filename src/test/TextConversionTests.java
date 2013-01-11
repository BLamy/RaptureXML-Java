package test;

import junit.framework.TestCase;
import org.junit.Before;

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

    - (void)testIntTags {
    RXMLElement *rxml = [RXMLElement elementFromXMLString:simplifiedXML_ encoding:NSUTF8StringEncoding];
    __block NSInteger i = 0;

    [rxml iterate:@"*" usingBlock:^(RXMLElement *e) {
        if (i == 0) {
            STAssertEquals([e child:@"id"].textAsInt, 1, nil);
        } else if (i == 1) {
            STAssertEqualsWithAccuracy([e child:@"id"].textAsDouble, 2.5, 0.01, nil);
        }

        i++;
    }];
}

    - (void)testIntAttributes {
    RXMLElement *rxml = [RXMLElement elementFromXMLString:attributedXML_ encoding:NSUTF8StringEncoding];
    __block NSInteger i = 0;

    [rxml iterate:@"*" usingBlock:^(RXMLElement *e) {
        if (i == 0) {
            STAssertEquals([e attributeAsInt:@"id"], 1, nil);
        } else if (i == 1) {
            STAssertEqualsWithAccuracy([e attributeAsDouble:@"id"], 2.5, 0.01, nil);
        } else if (i == 2) {
            STAssertEquals([e attributeAsInt:@"id"], 3, nil);
        }

        i++;
    }];
}
}
