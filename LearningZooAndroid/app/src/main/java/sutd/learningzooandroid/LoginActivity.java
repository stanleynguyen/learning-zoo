package sutd.learningzooandroid;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (TextInputEditText) findViewById(R.id.input_email);
        password = (TextInputEditText) findViewById(R.id.input_password);
    }


    public void toRegister(View v){
        Intent regis = new Intent(this,RegistrationActivity.class);
        startActivity(regis);
    }



    public void validateLogin(View v){

        RequestParams params = new RequestParams();
        params.put("email",email.getText().toString());
        params.put("password",password.getText().toString());
        ClientUsage login = ClientUsage.getClientUsage();
        login.getCookieStore(this); //store the cookiessss

        CookieStore cookiez = login.getCookieStore(getApplicationContext());
        List<Cookie> cookieList = cookiez.getCookies();
        for(Cookie cook: cookieList){
            System.out.println(cook.getValue());
        }

        login.postLoginHeaders(params,new OnJSONResponseCallback(){
        @Override
            public void onJSONResponse(boolean success, JSONObject response){ //waits for a response before executing login logic
            if (success) {
                Intent switchView = new Intent(getBaseContext(),SessionListActivity.class); //used base context for shortcut.
                startActivity(switchView);
            }
            else{
                Toast.makeText(getApplicationContext(),"Wrong email or password",Toast.LENGTH_SHORT).show();
            }
        }
    });
    }
//    private class getLoginHeadersTask extends AsyncTask<String,Integer,Boolean> {
//        protected Boolean doInBackground(String... urls){
//            Client.post(urls,)
//            return true;
//        }
//        protected void onPostExecute(Boolean result){
//
//        }
//    }


//    public void validateLogin(View v) {
//        final TextView mTextView = (TextView) findViewById(R.id.mTextView);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String uri = String.format("http://learning-zoo.herokuapp.com/auth/signin",);
//
//        // Request a string response from the provided URL.
//        //StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,uri,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        final String statement = "Response is: " + response.substring(0, 500);
//                        mTextView.setText(statement);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                final String statement = "That didn't work!";
//                mTextView.setText(statement);
//            }
//        });
//// Add the request to the RequestQueue.
//        queue.add(jsonRequest);
//    }
}
