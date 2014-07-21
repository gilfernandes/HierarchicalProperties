/*
 OSSCUBE 2014
 */
package org.fernandes.properties.parser;

import org.fernandes.properties.DefaultHierarchicalProperties;
import org.parboiled.BaseParser;
import static org.parboiled.BaseParser.EOI;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressSubnodes;

/**
 * Parser containing the parsing rules for the hierarchical properties.
 * @author onepoint
 */
@BuildParseTree
public class HierarchicalPropertiesParser extends BaseParser<DefaultHierarchicalProperties> {

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
                sequence(comment(), zeroOrMore(newline())),
                sequence(multilineComment(), zeroOrMore(newline())),
                sequence(expression(), zeroOrMore(newline())),
                sequence(categoryNode(), zeroOrMore(newline()))
        );
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
    
    @SuppressSubnodes
    public Rule value() {
        // Supports multiline values using FirstOf
        return oneOrMore(firstOf(sequence('\\', newline()), ENV(), SYS(), reference(), generalText()));
    }

    public Rule alphaNumeric() {
        return firstOf(charRange('0', '9'), charRange('A', 'Z'), charRange('a', 'z'));
    }
    
    public Rule alphaNumericWithDot() {
        return firstOf(alphaNumeric(), '.');
    }
    
    public Rule generalText() {
        return firstOf(charRange(' ', '~'), charRange('\u0080', '\u00ff'));
    }

    /**
     * Allows optional separator, either ":", "="
     * @return the separator rule allowing 2 separators and space around it.
     */
    public Rule separator() {
        return sequence(zeroOrMore(' '), firstOf(':', '='), zeroOrMore(' '));
    }
    
    /**
     * Extract one system environment property which is to be injected, like e.g $PATH.
     * @return rule for extraction of one system environment property which is to be injected, like e.g $PATH.
     */
    public Rule ENV() {
        return ELRule("ENV");
    }
    
    /**
     * Extract one system environment property which is to be injected, like e.g $PATH.
     * @return rule for extraction of one system environment property which is to be injected, like e.g $PATH.
     */
    public Rule SYS() {
        return ELRule("SYS");
    }
    
    /**
     * Extracts the expression language variables. Supports "ENV" or "SYS" variables.
     * @param classProp The class of the value to change ("ENV or "SYS").
     * @return rule for extraction of the expression language variables. Supports "ENV" or "SYS" variables.
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
                push(props.addLineComment(match())), newline());
    }
    
    public Rule multilineComment() {
        return sequence("/*", zeroOrMore(testNot("*/"), ANY), push(props.addMultilineComment(match())), "*/");
    }
    
    public Rule categoryNode() {
        return sequence(zeroOrMore(' '), ch('['), zeroOrMore(' '), optional('/'), 
                zeroOrMore(alphaNumerics(), optional(ch('/'))), push(props.createNodes(match())),
                ch(']'), 
                zeroOrMore(' '), zeroOrMore(newline()));
    }
}
