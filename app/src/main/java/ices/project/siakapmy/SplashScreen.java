package ices.project.siakapmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

public class SplashScreen extends AppCompatActivity {

    Intent intent;
    LottieAnimationView lottie;

    private AppViewModel viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottie = findViewById(R.id.lottie);
        lottie.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);

        intent = new Intent(this, LoginActivity.class);

        // passing a data from view modal.
        viewmodal = ViewModelProviders.of(this).get(AppViewModel.class);

        // below line is use to get all the courses from view modal.
        viewmodal.getAllUsers().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.

                if(models.size() != 0){
                    intent = new Intent(SplashScreen.this, DashboardActivity.class);
                }
                else{
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }
            }
        });

        Thread thread = new Thread(){
            public void run(){

                try{
                    Thread.sleep(5000);
                }
                catch(Exception e){
                }

                startActivity(intent);

            }};
        thread.start();
    }

}