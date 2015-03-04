package com.csu.booch.mylibrary.ui.activity;

import com.csu.booch.mylibrary.ui.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * @name AboutActivity.java
 * @author Tomorrow
 * @since  2014-5-4
 */
public class AboutActivity extends Activity{
	private Button aboutTeam;
	private Button declare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		aboutTeam = (Button)findViewById(R.id.about_team);
		declare = (Button)findViewById(R.id.declare);
		
		aboutTeam.setOnClickListener(listener);
		declare.setOnClickListener(listener);
	}
	OnClickListener listener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.about_team:
				
				new AlertDialog.Builder(AboutActivity.this)
				.setTitle("关于Booch")
				.setMessage(R.string.booch)
				.setPositiveButton("确定", null)
				.show();
				break;
			case R.id.declare:
				new AlertDialog.Builder(AboutActivity.this)
				.setTitle("声明")
				.setMessage(R.string.declare_detail)
				.setPositiveButton("确定", null)
				.show();
				break;
			}
			
		}
		
	};
}
