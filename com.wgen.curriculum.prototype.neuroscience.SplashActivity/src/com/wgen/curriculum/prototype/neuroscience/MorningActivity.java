package com.wgen.curriculum.prototype.neuroscience;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MorningActivity extends QuestActivity {
	
	private void setVisibility(int visibility) {
	    ImageView imgView = (ImageView) findViewById(R.id.headDownImageView);
        imgView.setVisibility(visibility);
        imgView = (ImageView) findViewById(R.id.headDownTextView);
        imgView.setVisibility(visibility);
        Button button = (Button) findViewById(R.id.buttonStop);
		button.setVisibility(visibility);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning);
        setVisibility(View.INVISIBLE);
        
        this.doBindService();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_morning, menu);
        return true;
    }
    
    protected void doAfterServiceConnected() {
     	//this.mBoundService.stopAudio();
        
        OnCompletionListener onAudioCompletionListener = new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				
				OnCompletionListener onAudioCompletionListener2 = new OnCompletionListener() {
		        	public void onCompletion(MediaPlayer mp) {
		        		Button button = (Button) findViewById(R.id.buttonToLayar);
		        		button.setVisibility(View.VISIBLE);
		        	}
		        };
		        
		    	setVisibility(View.VISIBLE);
		    	int audioId = getMorningAudio();
		    	mBoundService.setupAudio(audioId, false, onAudioCompletionListener2);
		  }
		};
		this.mBoundService.setupAudio(R.raw.opening_1, false, onAudioCompletionListener);
		
	}
    
    public void onAlarmClockClicked(View v) {
    	mBoundService.stopAudio();
    	Button button = (Button) findViewById(R.id.buttonStop);
		button.setVisibility(View.INVISIBLE);
    	button = (Button) findViewById(R.id.buttonToLayar);
		button.setVisibility(View.VISIBLE);
    }
    
    public void onToLayarClick(View v) {
    	// move to layar screen
		Intent intent = new Intent(MorningActivity.this, LayarActivity.class);
    	Bundle b = new Bundle();
        intent.putExtras(b);
    	MorningActivity.this.startActivity(intent);
    	MorningActivity.this.finish();
    }
}
