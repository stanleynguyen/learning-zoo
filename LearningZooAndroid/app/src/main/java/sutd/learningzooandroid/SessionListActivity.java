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

import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

public class SessionListActivity extends AppCompatActivity {

    protected ListView listView;
    protected ArrayList<Cookie> cookieList;

    protected ArrayList<Integer> sessionTabKeeper = new ArrayList<Integer>();
    protected ArrayList<String> classroomTabKeeper = new ArrayList<String>();
    protected ArrayList<String> lecturerTabKeeper = new ArrayList<String>();
    protected ArrayList<String> sessionNameKeeper = new ArrayList<String>();
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        listView = (ListView)findViewById(R.id.listView);
        createList(listView);
        getSessions(listView);
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
                ClientUsage idSet = ClientUsage.getClientUsage();
                idSet.setSessionId(sessionTabKeeper.get(position)); //check if index starts 1
                idSet.setClassroomId(classroomTabKeeper.get(position));
                idSet.setLecturer(lecturerTabKeeper.get(position));
                idSet.setSessionName(listItems.get(position));
                toIndividualSession();
            }
        });
    }

    public void toIndividualSession(){
        Intent toIndividual = new Intent(this,IndividualSessionActivity.class);
        startActivity(toIndividual);
    }

    public void getSessions(View v){
        listItems.clear();
        RequestParams params = new RequestParams();
        ClientUsage allSessions = ClientUsage.getClientUsage();

        final int id = allSessions.getSubjectId(); //checks subject id, not session id take note!
        final PersistentCookieStore cookiez = allSessions.getCookieStore(getBaseContext()); //check if need to use a different context

        allSessions.getAllSessions(params,new OnJSONArrayResponseCallback(){
            @Override
            public void onJSONArrayResponse(boolean success, JSONArray response){ //waits for a response before executing login logic
                if (success) {
                    for(int i=0;i<response.length();i++){
                        try {
                            JSONObject data = response.getJSONObject(i);

                            if (data.getInt("subject_id") == id){

                                lecturerTabKeeper.add(data.getString("lecturer_name"));
                                classroomTabKeeper.add(data.getString("classroom"));
                                System.out.println(data.getString("lecturer_name"));

                                sessionTabKeeper.add(data.getInt("id"));
                                listItems.add(listItems.size(),data.getString("name"));

                            } //after filtering JSONs, take each JSON name value and put into updates.
                        }
                        catch(Exception e) {
                            System.out.println("Something went wrong while filtering!");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to refresh sessions at the moment",Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

}
