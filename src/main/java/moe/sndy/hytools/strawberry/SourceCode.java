package moe.sndy.hytools.strawberry;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class SourceCode extends SimpleJavaFileObject {
    private String contents;
    private String className;

    public SourceCode(String className, String contents) {
        super(URI.create("string:///" + className.replace('.', '/')
                + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors){
        return contents;
    }

}
