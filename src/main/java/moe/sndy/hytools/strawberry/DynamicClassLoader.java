package moe.sndy.hytools.strawberry;


import java.util.HashMap;

public class DynamicClassLoader extends ClassLoader {

    private final HashMap<String, CompiledCode> customCompiledCode = new HashMap<String, CompiledCode>();

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addCode(CompiledCode cc) {
        customCompiledCode.put(cc.getName(), cc);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        CompiledCode cc = customCompiledCode.get(name);
        if (cc == null) return super.findClass(name);
        byte[] byteCode = cc.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }

}
