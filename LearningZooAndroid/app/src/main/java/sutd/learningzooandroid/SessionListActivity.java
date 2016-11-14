package sutd.learningzooandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SessionListActivity extends AppCompatActivity {

    protected ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        listView = (ListView)findViewById(R.id.listView);
        updateList(listView);
    }

    public void updateList(View v) {
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_session,listItems);

        listItems.add("Hello");
        listItems.add("Iz me");
        listItems.add("I was wondering");
        listItems.add("if after all these years");
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

    public void toIndividualSession(){
        Intent toIndividual = new Intent(this,IndividualSessionActivity.class);
        startActivity(toIndividual);
    }


}
