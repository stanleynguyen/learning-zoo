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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import org.json.JSONObject;

public class IndividualSessionActivity extends AppCompatActivity {

    protected GraphView graph;
    protected ArrayList<String> topics = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_session);
        graph = (GraphView) findViewById(R.id.includegraph);
        getGraphInfo(1);
        plotGraph(graph);
    }

    public void getGraphInfo(int id){
        RequestParams params = new RequestParams();
        ClientUsage grapher = ClientUsage.getClientUsage();
        CookieStore cookiez = grapher.getCookieStore(getApplicationContext());
        List<Cookie> cookieList = cookiez.getCookies();
        for(Cookie cook: cookieList){
            System.out.println(cook.getValue());
        }


        params.put("topic",id);
        grapher.getPlotData(params, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                if(success){
                    //topics = response.topic; append all the topics to the arraylist
                    plotGraph(findViewById(R.id.graph)); //add arraylist argument later.
                    recreate();
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
        graph.getViewport().setMaxX(3.0);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
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
