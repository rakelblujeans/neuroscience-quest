package com.wgen.curriculum.prototype.neuroscience;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends QuestActivity {
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);
        
        // if we have previously saved progress, jump to the correct place
        if (skipToActivity()) {
        	return;
        }
        
       setVolumeControlStream(AudioManager.STREAM_MUSIC); // have audio respond to volume buttons
        
       Button mEnterBtn = (Button) findViewById(R.id.signin_enter_btn);
        mEnterBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//open/examplelayer
            	//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.layar.com/open/examplelayer"));
        		//startActivity(i);
    	    	Intent intent = new Intent(SplashActivity.this, NeuroscienceActivity.class);
    	        SplashActivity.this.startActivity(intent);
    	        SplashActivity.this.finish();
            }
        });
            
        this.doStartService();
        this.doBindService();
    }
    
    private Boolean skipToActivity() {
    	
    	String myName = this.getComponentName().getClassName();
        String activityName = getProgress();
        if (!activityName.isEmpty() && !activityName.equals(myName)) {
        	Class<?> clazz;
			try {
				clazz = Class.forName(activityName);
				Intent intent = new Intent(SplashActivity.this, clazz);
	        	SplashActivity.this.startActivity(intent);
		        SplashActivity.this.finish();
		        return true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				clearPreferences();
			}
        }
        
        return false;
    }
    
    @Override
	 protected void doAfterServiceConnected() {
		 this.mBoundService.setupAudio(R.raw.neuro_spooky, true, null);
	 }
}
