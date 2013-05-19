package su.mctop.bot.vk_api;

import su.mctop.bot.vk_api.VKRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import su.mctop.bot.WallMessage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Enelar
 */
public class ApiRequest extends VKRequest {
    public ApiRequest( Integer app_id, String scope ) {
        super(app_id, scope);
    }

    public WallMessage[] GetWall(String group, int offset, int count, String filter) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("domain", group);
        m.put("offset", String.valueOf(offset));
        m.put("count", String.valueOf(count));
        m.put("filter", filter);
        m.put("extended", "0");

        JSONObject obj = RequestAndParse("wall.get", m, null);
        JSONArray a = (JSONArray) obj.get("response");
        WallMessage[] ret = new WallMessage[count];
        for (int i = 1; i < a.size(); ++i) {
            WallMessage wm = new WallMessage();
            JSONObject jo = (JSONObject) a.get(i);
            wm.text = (String) jo.get("text");
            if (wm.text == null) {
                wm.text = "";
            }
            wm.author = Integer.getInteger(jo.get("from_id").toString());
            wm.owner = Integer.getInteger(jo.get("to_id").toString());
            wm.id = Integer.getInteger(jo.get("id").toString());
            ret[i - 1] = wm;
        }
        return ret;
    }

    public Boolean DeleteWallMessage(int owner, int id, String access) {
     
        Map<String, String> m = new HashMap<String, String>();
        m.put("owner_id", String.valueOf(owner));
        m.put("post_id", String.valueOf(id));
        
        try {
          String ret = Request("wall.delete", m, access).Result();
          if (ret.contains("{\"response\":1}"))
            return true;
        } catch (IOException e) {
          
        }           
        System.out.print("F");
        return false;
    }

    public String MakeAuth() {
        String ret = "LALALA";
        String url = "https://oauth.vk.com/authorize?client_id=3571081&scope=wall,groups&redirect_uri=https://oauth.vk.com/blank.html&display=popup&response_type=token";
        return ret;
    }

    public void Ban(int gid, int id, String access) {
        int Current = (int) (System.currentTimeMillis() / 1000L);
        Current += 3600;

        Map<String, String> m = new HashMap<String, String>();
        m.put("gid", String.valueOf(gid));
        m.put("uid", String.valueOf(id));
        m.put("end_date", String.valueOf(Current));
        m.put("reason", "1"); //spam
        m.put("comment", "Wall%20Cleaner%20Bot");
        m.put("comment_visible", "1");
        try {
          Request("groups.banUser", m, access);
        } catch (IOException e) {
          Logger.getLogger(ApiRequest.class.getName()).log(Level.SEVERE, null, e);  
        }
    }
}
