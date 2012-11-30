package com.chyblog.tomato;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class ContactActivity extends Activity {
	
	private Button backToMenu = null;
	private WebView contactWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.contact_title);
        
        backToMenu = (Button) findViewById(R.id.menuBtn);
        contactWebView = (WebView) findViewById(R.id.contactWebView);
        contactWebView.loadUrl("file:///android_asset/web-zh/contact.html");
        
        backToMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		});
    }
}
