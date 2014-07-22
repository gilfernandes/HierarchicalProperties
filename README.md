HierarchicalProperties
======================

Hierarchical Properties is a small project for a parser and serializer of enriched properties for Java 7+. 

It contains a parser for properties in a tree of nodes. It supports also node references and also allows includes via a pre-processor.

This project has also a serialiser. So now you can serialise and deserialise properties.

Main features
-------------

Properties are stored in a tree. A property looks like e.g:

<code>key2 = value2</code>

or

<code>key2 : value2</code>


The nodes are defined in this format: 

<code>[/level1]</code>
<code>[/level1/level2]</code>

If no node is specified you are simply writing a property in the root node.

Two types of comments are also supported:

<ul>
<li>Single line comments:
<code># this is a single line comment</code>
</li>

<li>Multi-line comments
<code>/*  This is a multiline comment !!!!! 

With another line.
*/</code>
</li>
</ul>
