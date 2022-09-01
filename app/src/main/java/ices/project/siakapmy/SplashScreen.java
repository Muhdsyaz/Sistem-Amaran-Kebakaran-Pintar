package ices.project.siakapmy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    Intent intent;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottie = findViewById(R.id.lottie);
        lottie.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);

        intent = new Intent(this, LoginActivity.class);

        Thread thread = new Thread(){
            public void run(){

                try{
                    Thread.sleep(5000);
                }
                catch(Exception e){
                }

                intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);

            }};
        thread.start();
    }

}