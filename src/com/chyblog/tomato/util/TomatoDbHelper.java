package com.chyblog.tomato.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chyblog.tomato.entity.TomatoReaderContract;

public class TomatoDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_NAME = "Tomato.db";
	
	public TomatoDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TomatoReaderContract.SQL_CREATE_WORK_TASK_ENTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
