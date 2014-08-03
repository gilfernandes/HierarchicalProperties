/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

/**
 * Base parser for all parsers in this project with common rules.
 * @author onepoint
 */
public class AbstractParser <T> extends BaseParser<T> {
    
    /**
     * Alphanumeric rule.
     * @return rule for numbers and ASCII characters.
     */
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
     * Returns a rule that recognises optional multiple spaces.
     * @return a rule that recognises optional multiple spaces. 
     */
    protected Rule spaces() {
        return zeroOrMore(" ");
    }
}
