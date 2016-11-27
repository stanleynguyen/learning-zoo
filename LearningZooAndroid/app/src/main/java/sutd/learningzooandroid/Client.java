package sutd.learningzooandroid;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.loopj.android.http.*;

import org.json.*;

import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.cookie.Cookie;

public class Client {
    private static PersistentCookieStore myCookieStore = null;
    private static final String BASE_URL = "http://learning-zoo.herokuapp.com/";
    //private static final String BASE_URL = "https://dweet.io/dweet/for/welp";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Content-Type","application/x-www-form-urlencoded");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Content-Type","application/x-www-form-urlencoded"); //wtf
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void setupCookieStore(Context context){
        myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
    }

    public static PersistentCookieStore getCookieStore(Context context){
        if (myCookieStore == null){
            setupCookieStore(context);
        }
        return myCookieStore;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}

class ClientUsage {
    private static ClientUsage instance = null;
    public static ClientUsage getClientUsage(){ //one for the whole thing
        if (instance==null){
            instance = new ClientUsage();
        }
        return instance;
    }

    private ClientUsage(){

    }

    public PersistentCookieStore getCookieStore(Context context){
        return Client.getCookieStore(context);

    }

    public void postLoginHeaders(RequestParams params,final OnJSONResponseCallback callback) {
        Client.post("/auth/sign_in", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {

                    callback.onJSONResponse(true, response);
                    System.out.println("Successfully logged in");
                    System.out.println(response.toString());
                    //Client.storeCookie()
                }
                catch(Exception e1){
                    System.out.println("Who am I kidding?");
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                try {
                    callback.onJSONResponse(false, response);
                }
                catch(Exception e1){
                    System.out.println("Failed to get response from server");
                }
            }
        });


    }

    public void registerTeacher(RequestParams params,final OnJSONResponseCallback callback){
        //Client.post("",params, new JsonHttpResponseHandler(){
        Client.post("/auth",params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try{
                    callback.onJSONResponse(true,response);
                    System.out.println(response.toString());
                    System.out.println("Succeeded at registering user!");
                }
                catch(Exception e1){
                    System.out.println("Heh");
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers,String response, Throwable e) {
                try {
                    JSONObject jObj = new JSONObject();

                    callback.onJSONResponse(false, jObj);
                }
                catch(Exception e1){
                    System.out.println("Failed to get response from server");
                }
            }

        });

    }

    public void getAllSessions(RequestParams params,final OnJSONArrayResponseCallback callback) {

        Client.get("/topics", params, new JsonHttpResponseHandler() { // change this later.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    callback.onJSONArrayResponse(true, response);
                    System.out.println("Retrieved topics!");

                }
                catch(Exception e1){
                    System.out.println("Who am I kidding?");
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray response) {
                try {
                    callback.onJSONArrayResponse(false, response);
                }
                catch(Exception e1){
                    System.out.println("Failed to get response from server");
                }
            }
        });


    }

    public void getPlotData(RequestParams params,final OnJSONResponseCallback callback) { //doesn't work yet since there are no topics...
        Client.get("/topics", params, new JsonHttpResponseHandler() { // change this later.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    callback.onJSONResponse(true, response);
                    System.out.println("Retrieved plot data!");
                    System.out.println(response.toString());
                }
                catch(Exception e1){
                    System.out.println("Who am I kidding?");
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                try {
                    callback.onJSONResponse(false, response);
                }
                catch(Exception e1){
                    System.out.println("Failed to get response from server");
                }
            }
        });


    }
}


//auth/signin
//auth/signout
//auth/password
//auth/cancel
//auth/signup
//auth/edit
//topics/...

//    @Override
//    public void onSuccess(int statusCode, Header[] headers, JSONArray jsons) {
//        // Pull out the first event on the public timeline
//        String someText = "";
//        System.out.println(jsons.toString());
//        try {
//            JSONObject firstEvent = (JSONObject) jsons.get(0);
//            someText = firstEvent.getString("text");
//        }
//        catch (JSONException e){
//            System.out.println("hello");
//        }
//        // Do something with the response
//        System.out.println(someText);
//    }