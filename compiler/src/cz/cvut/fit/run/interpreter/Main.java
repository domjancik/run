package cz.cvut.fit.run.interpreter;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.ProgramEndException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.classes.VMString;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import cz.cvut.fit.run.parser.JavaLexer;
import cz.cvut.fit.run.parser.JavaParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, VMException, ParseException {
        PosixParser cliParser = new PosixParser();
        Options parseOptions = new Options();
        parseOptions.addOption("f", "file", true, "The source code to interpret.");
        parseOptions.addOption("c", "class", true, "Name of the main class containing main() method.");
        parseOptions.addOption("a", "args", true, "Arguments for the program, pass in quotes");

        CommandLine cl = cliParser.parse(parseOptions, args);

        String mainClassName = cl.getOptionValue("class");
        String sourceFilename = cl.getOptionValue("file");
        String argsString = cl.getOptionValue("args");

        if (mainClassName == null || sourceFilename == null) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("-f \"Path to source\" -c \"Main class name\"", parseOptions);
            return;
        }

        System.out.println("VMMachine Starting up...");
        System.out.println("***************************");

        ANTLRInputStream input = new ANTLRFileStream(sourceFilename);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

        for (JavaParser.TypeDeclarationContext type : compilationUnit.typeDeclaration()) {
            VMMachine.getInstance().registerType(type);
        }

        VMMemory.getInstance().init(2048);
        VMMachine vm = VMMachine.getInstance();

        // Arguments passing
        String[] vmArgs = argsString == null ? new String[0] : argsString.split(" ");
        VMString stringClass = (VMString)vm.getClazz("String");
        VMInteger integerClass = (VMInteger)vm.getClazz("Integer");
        VMPointer vmArguments = vm.getArrayClazz(VMType.STRING).createInstance(vmArgs.length);

        VMArrayInstance vmArgumentsArray = (VMArrayInstance)vmArguments.getObject();

        for (int i = 0; i < vmArgs.length; i++) {
            VMPointer stringValuePointer = stringClass.createInstance(vmArgs[i]);
            vmArgumentsArray.get(i).setValue(stringValuePointer);
        }

        vm.enterFrame();
        vm.push(vmArgumentsArray.getPointer());
        vm.push(integerClass.createInstance(1)); // Arg num (1 array)

        try {
            vm.getClazz(mainClassName).callMethod("main");
            vm.exitFrame(null);
        } catch (ProgramEndException ex) {
            System.out.println("***************************");
            System.out.println("Program ended");
        }
    }
}
