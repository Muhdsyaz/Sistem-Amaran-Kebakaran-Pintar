package ices.project.siakapmy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ices.project.siakapmy.utility.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {

    Intent intent;
    Button btLogin;

    EditText etEmail, etPassword;
    String email, password;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btLogin = findViewById(R.id.btLogin);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        email = prefs.getString("email",null);
        password = prefs.getString("password",null);

        if(email != null && password != null){
            toDashboard();
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }
        });
    }

    public void loginUser(){
        if(etEmail.getText().toString().equals("test@gmail.com") && etPassword.getText().toString().equals("abc123")){

            SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
            editor.putString("email", etEmail.getText().toString());
            editor.putString("password", etPassword.getText().toString());
            editor.apply();

            toDashboard();

        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid email or password.", Toast.LENGTH_SHORT).show();
        }
    }

    public void toDashboard(){
        intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
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

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}