package ices.project.siakapmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    BarChart bcBarchart;

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

        // Barchart implementation
        initializeBarChart();

    }

    private void initializeBarChart() {

        // Initialize array list
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xAxisLabel = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();

        values.add((int)10);
        values.add((int)2);
        values.add((int)3);
        values.add((int)1);

        for(int i = 0; i < values.size(); i++){
            // Initialize barchart entry
            BarEntry barEntry = new BarEntry(i, values.get(i).floatValue());
            barEntries.add(barEntry);
        }

        //initialize x Axis Labels
        xAxisLabel.add("Normal");
        xAxisLabel.add("Fault");
        xAxisLabel.add("Alarm");
        xAxisLabel.add("Repair");

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
                return xAxisLabel.get((int) value);
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
        bcBarchart.setPinchZoom(false);

        bcBarchart.setDrawBarShadow(false);
        bcBarchart.setDrawGridBackground(false);

        bcBarchart.getAxisLeft().setDrawGridLines(false);
        bcBarchart.getAxisRight().setDrawGridLines(false);
        bcBarchart.getAxisRight().setEnabled(false);
        bcBarchart.getAxisLeft().setEnabled(true);
        bcBarchart.getXAxis().setDrawGridLines(false);

        // add a nice and smooth animation
        bcBarchart.animateY(1500);

        bcBarchart.getLegend().setEnabled(true);

        bcBarchart.getAxisRight().setDrawLabels(true);
        bcBarchart.getAxisLeft().setDrawLabels(true);
        bcBarchart.setTouchEnabled(true);
        bcBarchart.setDoubleTapToZoomEnabled(false);
        bcBarchart.getXAxis().setEnabled(true);
        bcBarchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bcBarchart.invalidate();

    }

}