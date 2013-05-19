/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package su.mctop.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Enelar
 */
public class VKRequest {
    private String getHTML(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private static final String base_url = "https://api.vk.com/method/";
    
    protected String Request(String method, Map<String, String> arguments) {
        return Request(method, arguments, null);
    }

    protected String Request(String method, Map<String, String> arguments, String token) {
        String t = base_url + method + "?";
        if (token != null) {
            arguments.put("access_token", token);
        }
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            t += entry.getKey() + "=" + entry.getValue() + "&";
        }
        String ret = getHTML(t);
        return ret;
    }
    
    protected JSONObject RequestAndParse(String method, Map<String, String> arguments, String token) {
        String t = Request(method, arguments, token);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(t);
        } catch (ParseException ex) {
            Logger.getLogger(ApiRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject;
    }    
}
