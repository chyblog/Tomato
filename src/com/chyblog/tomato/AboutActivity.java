package com.chyblog.tomato;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        
        backToMenu = (Button) findViewById(R.id.backToMenu);
        aboutWebView = (WebView) findViewById(R.id.aboutWebView);
        aboutWebView.loadUrl("file:///android_asset/web-zh/about.html");
        
        backToMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_about, menu);
        return true;
    }
}
