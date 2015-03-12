package cz.cvut.fit.run.interpreter.core.functions.binary;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.functions.VMStackFunction;
import cz.cvut.fit.run.interpreter.core.types.VMIdentifier;
import cz.cvut.fit.run.interpreter.core.types.VMString;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMAssignment implements VMStackFunction {
    @Override
    public Object call() throws VMException {
        VMObject value = VMMachine.pop();
        VMObject identifier = VMMachine.pop();

        System.out.println("Assigning " + identifier + " = " + value);

        VMMachine.getFrame().assignVariable((VMIdentifier) identifier, value);
        return null;
    }
}
