package ices.project.siakapmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ReportActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    BarChart bcBarchart;

    String username, random_password;
    ArrayList<String> xData;
    ArrayList<Integer> yData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        /// declare barchart
        bcBarchart = findViewById(R.id.bcBarchart);

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.report);

        //perform ItemSelectedListener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.report:
                        return true;

                    case R.id.isolate:
                        startActivity(new Intent(getApplicationContext(),IsolateActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        xData = new ArrayList<>();
        yData = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("UserLogin", MODE_PRIVATE);
        username = prefs.getString("username",null);
        random_password = prefs.getString("sessionid",null);

        // test volley
        getDataFromAPI();

        // Barchart implementation
        //initializeBarChart();

    }

    private void initializeBarChart() {

        // Initialize array list
        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        ArrayList<Integer> values = new ArrayList<>();
//
//        values.add((int)10);
//        values.add((int)2);
//        values.add((int)3);
//        values.add((int)1);

        for(int i = 0; i < yData.size(); i++){
            // Initialize barchart entry
            BarEntry barEntry = new BarEntry(i, yData.get(i).floatValue());
            barEntries.add(barEntry);
        }

        // Initialize bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries,"Status");
        // Set colors
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // Hide draw value
        barDataSet.setDrawValues(false);
        // Set bar data
        bcBarchart.setData(new BarData(barDataSet));

        bcBarchart.getXAxis().setEnabled(true);
        bcBarchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //initialize xAxis
        XAxis xAxis = bcBarchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        // set label to x-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xData.get((int) value);
            }
        });

        // description
        bcBarchart.getDescription().setEnabled(false);
        // Set description text and color
        bcBarchart.getDescription().setText("Status Chart");
        bcBarchart.getDescription().setTextColor(Color.BLACK);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        //bcBarChart.setMaxVisibleValueCount(4);
        bcBarchart.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        bcBarchart.setPinchZoom(true);
        bcBarchart.setVisibleXRangeMaximum(25);

        bcBarchart.setDrawBarShadow(false);
        bcBarchart.setDrawGridBackground(false);

        bcBarchart.getAxisLeft().setDrawGridLines(false);
        bcBarchart.getAxisRight().setDrawGridLines(false);
        bcBarchart.getAxisRight().setEnabled(false);
        bcBarchart.getAxisLeft().setEnabled(true);
        bcBarchart.getXAxis().setDrawGridLines(false);

        // add a nice and smooth animation
        bcBarchart.animateY(1500);

        bcBarchart.getLegend().setEnabled(false);

        bcBarchart.getAxisRight().setDrawLabels(true);
        bcBarchart.getAxisLeft().setDrawLabels(true);
        bcBarchart.setTouchEnabled(true);
        bcBarchart.setDoubleTapToZoomEnabled(true);
        bcBarchart.getXAxis().setEnabled(true);
        bcBarchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bcBarchart.invalidate();

    }

    public void getDataFromAPI(){

        String url = "http://smartfarm22.ddns.net/pages/feed.php?s=level&ct=100";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    parseData(response);
                    //Log.e("response",response);
                }
                else{
                    Log.e("response", "null");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("random_password", random_password);

                Log.e("data",data.toString());

                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void parseData(String response) {
        try {
            // Create JSOn Object
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int timestamp = Integer.parseInt(jsonObject.getString("x"));

                Date date = new Date(timestamp * 1000L);
                // format of the time
                SimpleDateFormat jdf = new SimpleDateFormat("HH:mm:ss");
                jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                String time = jdf.format(date);

                xData.add(time);
                yData.add(Integer.parseInt(jsonObject.getString("y")));

            }

            Log.e("xData before", xData.toString());

            xData = reverseArrayList(xData);

            Log.e("xData after", xData.toString());
            Log.e("yData", yData.toString());

            initializeBarChart();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Takes an arraylist as a parameter and returns
    // a reversed arraylist
    public ArrayList<String> reverseArrayList(ArrayList<String> alist)
    {
        // Arraylist for storing reversed elements
        ArrayList<String> revArrayList = new ArrayList<String>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }


}