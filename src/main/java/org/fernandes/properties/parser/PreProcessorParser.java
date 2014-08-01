/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import org.fernandes.properties.PreProcessorContainer;
import static org.fernandes.properties.model.IncludeType.CLASSPATH;
import static org.fernandes.properties.model.IncludeType.FILE;
import static org.fernandes.properties.model.IncludeType.HTTP;
import static org.parboiled.BaseParser.EOI;
import org.parboiled.Rule;

/**
 * Parser used to process the includes of the preprocessor.
 * @author onepoint
 */
public class PreProcessorParser extends AbstractParser<PreProcessorContainer> {
    
    /**
     * The container used with the preprocessor content.
     */
    PreProcessorContainer preProcessorContainer = new PreProcessorContainer();
    
    public Rule main() {
        return sequence(lines(), EOI);
    }

    public Rule lines() {
        return oneOrMore(mainElement());
    }

    public Rule mainElement() {
        return firstOf(
                include(),
                normalText()
        );
    }
    
    public Rule normalText() {
        return firstOf(preprocessorCommand(), oneOrMore(testNot("!<"), ANY, push(preProcessorContainer.processText(match()))));
    }
    
    public Rule preprocessorCommand() {
        return sequence("!<", firstOf(define(), include(), defineVal()), ">");
    }
    
    /**
     * Example: !<classpath://hierarchicalProperties/include1.txt>
     * @return 
     */
    public Rule include() {
        return sequence(firstOf(CLASSPATH.getPrefix(), FILE.getPrefix(), HTTP.getPrefix()), 
                push(preProcessorContainer.processCurIncludeType(match())), ":", 
                oneOrMore(dirContent()), push(preProcessorContainer.processInclude(match())));
    }
    
    /**
     * Example: {@code !<def:env=production> }
     * @return rule for defines.
     */
    public Rule define() {
        return sequence(zeroOrMore(" "), "def", zeroOrMore(" "), ":", zeroOrMore(" "), 
                oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantKey(match())), 
                zeroOrMore(" "), "=", oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantVal(match())));
    }
    
    /**
     * Corresponds to a value of a define to be inserted.
     * Example: {@code !<def:env=production> }
     * @return a define to be inserted.
     */
    public Rule defineVal() {
        return sequence(zeroOrMore(" "), "$", oneOrMore(alphaNumericWithDot()), 
                push(preProcessorContainer.addDefineVal(match())), zeroOrMore(" "));
    }
    
    public Rule dirContent() {
        return noneOf(">");
    }
}
