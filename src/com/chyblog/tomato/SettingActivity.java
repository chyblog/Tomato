package com.chyblog.tomato;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SettingActivity extends PreferenceActivity {
	
	private Button menuBtn = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settingpreference);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.setting_title);
		
		menuBtn = (Button) findViewById(R.id.menuBtn);
		menuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
