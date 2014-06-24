package com.wgen.curriculum.prototype.neuroscience;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class NeuroscienceActivity extends QuestActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuroscience);
        this.doBindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_neuroscience, menu);
        return true;
    }
    
    @Override
	protected void doAfterServiceConnected() {}
    
    public void brainSelected(View v) {
    	// move to confirmation screen
    	ImageButton brainSelected = (ImageButton) findViewById(v.getId());
    	setContentView(R.layout.activity_neuroscience_confirmation);
    	ImageView brainView = (ImageView) findViewById(R.id.imageViewBrain);
    	brainView.setImageDrawable(brainSelected.getDrawable());
    	
    	String tagId = brainSelected.getTag().toString();
    	int resId = getResources().getIdentifier(tagId, "drawable", getPackageName());
    	saveBrainId(resId);
    }
    
    public void brainConfirmed(View v) {
    	// move to night sequence
        Intent intent = new Intent(NeuroscienceActivity.this, HeadphonesActivity.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        NeuroscienceActivity.this.startActivity(intent);
        NeuroscienceActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        NeuroscienceActivity.this.finish();
    }
    
    public void brainNotConfirmed(View v) {
    	setContentView(R.layout.activity_neuroscience);
    }
}
