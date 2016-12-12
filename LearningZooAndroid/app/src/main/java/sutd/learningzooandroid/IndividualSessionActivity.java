package sutd.learningzooandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import org.json.JSONObject;

public class IndividualSessionActivity extends AppCompatActivity {

    protected GraphView graph;
    protected ArrayList<String> topics = new ArrayList<String>();

    protected ArrayList<Integer> topicCounter = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_session);
        graph = (GraphView) findViewById(R.id.includegraph);
        getGraphInfo();
        plotGraph(graph);
    }

    public void getGraphInfo(){
        RequestParams params = new RequestParams();
        ClientUsage grapher = ClientUsage.getClientUsage();
        int sid = grapher.getSessionId();
        grapher.getTopicsForSession(params, sid, new OnJSONArrayResponseCallback() {
            @Override
            public void onJSONArrayResponse(boolean success, JSONArray response) {
                if(success){
                    DataPoint[] dataPointList = new DataPoint[response.length()];
                    //topics = response.topic; append all the topics to the arraylist
                    for (int i=0;i<response.length();i++) {
                        try {

                            JSONObject data = response.getJSONObject(i);
                            topicCounter.add(i);
                            topics.add(data.getString("name"));
                            int buttonPress = data.getInt("std_unclear");

                            dataPointList[i] = (new DataPoint(i,buttonPress));
                        }
                        catch(Exception e){
                            System.out.println(e);
                        }
                    }
                    plotGraph(findViewById(R.id.graph)); //add arraylist argument later.
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to get graph info for plot",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void plotGraph(View v){ //change argument to accept an arraylist later on.
        graph.setTitle("Class Doubts");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Topic");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(20.0);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(
        new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    //get topics into an arraylist and iterate them here.
                    return "JAVA";
                } else {
                    //y value should follow range and just map accordingly.
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }
}
