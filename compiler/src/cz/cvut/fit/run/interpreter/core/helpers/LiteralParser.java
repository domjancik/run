package cz.cvut.fit.run.interpreter.core.helpers;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMNull;
import cz.cvut.fit.run.interpreter.core.types.instances.*;
import cz.cvut.fit.run.interpreter.core.types.classes.VMBoolean;
import cz.cvut.fit.run.interpreter.core.types.classes.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.classes.VMString;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class LiteralParser {
    private static final Pattern STRING_PATTERN = Pattern.compile("^\".*\"$");
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(false|true)$");

    public static VMPointer parseLiteral(String literalString) throws VMException {
        // Null
        if (literalString.equals("null"))
            return VMPointer.NULL_POINTER;

        // String
        Matcher m = STRING_PATTERN.matcher(literalString);
        if (m.matches()) return parseString(literalString);

        // Boolean
        m = BOOLEAN_PATTERN.matcher(literalString);
        if (m.matches()) return parseBoolean(literalString);

        // Integer
        return parseInt(literalString);
    }

    public static VMPointer parseBoolean(String string) throws VMException {
        switch (string) {
            case "false": return VMPointer.FALSE_POINTER;
            case "true": return VMPointer.TRUE_POINTER;
            default: throw new IllegalArgumentException();
        }
    }

    public static VMPointer parseString(String string) throws VMException {
        String strippedString = string.substring(1, string.length() - 1); // TODO \ sequences
        return ((VMString)VMMachine.getInstance().getClazz("String")).createInstance(strippedString);
    }

    public static VMPointer parseInt(String string) throws VMException {
        int intValue = Integer.parseInt(string);
        return ((VMInteger)VMMachine.getInstance().getClazz("Integer")).createInstance(intValue);
    }
}
