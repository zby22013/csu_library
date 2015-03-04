package com.csu.booch.mylibrary.data.adapters;

import com.csu.booch.mylibrary.data.domain.MyAccount;
import com.csu.booch.mylibrary.ui.R;
import com.csu.booch.mylibrary.ui.activity.LoginActivity;
import com.csu.booch.mylibrary.ui.activity.PersonalCenterActivity;
import com.csu.booch.mylibrary.ui.activity.RemindTimingActivity;
import com.csu.booch.mylibrary.ui.activity.BookManageActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
/**
 * ������������
 * @name DrawerViewAdapter.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class DrawerViewAdapter extends BaseAdapter {
	private Activity context;
	ViewHolder viewHolder;
	public DrawerViewAdapter(Activity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		viewHolder = new ViewHolder();
		convertView = View.inflate(context, 
				R.layout.drawer_view, null);
		viewHolder.login = (Button)convertView.findViewById(R.id.btn_login);
		viewHolder.like = (ImageButton)convertView.findViewById(R.id.btn_like);
		viewHolder.manager = (ImageButton)convertView.findViewById(R.id.btn_manager);
		viewHolder.remind = (ImageButton)convertView.findViewById(R.id.btn_remind);
		convertView.setTag(viewHolder);
		//���ü���
		viewHolder.login.setOnClickListener(BtnListener);
		viewHolder.like.setOnClickListener(BtnListener);
		viewHolder.manager.setOnClickListener(BtnListener);
		viewHolder.remind.setOnClickListener(BtnListener);
		if(!MyAccount.getUserName().equals("")){
			viewHolder.login.setText(MyAccount.getUserName());
		}
		return convertView;
	}
	OnClickListener BtnListener = new OnClickListener(){



		/**
		 * ��������¼��ť���¼�
		 * ��ת����¼����
		 * �����ǲ��ǵ�¼�ɹ�
		 * ���ɹ����򷵻��û���
		 */
		@Override
		public void onClick(View v) {
			int id = v.getId();
			Intent intent;
			switch(id){
			case R.id.btn_login:
				if(!viewHolder.login.getText().toString().equals("��¼")){
					new AlertDialog.Builder(context)
					.setTitle("ȷ��ע����")
					.setNegativeButton("ȡ��", null)
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent1 = new Intent(context,LoginActivity.class);
							context.startActivityForResult(intent1,200);
							viewHolder.login.setText("��¼");
						}
					})
					.show();
				}
				else{
					intent = new Intent(context,LoginActivity.class);
					context.startActivityForResult(intent,200);
				}
				break;

			case R.id.btn_like:
				if(MyAccount.getCookies()==null){
					Toast.makeText(context, "δ��¼", Toast.LENGTH_SHORT).show();
					Intent intent_login = new Intent(context,LoginActivity.class);
					context.startActivityForResult(intent_login,200);
				}
				else{
					intent = new Intent(context,PersonalCenterActivity.class);
					context.startActivity(intent);
				}
				break;
			case R.id.btn_manager:
				
				if(MyAccount.getCookies()==null){
					Toast.makeText(context, "δ��¼", Toast.LENGTH_SHORT).show();
					Intent intent_login = new Intent(context,LoginActivity.class);
					context.startActivityForResult(intent_login,200);
				}
				else{
					intent = new Intent(context,BookManageActivity.class);
					context.startActivity(intent);
				}
				break;
			case R.id.btn_remind:
				if(MyAccount.getCookies()==null){
					Toast.makeText(context, 
							"��δ��¼�����ȵ�¼", Toast.LENGTH_SHORT).show();
					Intent intent_login = new Intent(context,LoginActivity.class);
					context.startActivityForResult(intent_login,200);
				}
				else{
					Intent intent_remindTiming = new Intent(context,RemindTimingActivity.class);
					context.startActivity(intent_remindTiming);
				}
		
				break;
			}


		}

	};

	public class ViewHolder{
		Button login;
		ImageButton like;
		ImageButton manager;
		ImageButton remind;
	}

}
