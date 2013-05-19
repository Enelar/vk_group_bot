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
    private String token;
    private String user;
    
    public VKRequest( Integer app_id, String scope ) {
        MakeAuth(app_id, scope);
    }
    
    private void MakeAuth( Integer client_id, List<String> scope ) {
        MakeAuth(client_id, scope, "https://oauth.vk.com/blank.html");
    }
    
    private void MakeAuth( Integer client_id, String scope ) {    
      MakeAuth(client_id, scope, "https://oauth.vk.com/blank.html");
    }
    
    private void MakeAuth( Integer client_id, List<String> scope, String redirect ) {
        String res = "";
        Iterator<String> i = scope.iterator();
        
        while (i.hasNext()) {
            String t = i.next();
            res += t + ",";
        }
        MakeAuth(client_id, res, redirect);
    }
    
    private void MakeAuth( Integer client_id, String scope, String redirect ) {
        String url = 
                auth_url + 
                "?client_id=" + client_id.toString() +
                "&scope=" + scope +
                "&redirect_url=" + redirect + 
                "&display=popup&response_type=token";
        HttpURLConnection c = null;
        try {
          SoftReference<HttpURLConnection> r = getHTML(url).RawConnection();
          c = r.get();
        } catch (IOException e) {}
        if (c == null)
            return;
        String location = c.getHeaderField("Location");
        ExtractToken(location);
    }
    
    private void ExtractToken( String url ) {
        String[] parts = url.split("#");
        String[] params = parts[1].split("&");
        
        Map<String, String> map = new HashMap<String, String>();  
        for (String param : params) {
            String[] t = param.split("=");
            map.put(t[0], t[1]);
        }
        token = map.get("access_token");
        user = map.get("user_id");
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

    protected HTTPResult Request(String method, Map<String, String> arguments, String token) throws IOException {
        String t = base_url + method + "?";
        if (token != null) {
            arguments.put("access_token", token);
        }
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            t += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return getHTML(t);
    }
    
    protected JSONObject RequestAndParse(String method, Map<String, String> arguments, String token) {
        String t;
        try {
            t = Request(method, arguments, token).Result();
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
