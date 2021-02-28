package strawberry;

import java.util.HashMap;

public class Cache {

    private Cache(){}

    private static final HashMap<String, Object> cached = new HashMap<String, Object>();

    public static void save(Object val, String key){
        cached.put(key, val);
    }

    public static Object load(String key){
        return cached.get(key);
    }

}
