package baajna.scroll.owner.mobioapp.parser;

import android.util.Log;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.util.List;

public class WebServerOperation {

    private static CookieStore cookieStore;

  /*  public static String sendHTTPPostRequestToServer(String urlPath, List<NameValuePair> postData, boolean readfromServer) throws Exception {
        String data = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlPath);

        HttpResponse response = null;
        HttpEntity entity = null;
        InputStream is = null;
        BufferedReader reader = null;

        if (postData != null) {
            httppost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
        }

        if (cookieStore == null) {
            cookieStore = ((DefaultHttpClient) httpclient).getCookieStore();
        } else {
            ((DefaultHttpClient) httpclient).setCookieStore(cookieStore);
        }

        response = httpclient.execute(httppost);

        if (readfromServer) {
            entity = response.getEntity();
            is = entity.getContent();
            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            is.close();
            data = sb.toString();
        }
        Log.e("Inside POST", "Checking");

        return data.trim();
    }
*/
}
