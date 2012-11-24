package com.chyblog.tomato;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView timeCounterView = null;
	private TextView workTaskView = null;
	private EditText workTask = null;
	private EditText workTime = null;
	private EditText restTime = null;
	private TimeCounter worktimeCounter = null;
	private TimeCounter resttimeCounter = null;
	private CountDownTimer worktimer = null;
	private CountDownTimer resttimer = null;
	private Button workbeginBtn = null;
	private Button restbeginBtn = null;
	private Button stopBtn = null;
	private Button menuBtn = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		workbeginBtn = (Button) findViewById(R.id.workbeginBtn);
		restbeginBtn = (Button) findViewById(R.id.restbeginBtn);
		stopBtn = (Button) findViewById(R.id.stopBtn);
		menuBtn = (Button) findViewById(R.id.menuBtn);
		timeCounterView = (TextView) findViewById(R.id.timeCounter);
		workTaskView = (TextView) findViewById(R.id.workTaskView);
		workTask = (EditText) findViewById(R.id.workTask);
		workTime = (EditText) findViewById(R.id.workTime);
		restTime = (EditText) findViewById(R.id.restTime);
		final Handler handler = new TimeCounterHandler();

		workbeginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Pattern p = Pattern.compile("//s*|\t|\r|\n");
				String strWorkTask = workTask.getText().toString();
				Matcher m = p.matcher(strWorkTask);
				strWorkTask = m.replaceAll(" ");
				if("".equals(strWorkTask)) {
					Dialog dialog = new Dialog(MainActivity.this);
					dialog.setTitle(getString(R.string.error_notask));
					dialog.show();
					return;
				}
				workTaskView.setText(strWorkTask);
				workTask.setVisibility(View.INVISIBLE);
				int iWorkTime = 0;
				String strWorkTime = workTime.getText().toString();
				if (!"".equals(strWorkTime)) {
					iWorkTime = Integer.parseInt(strWorkTime);
				}
				worktimeCounter = new TimeCounter(iWorkTime, 0,
						TimeCounter.WORK);
				worktimer = new CountDownTimer((iWorkTime + 1) * 60 * 1000, 997) {

					@Override
					public void onTick(long millisUntilFinished) {
						Message message = new Message();
						message.arg1 = TimeCounter.WORK;
						handler.sendMessage(message);
					}

					@Override
					public void onFinish() {

					}
				}.start();
			}
		});

		restbeginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				workTask.setVisibility(View.INVISIBLE);
				workTaskView.setText(getString(R.string.rest_tip));
				int iRestTime = 0;
				String strRestTime = restTime.getText().toString();
				if (!"".equals(strRestTime)) {
					iRestTime = Integer.parseInt(strRestTime);
				}
				resttimeCounter = new TimeCounter(iRestTime, 0,
						TimeCounter.REST);
				resttimer = new CountDownTimer((iRestTime + 1) * 60 * 1000, 997) {

					@Override
					public void onTick(long millisUntilFinished) {
						Message message = new Message();
						message.arg1 = TimeCounter.REST;
						handler.sendMessage(message);
					}

					@Override
					public void onFinish() {

					}
				}.start();
			}
		});

		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				workTask.setVisibility(View.VISIBLE);
				workbeginBtn.setEnabled(true);
				restbeginBtn.setEnabled(true);
				timeCounterView.setText("00:00");
				if (resttimer != null) {
					resttimer.cancel();
				}
				if (worktimer != null) {
					worktimer.cancel();
				}
				stopBtn.setEnabled(false);
			}
		});
		
		menuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MenuActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class TimeCounterHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String currentTime = "";
			if (msg.arg1 == TimeCounter.WORK) {
				currentTime = worktimeCounter.getNextSecondTime();
			} else if (msg.arg1 == TimeCounter.REST) {
				currentTime = resttimeCounter.getNextSecondTime();
			}
			timeCounterView.setText(currentTime);
		}
	}

	/**
	 * 倒计时工具
	 * 
	 * @author chenyang
	 * 
	 */
	private class TimeCounter {

		/** 类型：工作 */
		public static final int WORK = 1;

		/** 类型：休息 */
		public static final int REST = 2;

		/** 倒计时分钟 */
		private int minute;

		/** 倒计时秒钟 */
		private int second;

		/** 倒计时类型（工作or休息） */
		private int type;

		/**
		 * 构造函数，初始化倒计时时间
		 * 
		 * @param minute
		 *            倒计时分钟
		 * @param second
		 *            倒计时秒钟
		 * @param type
		 *            倒计时类型
		 */
		public TimeCounter(int minute, int second, int type) {
			this.minute = minute;
			this.second = second;
			this.type = type;
			workbeginBtn.setEnabled(false);
			workbeginBtn.setEnabled(false);
			restbeginBtn.setEnabled(false);
			stopBtn.setEnabled(true);
			if (this.type == TimeCounter.WORK) {
				if (resttimer != null) {
					resttimer.cancel();
				}
			}
			if (this.type == TimeCounter.REST) {
				if (worktimer != null) {
					worktimer.cancel();
				}
			}
		}

		/**
		 * 设置下一秒钟的分、秒
		 */
		private void getNextSecond() {
			if (this.minute == 0 && this.second == 0) {
				MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,
						R.raw.ring);
				mediaPlayer.start();
				workTask.setVisibility(View.VISIBLE);
				if (this.type == TimeCounter.WORK) {
					worktimer.cancel();
					workbeginBtn.setEnabled(true);
				}
				if (this.type == TimeCounter.REST) {
					resttimer.cancel();
					restbeginBtn.setEnabled(true);
				}
				return;
			}
			--this.second;
			if (this.second < 0) {
				this.second = 59;
				--this.minute;
			}
		}

		/**
		 * 获取下一秒的显示值
		 * 
		 * @return 下一秒的显示值
		 */
		public String getNextSecondTime() {

			getNextSecond();
			StringBuffer timeView = new StringBuffer(16);
			if (this.minute < 10) {
				timeView.append("0");
			}
			timeView.append(this.minute).append(":");
			if (this.second < 10) {
				timeView.append("0");
			}
			timeView.append(this.second);
			return timeView.toString();
		}
	}
}
