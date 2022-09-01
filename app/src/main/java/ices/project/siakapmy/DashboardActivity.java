package ices.project.siakapmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ices.project.siakapmy.utility.NetworkChangeListener;

public class DashboardActivity extends AppCompatActivity implements BuildingRVAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{

    BottomNavigationView bottomNav;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    BuildingRVAdapter buildingRVAdapter;
    RecyclerView rvBuilding;

    Spinner spinner;
    String selectedLocation;

    TextView tvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //declare textview
        tvSearch = findViewById(R.id.tvSearch);

        //declare recyclerview
        rvBuilding = findViewById(R.id.rvBuilding);

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.dashboard);

        //perform ItemSelectedListener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        return true;

                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        overridePendingTransition(0,0);
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

        // data to populate the RecyclerView with
        ArrayList<String> bulding = new ArrayList<>();
        bulding.add("Pintu Gerbang UTeM");
        bulding.add("Pusat Pengajian Siswazah");
        bulding.add("Pusat Pembuatan Termaju");
        bulding.add("FTMK");
        bulding.add("FKM");
        bulding.add("FKP");

        bulding.add("Perpustakaan Laman Hikmah");
        bulding.add("PBPI");
        bulding.add("PPP");
        bulding.add("PPPK UTeM");
        bulding.add("Klinik UTeM");

        bulding.add("Masjid Sayyidina Abu Bakar");
        bulding.add("FKEKK");
        bulding.add("Kafe Staf UTeM");
        bulding.add("FKE");
        bulding.add("Pejabat HEPA");

        bulding.add("Canselori");
        bulding.add("Kompleks Sukan");
        bulding.add("Stadium UTeM");
        bulding.add("Pusat KOKU");
        bulding.add("Pejabat Pembangunan");

        bulding.add("KK Lestari");
        bulding.add("KK Satria (Blok Tuah)");
        bulding.add("KK Satria (Blok Jebat)");
        bulding.add("KK Satria (Blok Lekir)");
        bulding.add("KK Satria (Blok Kasturi)");
        bulding.add("Kafe Satria");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvBuilding);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buildingRVAdapter = new BuildingRVAdapter(this, bulding);
        buildingRVAdapter.setClickListener(this);
        recyclerView.setAdapter(buildingRVAdapter);

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mainLocation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        selectedLocation = spinner.getSelectedItem().toString();

        // to filter recyclerview
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ArrayList<String> searchItems = new ArrayList<>();
                for(String building : bulding){
                    if(building.toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(building);
                    }
                }
                buildingRVAdapter = new BuildingRVAdapter(getApplicationContext(), searchItems);
                rvBuilding.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                buildingRVAdapter.setClickListener(DashboardActivity.this);
                rvBuilding.setAdapter(buildingRVAdapter);

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        String location = buildingRVAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), LocationInfoActivity.class);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        category = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), category, Toast.LENGTH_SHORT).show();
        selectedLocation = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}