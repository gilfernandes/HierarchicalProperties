/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import static org.fernandes.properties.model.IncludeType.CLASSPATH;
import static org.fernandes.properties.model.IncludeType.FILE;
import static org.fernandes.properties.model.IncludeType.HTTP;
import org.fernandes.properties.model.PreProcessorContainer;
import static org.parboiled.BaseParser.EOI;
import org.parboiled.Rule;

/**
 * Parser used to process the includes of the preprocessor.
 * @author onepoint
 */
public class PreProcessorParser extends AbstractParser<PreProcessorContainer> {

    /**
     * Used for the if statements.
     */
    private static final java.lang.String IF_TOKEN = "if";

    /**
     * Used for the else if statements.
     */
    private static final java.lang.String ELSEIF_TOKEN = "elseif";

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

    /**
     * Returns a rule for a pre-processor command, one of a define and include and define value
     * or an if component.
     * @return a rule for a pre-processor command, one of a define and include and define value
     * or an if component.
     */
    public Rule preprocessorCommand() {
        return sequence("!<", firstOf(define(), include(), defineVal(), ifStart(), elseIfStart(), ifEnd(), ifElse()), ">");
    }

    /**
     * Example: !<classpath://hierarchicalProperties/include1.txt>
     * @return rule for include.
     */
    public Rule include() {
        return sequence(firstOf(CLASSPATH.getPrefix(), FILE.getPrefix(), HTTP.getPrefix()),
                push(preProcessorContainer.processCurIncludeType(match())), ":",
                oneOrMore(urlContent()), push(preProcessorContainer.processInclude(match())));
    }

    /**
     * Example: {@code !<def:env=production> }
     * @return rule for defines.
     */
    public Rule define() {
        return sequence(spaces(), "def", spaces(), ":", spaces(),
                oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantKey(match())),
                spaces(), "=", spaces(), oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantVal(match())));
    }

    /**
     * Corresponds to a value of a define to be inserted.
     * Example: {@code !<def:env=production> }
     * @return a define to be inserted.
     */
    public Rule defineVal() {
        return sequence(spaces(), "$", oneOrMore(alphaNumericWithDot()),
                push(preProcessorContainer.addDefineVal(match())), spaces());
    }

    /**
     * Example: !<if:env == prod>
     * @return the rule for the if start.
     */
    public Rule ifStart() {
        return ifRule(IF_TOKEN);
    }

    /**
     * Example: !<if:env == prod>
     * @return the rule for the if start.
     */
    public Rule elseIfStart() {
        return ifRule(ELSEIF_TOKEN);
    }

    /**
     * Example: !<if:env == prod>
     * @param prefix The prefix for this operation. Can be "if" for example.
     * @return the rule for the if start.
     */
    public Rule ifRule(String prefix) {
        boolean isIf = IF_TOKEN.equals(prefix);
        return sequence(spaces(), prefix, spaces(), ":", spaces(),
                oneOrMore(alphaNumericWithDot()),
                    isIf ? push(preProcessorContainer.ifStartVar(match())) : push(preProcessorContainer.elseIfStartVar(match())),
                spaces(), firstOf("==", "!="), push(preProcessorContainer.ifOperator(match())), spaces(),
                oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.ifStartVal(match())));
    }

    /**
     * The end of the if statement.
     * @return a rule parsing the syntax of the end of the if statement.
     */
    public Rule ifEnd() {
        return sequence(spaces(), "endif", push(preProcessorContainer.ifEnd()), spaces());
    }


    /**
     * Returns a rule with the else start.
     * @return a rule with the else start.
     */
    public Rule ifElse() {
        return sequence(spaces(), "else", push(preProcessorContainer.ifElse()), spaces());
    }
    /**
     * Returns a rule for the content of a URL. Used in an include.
     * @return a rule for the content of a URL.
     */
    public Rule urlContent() {
        return noneOf(">");
    }
}
