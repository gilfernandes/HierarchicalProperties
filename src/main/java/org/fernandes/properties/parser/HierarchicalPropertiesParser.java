/*
 OSSCUBE 2014
 */
package org.fernandes.properties.parser;

import org.fernandes.properties.model.DefaultHierarchicalProperties;
import org.fernandes.properties.model.ExternalEnvironment;
import static org.parboiled.BaseParser.EOI;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressSubnodes;

/**
 * Parser containing the parsing rules for the hierarchical properties.
 *
 * @author onepoint
 */
@BuildParseTree
public class HierarchicalPropertiesParser extends AbstractParser<DefaultHierarchicalProperties> {

    /**
     * The domain object to be filled with data.
     */
    DefaultHierarchicalProperties props = new DefaultHierarchicalProperties();

    public Rule main() {
        return sequence(lines(), EOI);
    }

    public Rule lines() {
        return oneOrMore(mainElement());

    }

    public Rule mainElement() {
        return firstOf(
                sequence(comment(), multipleNewLines()),
                sequence(multilineComment(), multipleNewLines()),
                sequence(expression(), multipleNewLines()),
                sequence(categoryNode(), multipleNewLines())
        );
    }

    public Rule multipleNewLines() {
        return zeroOrMore(newline());
    }

    public Rule expression() {
        return sequence(
                alphaNumerics(), push(props.putKey(match())),
                separator(),
                value(), push(props.putValue(match()))
        );
    }

    @SuppressSubnodes
    public Rule alphaNumerics() {
        return oneOrMore(alphaNumeric());
    }

    /**
     * Extracts a textual value, but allows the line comment.
     * @return a rule that extract the property value or a comment at the end.
     */
    @SuppressSubnodes
    public Rule value() {
        // Supports also single line comments after the value.
        return firstOf(
                oneOrMore(
                        firstOf(sequence('\\', newline()),
                                ENV(),
                                SYS(),
                                reference(),
                                generalTextNoComment())
                ),
                sequence(comment(), newline()) // You can also put a line comment after a value.
        );
    }

    /**
     * Returns a rule that matches most characters in the extended ascii range
     * except the "#" when not preceded by a backslash.
     *
     * @return a rule that matches most characters in the extended ascii range.
     */
    public Rule generalTextNoComment() {
        return firstOf("\\#", charRange(' ', '"'), charRange('$', '~'), charRange('\u0080', '\u00ff'));
    }

    /**
     * Allows optional separator, either ":", "="
     *
     * @return the separator rule allowing 2 separators and space around it.
     */
    public Rule separator() {
        return sequence(zeroOrMore(' '), firstOf(':', '='), zeroOrMore(' '));
    }

    /**
     * Extract one system environment property which is to be injected, like e.g
     * $PATH.
     *
     * @return rule for extraction of one system environment property which is
     * to be injected, like e.g $PATH.
     */
    public Rule ENV() {
        return ELRule(ExternalEnvironment.ENV.toString());
    }

    /**
     * Extract one system environment property which is to be injected, like e.g
     * $PATH.
     *
     * @return rule for extraction of one system environment property which is
     * to be injected, like e.g $PATH.
     */
    public Rule SYS() {
        return ELRule(ExternalEnvironment.SYS.toString());
    }

    /**
     * Extracts the expression language variables. Supports "ENV" or "SYS"
     * variables.
     *
     * @param classProp The class of the value to change ("ENV or "SYS").
     * @return rule for extraction of the expression language variables.
     * Supports "ENV" or "SYS" variables.
     */
    public Rule ELRule(java.lang.String classProp) {
        return sequence("${", classProp, '.', oneOrMore(alphaNumericWithDot()), push(props.putCurEnvVarMap(classProp, match())), "}");
    }

    public Rule reference() {
        return sequence("$", "{", oneOrMore(firstOf(alphaNumeric(), '.', '/')), push(props.putReferenceNode(match())),
                separator(), oneOrMore(alphaNumericWithDot()), push(props.putReferenceValue(match())), "}");
    }

    public Rule blankLine() {
        return sequence(spaces(), newline());
    }

    @Override
    public Rule spaces() {
        return zeroOrMore(spacechar());
    }

    public Rule spacechar() {
        return anyOf(" \t");
    }

    public Rule newline() {
        return firstOf('\n', sequence('\r', optional('\n')));
    }

    public Rule comment() {
        return sequence(zeroOrMore(' '), firstOf("//", "#"), zeroOrMore(generalText()),
                push(props.addLineComment(match())));
    }

    public Rule multilineComment() {
        return sequence("/*", zeroOrMore(testNot("*/"), ANY), push(props.addMultilineComment(match())), "*/");
    }

    public Rule categoryNode() {
        return sequence(zeroOrMore(' '), ch('['), zeroOrMore(' '), optional('/'),
                zeroOrMore(alphaNumerics(), optional(ch('/'))), push(props.createNodes(match())),
                ch(']'),
                zeroOrMore(' '), multipleNewLines());
    }
}
