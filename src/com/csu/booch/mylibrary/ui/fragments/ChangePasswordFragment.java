package com.csu.booch.mylibrary.ui.fragments;

import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
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
import android.widget.Toast;
/**
 * 修改密码
 * @name ChangePasswordFragment.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint({ "NewApi", "HandlerLeak" })
public class ChangePasswordFragment extends Fragment{
	private EditText oldPassord;
	private EditText newPassord;
	private EditText reNewPassord;
	private Button confirm;
	private String op ;
	private String np;
	private String renp ;
	private String msgs ; 
	private PostService postService;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			if(msg.what==100){
				confirm.setEnabled(false);
				new Thread(){
					@Override
					public void run() {
						try {
							if(postService.changePwdPost(op, np, renp)){
								msgs = "修改成功";
							}
							else{
								msgs = "修改失败，请检查你的输入";
							}
							handler.sendEmptyMessage(200);
						} catch (Exception e) {
							msgs = e.toString();
						}
					}

				}.start();
			}
			else{
				confirm.setEnabled(true);
				reNewPassord.setText("");
				newPassord.setText("");
				oldPassord.setText("");
				Toast.makeText(getActivity(), msgs, Toast.LENGTH_SHORT).show();
			}
			
		}

	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		postService = new PostService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_password,
				container, false);
		oldPassord = (EditText)rootView.findViewById(R.id.old_pwd);
		newPassord = (EditText)rootView.findViewById(R.id.new_pwd);
		reNewPassord = (EditText)rootView.findViewById(R.id.renew_pwd);
		confirm = (Button)rootView.findViewById(R.id.btn_conchange);
		confirm.setOnClickListener(listener);
		return rootView;
	}

	OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			op = oldPassord.getText().toString();
			np = newPassord.getText().toString();
			renp = reNewPassord.getText().toString();
			if(!op.isEmpty()&&!np.isEmpty()&&!renp.isEmpty()){
				if(!np.equals(renp)){
					Toast.makeText(getActivity(), "两次新密码不一致", Toast.LENGTH_SHORT).show();
				}
				else{
					handler.sendEmptyMessage(100);
				}
			}
			else{
				Toast.makeText(getActivity(), "任何一项不能为空", Toast.LENGTH_SHORT).show();
			}
		}

	};
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
