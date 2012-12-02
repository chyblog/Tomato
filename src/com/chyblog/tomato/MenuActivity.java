package com.chyblog.tomato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MenuActivity extends ListActivity {
	
	private static final long ITEM_SETTING = 0;
	
	private static final long ITEM_REPORT = 10;
	
	private static final long ITEM_ABOUT = 1;
	
	private static final long ITEM_CONTACT = 2;

	private Button backToManBtn = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.menu_title);

		backToManBtn = (Button) findViewById(R.id.homeBtn);

		List<Map<String, String>> menuItems = new ArrayList<Map<String, String>>();
		Map<String, String> setting = new HashMap<String, String>();
		setting.put("menuItem", getString(R.string.menuSetting));
		Map<String, String> report = new HashMap<String, String>();
		report.put("menuItem", getString(R.string.menuReport));
		Map<String, String> about = new HashMap<String, String>();
		about.put("menuItem", getString(R.string.menuAbout));
		Map<String, String> contact = new HashMap<String, String>();
		contact.put("menuItem", getString(R.string.menuContact));
		menuItems.add(setting);
//		menuItems.add(report); 暂未开发统计
		menuItems.add(about);
		menuItems.add(contact);

		SimpleAdapter menuAdapter = new SimpleAdapter(MenuActivity.this, menuItems,
				R.layout.menu_list, new String[] { "menuItem" },
				new int[] { R.id.menuItem });
		setListAdapter(menuAdapter);

		backToManBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(id == MenuActivity.ITEM_ABOUT) {
			Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
			startActivity(intent);
		}
		if(id == MenuActivity.ITEM_SETTING) {
			Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
			startActivity(intent);
		}
		if(id == MenuActivity.ITEM_CONTACT) {
			Intent intent = new Intent(MenuActivity.this, ContactActivity.class);
			startActivity(intent);
		}
	}
}
