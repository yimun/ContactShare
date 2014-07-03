package com.yimu.contactshare;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeActivity extends Activity implements SensorEventListener {
	// Sensor管理器
	private SensorManager mSensorManager = null;
	// 震动
	private Vibrator mVibrator = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		((TextView) findViewById(R.id.TITLE_TEXT)).setText("摇一摇分享");
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.LEFT_BUTTON))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						ShakeActivity.this.finish();
					}
				});
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		int sensorType = arg0.sensor.getType();
		float[] values = arg0.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if (Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14
					|| Math.abs(values[2]) > 14) {
				mVibrator.vibrate(100);
				SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
				System.out.println("感应成功");
				Toast.makeText(this,f.format(new Date()) + "手机摇动了...",Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
}