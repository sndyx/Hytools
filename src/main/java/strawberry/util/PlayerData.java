package strawberry.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private final HashMap<UUID, HashMap<String, Object>> data;

    public PlayerData(){
        data = new HashMap<UUID, HashMap<String, Object>>();
    }

    public Object get(UUID id, String key){
        return data.get(id).get(key);
    }

    public Collection<Object> getAll(UUID id, String key){
        return data.get(id).values();
    }

    public void clear(){
        data.clear();
    }

    public void clearPlayer(UUID id){
        data.get(id).clear();
    }

    public void add(UUID id, String key, Object val){
        data.get(id).put(key, val);
    }

}
