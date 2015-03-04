package com.csu.booch.mylibrary.ui.activity;


import org.apache.http.client.CookieStore;
import com.csu.booch.mylibrary.data.domain.MyAccount;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��¼����
 * �ṩ��¼����
 * ��ʾ�Ƿ��½�ɹ�
 * @name LoginActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class LoginActivity extends Activity {

	private EditText editUserName;//�û��������
	private EditText editPassWord;//���������
	private Button btnSubmit;		//��¼��ť
	private final int LOGINMSG = 102;//��¼������
	private final int ERROR = 0;//��¼������
	private String error = "";//������Ϣ
	private String username="";
	private String password="";
	private PostService postService;//�ύ��������
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==LOGINMSG){
				//���õ�¼��ť
				btnSubmit.setEnabled(false);
				//��ȡ�û���������
				
				login(username,password);
			}
			//��¼���
			else if(msg.what == 1){
				Toast.makeText(LoginActivity.this,
						"��¼�ɹ�"+username, Toast.LENGTH_SHORT).show();
				CookieStore cookie = Connection.getCookies();
				MyAccount.setCookies(cookie);
				MyAccount.setUserName(username);
				MyAccount.setUserId(editUserName.getText().toString());
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			else if(msg.what == ERROR){
				{
					btnSubmit.setEnabled(true);
					editPassWord.setText("");
					if(error.equals("")){
						Toast.makeText(LoginActivity.this,
								"�û������������", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(LoginActivity.this,
								error, Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initView();
	}
	/**
	 * ��ʼ������
	 * ��ȡ����ؼ�
	 * ���ÿؼ�����
	 */
	private void initView() {
		postService = new PostService();
		editUserName = (EditText)this.findViewById(R.id.edt_account);
		editPassWord = (EditText)this.findViewById(R.id.edt_password);
		btnSubmit = (Button)this.findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(btnSubmitListener);
		MyAccount.setCookies(null);
		MyAccount.setUserName("");
	}
	OnClickListener btnSubmitListener = new OnClickListener(){
		/**
		 * ��������¼��ť���¼�
		 * �����¼���ɹ�������ʾ�û��û������������
		 * ����ɹ�������ת�� ������
		 * 
		 * ���裺 1.��ȡ����
		 * 	   2.������¼����ı�
		 * 	   3.�ύ��
		 * 	   4.�����¼�Ƿ�ɹ�
		 */
		@Override
		public void onClick(View v) {
			if(Connection.isNetworkConnected(LoginActivity.this)){
				username = editUserName.getText().toString();
				password = editPassWord.getText().toString();
				if(!username.equals("")&&!password.equals("")){
					handler.sendEmptyMessage(LOGINMSG);
					//�õ�InputMethodManager��ʵ��
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//�ر������
					imm.hideSoftInputFromWindow(editPassWord.getWindowToken(), 0);
				}
				else{
					error="�û��������벻��Ϊ��";
					handler.sendEmptyMessage(ERROR);
				}
			}
			else{
				error="���������ӣ�����������������";
				handler.sendEmptyMessage(ERROR);
			}
			
		}
		
	};
	/**
	 * ��¼����
	 * @param userName
	 * @param psw
	 * @return result
	 */
	private void login(final String userName, final String psw){
		new Thread(){
			@Override
			public void run() {
				//ִ�е�¼����
				//��¼�ɹ�
				try {
					username = postService.logInPost(userName, psw);
					if(!username.equals("null")){
						handler.sendEmptyMessage(1);
					}
					//��¼ʧ��
					else{
						handler.sendEmptyMessage(ERROR);
					}
				} catch (Exception e) {
					error ="��½ʧ��,�������������";
					handler.sendEmptyMessage(ERROR);
				}
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			Connection.disConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
