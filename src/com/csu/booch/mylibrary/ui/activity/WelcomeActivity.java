
package com.csu.booch.mylibrary.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.csu.booch.mylibrary.ui.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * @name WelcomeActivity.java
 * @author Tomorrow
 * @since  2014-5-7
 */
@SuppressLint("HandlerLeak")
public class WelcomeActivity extends Activity{

	private Timer timer;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==100){
				timer = new Timer();
				timer.schedule(task, 3000);
			}
		}
	};
	private TimerTask task = new TimerTask(){

		@Override
		public void run() {
			Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ImageView welcome = new ImageView(WelcomeActivity.this);
		welcome.setBackgroundResource(R.drawable.welcome);
		Animation animation = AnimationUtils
					.loadAnimation(welcome.getContext(), R.anim.my_anim_design);
		welcome.setAnimation(animation);
		this.setContentView(welcome);
		handler.sendEmptyMessage(100);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return true;
	}
	
	
	
}
