HierarchicalProperties
======================

Hierarchical Properties is a small project for a parser and serializer of enriched properties for Java 8. 

It contains a parser for properties in a tree of nodes. It supports also node references and also allows includes via a pre-processor.

This project has also a serialiser. So now you can serialise and deserialise properties.

Main features
-------------

Properties are stored in a tree. A property looks like e.g:

<code>key2 = value2</code>

or

<code>key2 : value2</code>


The tree nodes are defined in this format: 

<code>[/level1]</code><br/>
<code>[/level1/level2]</code><br/>
<code>[/level1/level2/level3]</code>
If no node is specified you are simply writing a property in the root node.

Two types of comments are also supported:

<ul>
<li>Single line comments:<br />
<code># this is a single line comment</code>
</li>

<li>Multi-line comments:<br />
<pre>/*  This is a multiline comment !!!!! 

With another line.
*/</pre>
</li>
</ul>
