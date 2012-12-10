package com.chyblog.tomato.entity;

import android.provider.BaseColumns;

public class TomatoReaderContract {

	private TomatoReaderContract() {
	}

	public class WorkTaskEntry implements BaseColumns {

		public static final String TABLE_NAME = "work_task";

		public static final String CLUMN_NAME_TASK_NAME = "task_name";

		public static final String CLUMN_NAME_CREATE_TIME = "create_time";

	}

	public static final String SQL_CREATE_WORK_TASK_ENTRY = "CREATE TABLE "
			+ TomatoReaderContract.WorkTaskEntry.TABLE_NAME + " ("
			+ TomatoReaderContract.WorkTaskEntry._ID
			+ " INTEGER PRIMARY KEY,"
			+ TomatoReaderContract.WorkTaskEntry.CLUMN_NAME_TASK_NAME
			+ " TEXT,"
			+ TomatoReaderContract.WorkTaskEntry.CLUMN_NAME_CREATE_TIME
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
	
}
