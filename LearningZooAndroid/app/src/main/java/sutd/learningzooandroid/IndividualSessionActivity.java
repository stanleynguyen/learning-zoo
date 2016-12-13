package sutd.learningzooandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import org.json.JSONObject;

public class IndividualSessionActivity extends AppCompatActivity {

    protected GraphView graph;
    protected ArrayList<String> topics = new ArrayList<String>();
    protected ArrayList<DataPoint> dataPointList = new ArrayList<DataPoint>();
    protected int plotCounter = 0;
    protected String lecturerName;
    protected TextView lecturer;
    protected String classroomNumber;
    protected TextView classroom;
    protected TextView subject;
    protected int subjectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_session);
        graph = (GraphView) findViewById(R.id.includegraph);
        lecturer = (TextView) findViewById(R.id.lecturer);
        classroom = (TextView) findViewById(R.id.classroom);
        subject = (TextView) findViewById(R.id.subjectid);
        getGraphInfo();

    }

    public void getGraphInfo(){
        RequestParams params = new RequestParams();
        ClientUsage grapher = ClientUsage.getClientUsage();
        lecturerName = grapher.getLecturer();
        classroomNumber = grapher.getClassroom();
        subjectId = grapher.getSubjectId();
        grapher.getTopicsForSession(params, new OnJSONArrayResponseCallback() {
            @Override
            public void onJSONArrayResponse(boolean success, JSONArray response) {
                if(success){
                    dataPointList.clear();
                    System.out.println(response.length());
                    //topics = response.topic; append all the topics to the arraylist
                    for (int i=0;i<response.length();i++) {
                        try {

                            JSONObject data = response.getJSONObject(i);
                            topics.add(data.getString("name"));
                            int buttonPress = data.getInt("std_unclear");
                            dataPointList.add(new DataPoint(i+0.5,buttonPress));
                        }
                        catch(JSONException e){
                            dataPointList.add(new DataPoint(i,0));
                            System.out.println(e);
                        }
                    }
                    plotGraph(graph); //add arraylist argument later.
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to get graph info for plot",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void plotGraph(View v){ //change argument to accept an arraylist later on.
        double maxY = 0;
        for (DataPoint entry: dataPointList){
            if(maxY < entry.getY()){
                maxY = entry.getY();
            }
        }
        graph.setTitle("Class Doubts");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Topic");
//      graph.getViewport().setScalable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(maxY+1);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX((double)dataPointList.size());
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

//        graph.setTitleTextSize(100);
        DataPoint[] plotz = new DataPoint[dataPointList.size()];
        for (int i = 0;i < dataPointList.size();i++){
            plotz[i] = dataPointList.get(i);
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(plotz);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.DKGRAY);
        series.setSpacing(35);
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    if (plotCounter< topics.size()&& dataPointList.get(plotCounter).getX() == value) {
                        String currentTopic = topics.get(plotCounter);
                        //topics.remove(0);
                        //compare dataPointList.get(plotCounter).getX to value
                        //put in index 0 , remove index 0
                        //get topics into an arraylist and iterate them here.
                        plotCounter += 1;
                        return currentTopic;
                    }
                    else{
                        return "";
                    }
                } else {
                    //y value should follow range and just map accordingly.
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }
}
