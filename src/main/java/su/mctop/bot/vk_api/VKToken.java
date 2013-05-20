/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package su.mctop.bot.vk_api;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Enelar
 */
public class VKToken {
   private static SoftReference<List<String>> default_tokens = null;
   private List<String> requested_tokens;
   private Integer appid;   
   private String token;
   private Integer user_id;
    
   public VKToken( Integer client_id, List<String> scope ) {
     requested_tokens = scope;
     appid = client_id;
     SetToken(null, "0");
   }
   
   public VKToken( Integer client_id ) {
     requested_tokens = new ArrayList<String>();
     appid = client_id;
     SetToken(null, "0");
   }
   
   private void SetToken( String _token, String _user ) {
       user_id = Integer.parseInt(_user);
       token = _token;
   }
   
  
   private List<String> DefaultTokens() {
       List<String> ret = default_tokens.get();
       if (ret != null)
           return ret;
       ret = new ArrayList<String>();
       ret.add("wall");
       ret.add("groups");
       ret.add("offline");
       default_tokens = new SoftReference<List<String>>(ret);
       return ret;
   }
   
   public String Token() {
       GetToken();
       return token;
   }
   
   public Integer UserId() {
       GetToken();
       return user_id;
   }
   
   public String User() {
       GetToken();
       return UserId().toString();
   }
   
   private void GetToken() {
       if (Token() != null)
           return;
      
       String scope = GenScope();
       MakeAuth(scope, "https://oauth.vk.com/blank.html");
   }
   
   private String GenScope() {
        String res = "";
        Iterator<String> i = DefaultTokens().iterator();
        
        while (i.hasNext()) {
            String t = i.next();
            res += t + ",";
        }

        i = requested_tokens.iterator();
        while (i.hasNext()) {
            String t = i.next();
            res += t + ",";
        }
        return res;
   }
           
    private static final String auth_url = "https://oauth.vk.com/authorize";   
   
    private void MakeAuth( String scope, String redirect ) {
        String url = 
                auth_url + 
                "?client_id=" + appid.toString() +
                "&scope=" + scope +
                "&redirect_url=" + redirect + 
                "&display=popup&response_type=token";
        HttpURLConnection c = null;
        try {
          HTTPRequest req = new HTTPRequest();
          SoftReference<HttpURLConnection> r = req.Get(url).RawConnection();
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
        SetToken(map.get("access_token"), map.get("user_id"));
    }    
}