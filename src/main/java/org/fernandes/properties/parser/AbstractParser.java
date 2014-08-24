/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

/**
 * Base parser for all parsers in this project with common rules.
 * @author onepoint
 * @param <T> The model object to be manipulated by this parser.
 */
public class AbstractParser <T> extends BaseParser<T> {

    /**
     * Alphanumeric rule.
     * @return rule for numbers and ASCII characters.
     */
    public Rule alphaNumeric() {
        return firstOf(charRange('0', '9'), charRange('A', 'Z'), charRange('a', 'z'));
    }

    /**
     * Returns a rule that matches an alphanumeric character or a dot.
     * @return a rule that matches an alphanumeric character or a dot.
     */
    public Rule alphaNumericWithDot() {
        return firstOf(alphaNumeric(), '.');
    }

    /**
     * Returns a rule that matches most characters in the extended ascii range.
     * @return a rule that matches most characters in the extended ascii range.
     */
    public Rule generalText() {
        return firstOf(charRange(' ', '~'), charRange('\u0080', '\u00ff'));
    }

    /**
     * Returns a rule that recognises optional multiple optionalSpaces.
     * @return a rule that recognises optional multiple optionalSpaces.
     */
    protected Rule optionalSpaces() {
        return zeroOrMore(spacechar());
    }

    /**
     * Returns a rule that recognises mandatory multiple optionalSpaces.
     * @return a rule that recognises mandatory multiple optionalSpaces.
     */
    protected Rule mandatorySpaces() {
        return oneOrMore(spacechar());
    }

    /**
     * Space characters including the tab.
     * @return a rule for space characters including the tab.
     */
    public Rule spacechar() {
        return anyOf(" \t");
    }

}
