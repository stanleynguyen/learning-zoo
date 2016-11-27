package sutd.learningzooandroid;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

//TODO: Add a filter that uses the subject_id of the instructor to filter the information automatically
//TODO: Also add a manual filter for people who to search faster.

public class SessionListActivity extends AppCompatActivity {

    protected ListView listView;
    protected ArrayList<Cookie> cookieList;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        listView = (ListView)findViewById(R.id.listView);
        //getSession();

        createList(listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.include1);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.ic_cached_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getSessions(findViewById(R.id.action_refresh));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createList(View v){
        adapter = new ArrayAdapter<String>(this,R.layout.item_session,listItems);
        listView.setAdapter(adapter);
        final SessionListActivity _this = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(_this,"position: " + position, Toast.LENGTH_SHORT).show();
                toIndividualSession();
            }
        });
    }

    public void updateList(View v,String id1) {

        listItems.add("Dijkstra's Algorithm");
        listItems.add("Android Programming");
        listItems.add("Computer Memory");
        listItems.add("Weka");
        if(id1 != null) {
            listItems.add(id1);
        }
        adapter.notifyDataSetChanged();

    }

    public void toIndividualSession(){
        Intent toIndividual = new Intent(this,IndividualSessionActivity.class);
        startActivity(toIndividual);
    }

    public void getSessions(View v){

        final int id; //change id later on
        RequestParams params = new RequestParams();
        //params.put("id",id);
        ClientUsage allSessions = ClientUsage.getClientUsage();
        allSessions.getAllSessions(params,new OnJSONArrayResponseCallback(){
            @Override
            public void onJSONArrayResponse(boolean success, JSONArray response){ //waits for a response before executing login logic
                if (success) {
                    updateList(listView,"Dynamic Programming");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to refresh sessions at the moment",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
