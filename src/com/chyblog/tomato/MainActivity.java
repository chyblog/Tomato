package com.chyblog.tomato;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.domob.android.ads.DomobActivity;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobSplashAd;

import com.chyblog.tomato.util.TispToastFactory;

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
	private WakeLock wakeLock = null;
	private Handler handler = null;
	private LinearLayout mAdContainer = null;
	private DomobAdView mAdView300x50 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		workbeginBtn = (Button) findViewById(R.id.workbeginBtn);
		restbeginBtn = (Button) findViewById(R.id.restbeginBtn);
		stopBtn = (Button) findViewById(R.id.stopBtn);
		menuBtn = (Button) findViewById(R.id.menuBtn);
		timeCounterView = (TextView) findViewById(R.id.timeCounter);
		workTaskView = (TextView) findViewById(R.id.workTaskView);
		workTask = (EditText) findViewById(R.id.workTask);
		workTime = (EditText) findViewById(R.id.workTime);
		restTime = (EditText) findViewById(R.id.restTime);
		mAdContainer = (LinearLayout) findViewById(R.id.domoAdv);
		mAdView300x50 = new DomobAdView(this, "填写多盟public_key", DomobAdView.INLINE_SIZE_320X50);
		mAdContainer.addView(mAdView300x50);
		
		
		wakeLock = ((PowerManager) this
				.getSystemService(MainActivity.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK
						| PowerManager.ON_AFTER_RELEASE, "MainActivity");
		handler = new TimeCounterHandler();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		
		workTime.setText(preferences.getString("workTaskTime", "25"));
		restTime.setText(preferences.getString("restTime", "5"));

		workbeginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beginWorkTask();
			}

		});

		restbeginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beginRest();
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
				Intent intent = new Intent(MainActivity.this,
						MenuActivity.class);
				startActivity(intent);
			}
		});

		workTime.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				Editor editor = preferences.edit();
				editor.putString("workTaskTime", workTime.getText()
						.toString());
				editor.commit();
				return false;
			}
		});
		
		restTime.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				Editor editor = preferences.edit();
				editor.putString("restTime", restTime.getText()
						.toString());
				editor.commit();
				return false;
			}
		});
	}

	private void beginWorkTask() {
		Pattern p = Pattern.compile("//s*|\t|\r|\n");
		String strWorkTask = workTask.getText().toString();
		Matcher m = p.matcher(strWorkTask);
		strWorkTask = m.replaceAll(" ");
		int iWorkTime = 0;
		String strWorkTime = workTime.getText().toString();
		if (!"".equals(strWorkTime)) {
			iWorkTime = Integer.parseInt(strWorkTime);
		}
		if ("".equals(strWorkTask)) {
			Toast toast = TispToastFactory.getToast(MainActivity.this, getString(R.string.error_notask));
			toast.show();
			workTask.requestFocus();
			return;
		}
		if (iWorkTime <= 0) {
			Toast toast = TispToastFactory.getToast(MainActivity.this, getString(R.string.work_time_more_than_0));
			toast.show();
			workTime.requestFocus();
			return;
		}
		workTaskView.setText(strWorkTask);
		workTask.setVisibility(View.INVISIBLE);
		worktimeCounter = new TimeCounter(iWorkTime, 0, TimeCounter.WORK);
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

	private void beginRest() {
		int iRestTime = 0;
		String strRestTime = restTime.getText().toString();
		if (!"".equals(strRestTime)) {
			iRestTime = Integer.parseInt(strRestTime);
		}
		if (iRestTime <= 0) {
			Toast toast = TispToastFactory.getToast(MainActivity.this, getString(R.string.rest_time_more_than_0));
			toast.show();
			restTime.requestFocus();
			return;
		}
		workTaskView.setText(getString(R.string.rest_tip));
		workTask.setVisibility(View.INVISIBLE);
		resttimeCounter = new TimeCounter(iRestTime, 0, TimeCounter.REST);
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

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		boolean alywas_light_flag = preference
				.getBoolean("always_light", false);
		if (null != wakeLock && alywas_light_flag) {
			wakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != wakeLock && wakeLock.isHeld()) {
			wakeLock.release();
		}
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
	 * ����跺伐��	 * 
	 * @author chenyang
	 * 
	 */
	private class TimeCounter {

		/** 绫诲�锛�伐浣�*/
		public static final int WORK = 1;

		/** 绫诲�锛����*/
		public static final int REST = 2;

		/** ����跺���*/
		private int minute;

		/** ����剁���*/
		private int second;

		/** ����剁被���宸ヤ�or浼��锛�*/
		private int type;

		/**
		 * ����芥�锛��濮������舵���		 * 
		 * @param minute
		 *            ����跺���		 * @param second
		 *            ����剁���		 * @param type
		 *            ����剁被��		 */
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
		 * 璁剧疆涓��绉��������
		 */
		private void getNextSecond() {
			if (this.minute == 0 && this.second == 0) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				if(preferences.getBoolean("vibrate", false)) {
					Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
					long[] pattern = { 800, 2000};
					vibrator.vibrate(pattern,-1);
				}
				if(preferences.getBoolean("ring", false)) {
					MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,
							R.raw.ring);
					mediaPlayer.start();
				}
				workTask.setVisibility(View.VISIBLE);
				SharedPreferences preference = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				boolean cycle_flag = preference.getBoolean("cycle", false);
				if (this.type == TimeCounter.WORK) {
					worktimer.cancel();
					if (cycle_flag) {
						beginRest();
					}

				}
				if (this.type == TimeCounter.REST) {
					resttimer.cancel();
					if (cycle_flag) {
						beginWorkTask();
					}
				}
				workbeginBtn.setEnabled(true);
				restbeginBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				return;
			}
			--this.second;
			if (this.second < 0) {
				this.second = 59;
				--this.minute;
			}
		}

		/**
		 * �峰�涓��绉���剧ず��		 * 
		 * @return 涓��绉���剧ず��		 */
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
