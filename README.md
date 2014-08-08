HierarchicalProperties
======================

Hierarchical Properties is a small project for a parser and serializer of enriched properties for Java 8. 

It contains a parser for properties in a tree of nodes. It supports also node references and also allows includes and conditional statements via a pre-processor.

This project has also a serialiser. So now you can serialise and deserialise properties.

Pre-processor
-------------

As of now the pre-processor only has three features: it can process includes from the file system, 
classpath or from the web using http, it can process defined constants and if clauses.

The typical syntax for a classpath pre-processor include is:

<code>!&lt;classpath://hierarchicalProperties/include1.txt&gt;</code>

This is how you define variables:

<code>!&lt;def:env=prod&gt;</code>

If's can also be included like this:

<code>!&lt;if:env == prod&gt;</code>

or you can use negation:

<code>!&lt;if:env != prod&gt;</code>


Properties - main features
--------------------------

Properties are stored in a tree. A property looks like e.g:

<code>key2 = value2</code>

or

<code>key2 : value2</code>


The tree nodes are defined in this format: 

<code>[/level1]</code><br/>
<code>[/level1/level2]</code><br/>
<code>[/level1/level2/level3]</code><br/><br/>
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
