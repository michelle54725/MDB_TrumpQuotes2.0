package com.demo.mdb.spring2017finalassessment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.Response;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hp on 3/14/2017.
 */

public class Utils {
    /* TODO Part 5
     * implement getRandomPhrase on a thread pool of size 1. Use a callable to make a GET request on
     * this urlString: "https://api.whatdoestrumpthink.com/api/v1/quotes/random". You'll probably
     * need to actually go to the URL to see the JSON structure to know what String you want (don't
     * worry, it's a very simple JSON file.)
     *
     * convertStreamToString has been provided
     *
     * Note: if you can't remember how to use a Callable, you can get partial credit without one!
     */
    static final String url_str = "https://api.whatdoestrumpthink.com/api/v1/quotes/random";

    static Future<String> getRandomPhrase() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CallableClass mCallable = new CallableClass(new URL(url_str));
        Future<String> result = executor.submit(mCallable);
        return result;
    }

    static public class CallableClass implements Callable<String> {
        private URL url;

        public CallableClass(URL url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            URL url = new URL(url_str);
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            JSONObject obj = new JSONObject(buffer.toString());
            String quote = obj.getString("message"); //TODO: parse JSON to get message
            return quote;
        }
    }

//    public class Request implements Callable<Response> {
//        private URL url;
//
//        public Request(URL url) {
//            this.url = url;
//        }
//
//        @Override
//        public Response call() throws Exception {
//            return new Response(url.openStream());
//        }
//    }
//
//    public class Response {
//        private InputStream body;
//
//        public Response(InputStream body) {
//            this.body = body;
//        }
//
//        public InputStream getBody() {
//            return body;
//        }
//    }
}
