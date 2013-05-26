package im.mctop.bot.vk_api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Enelar
 */
public class HTTPRequest {
    private static Date last_request = null;
    private static final int RequestDelay = 5;

    private void GetLock() {
        if (last_request == null) {
            last_request = new Date();
            return;
        }
        Date t;
        do {
            t = new Date();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        } while (t.getTime() - last_request.getTime() < RequestDelay * 1000);
        last_request = t;
    }

    public HTTPResult Get( String urlToRead ) throws IOException {
        URL url = new URL(urlToRead);
        GetLock();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return new HTTPResult(conn);
    }
    
    public HTTPResult Post( String urlToRead, Map<String, String> data ) throws IOException {
        URL url = new URL(urlToRead);
        GetLock();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        return new HTTPResult(conn);
    }
    public void UseCookie( String cookie ) {
        //todo
    }
}
