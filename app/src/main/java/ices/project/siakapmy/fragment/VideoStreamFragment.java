package ices.project.siakapmy.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import ices.project.siakapmy.DashboardActivity;
import ices.project.siakapmy.HistoryActivity;
import ices.project.siakapmy.IsolateActivity;
import ices.project.siakapmy.ProfileActivity;
import ices.project.siakapmy.R;

public class VideoStreamFragment extends Fragment{

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer1;
    private VLCVideoLayout videoLayout1;

    ImageView ivPlay1;
    TextView tvLinkVideo;

    private static final String url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4";
    String insertedURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_video_stream, container, false);

        libVlc = new LibVLC(getContext());
        mediaPlayer1 = new MediaPlayer(libVlc);
        videoLayout1 = view.findViewById(R.id.videoLayout1);

        tvLinkVideo = view.findViewById(R.id.tvLinkVideo);

        ivPlay1 = view.findViewById(R.id.ivPlay1);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mediaPlayer1.attachViews(videoLayout1, null, false, false);

        Media media = new Media(libVlc, Uri.parse(url));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=600");

        ivPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer1.setMedia(media);
                media.release();

                ivPlay1.setImageResource(R.drawable.ic_pause);
                mediaPlayer1.play();


                ivPlay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ivPlay1.setImageResource(R.drawable.ic_play);
                        mediaPlayer1.pause();

                        String check = new Boolean(!mediaPlayer1.isPlaying()).toString();

                        Log.e("Check", check);

                        if(!mediaPlayer1.isPlaying()){
                            ivPlay1.setImageResource(R.drawable.ic_pause);
                            mediaPlayer1.play();
                        }
                    }
                });
            }
        });

        tvLinkVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Insert Link");
                builder.setMessage("Please insert the link to the video stream.");
                builder.setCancelable(false);

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tvLinkVideo.setText(input.getText());
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("VideoLink", Context.MODE_PRIVATE).edit();
                        editor.putString("insertedLink",input.getText().toString());
                        editor.commit();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void onStop()
    {
        super.onStop();

        ivPlay1.setImageResource(R.drawable.ic_play);
        mediaPlayer1.stop();
        mediaPlayer1.detachViews();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mediaPlayer1.release();
        libVlc.release();
    }

    public void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getActivity().getSharedPreferences("VideoLink", Context.MODE_PRIVATE);
        String s1 = sh.getString("insertedLink", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4");
        // Setting the fetched data
        // in the Tv
        tvLinkVideo.setText(s1);
    }


}