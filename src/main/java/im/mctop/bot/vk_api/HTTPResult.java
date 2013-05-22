package im.mctop.bot.vk_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;

/**
 *
 * @author Enelar
 */
public class HTTPResult {
    private HttpURLConnection c;
    private Boolean finished;
    private String ResultString;

    protected HTTPResult( HttpURLConnection _c ) {
        finished = false;
        c = _c;
    }

    public String Result() throws IOException {
        if (finished)
            return ResultString;

        BufferedReader rd = new BufferedReader(new InputStreamReader(c.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        c = null;
        finished = true;
        ResultString = result.toString();
        result.delete(0, result.length());
        return ResultString;
    }

    public SoftReference<HttpURLConnection> RawConnection() {
        return new SoftReference<HttpURLConnection>(c);
    }
}
