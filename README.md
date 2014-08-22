HierarchicalProperties
======================

Hierarchical Properties is a small project for a parser and serializer of enriched properties for Java 8. 

It contains a parser for properties in a tree of nodes. It supports also node references and also allows includes and conditional statements via a pre-processor.

This project has also a serialiser. So now you can serialise and deserialise properties.

Pre-processor
-------------

As of now the pre-processor only has three features: it can process includes from the file system, 
classpath or from the web using http, it can process defined constants and <tt>if</tt> <tt>else</tt> clauses.

The typical syntax for a classpath pre-processor include is:

<code>!&lt;classpath://hierarchicalProperties/include1.txt&gt;</code>

This is how you define variables:

<code>!&lt;def:env=prod&gt;</code>

If's can also be included like this:

<code>!&lt;if:env == prod&gt;</code>

or you can use negation:

<code>!&lt;if:env != prod&gt;</code>

Else if is also possible, like this:

<code>!&lt;elseif:env == staging&gt;</code>

<blockquote>Note: the preprocessor commands are all enclosed in "!&lt;&gt;".</blockquote>

This is how you use the if clause combined with the else clause:

<pre>!&lt;if:env == prod&gt;
key3 = value3
!&lt;else&gt;
keyDef = !&lt;$env&gt;_val # First inclusion of pre-processor value.
keyDef2 = !&lt;$system&gt;_val # Second inclusion of pre-processor value.
!&lt;endif&gt;
</pre>

It is also possible to access environment and system variables using the pre-processor:

<strong>Environment:</strong>
<code>!&lt;$ENV.PAT&gt;</code>
<strong>Java system variable:</strong>
<code>!&lt;$SYS.os.name&gt;</code>

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

References are also supported:

<pre>
# Here is a reference to key4 and key6 on the same node
keyRef5 = ${/Test/hello/world:key4} ${/Test/hello/world:key4} ${/Test/hello/world:key6}
</pre>

Java Interfaces
---------------

<h5>Factories</h5>

There are three factories in this project:
<ul>
    <li><a href="https://github.com/gilfernandes/HierarchicalProperties/blob/master/src/main/java/org/fernandes/properties/factory/PreProcessorFactory.java">org.fernandes.properties.factory.PreProcessorFactory</a> - To be used just to pre-process files.</li>
    <li><a href="https://github.com/gilfernandes/HierarchicalProperties/blob/master/src/main/java/org/fernandes/properties/factory/HierarchicalPropertiesFactory.java">org.fernandes.properties.factory.HierarchicalPropertiesFactory</a> - 
        To be used to instantiate the hierarchical properties data structure.</li>
    <li><a href="https://github.com/gilfernandes/HierarchicalProperties/blob/master/src/main/java/org/fernandes/properties/factory/HierarchicalPreprocessorFactory.java">org.fernandes.properties.factory.HierarchicalPreprocessorFactory</a> - 
        To be used to pre-process and instantiate the hierarchical properties data structure at the same time.</li>
</ul>

Here is a simple JUnit based example on how to use these the <code>HierarchicalPreprocessorFactory</code>:

```java
HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstanceCp("hierarchicalProperties/map_if_types.txt");
org.junit.Assert.assertNotNull("The properties are null", props);
PropertyNode testProps = props.getNode("/Test");
Assert.assertNotNull("Test is null", props);
Boolean keyBoolean = testProps.getPropertyAsBoolean("keyBoolean");
Assert.assertNotNull("keyBoolean is null", keyBoolean);
Assert.assertTrue("keyBoolean is not true", keyBoolean);
Double keyDouble = testProps.getPropertyAsDouble("keyDouble");
Assert.assertNotNull("keyDouble is null", keyDouble);
Assert.assertTrue("keyDouble is not 2.345", keyDouble == 2.345);
Integer keyInt = testProps.getPropertyAsInt("keyInt");
Assert.assertNotNull("keyInt is null", keyInt);
Assert.assertTrue("keyInt is not 123", keyInt == 123);
```