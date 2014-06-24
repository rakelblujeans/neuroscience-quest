package com.wgen.curriculum.prototype.neuroscience;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MediaManagementService extends Service
									implements AudioManager.OnAudioFocusChangeListener, 
									MediaPlayer.OnErrorListener {
	private AudioManager am;
	private MediaPlayer mp;	
	
	@Override 
	public void onCreate() {
		am = (AudioManager)(this.getSystemService(Context.AUDIO_SERVICE));
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MediaManagementService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
	@Override
    public void onDestroy() {
		am.abandonAudioFocus(this);
		if (mp != null) {
			if (mp.isPlaying()) mp.stop();
			mp.release(); 
			mp = null;
		}
    }
	
	// This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = (IBinder) new LocalBinder();
    
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		MediaManagementService getService() {
			return MediaManagementService.this;
		}
	}
	
	
	public void restartAudio() {
		if (mp != null) {
			mp.seekTo(0);
			mp.start();
		}
	}
	
	public void stopAudio() {
		if (mp != null) {
			if (mp.isPlaying()) { 
				mp.stop();
			}
		}
	}
	
	public void pauseAudio() {
		if (mp != null && mp.isPlaying()) {
			mp.pause();
		}
	}
	
	public void resumeAudio() {
		if (mp == null) { 
			initMediaPlayer();
		}
        else if (!mp.isPlaying()) {
        	/*try {
				mp.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
        	mp.start();
        }
        mp.setVolume(1.0f, 1.0f);
	}
	
	public void initMediaPlayer() {
		 mp = new MediaPlayer();
	}
	
	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
        case AudioManager.AUDIOFOCUS_GAIN:
            resumeAudio();
            break;

        case AudioManager.AUDIOFOCUS_LOSS:
            // Lost focus for an unbounded amount of time: stop playback and release media player
            if (mp.isPlaying()) mp.stop();
            mp.release();
            mp = null;
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            // Lost focus for a short time, but we have to stop
            // playback. We don't release the media player because playback
            // is likely to resume
            pauseAudio();
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            // Lost focus for a short time, but it's ok to keep playing
            // at an attenuated level
            if (mp.isPlaying()) mp.setVolume(0.1f, 0.1f);
            break;
		}
	}
	
	public void setupAudio(int audio_file_id, boolean isLooping, OnCompletionListener onAudioCompletionListener) {
		stopAudio();
    	if (mp != null) {
    		mp.release();
    	}
	    mp = MediaPlayer.create(this,  audio_file_id);
		
	    if (onAudioCompletionListener != null) {
	    	mp.setOnCompletionListener(null);
	    	mp.setOnCompletionListener(onAudioCompletionListener);
	    }
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mp.setLooping(isLooping);         
        
        // Request audio focus for playback
        int result = am.requestAudioFocus(this,
                                         // Use the music stream.
                                         AudioManager.STREAM_MUSIC,
                                         // Request permanent focus.
                                         AudioManager.AUDIOFOCUS_GAIN);
           
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
        	// Start playback.
        	mp.start();
        }
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return false;
	}
}
