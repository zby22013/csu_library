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
 * 登录界面
 * 提供登录功能
 * 提示是否登陆成功
 * @name LoginActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class LoginActivity extends Activity {

	private EditText editUserName;//用户名输入框
	private EditText editPassWord;//密码输入框
	private Button btnSubmit;		//登录按钮
	private final int LOGINMSG = 102;//登录请求码
	private final int ERROR = 0;//登录请求码
	private String error = "";//错误信息
	private String username="";
	private String password="";
	private PostService postService;//提交操作服务
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==LOGINMSG){
				//禁用登录按钮
				btnSubmit.setEnabled(false);
				//获取用户名和密码
				
				login(username,password);
			}
			//登录结果
			else if(msg.what == 1){
				Toast.makeText(LoginActivity.this,
						"登录成功"+username, Toast.LENGTH_SHORT).show();
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
								"用户名或密码错误", Toast.LENGTH_SHORT).show();
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
	 * 初始化界面
	 * 获取界面控件
	 * 设置控件监听
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
		 * 处理点击登录按钮的事件
		 * 如果登录不成功，则提示用户用户名或密码错误
		 * 如果成功，则跳转回 主界面
		 * 
		 * 步骤： 1.获取输入
		 * 	   2.创建登录所需的表单
		 * 	   3.提交表单
		 * 	   4.检验登录是否成功
		 */
		@Override
		public void onClick(View v) {
			if(Connection.isNetworkConnected(LoginActivity.this)){
				username = editUserName.getText().toString();
				password = editPassWord.getText().toString();
				if(!username.equals("")&&!password.equals("")){
					handler.sendEmptyMessage(LOGINMSG);
					//得到InputMethodManager的实例
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//关闭软键盘
					imm.hideSoftInputFromWindow(editPassWord.getWindowToken(), 0);
				}
				else{
					error="用户名和密码不能为空";
					handler.sendEmptyMessage(ERROR);
				}
			}
			else{
				error="无网络链接，请检查您的网络设置";
				handler.sendEmptyMessage(ERROR);
			}
			
		}
		
	};
	/**
	 * 登录操作
	 * @param userName
	 * @param psw
	 * @return result
	 */
	private void login(final String userName, final String psw){
		new Thread(){
			@Override
			public void run() {
				//执行登录请求
				//登录成功
				try {
					username = postService.logInPost(userName, psw);
					if(!username.equals("null")){
						handler.sendEmptyMessage(1);
					}
					//登录失败
					else{
						handler.sendEmptyMessage(ERROR);
					}
				} catch (Exception e) {
					error ="登陆失败,请检查输入后重试";
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
