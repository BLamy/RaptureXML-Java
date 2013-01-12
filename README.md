RaptureXML-Java
===============

A port of [John Blanco's RaptureXML](https://github.com/ZaBlanc/RaptureXML) to Java





RaptureXML is a simple, block-based XML library written in java that provides an expressive API that makes XML processing freakin' fun for once in my life.

# Why do we need *another* XML library? #

You tell me.  Processing XML in Java is an awful, frustrating experience and the resulting code is never readable.  I'm tired of it! RaptureXML solves this by providing a *powerful* new interface on top of libxml2.  Imagine for a minute the code you'd write to process the XML for a list of baseball team members, retrieving their numbers, names, and positions using your favorite XML processing library.  Now, take a look at how you do it with RaptureXML:

	File file = new File(DeepChildrenTests.class.getResource("players.xml").toURI());
    RXMLElement rxml = RXMLElement.elementFromFile(file);
    rxml.iterate("players.player", new RXMLElement.Block()
    {
         @Override
         public void block(RXMLElement player)
         {
             System.out.println(Player: "+player.child("name").text()+" (#"+player.child("number").text()+")");
         }
     });


RaptureXML changes the game when it comes to XML processing in java.  As you can see from the code, it takes only seconds to understand what this code does.  There are no wasted arrays and verbose looping you have to do.  The code is a breeze to read and maintain.

I don't think any more needs to be said.

# Adding RaptureXML to Your Project #

There's just a few simple steps:

  * Copy the RaptureXML/RaptureXML folder into your own project and import "RXMLElement.h" somewhere (e.g., your PCH file).
  * Link in libz.dylib to your target.
  * Link in libxml2.dylib to your target.
  * In your build settings, for the key "Header Search Paths", add "$(SDK_DIR)"/usr/include/libxml2

RaptureXML supports ARC.  In fact, it does so without a branch.  The code automatically detects if ARC is being used in your project and compiles accordingly.  You are free to use any version of LLVM or gcc as well! (Though you should be using LLVM by now.)

# Getting Started #

RaptureXML processes XML in two steps: load and path.  This means that you first load the XML from any source you want such as file, data, string, or even from a URL.  Then, you simply use its query language to find what you need.

You can load the XML with any of the following constructors:

	RXMLElement rootXML = RXMLElement.elementFromXMLString("...my xml...");
	RXMLElement rootXML = RXMLElement.elementFromXMLFile(new File(DeepChildrenTests.class.getResource("myfile.xml").toURI());

These constructors return an RXMLElement object that represents the top-level tags. Now, you can query the data in any number of ways.

Let's pretend your XML looks like this:

	<team year="2011" name="New York Mets">
		<players>
			<coach>
				<name>Terry Collins</name>
				<year>1</year>
			</coach>

			<player number="7">
				<name>Jose Reyes</name>
				<position>SS</position>
			</player>

			<player number="16">
				<name>Angel Pagan</name>
				<position>CF</position>
			</player>

			<player number="5">
				<name>David Wright</name>
				<position>3B</position>
			</player>

			...

		</players>
	</team>

First, we'd load the XML:

	RXMLElement rootXML = RXMLElement.elementFromXMLFile("players.xml");

We can immediately query the top-level tag name:

	rootXML.tag() --> "team"

We can read attributes with:

	rootXML.attribute("year") --> "2011"
	rootXML.attribute("name") --> "New York Mets"

We can get the players tag with:

	RXMLElement rxmlPlayers = rootXML.child("players")];

If we like, we can get all the individual player tags with:

	ArrayList<RXMLElement> children = rxmlPlayers.children("player");

From there, we can process the individual players and be happy.  Now, this is already much better than any other XML library we've seen, but RaptureXML can use query paths to make this ridiculously easy.  Let's use query paths to improve the conciseness our code:

    rxml.iterate("players.player", new RXMLElement.Block()
    {
        @Override
        public void block(RXMLElement player)
        {
            System.out.println(Player: "+player.child("name").text()+" (#"+player.child("number").text()+")");
        }
    });



Query paths are even more powerful with wildcards.  Let's say we wanted the name of every person on the team, player or coach.  We use the wildcard to get it:

    rxml.iterate("players.*.name", new RXMLElement.Block()
    {
        @Override
        public void block(RXMLElement name)
        {
            System.out.println("name: " + name.tag());
        }
    });

The wildcard processes every tag rather than the one you would've named.  You can also use the wildcard to iterate all the children of an element:

    rxml.iterate("players.coach.*", new RXMLElement.Block()
    {
        @Override
        public void block(RXMLElement element)
        {
            System.out.println("Tag: " + element.tag() + " Text: " + element.text());
        }
    });

This gives us all the tags for the coach.  Easy enough?

# XPath #

If you don't want to use the custom RaptureXML iteration query syntax, you can use the standard XPath query language as well.  Here's how you query all players with XPath:

    rootXML.iterateWithRootXPath("//player", new RXMLElement.Block()
    {
        @Override
        public void block(RXMLElement element)
        {
            System.out.println("Player:" + player.child(name).text() + " (" + player.child("number").text()+")");
        }
    });

And remember, you can also test attributes using XPath as well. Here's how you can find the player with #5:

    rootXML.iterateWithRootXPath("//player[number='5']", new RXMLElement.Block()
    {
        @Override
        public void block(RXMLElement element)
        {
            System.out.println("Player #5: "+player.child("name"));
        }
    });

Note that you can only use XPath from the document root and it won't matter what RXMLElement you have.  If you have a derived RXMLElement, you can still build from the document root. If you're not familiar with XPath, you can use this [handy guide](http://www.w3schools.com/xpath/xpath_syntax.asp).

# Namespaces #

Namespaces are supported for most methods, however not for iterations.  If you want to use namespaces for that kind of thing, use the -children method manually.  When specifying namespaces, be sure to specify the namespace URI and *not* the prefix.  For example, if your XML looked like:

	<team xmlns:sport="*" sport:year="2011" sport:name="New York Mets">
		...
	</team>

You would access the attributes with:

	System.out.println("Team Name: "+ e.attribute("name", "*"));

# Unit Tests as Documentation #

You can see the full usage of RaptureXML by reading the unit tests in the project.  Not only does it show you all the code, but you'll know it works! (You can run the unit test using the included junit-4.11.jar)

# Who Created RaptureXML? #

RaptureXML was created by John Blanco <john.blanco@raptureinvenice.com> of Rapture In Venice because he got sick of using all of the bizarre XML solutions for iPhone development.  If you like this code and/or need an iOS consultant, get in touch with me via my website, [Rapture In Venice](http://raptureinvenice.com).
RaptureXML was ported to java by Brett Lamy <bel423@me.com>


# Differences between the Java port and the original Objective-c RaptureXML
None of the functions in RaptureXML-java will return a null value. One of the greatest things of this libary is it's elegent syntax and needing to perform sanity checks on every variable would ruin it.
Objective-c will not throw null pointer exceptions given the following code if name does not exist.

	[rxml child:@"name"].text;
However, in java if no element with name exist the following will crash

	rxml.child("name").text();

What does this mean?
When checking to see if a element exist you must check using if(rxml.child("name").isValid()) instead of if(rxml.child("name")==null)