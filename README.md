HierarchicalProperties
======================

Hierarchical Properties is a small project for a parser and serializer of enriched properties for Java 7+. 

It contains a parser for properties in a tree of nodes. It supports also node references and also allows includes via a pre-processor.

This project is not yet in alpha stage, because the serializer is not yet there.

Main features
-------------

Properties are stored in a tree. A property looks like e.g:

<code>key2 = value2</code>

or

<code>key2 : value2</code>


The nodes are defined in this format: 

<code>[/Test]</code>

If no node is specified you are simply writing a property in the root node.
