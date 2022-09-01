package ices.project.siakapmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import ices.project.siakapmy.fragment.HistoryFragmentAdapter;
import ices.project.siakapmy.fragment.LocationFragmentAdapter;

public class LocationInfoActivity extends AppCompatActivity {

    TextView tvLocationName;
    String location;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private LocationFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");

        tvLocationName = findViewById(R.id.tvLocationName);

        tvLocationName.setText(location);

        // setting up for tablayout
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        tabLayout.addTab(tabLayout.newTab().setText("Location"));
        tabLayout.addTab(tabLayout.newTab().setText("Video Stream"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new LocationFragmentAdapter(fragmentManager , getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        }); // tablayout impplementation end here


    }

}