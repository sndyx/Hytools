package moe.sndy.hytools.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class ApiTools {

    public static JsonObject getNode(JsonObject json, String... nodes){
        for(String node : nodes){
            json = json.get(node).getAsJsonObject();
        }
        return json;
    }

    public static String getNode(String json, String... nodes){
        JsonObject profileData = new JsonParser().parse(json).getAsJsonObject();
        for(int i = 0; i < nodes.length ; i++){
            if(i + 1 != nodes.length){
                //
                for(Map.Entry<String, JsonElement> set : profileData.entrySet()){
                    System.out.println(set.getKey());
                }
                profileData = profileData.get(nodes[i]).getAsJsonObject();
            } else {
                return profileData.get(nodes[i]).toString();
            }
        }
        return "";
    }

    public static String getAwkwardNode(String json, String... nodes){
        for(String node : nodes){
            if(json.contains(node)){
                json = json.substring(json.indexOf(node) + node.length() + 2);
                if(json.charAt(0) == '{'){
                    int bracesCount = 0;
                    int pos = 0;
                    for(char c : json.toCharArray()){
                        if(c == '}'){
                            bracesCount -= 1;
                        } else if(c == '{'){
                            bracesCount += 1;
                        }
                        if(bracesCount == 0){
                            break;
                        }
                        pos++;
                    }
                    json = json.substring(0, pos + 1);
                } else {
                    int pos = 0;
                    for(char c : json.toCharArray()){
                        if(c == ','){
                            break;
                        } else if(c == '}') {
                            break;
                        }
                        pos++;
                    }
                    json = json.substring(0, pos);
                }
            }
        }
        return json;
    }

}
