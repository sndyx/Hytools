package moe.sndy.hytools.strawberry;

import strawberry.annotation.Command;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Strawberry {

    private Strawberry(){}

    private static final ArrayList<Object> objects = new ArrayList<Object>();

    public static void compile() throws Exception {
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        File berries = new File("berries");
        for(File file : berries.listFiles()){
            String src = StrawberryTranspiler.translate("berries/" + file.getName());
            Class<?> c = compiler.compile("moe.sndy.hytools.strawberry." + file.getName().substring(0, file.getName().indexOf(".")), src);
            objects.add(c.getConstructors()[0].newInstance());
        }
        //tests...
        int index = 0;
        for(Class<?> c : classes){
            for(Method m : c.getDeclaredMethods()){
                m.invoke(objects.get(index));
            }
            index++;
        }
    }

    public static void invokeCommand(String command){
        for(Class<?> c : classes){
            for(Method m : c.getMethods()){
                if(m.isAnnotationPresent(Command.class)){
                    boolean found = false;
                    for(String executor : m.getAnnotation(Command.class).executors()){
                        if(executor.equals(command)){
                            found = true;
                            break;
                        }
                    }
                    if(found)
                }
            }
        }
    }

    public static String getExceptionExplanation(String exception){
        if(exception.equals("Exception")) return "The very most basic exception. No further info";
        if(exception.equals("RuntimeException")) return "Unchecked runtime exception. No further info.";
        if(exception.equals("NullPointerException")) return "Thrown when an application tries to use a null value in a case when an object is required. This might mean one of your variables was never assigned a value or a function is returning null.";
        if(exception.equals("StackOverflowError")) return "Thrown when an application enters an infinitely or extremely long repeating statement.";
        if(exception.equals("NumberFormatException")) return "Thrown when an application attempts to convert a string into a numeric value but the input string is not legal.";
        if(exception.equals("IllegalArgumentException")) return "Thrown to indicate that a method has been passed an illegal or inappropriate argument.";
        if(exception.equals("IllegalStateException")) return "Thrown when a method is called at the wrong time.";
        if(exception.equals("NoSuchMethodException")) return "Thrown when a method that does not exist is called.";
        if(exception.equals("ClassCastException")) return "Thrown when an application attempts to convert an object to a subclass of which it is not an instance of.";
        if(exception.equals("ParseException")) return "Signals that an error has been reached unexpectedly while parsing.";
        if(exception.equals("FileNotFoundException")) return "Thrown when an application attempts to interact with a file that does not exist.";
        if(exception.equals("MalformedURLException")) return "Thrown if a specified URL is invalid.";
        if(exception.equals("ArrayIndexOutOfBoundsException")) return "Thrown when an application attempts to access an element of an array that does not exist.";
        if(exception.equals("ArithmeticException")) return "Thrown when an invalid arithmetic operation is performed, like dividing by zero.";
        return "You've entered the deep zone! This exception has not been documented yet. Sorry!";
    }

}
