package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMType {
    public final static VMType INT = new VMType("Integer");
    public final static VMType STRING = new VMType("String");
    public final static VMType BOOLEAN = new VMType("Boolean");
    public final static VMType ID = new VMType("ID");
    public final static VMType CLASS = new VMType("Class");

    public final static VMType REFERENCE = new VMType("<<REFERENCE>>");
    public final static VMType VOID = new VMType("<<VOID>>");
    public final static VMType NULL = new VMType("<<NULL>>");

    private String name;

    // TODO nested type (for array)

    public VMType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VMType{" +
                "name='" + name + '\'' +
                '}';
    }

    // TODO equals for type checking


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMType)) return false;

        VMType vmType = (VMType) o;

        if (!name.equals(vmType.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean canBeAssignedTo(VMObject value) {
        if (value.getType() != NULL && !value.getType().equals(this)) {
            return false;
            // TODO inherited type checking (iterate through object type and its super types)
            // TODO null can be assigned to anything
        }

        return true;
    }
}
