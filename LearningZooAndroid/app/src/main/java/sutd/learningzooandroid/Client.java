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

public class Client {

    private static final String BASE_URL = "http://learning-zoo.herokuapp.com/";
    //private static final String BASE_URL = "https://dweet.io/dweet/for/welp";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Content-Type","application/x-www-form-urlencoded");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //client.addHeader("Content-Type","application/json");
        client.addHeader("Content-Type","application/x-www-form-urlencoded"); //wtf

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void storeCookie(Context context){ //might have to come back and check this.
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        Client.client.setCookieStore(myCookieStore);
    }
}


class ClientUsage {
    public Boolean fakereturn;
    public Boolean getConfirmation(){
        return fakereturn;
}




    public void getLoginHeaders(RequestParams params,final OnJSONResponseCallback callback) {
        Client.post("/auth/sign_in", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    fakereturn = true;
                    callback.onJSONResponse(true, response);
                    System.out.println("Successfully logged in");
                    System.out.println(response.toString());
                }
                catch(Exception e1){
                    System.out.println("Who am I kidding?");
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    callback.onJSONResponse(false, jObj);
                }
                catch(Exception e1){
                    System.out.println("Who am I kidding?");
                }
            }
        });


    }

    public void registerTeacher(RequestParams params){
        //Client.post("",params, new JsonHttpResponseHandler(){
        Client.post("/auth",params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
                System.out.println("Succeeded at registering user!");
            }

        });

    }

    public void getAllSessions(RequestParams params){
        Client.get("/auth/sign_in", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray

                System.out.println("Only got json object");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                String someText = "";
                try {
                    JSONObject firstEvent = (JSONObject) timeline.get(0);
                    someText = firstEvent.getString("text");
                }
                catch (JSONException e){
                    System.out.println("hello");
                }
                // Do something with the response
                System.out.println(someText); //continue here later...
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