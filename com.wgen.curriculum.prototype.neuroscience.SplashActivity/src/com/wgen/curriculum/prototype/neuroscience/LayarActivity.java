package com.wgen.curriculum.prototype.neuroscience;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class LayarActivity extends QuestActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layar);
        this.doBindService();
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.layar.com/open/examplelayer"));
		//startActivity(i);
        
        //readLogcat();
    }

    public void readLogcat() {
    	try
    	{
    	    Process mLogcatProc = null;
    	    BufferedReader reader = null;
    	    mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-v"});

    	    reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

    	    String line;
    	    final StringBuilder log = new StringBuilder();
    	    String separator = System.getProperty("line.separator"); 

    	    while ((line = reader.readLine()) != null)
    	    {
    	    	if (line.contains("START {act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER]")) {
    	    		Log.e("layar", "detected launch");
    	    		// START {act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] 
    	            // flg=0x10200000 pkg=com.layar cmp=com.layar/.VisionArActivity bnds=[272,52][368,148] u=0} from pid 734
    	    		 log.append(line);
    	    	     log.append(separator);
    	    	}
    	    }
    	    //String w = log.toString();
    	   // Toast.makeText(getApplicationContext(),w, Toast.LENGTH_LONG).show();
    	}
    	catch (Exception e) 
    	{
    	   // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_layar, menu);
        return true;
    }
    
    public void proceedToWalking(View v) {
    	Intent intent = new Intent(LayarActivity.this, WalkingActivity.class);
    	Bundle b = new Bundle();
        intent.putExtras(b);
    	LayarActivity.this.startActivity(intent);
        LayarActivity.this.finish();
    }
    
    @Override
	protected void doAfterServiceConnected() {}
}
