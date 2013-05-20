/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package su.mctop.bot.vk_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private HTTPRequest req;
    private VKToken token;
    
    public VKRequest( VKToken t ) {
      token = t;
    }
        
    private HTTPResult getHTML(String urlToRead) throws IOException {
        if (req == null)
            req = new HTTPRequest();
        return req.Get(urlToRead);
    }
    
    private static final String base_url = "https://api.vk.com/method/";
    private static final String auth_url = "https://oauth.vk.com/authorize";
    
    protected HTTPResult Request(String method, Map<String, String> arguments) throws IOException {
        return Request(method, arguments, null);
    }

    protected HTTPResult Request(String method, Map<String, String> arguments ) throws IOException {
        String t = base_url + method + "?";
        if (token != null) {
            arguments.put("access_token", token.Token());
        }
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            t += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return getHTML(t);
    }
    
    protected JSONObject RequestAndParse(String method, Map<String, String> arguments ) {
        String t;
        try {
            t = Request(method, arguments, token.Token()).Result();
        } catch (IOException e) {
          return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(t);
        } catch (ParseException ex) {
            Logger.getLogger(ApiRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject;
    }    
}
