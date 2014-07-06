/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

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
public class HierarchicalPropertiesParser extends BaseParser<HierarchicalProperties> {

    HierarchicalProperties props = new HierarchicalProperties();
    
    public Rule Main() {
        return sequence(Lines(), EOI);
    }

    public Rule Lines() {
        return oneOrMore(MainElement());
                
    }

    public Rule MainElement() {
        return firstOf(
                sequence(Comment(), zeroOrMore(newline())),
                sequence(MultilineComment(), zeroOrMore(newline())),
                sequence(Expression(), zeroOrMore(newline())),
                sequence(CategoryNode(), zeroOrMore(newline()))
        );
    }

    public Rule Expression() {
        return sequence(
                AlphaNumerics(), push(props.putKey(match())),
                Separator(), 
                Value(), push(props.putValue(match()))
        );
    }

    @SuppressSubnodes
    public Rule AlphaNumerics() {
        return oneOrMore(AlphaNumeric());
    }
    
    @SuppressSubnodes
    public Rule Value() {
        // Supports multiline values using FirstOf
        return oneOrMore(firstOf(sequence('\\', newline()), ENV(), SYS(), Reference(), GeneralText()));
    }

    public Rule AlphaNumeric() {
        return firstOf(charRange('0', '9'), charRange('A', 'Z'), charRange('a', 'z'));
    }
    
    public Rule AlphaNumericWithDot() {
        return FirstOf(AlphaNumeric(), '.');
    }
    
    public Rule GeneralText() {
        return firstOf(charRange(' ', '~'), charRange('\u0080', '\u00ff'));
    }

    /**
     * Allows optional separator, either ":", "="
     * @return the separator rule allowing 2 separators and space around it.
     */
    public Rule Separator() {
        return Sequence(ZeroOrMore(' '), FirstOf(':', '='), ZeroOrMore(' '));
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
        return Sequence("${", classProp, '.', OneOrMore(AlphaNumericWithDot()), push(props.putCurEnvVarMap(classProp, match())), "}");
    }
    
    public Rule Reference() {
        return Sequence("$", "{", OneOrMore(FirstOf(AlphaNumeric(), '.', '/')), push(props.putReferenceNode(match())), 
                        Separator(), OneOrMore(AlphaNumericWithDot()), push(props.putReferenceValue(match())), "}");
    }
    
    public Rule BlankLine() {
        return sequence(Sp(), newline());
    }

    public Rule Sp() {
        return zeroOrMore(spacechar());
    }

    public Rule spacechar() {
        return anyOf(" \t");
    }

    public Rule newline() {
        return firstOf('\n', sequence('\r', optional('\n')));
    }
    
    public Rule Comment() {
        return Sequence(ZeroOrMore(' '), FirstOf("//", "#"), ZeroOrMore(GeneralText()), ZeroOrMore(newline()));
    }
    
    public Rule MultilineComment() {
        return sequence("/*", zeroOrMore(noneOf("*/")), push(props.addLineComment(match())), "*/");
    }
    
    public Rule CategoryNode() {
        return Sequence(ZeroOrMore(' '), Ch('['), ZeroOrMore(' '), Optional('/'), 
                ZeroOrMore(AlphaNumerics(), Optional(Ch('/'))), push(props.createNodes(match())),
                Ch(']'), 
                ZeroOrMore(' '), ZeroOrMore(newline()));
    }
}
