package com.csu.booch.mylibrary.ui.fragments;

import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 借阅权限查询
 * @name PermissionFragment.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("HandlerLeak")
public class PermissionFragment extends Fragment{
//	private TextView txt_per_detail;
//	private final int LOAD = 997;
//	private final int SHOW = 996;
//	private String result;
//	private Handler handler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.what==LOAD){
//				new Thread(){
//
//					@Override
//					public void run() {
//						//请求资源
//						try {
//							if(Connection.doGet(ConnectionURL.getPermissionURL(), true)){
//								result = new Analysis()
//								.getPermissionDetail(Connection.getResult());
//							}
//							else{
//								//error
//							}
//							handler.sendEmptyMessage(SHOW);
//							Connection.disConnection();
//						} catch (ClientProtocolException e) {
//							
//							e.printStackTrace();
//						} catch (IOException e) {
//							
//							e.printStackTrace();
//						}
//					}
//					
//				}.start();
//			}
//			else{
//				txt_per_detail.setText(result);
//			}
//		}
//		
//	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_permission,
				container, false);
//		txt_per_detail = (TextView)rootView.findViewById(R.id.txt_per_detail);
//		txt_per_detail
//		handler.sendEmptyMessage(LOAD);
		return rootView;
	}
	
	
	
}
