package com.chyblog.tomato;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class AboutActivity extends Activity {
	
	private Button backToMenu = null;
	private WebView aboutWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.about_title);
        
        backToMenu = (Button) findViewById(R.id.menuBtn);
        aboutWebView = (WebView) findViewById(R.id.aboutWebView);
        aboutWebView.loadUrl("file:///android_asset/web-zh/about.html");
        
        backToMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		});
    }
}
