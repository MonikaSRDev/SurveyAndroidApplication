package com.example.mithraapplication;

import static com.example.mithraapplication.VideoScreen.videoModulesArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.SingleVideo;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FullScreenVideoView extends AppCompatActivity{

    private ImageView backButton, logoutButton, videoPlayButton, videoPauseButton, videoBackwardButton;
    private TextView backTV, logoutTV, participantName, videoModuleNameTV, videoDurationTV;
    private SeekBar fullScreenVideoSeekBar;
    private Button englishButton, kannadaButton;
    private VideoView videoViewFullScreen;
    private MithraUtility mithraUtility = new MithraUtility();
    private SingleVideo singleVideo = new SingleVideo();
    private int pos = 0, position = 0;
    private String totalDuration = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_screen_view);
        RegisterViews();
        onClickOfBackButton();
        onClickOfLogoutButton();
        onClickOfLanguageButton();
        getCurrentLocale();
        onClickOfVideoPlayButton();
        onClickOfVideoPauseButton();
        onClickOfBackwardButton();
        onTouchVideoEvent();

        pos = getIntent().getIntExtra("ModulePosition", 0);
        position = getIntent().getIntExtra("VideoPosition", 0);
    }

    private void RegisterViews(){

        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_participant));
        participantName = findViewById(R.id.participantNameVFSTV);
        participantName.setText(participantUserName);
        videoModuleNameTV = findViewById(R.id.videoModuleNameTV);
        videoModuleNameTV.setText(videoModulesArrayList.get(pos).getVideoModuleName());

        backButton = findViewById(R.id.backButtonVFS);
        logoutButton = findViewById(R.id.logoutVFSIcon);
        videoPlayButton = findViewById(R.id.fullScreenVideoPlayButton);
        videoPauseButton = findViewById(R.id.fullScreenVideoPauseButton);
        videoPauseButton.setVisibility(View.GONE);
        videoBackwardButton = findViewById(R.id.fullScreenBackwardButton);
        videoBackwardButton.setVisibility(View.GONE);

        logoutTV = findViewById(R.id.logoutVFSTV);
        backTV = findViewById(R.id.backButtonTVVFS);

        englishButton = findViewById(R.id.englishVFSButton);
        kannadaButton = findViewById(R.id.kannadaVFSButton);

        videoDurationTV = findViewById(R.id.videoDuration);
        fullScreenVideoSeekBar = findViewById(R.id.fullScreenVideoSeekbar);
        fullScreenVideoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    // this is when actually seekbar has been sought to a new position
                    videoViewFullScreen.seekTo(progress);
                }
            }
        });
        fullScreenVideoSeekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        videoViewFullScreen = findViewById(R.id.fullScreenVideoView);
        String path = getIntent().getStringExtra("VideoPath");
        Uri uri = Uri.parse(path);
        videoViewFullScreen.setVideoURI(uri);
        videoViewFullScreen.requestFocus();
        videoViewFullScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                videoViewFullScreen.stopPlayback();
                showAlertForFeedback();
                if(!(position + 1 >= videoModulesArrayList.get(pos).getSingleVideoArrayList().size())){
                    videoModulesArrayList.get(pos).getSingleVideoArrayList().get(position + 1).setVideoPlayed(true);
                    videoModulesArrayList.get(pos).getSingleVideoArrayList().get(position).setVideoStatus("Completed");
                }
            }
        });

    }

    private String timeConversion(int millis){
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    private void onClickOfVideoPlayButton(){
        videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoViewFullScreen.start();
                totalDuration = timeConversion(videoViewFullScreen.getDuration());
                fullScreenVideoSeekBar.setMax(videoViewFullScreen.getDuration());
                fullScreenVideoSeekBar.postDelayed(onEverySecond, 1000);
                videoPlayButton.setVisibility(View.GONE);
            }
        });
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if(fullScreenVideoSeekBar != null) {
                fullScreenVideoSeekBar.setProgress(videoViewFullScreen.getCurrentPosition());
                String currentTime = timeConversion(videoViewFullScreen.getCurrentPosition());
                videoDurationTV.setText(currentTime+"/"+ totalDuration);
            }

            if(videoViewFullScreen.isPlaying()) {
                fullScreenVideoSeekBar.postDelayed(onEverySecond, 1000);
            }

        }
    };

    private void onClickOfVideoPauseButton(){
        videoPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoViewFullScreen.pause();
                videoPauseButton.setVisibility(View.GONE);
                videoBackwardButton.setVisibility(View.GONE);
                videoPlayButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onClickOfBackwardButton(){
        videoBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = videoViewFullScreen.getCurrentPosition();
                videoViewFullScreen.seekTo(position - (10 * 1000));
            }
        });
    }

    private void onTouchVideoEvent(){
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            public boolean onSingleTapUp(MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_UP){
                    if(videoPlayButton.getVisibility() == View.VISIBLE){
                        videoPauseButton.setVisibility(View.GONE);
                        videoBackwardButton.setVisibility(View.GONE);
                    } else{
                        videoPauseButton.setVisibility(View.VISIBLE);
                        videoBackwardButton.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            // Using handler with postDelayed called runnable run method
                            @Override
                            public void run() {
                                videoPauseButton.setVisibility(View.GONE);
                                videoBackwardButton.setVisibility(View.GONE);
                            }
                        }, 2*1000);
                    }
                    return true;
                }
                return false;
            }
        });

        videoViewFullScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private Bitmap generateThumbnailForVideo(){
        String path = getIntent().getStringExtra("VideoPath");
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this.getApplicationContext(), uri);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        return thumb;
    }

    private void showAlertForFeedback(){

        View customLayout = getLayoutInflater().inflate(R.layout.feedback_popup, null);

        Button yesFeedbackButton = customLayout.findViewById(R.id.yesFeedbackButton);
        Button noFeedbackButton = customLayout.findViewById(R.id.noFeedbackButton);

        ImageView imageViewThumbnail = customLayout.findViewById(R.id.feedbackImageOfVideo);
        if(imageViewThumbnail!=null){
            imageViewThumbnail.setBackground(new BitmapDrawable(getResources(), generateThumbnailForVideo()));
        }

        Dialog dialog  = new Dialog(FullScreenVideoView.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        yesFeedbackButton.setOnClickListener(v -> {
            Toast.makeText(FullScreenVideoView.this, getString(R.string.opinion_toast), Toast.LENGTH_LONG).show();
            dialog.dismiss();
            moveToVideoScreen();
        });

        noFeedbackButton.setOnClickListener(v -> {
            Toast.makeText(FullScreenVideoView.this, getString(R.string.opinion_toast), Toast.LENGTH_LONG).show();
            dialog.dismiss();
            moveToVideoScreen();
        });
    }

    private void moveToVideoScreen(){
        Intent loginIntent = new Intent(FullScreenVideoView.this, VideoScreen.class);
        loginIntent.putExtra("FromActivity", "FullScreenVideoView");
        loginIntent.putExtra("VideoFile", singleVideo);
        startActivity(loginIntent);
        finish();
    }

    private void onClickOfBackButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, VideoScreen.class);
                intent.putExtra("FromActivity", "FullScreenVideoView");
                startActivity(intent);
                finish();
            }
        });

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, VideoScreen.class);
                intent.putExtra("FromActivity", "FullScreenVideoView");
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickOfLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("kn");
            }
        });
    }

    /**
     * @param selectedLanguage
     * Description : This method is used to change the content of the screen to user selected language
     */
    public void changeLocalLanguage(String selectedLanguage){
        Locale myLocale = new Locale(selectedLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    public void getCurrentLocale(){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
        }
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logoutTV.setText(R.string.logout);
        backTV.setText(R.string.back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FullScreenVideoView.this, VideoScreen.class);
        intent.putExtra("FromActivity", "FullScreenVideoView");
        startActivity(intent);
        finish();
    }
}
