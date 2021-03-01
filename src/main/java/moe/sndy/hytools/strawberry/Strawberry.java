package moe.sndy.hytools.strawberry;

import strawberry.event.EventBase;
import strawberry.annotation.Command;
import strawberry.annotation.Event;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Strawberry {

    private Strawberry(){}

    private static final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    private static boolean enabled = false;

    public static void compile() {
        classes.clear();
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        File berries = new File("berries");
        for (File file : berries.listFiles()) {
            try {
                String src = StrawberryTranspiler.translate("berries/" + file.getName());
                Class<?> c = compiler.compile("moe.sndy.hytools.strawberry." + file.getName().substring(0, file.getName().indexOf(".")), src);
                classes.add(c);
            } catch (Exception e) {
                thrown(e, "compilation");
            }
        }
        enabled = true;
    }

    public static void invokeEvent(EventBase event) {
        if (!enabled) return;
        for (Class<?> c : classes) {
            for (Method m : c.getMethods()) {
                if (m.isAnnotationPresent(Event.class)
                && m.getParameterCount() == 1 
                && m.getParameters()[0].getType().equals(EventBase.class)
                && m.getParameters()[0].getName().equals(event.getName())) {
                    try {
                        m.invoke(null, event);                       
                    } catch (Exception e) {
                        thrown(e, "event execution");
                    }
                }
            }
        }
    }

    private static void thrown(Exception e, String location) {
        enabled = false;
        if (e instanceof InvocationTargetException) {
            Exception wrapped = new Exception(e.getCause());
            printException(wrapped, location);
        } else {
            printException(e, location);
        }
    }

    private static void printException(Exception e, String location){
        System.out.println("Error! An " + e.getClass().getSimpleName() + " was thrown during " + location + "!");
        System.out.println("[%TIP%] Explanation: [" + e.getClass().getSimpleName() + " | " + getExceptionExplanation(e.getClass().getSimpleName()) + "]");
        for (StackTraceElement line : e.getStackTrace()) {
            System.out.println("[%STACKTRACE%] " + line.toString());
        }
    }

    private static String getExceptionExplanation(String exception){
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
        if(exception.equals("InvocationTargetException")) return "Exceptionception! How'd you even manage to get this to happen?!";
        if(exception.equals("ArithmeticException")) return "Thrown when an invalid arithmetic operation is performed, like dividing by zero.";
        return "You've entered the deep zone! This exception has not been documented yet. Sorry!";
    }

}