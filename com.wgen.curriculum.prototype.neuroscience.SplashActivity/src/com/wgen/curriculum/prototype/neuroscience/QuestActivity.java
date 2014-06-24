package com.wgen.curriculum.prototype.neuroscience;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseArray;

public abstract class QuestActivity extends Activity {
	 protected MediaManagementService mBoundService;
	 // relates a brain's image to it's character
	 protected SparseArray<String> peopleBrains;
	 
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ActionBar ab = this.getActionBar();
		if (ab != null)
			ab.hide();
		
		peopleBrains = new SparseArray<String>();
	    peopleBrains.put(R.drawable.neuro_brain_blue, "drp");
	    peopleBrains.put(R.drawable.neuro_brain_gold, "lostmariner");
	    peopleBrains.put(R.drawable.neuro_brain_green, "miguel");
	    peopleBrains.put(R.drawable.neuro_brain_purple, "mrsb");
	    peopleBrains.put(R.drawable.neuro_brain_red, "mrss");
	 }
	
	protected void saveBrainId(int brainId) {
		Context context = getApplicationContext();
	    SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putInt(getString(R.string.character_brain_id), brainId);
	    editor.commit();
	}
	
	protected Integer getBrainId() {
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		int brainId = sharedPref.getInt(getString(R.string.character_brain_id), -1);
		return brainId;
	}
	
	protected void saveProgress(String activityName) {
		Context context = getApplicationContext();
	    SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putString(getString(R.string.neuroscience_progress), activityName);
	    editor.commit();
	}
	
	protected String getProgress() {
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String activityName = sharedPref.getString(getString(R.string.neuroscience_progress), "");
		return activityName;
	}
	
	protected void clearPreferences() {
		Context context = getApplicationContext();
	    SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
	    //int testId = getMorningAudio();
	    //String testStr = getProgress();
	    editor.clear();
	    editor.commit();
	    //testId = getMorningAudio();
	    //testStr = getProgress();
	}
	
	 protected int getMorningAudio() {
		 String character = peopleBrains.get(getBrainId());
	     int resId = getResources().getIdentifier("morning_" + character, "raw", getPackageName());
	     return resId;
	 }
	 
	 protected int getWalkingAudio() {
		 String character = peopleBrains.get(getBrainId());
	     int resId = getResources().getIdentifier("walking_" + character, "raw", getPackageName());
	     return resId;
	 }
	 
	 // force subclasses to implement
	 protected abstract void doAfterServiceConnected();
	 
	 private ServiceConnection mConnection = new ServiceConnection() {
	        public void onServiceConnected(ComponentName className, IBinder service) {
	            // This is called when the connection with the service has been
	            // established, giving us the service object we can use to
	            // interact with the service.  Because we have bound to a explicit
	            // service that we know is running in our own process, we can
	            // cast its IBinder to a concrete class and directly access it.
	            mBoundService = ((MediaManagementService.LocalBinder)service).getService();
	            //mBoundService.resumeAudio();
	            doAfterServiceConnected();
	        }

	        public void onServiceDisconnected(ComponentName className) {
	            // This is called when the connection with the service has been
	            // unexpectedly disconnected -- that is, its process crashed.
	            // Because it is running in our same process, we should never
	            // see this happen.
	            mBoundService = null;
	        }
	    };
	    
		private boolean mIsBound;
		
		void doStartService() {
			startService(new Intent(this, MediaManagementService.class));
		}
		
		void doStopService() {
			stopService(new Intent(this, MediaManagementService.class));
		}
		
	    void doBindService() {
	        // Establish a connection with the service.  We use an explicit
	        // class name because we want a specific service implementation that
	        // we know will be running in our own process (and thus won't be
	        // supporting component replacement by other applications).
	    	Intent intent = new Intent(this,MediaManagementService.class);
	    	bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	        mIsBound = true;
	    }

	    void doUnbindService() {
	        if (mIsBound) {
	            // Detach our existing connection.
	            unbindService(mConnection);
	            mIsBound = false;
	        }
	    }

	    @Override
	    protected void onPause() {
	    	// try to prevent audio hiccups when switching activities within our app
		    Context context = getApplicationContext();
		    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		    List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		    if (!taskInfo.isEmpty()) {
		    	ComponentName topActivity = taskInfo.get(0).topActivity; 
		        if (!topActivity.getPackageName().equals(context.getPackageName())) {
		        	if (mBoundService != null) {
		        		mBoundService.stopAudio();
		        	}
		        }
		    }
	    
		    super.onPause();	      
	    	saveProgress(this.getComponentName().getClassName());
	    }
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        doUnbindService();
	    }
}