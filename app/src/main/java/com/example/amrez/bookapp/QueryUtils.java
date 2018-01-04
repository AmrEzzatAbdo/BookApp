package com.example.amrez.bookapp;

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

/**
 * Created by amrez on 10/9/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<B_object> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant data fields from the JSON
        List<B_object> Books = extractFeatureFromJson(jsonResponse);

        // Return the list
        return Books;
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

    //Http Request
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // URL checking
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // request checker
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //read from stream
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
        //return stream data
        return output.toString();
    }

    //return parsing json data
    private static List<B_object> extractFeatureFromJson(String BookJSON) {
        // json string checker
        if (TextUtils.isEmpty(BookJSON)) {
            return null;
        }

        // Creat empty list to add books in it
        List<B_object> Books = new ArrayList<>();

        //Json Parsing
        try {
            JSONObject baseJsonResponse = new JSONObject(BookJSON);
            JSONArray BookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < BookArray.length(); i++) {

                JSONObject currentBook = BookArray.getJSONObject(i);
                JSONObject bookVolumeInfo = currentBook.getJSONObject("volumeInfo");

                //define B_object variable
                String name;
                String author1;
                // Extract the value for the title key
                name = bookVolumeInfo.getString("title");

                // Extract the value from authors array
                JSONArray authorsArray;
                StringBuilder author = new StringBuilder();
                if (bookVolumeInfo.has("authors")) {
                    authorsArray = bookVolumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        author.append(authorsArray.getString(j));
                        //if book have more than one auther
                        if (j != authorsArray.length() - 1) {
                            author.append("/ ");
                        } else {
                            author.append(".");
                        }
                    }

                } else {
                    author.append("No Authors");
                }
                //convert author from StringBuilder to string
                author1 = String.valueOf(author);

                // Extract  book description
                String description;
                if (bookVolumeInfo.has("description")) {
                    description = bookVolumeInfo.getString("description");
                } else {
                    description = "No Description";
                }

                // Extract the book info Link
                String bookPreviewLink;
                if (bookVolumeInfo.has("infoLink")) {
                    bookPreviewLink = bookVolumeInfo.getString("infoLink");

                } else {
                    bookPreviewLink = "No Link provided";
                }


                B_object book = new B_object(name, author1, description, bookPreviewLink);
                Books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing Book JSON result", e);
        }
        return Books;
    }


}
