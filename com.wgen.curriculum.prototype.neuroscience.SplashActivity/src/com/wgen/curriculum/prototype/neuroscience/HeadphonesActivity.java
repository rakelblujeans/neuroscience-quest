 package com.wgen.curriculum.prototype.neuroscience;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HeadphonesActivity extends QuestActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphones);
        // hide keyboard on launch
        EditText passwordTextbox = (EditText) findViewById(R.id.passwordTextbox);
        passwordTextbox.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				clearText();
				return false;
			}
		});
        
        this.doBindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_headphones, menu);
        return true;
    }
    
    public void onEnterPasswordTextbox(View v) {
    	clearText();
    }
    
    private void clearText() {
    	EditText passwordTextbox = (EditText) findViewById(R.id.passwordTextbox);
    	String text = passwordTextbox.getText().toString();
    	String defaultText = getResources().getString(R.string.input_password);
    	if (text.equals(defaultText)) {
    		passwordTextbox.setText(null);
    	}
    }
    
    public void checkPassword(View v) {
    	// if password is correct, start sequence
    	EditText passwordTextbox = (EditText) findViewById(R.id.passwordTextbox);
    	String passwd = passwordTextbox.getText().toString();
    	Log.d("password", passwd);
    	
    	if (passwd.equals(getString(R.string.password))) {
	    	Intent intent = new Intent(HeadphonesActivity.this, MorningActivity.class);
	    	Bundle b = new Bundle();
	        intent.putExtras(b);
	    	HeadphonesActivity.this.startActivity(intent);
	    	HeadphonesActivity.this.finish();

    	} else {
    		// prompt again
        	Context context = getApplicationContext();
        	CharSequence text = "Incorrect password!";
        	int duration = Toast.LENGTH_SHORT;    		
    		Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
    	}
    }
    
    @Override
	protected void doAfterServiceConnected() {}
}
