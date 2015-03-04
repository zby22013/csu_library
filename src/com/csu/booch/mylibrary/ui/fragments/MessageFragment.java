
package com.csu.booch.mylibrary.ui.fragments;

import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.ui.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @name MessageFragment.java
 * @author Tomorrow
 * @since  2014-5-10
 */
@SuppressLint("HandlerLeak")
public class MessageFragment extends Fragment {
	private final int LOAD=100;
	private final int SHOW=101;
	private TextView txtMsg;
	private String txt="";
	private DataService dataService;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==LOAD){
				new Thread(){

					@Override
					public void run() {
						try {
							txt="";
							txt = dataService.getMyMessage();
						} catch (Exception e) {
							txt="";
							e.printStackTrace();
						}
						handler.sendEmptyMessage(SHOW);
					}
				}.start();
				
			}
			else{
				if(txt.equals("")){
					Toast.makeText(getActivity(),
							"获取数据失败", Toast.LENGTH_SHORT).show();
				}
				else{
					String[] str = txt.split(" ");
					txt="";
					for(int i =0;i<str.length;i++){
						txt+=str[i]+"\n";
					}
					txtMsg.setTextSize(20);
					txtMsg.setText(txt);
				}
			}
			
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		dataService = new DataService();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_message,
				container, false);
		txtMsg = (TextView)rootView.findViewById(R.id.msg_txt);
		if(txt.equals("")){
			handler.sendEmptyMessage(LOAD);
		}
		else{
			handler.sendEmptyMessage(SHOW);
		}
		return rootView;
	}
	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
		try {
			Connection.disConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
