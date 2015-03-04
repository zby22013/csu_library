package com.csu.booch.mylibrary.ui.fragments;


import java.util.ArrayList;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 修改信息
 * @name ChangeInfoFragment.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("HandlerLeak")
public class ChangeInfoFragment extends Fragment{
	private TextView log_id;
	private TextView log_number;
	private TextView log_name;
	private TextView log_identify;
	private TextView log_state;
	private TextView log_date_effect;
	private EditText log_company;
	private EditText log_address;
	private EditText log_email;
	private EditText log_phone;
	private EditText log_password;
	private Button comfirm;
	private final int LOAD=999;
	private final int SHOW=998;
	private final int CHANGE=996;
	private final int MSG=997;
	private ArrayList<String> info;
	private DataService dataService;
	private PostService postService;
	private String company;
	private String address;
	private String email;
	private String phone;
	private String password;
	private String message;
	private ProgressDialog dlg;//进度对话框
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch(msg.what){
			case LOAD:
				new Thread(){
					@Override
					public void run() {

						//请求信息
						try {
							info = dataService.getUserInfo();
							if(info !=null){
								handler.sendEmptyMessage(SHOW);
							}
							else{
								//error
							}
						} catch (Exception e) {

							e.printStackTrace();
						} 
					}

				}.start();
				break;
			case SHOW:
				try{
					log_id.setText(info.get(0)+"");
					log_number.setText(info.get(1)+"");
					log_name.setText(info.get(2)+"");
					log_identify.setText(info.get(3)+"");
					log_state.setText(info.get(4)+"");
					log_date_effect.setText(info.get(5)+"");
					log_company.setText(info.get(6)+"");
					log_address.setText(info.get(7)+"");
					log_email.setText(info.get(8)+"");
					log_phone.setText(info.get(9)+"");
					log_password.setText(info.get(10)+"");
				}
				catch(Exception e){

				}
				break;
			case CHANGE:
				new Thread(){

					@Override
					public void run() {
						try {
							if(postService.changeInfoPost(company,address,email,phone,password)){
								message="ok";
							}
							else{
								message="no";
							}
						} catch (Exception e) {
							message="error";
							e.printStackTrace();
						} 
						handler.sendEmptyMessage(MSG);
					}

				}.start();
				break;
			case MSG:
				dlg.dismiss();
				if(message.equals("ok")){
					Toast.makeText(getActivity(),
							"修改成功", Toast.LENGTH_SHORT).show();
				}
				else if(message.equals("no")){
					Toast.makeText(getActivity(),
							"修改失败，请检查输入", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(),
							"请求失败,请重试", Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		postService = new PostService();
		dataService = new DataService();
		dlg = new ProgressDialog(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_info,
				container, false);
		inintView(rootView);
		if(info!=null){
			handler.sendEmptyMessage(SHOW);
		}
		else{
			handler.sendEmptyMessage(LOAD);
		}


		return rootView;
	}
	private void inintView(View rootView) {

		log_id = (TextView)rootView.findViewById(R.id.log_id);
		log_number= (TextView)rootView.findViewById(R.id.log_number);
		log_name= (TextView)rootView.findViewById(R.id.log_name);
		log_identify= (TextView)rootView.findViewById(R.id.log_identify);
		log_state= (TextView)rootView.findViewById(R.id.log_state);
		log_date_effect= (TextView)rootView.findViewById(R.id.log_date_effect);
		log_company = (EditText)rootView.findViewById(R.id.log_company);
		log_address= (EditText)rootView.findViewById(R.id.log_address);
		log_email= (EditText)rootView.findViewById(R.id.log_email);
		log_phone= (EditText)rootView.findViewById(R.id.log_phone);
		log_password= (EditText)rootView.findViewById(R.id.log_password);
		comfirm = (Button)rootView.findViewById(R.id.btn_info_submit);

		comfirm.setOnClickListener(listener);
	}
	OnClickListener listener= new OnClickListener(){

		@Override
		public void onClick(View v) {
			company = log_company.getText().toString();
			address = log_address.getText().toString();
			email = log_email.getText().toString();
			phone = log_phone.getText().toString();
			password=log_password.getText().toString();
			if(checkInput()){
				handler.sendEmptyMessage(CHANGE);
				dlg.setMessage("正在发送请求...");
				dlg.show();
				dlg.setCancelable(true);
				dlg.setOnCancelListener(cancelListener);
			}


		}

	};
	OnCancelListener cancelListener = new OnCancelListener(){

		@Override
		public void onCancel(DialogInterface dialog) {
			try {
				Connection.disConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	private boolean checkInput() {
		if(email.equals("")){
			Toast.makeText(getActivity(),
					"请输入邮箱", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(phone.equals("")){
			Toast.makeText(getActivity(),
					"请输入电话号码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(password.equals("")){
			Toast.makeText(getActivity(),
					"请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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
