package ices.project.siakapmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ices.project.siakapmy.utility.NetworkChangeListener;

public class ProfileActivity extends AppCompatActivity {

    MaterialIconView mvProfileMenu;
    LinearLayout layoutProfileMenu;
    ConstraintLayout layoutProfile;
    BottomNavigationView bottomNav;
    TextView tvLogout, tvEmail, tvPassword;

    String username, random_password;

    private AppViewModel viewmodal;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //set variable for material icon
        mvProfileMenu = findViewById(R.id.mvProfileMenu);

        // declare textview
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvLogout = findViewById(R.id.tvLogout);

        SharedPreferences prefs = getSharedPreferences("UserLogin", MODE_PRIVATE);

        username = prefs.getString("username",null);
        tvEmail.setText(username);
        Log.e("email", username);

        random_password = prefs.getString("sessionid",null);
        tvPassword.setText(random_password);
        Log.e("password", random_password);
//        String password = prefs.getString("password","");

        // passing a data from view modal.
        viewmodal = ViewModelProviders.of(this).get(AppViewModel.class);

        // below line is use to get all the courses from view modal.
//        viewmodal.getAllUsers().observe(this, new Observer<List<UserModel>>() {
//            @Override
//            public void onChanged(List<UserModel> models) {
//                // when the data is changed in our models we are
//                // adding that list to our adapter class.
//
//                for(UserModel model : models) {
//                    email = model.getUsername();
//                    password = model.getPasswordhash();
//
//                    Log.e("Test data ", "Id: " + model.getId() + " Email: " + model.getUsername() + " Password: " + model.getPasswordhash());
//                }
//                tvEmail.setText(email);
//                tvPassword.setText(password);
//            }
//        });



        // declare layout
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutProfileMenu = findViewById(R.id.layoutProfileMenu);

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.profile);

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
                        startActivity(new Intent(getApplicationContext(),ReportActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.isolate:
                        startActivity(new Intent(getApplicationContext(),IsolateActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        mvProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.VISIBLE);
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.INVISIBLE);
            }
        });

        layoutProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Logout Confirmation");
                dialog.setMessage("Are you sure you want to logout?" );
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Logout".
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                        viewmodal.deleteAllUsers();


                        SharedPreferences.Editor editor = getSharedPreferences("UserLogin", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();

                        startActivity(intent);

                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));

            }
        });

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