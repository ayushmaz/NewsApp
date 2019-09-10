package android.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.example.newsapp.MainActivity.LOG_TAG;

public class QueryUtils {


    private QueryUtils() {
    }

    public static ArrayList<News> extractFeatureFromJson(String newsJSON){
        if(TextUtils.isEmpty(newsJSON)){
            return null;
        }

        ArrayList<News> newsArrayList = new ArrayList<>();

        try{
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject getResponse = baseJsonObject.getJSONObject("response");
            JSONArray getResultsArray = getResponse.getJSONArray("results");
            String time;
            String date;
            String url;

            for(int i =0;i<getResultsArray.length();i++){
                JSONObject currentnews = getResultsArray.getJSONObject(i);
                String sectionName = currentnews.getString("sectionName");
                String title = currentnews.getString("webTitle");
                String dateJSON = currentnews.getString("webPublicationDate");
                url = currentnews.getString("webUrl");

                String parts[] = dateJSON.split("T");
                date = parts[0];
                time = parts[1].substring(0,5);
                News news = new News(sectionName,title,date,time,url);
                newsArrayList.add(news);
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return newsArrayList;
    }
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
