package moe.sndy.hytools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequest {

    private HttpURLConnection connection;

    public ApiRequest(String query) {
        try {
            URL url = new URL(query);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String get() {
        try {
            final BufferedReader reader;
            final StringBuilder response = new StringBuilder();
            String line;
            int status = connection.getResponseCode();
            if (status > 299) {
                return "error";
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            }
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

}
