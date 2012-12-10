package com.chyblog.tomato;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.chyblog.tomato.entity.TomatoReaderContract;
import com.chyblog.tomato.util.TomatoDbHelper;

public class ReportActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		setFeatureDrawableResource(Window.FEATURE_CUSTOM_TITLE, R.layout.report_title);
		
		TomatoDbHelper tomatoDbHelper = new TomatoDbHelper(ReportActivity.this);
		SQLiteDatabase db = tomatoDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) as count from " + TomatoReaderContract.WorkTaskEntry.TABLE_NAME, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		TextView reportTextView = (TextView) findViewById(R.id.report_view);
		String reportText = getString(R.string.report_text);
//		reportText.replaceAll("{0}", count+"");
		reportTextView.setText(reportText);
	}
}
