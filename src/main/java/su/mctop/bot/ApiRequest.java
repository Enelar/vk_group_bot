package su.mctop.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import java.util.Random;
//import org.json.JSONArray;
//import org.json.JSONObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Enelar
 */
public class ApiRequest {

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

    public String Request(String method, Map<String, String> arguments) {
        return Request(method, arguments, null);
    }

    public String Request(String method, Map<String, String> arguments, String token) {
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

    public JSONObject RequestAndParse(String method, Map<String, String> arguments, String token) {
        String t = Request(method, arguments, token);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(t);
        } catch (ParseException ex) {
            Logger.getLogger(ApiRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject;
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
        String ret = Request("wall.delete", m, access);
        if (ret.contains("{\"response\":1}")) {
            return true;
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
        Request("groups.banUser", m, access);
    }
}
