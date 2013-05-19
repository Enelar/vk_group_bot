/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package su.mctop.bot.vk_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Enelar
 */
public class HTTPRequest {
    public HTTPResult Get(String urlToRead ) throws IOException {
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return new HTTPResult(conn);
    }    
}
