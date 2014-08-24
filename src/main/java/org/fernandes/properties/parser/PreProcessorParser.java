/*
 OSSCUBE 2014
 */

package org.fernandes.properties.parser;

import org.fernandes.properties.model.ExternalEnvironment;
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
     * The system prefix.
     */
    static final java.lang.String SYS_PREFIX = ExternalEnvironment.SYS + ".";

    /**
     * The environment prefix.
     */
    static final java.lang.String ENV_PREFIX = ExternalEnvironment.ENV + ".";

    /**
     * The container used with the preprocessor content.
     */
    PreProcessorContainer preProcessorContainer = new PreProcessorContainer();

    public Rule main() {
        return sequence(lines(), EOI);
    }

    public Rule lines() {
        return oneOrMore(textOrCommand());
    }

    public Rule textOrCommand() {
        return firstOf(preprocessorCommand(), oneOrMore(testNot("!<"), ANY, push(preProcessorContainer.processText(match()))));
    }

    /**
     * Returns a rule for a pre-processor command, one of a define and include and define value
     * or an if component.
     * @return a rule for a pre-processor command, one of a define and include and define value
     * or an if component.
     */
    public Rule preprocessorCommand() {
        return sequence("!<", firstOf(
                define(),
                include(),
                defineVal(),
                ifStart(),
                elseIfStart(),
                ifEnd(),
                ifElse(),
                forStart(),
                endFor()),
        ">");
    }

    /**
     * Example: for i = 1 : 10 or for i = 1 : 2 : 10
     * Returns a rule for a simple numeric for loop.
     * @return a rule for a simple numeric for loop.
     */
//    public Rule forStart() {
//        return sequence("for", sequence(mandatorySpaces(),
//                oneOrMore(alpha()), push(preProcessorContainer.forVar(match())), optionalSpaces(),
//                "=", optionalSpaces(),
//                oneOrMore(digit()), push(preProcessorContainer.forStart(match())), ":", optionalSpaces(),
//                oneOrMore(digit()), optionalSpaces(),
//                optional(":", oneOrMore(digit(), optionalSpaces()))));
//    }

    /**
     * Example: for i = 1 : 10 or for i = 1 : 2 : 10
     * Returns a rule for a simple numeric for loop.
     * @return a rule for a simple numeric for loop.
     */
    public Rule forStart() {
        return sequence("for", mandatorySpaces(),
                oneOrMore(alpha()), push(preProcessorContainer.forVar(match())), optionalSpaces(),
                "=", optionalSpaces(), oneOrMore(digit()), push(preProcessorContainer.forStart(match())), optionalSpaces(),
                ":", optionalSpaces(), oneOrMore(digit()), push(preProcessorContainer.forEndOrStep(match())), optionalSpaces(),
                optional(":", oneOrMore(digit(), optionalSpaces())));
    }

    /**
     * Returns a rule for ending the for loop.
     * @return a rule for ending the for loop.
     */
    public Rule endFor() {
        return sequence(optionalSpaces(), string("endfor"), push(preProcessorContainer.forEnd()), optionalSpaces());
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
        return sequence(optionalSpaces(), "def", optionalSpaces(), ":", optionalSpaces(),
                oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantKey(match())),
                optionalSpaces(), "=", optionalSpaces(), oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.addConstantVal(match())));
    }

    /**
     * Corresponds to a value of a define to be inserted.
     * Example: {@code !<def:env=production> }
     * @return a define to be inserted.
     */
    public Rule defineVal() {
        final Rule varVal = oneOrMore(alphaNumericWithDot());
        return sequence(optionalSpaces(), "$", firstOf(
                sequence(ENV_PREFIX, varVal),
                sequence(SYS_PREFIX, varVal),
                varVal), push(preProcessorContainer.addDefineVal(match())), optionalSpaces());
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
        return sequence(optionalSpaces(), prefix, optionalSpaces(), ":", optionalSpaces(),
                oneOrMore(alphaNumericWithDot()),
                    isIf ? push(preProcessorContainer.ifStartVar(match())) : push(preProcessorContainer.elseIfStartVar(match())),
                optionalSpaces(), firstOf("==", "!="), push(preProcessorContainer.ifOperator(match())), optionalSpaces(),
                oneOrMore(alphaNumericWithDot()), push(preProcessorContainer.ifStartVal(match())));
    }

    /**
     * The end of the if statement.
     * @return a rule parsing the syntax of the end of the if statement.
     */
    public Rule ifEnd() {
        return sequence(optionalSpaces(), "endif", push(preProcessorContainer.ifEnd()), optionalSpaces());
    }


    /**
     * Returns a rule with the else start.
     * @return a rule with the else start.
     */
    public Rule ifElse() {
        return sequence(optionalSpaces(), "else", push(preProcessorContainer.ifElse()), optionalSpaces());
    }
    /**
     * Returns a rule for the content of a URL. Used in an include.
     * @return a rule for the content of a URL.
     */
    public Rule urlContent() {
        return noneOf(">");
    }
}
