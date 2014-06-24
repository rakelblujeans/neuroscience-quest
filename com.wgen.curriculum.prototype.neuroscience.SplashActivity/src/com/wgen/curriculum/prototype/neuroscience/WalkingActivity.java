package com.wgen.curriculum.prototype.neuroscience;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WalkingActivity extends QuestActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        this.doBindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_walking, menu);
        return true;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	clearPreferences();
    }
    
    @Override
    protected void doAfterServiceConnected() {
    	OnCompletionListener onAudioCompletionListener = new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				Button button = (Button) findViewById(R.id.end_quest_button);
				button.setVisibility(View.VISIBLE);
			}
    	};
    	
    	// play audio for this character
    	int audioId = getWalkingAudio();
    	mBoundService.setupAudio(audioId, false, onAudioCompletionListener);
	 }
    
    public void onEndQuest(View v) {
    	clearPreferences();
    	mBoundService.setupAudio(R.raw.wrap_up, false, null);
    	
    	ImageView imgView = (ImageView) findViewById(R.id.imageViewBkg);
    	imgView.setVisibility(View.INVISIBLE);
    	Button button = (Button) findViewById(R.id.restart_button);
    	button.setVisibility(View.INVISIBLE);
    	button = (Button) findViewById(R.id.play_button);
    	button.setVisibility(View.INVISIBLE);
    	button = (Button) findViewById(R.id.end_quest_button);
    	button.setVisibility(View.INVISIBLE);
    	TextView textView= (TextView) findViewById(R.id.textViewDone);
    	textView.setVisibility(View.VISIBLE);
    }
    
    public void onRestartSound(View v) {
    	mBoundService.restartAudio();
    	Button button = (Button) findViewById(R.id.play_button);
		button.setText(R.string.pause_button);
    }
    
    public void onTogglePlay(View v) {
    	Button button = (Button) findViewById(v.getId());
    	String play_button_text = getResources().getString(R.string.play_button);
    	if (button.getText().equals(play_button_text)) {
    		mBoundService.resumeAudio();
    		button.setText(R.string.pause_button);
    	} else {
    		mBoundService.pauseAudio();
    		button.setText(R.string.play_button);
    	}
    }
}
