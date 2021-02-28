package moe.sndy.hytools.strawberry;

import java.util.ArrayList;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final ArrayList<CompiledCode> compiledCode = new ArrayList<CompiledCode>();
    private final DynamicClassLoader cl;

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param cl          class loader
     */
    protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, DynamicClassLoader cl) {
        super(fileManager);
        this.cl = cl;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        try {
            CompiledCode innerClass = new CompiledCode(className);
            compiledCode.add(innerClass);
            cl.addCode(innerClass);
            return innerClass;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating in-memory output file for " + className, e);
        }
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return cl;
    }

}
