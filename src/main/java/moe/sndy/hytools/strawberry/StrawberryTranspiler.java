package moe.sndy.hytools.strawberry;

import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StrawberryTranspiler {

    private StrawberryTranspiler(){}

    public static String translate(final String file) throws IOException {
        String src = IOUtils.toString(new FileReader(file));
        StringBuilder java = new StringBuilder();
        ArrayList<String> strings = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        for(char c : src.toCharArray()){
            if(c == '"'){
                if(builder.toString().startsWith("\"")){
                    if(!isEscaped(builder.toString() + '"')){
                        strings.add(builder.toString() + "\"");
                        builder = new StringBuilder();
                    } else {
                        builder.append(c);
                    }
                } else {
                    java.append(builder.toString()).append("&T");
                    builder = new StringBuilder().append("\"");
                }
            } else {
                builder.append(c);
            }
        }
        java.append(builder.toString());
        String converted = convertString(java.toString());
        while(converted.contains("&T")){
            converted = converted.replaceFirst("&T", strings.get(0));
            strings.remove(0);
        }
        return converted;
    }

    private static String convertString(String src){
        src = src.replaceAll("//.*\n", "\n");
        src = src.replaceAll("/\\*.*\\*/", "");
        src = src.replace(":", "{");
        src = src.replace("#", "}");
        src = src.replace(" and ", " && ");
        src = src.replace(" or ", " || ");
        src = src.replace(" in ", " : ");
        src = src.replaceAll("([^a-zA-Z0-9_])public([^a-zA-Z0-9_])", "$1public_" + SEED + "$2");
        src = src.replaceAll("([^a-zA-Z0-9_])static([^a-zA-Z0-9_])", "$1static_" + SEED + "$2");
        src = src.replaceAll("([^a-zA-Z0-9_])import([^a-zA-Z0-9_])", "$1import_" + SEED + "$2");
        src = src.replaceAll("([^a-zA-Z0-9_])class([^a-zA-Z0-9_])", "$1class_" + SEED + "$2");
        src = src.replaceAll("([^a-zA-Z0-9_])int([^a-zA-Z0-9_])", "$1int_" + SEED + "$2");
        src = src.replaceAll("([^a-zA-Z0-9_])double([^a-zA-Z0-9_])", "$1double_" + SEED + "$2");
        src = src.replaceAll("=[ \n]*\\[([^|]+)]", "={$1}");
        src = src.replaceAll("([^a-zA-Z0-9_])func ", "$1public ");
        src = src.replaceAll("([^a-zA-Z0-9_])command[^|]*\\(([^|]*)\\) ", "$1@Command($2)public void ");
        src = src.replaceAll("([^a-zA-Z0-9_])event ", "$1@Event public void ");
        src = src.replaceAll("([^a-zA-Z0-9_])global ", "$1static ");
        src = src.replaceAll("([^a-zA-Z0-9_])berry |^berry ", "$1import ");
        src = src.replaceAll("([^a-zA-Z0-9_])strawberry |^strawberry ", "$1public class ");
        src = src.replaceAll("([^a-zA-Z0-9_])text([^a-zA-Z0-9_])", "$1String$2");
        src = src.replaceAll("([^a-zA-Z0-9_])string([^a-zA-Z0-9_])", "$1String$2");
        src = src.replaceAll("([^a-zA-Z0-9_])number([^a-zA-Z0-9_])", "$1int$2");
        src = src.replaceAll("([^a-zA-Z0-9_])decimal([^a-zA-Z0-9_])", "$1double$2");
        src = src.replaceAll("([^a-zA-Z0-9_])iteration([^a-zA-Z0-9_])", "$1&I$2");
        src = src.replaceAll("loop[ \n]*\\(([1-9][0-9]*)\\)[ \n]*\\{", "int &2=0;for(int &3=0;&3<$1;&3++){&2++;");
        src = src.replaceAll("loop[ \n]*\\(([^{]*)\\)[ \n]*\\{", "int &2=0;for($1){&2++;");
        src = src.replaceAll("do[ \n]*\\{", "int &I=0;do{&I++;");
        src = src.replaceAll("while[ \n]*\\(([^{]*)\\)[ \n]*\\{", "int &2=0;while($1){&2++;");
        src = src.replaceAll("catch[ \n]*\\{", "catch(Exception &2){&2.printStackTrace();");
        //TODO: catch-less try blocks
        while(src.contains("&2")) {
            src = src.replaceFirst("&2", newVar("two"));
            src = src.replaceFirst("&2", var);
        }
        while(src.contains("&3")) {
            src = src.replaceFirst("&3", newVar("three"));
            src = src.replaceFirst("&3", var);
            src = src.replaceFirst("&3", var);
        }
        String root = String.valueOf(SEED);
        while(src.contains("&I")) {
            int index = src.indexOf("&I");
            if(src.substring(0, index).contains(root)){
                String beforeIndex = src.substring(0, index);
                String afterSeed = beforeIndex.substring(beforeIndex.lastIndexOf(root) - 2, beforeIndex.length());
                int rBraceCount = occurrencesOf(afterSeed, '}') - occurrencesOf(afterSeed, '{');
                src = src.replaceFirst("&I", "two_" + (Integer.parseInt("" + afterSeed.charAt(0)) - rBraceCount) + "_&S");
            } else {
                break;
            }
        }
        src = src.replace("&S", root);
        src = "import strawberry.annotation.Event;import strawberry.annotation.Command;" + src;
        return src;
    }

    private static boolean isEscaped(String val){
        if(val.length() - 2 < 0) return false;
        if(val.charAt(val.length() - 2) == '\\') return !isEscaped(val.substring(0, val.length() - 1));
        return false;
    }

    private static final long SEED = Long.parseLong(String.valueOf(Math.random()).substring(2));
    private static int current = 0;
    private static String var;
    private static String newVar(String type){
        current++;
        var = (type + "_" + current + "_" + SEED);
        return var;
    }

    private static int occurrencesOf(String in, char match){
        int count = 0;
        for(char c : in.toCharArray()){
            if(c == match){
                count++;
            }
        }
        return count;
    }

}
