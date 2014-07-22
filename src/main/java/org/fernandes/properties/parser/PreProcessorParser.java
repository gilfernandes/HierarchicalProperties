/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import org.fernandes.properties.PreProcessorContainer;
import static org.fernandes.properties.model.IncludeType.CLASSPATH;
import static org.fernandes.properties.model.IncludeType.FILE;
import static org.fernandes.properties.model.IncludeType.HTTP;
import org.parboiled.BaseParser;
import static org.parboiled.BaseParser.EOI;
import org.parboiled.Rule;

/**
 * Parser used to process the includes of the preprocessor.
 * @author onepoint
 */
public class PreProcessorParser extends BaseParser<PreProcessorContainer> {
    
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
        return firstOf(include(), oneOrMore(testNot("!<"), ANY, push(preProcessorContainer.processText(match()))));
    }
    
    public Rule include() {
        return sequence("!<", firstOf(CLASSPATH.getPrefix(), FILE.getPrefix(), HTTP.getPrefix()), 
                push(preProcessorContainer.processCurIncludeType(match())), ":", 
                oneOrMore(path()), push(preProcessorContainer.processInclude(match())), ">");
    }
    
    public Rule path() {
        return noneOf(">");
    }
    
}