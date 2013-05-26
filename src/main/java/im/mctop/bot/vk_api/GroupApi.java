/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package im.mctop.bot.vk_api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author enelar
 */
public class GroupApi extends HTTPRequest {
    public GroupApi( String user, String password ) throws IOException{
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("act", "login");
        ret.put("to", "");
        ret.put("_origin", "http://vk.com");
        ret.put("ip_h", "??");
        ret.put("email", user);
        ret.put("pass", password);
        String cookie = Post("https://login.vk.com/", ret).RawConnection().get().getHeaderField("Cookie");
        UseCookie(cookie);
    }
    public List<Integer> GetRequests() {
        String url = "http://vk.com/radio_exsul_group?act=users&tab=requests";
    }
            
}
