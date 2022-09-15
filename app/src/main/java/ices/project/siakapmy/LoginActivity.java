package ices.project.siakapmy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ices.project.siakapmy.utility.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {

    Intent intent;
    Button btLogin;

    EditText etEmail, etPassword;
    String email, password;
    String url = "http://smartfarm22.ddns.net/pages/loginauthtest.php";

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";

    private SharedPreferences sharedPreferences;

    private AppViewModel viewmodal;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btLogin = findViewById(R.id.btLogin);

        sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE);
        email = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString(SESSION_COOKIE, null);

        if(email != null && password != null){
            toDashboard();
        }

//        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
//        email = prefs.getString("email",null);
//        password = prefs.getString("password",null);

//        if(email != null && password != null){
//            toDashboard();
//        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }
        });
    }

//    public void loginUser(){
//        if(etEmail.getText().toString().equals("test@gmail.com") && etPassword.getText().toString().equals("abc123")){
//
////            SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
////            editor.putString("email", etEmail.getText().toString());
////            editor.putString("password", etPassword.getText().toString());
////            editor.apply();
//
//
//            // test room database
//            // passing a data from view modal.
//            viewmodal = ViewModelProviders.of(this).get(AppViewModel.class);
//
//            Random rand = new Random();
//            int num = rand.nextInt();
//            email = etEmail.getText().toString();
//            password = etPassword.getText().toString();
//
//            UserModel model = new UserModel(num, email, password);
//
//            viewmodal.insert(model);
//
//            // below line is use to get all the courses from view modal.
//            viewmodal.getAllUsers().observe(this, new Observer<List<UserModel>>() {
//                @Override
//                public void onChanged(List<UserModel> models) {
//                    // when the data is changed in our models we are
//                    // adding that list to our adapter class.
//                    Log.e("Test data ", "Check " + models);
//                }
//            });
//
//            toDashboard();
//
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Invalid email or password.", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void loginUser(){

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        Log.e("response check",response);
                        toDashboard();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error",error.getMessage());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> data = new HashMap<>();
                    data.put("username", email);
                    data.put("password", password);
                    data.put("remember", "remember");
                    data.put("login", "login");
                    data.put("app_login", "app_login");


                    Log.e("TAG","Message: " + data);

                    return data;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    // since we don't know which of the two underlying network vehicles
                    // will Volley use, we have to handle and store session cookies manually
//                    Log.e("response",response.headers.toString());
//                    Map<String, String> responseHeaders = response.headers;
//                    String rawCookies = responseHeaders.get("Set-Cookie");
//                    Log.e("cookies",rawCookies);

                    checkSessionCookie(response.headers);

                    Log.e("response",response.headers.toString());
                    Log.e("cookie",response.headers.get("Set-Cookie"));

                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = super.getHeaders();

                    if (headers == null
                            || headers.equals(Collections.emptyMap())) {
                        headers = new HashMap<String, String>();
                    }

                    addSessionCookie(headers);

                    Log.e("getHeaders", headers.toString());

                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter both email and password.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
//        if (headers.containsKey(SET_COOKIE_KEY)
//                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {

        if (headers.containsKey(SET_COOKIE_KEY)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.putString("username", email);
                prefEditor.commit();

            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = sharedPreferences.getString(SESSION_COOKIE, "");

        Log.e("sessionId", sessionId);

        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());

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