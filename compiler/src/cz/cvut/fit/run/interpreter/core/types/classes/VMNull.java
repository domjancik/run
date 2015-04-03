package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.IllegalOperationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMNullInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public final class VMNull extends VMClass {
    private static VMNull instance;

    public VMNull() throws VMException {
        super(VMType.NULL);
    }

    public static VMNull getInstance() throws VMException {
        if (instance == null)
            instance = new VMNull();

        return instance;
    }

    @Override
    public VMPointer createInstance(VMPointer... args) throws VMException {
        throw new IllegalOperationException("Can not create a new null object");
    }
}
