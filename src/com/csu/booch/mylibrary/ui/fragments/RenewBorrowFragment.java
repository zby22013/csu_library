
package com.csu.booch.mylibrary.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

/**
 * @name RemewBorrowFragment.java
 * @author Tomorrow
 * @since  2014-5-10
 */
@SuppressLint("HandlerLeak")
public class RenewBorrowFragment extends Fragment{
	
	private final int LOAD=100;
	private final int SHOW=101;
	private final int RENEW=102;
	private final int BACK=103;
	private ArrayList<String> table;
	private DataService dataService;
	private CheckBox all;
	private TableLayout infoTable;
	private ArrayList<CheckBox> boxs;
	private Button btn_renew;
	private ArrayList<String> num;
	private ArrayList<String> back;//返回信息
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
						try {
							table = dataService.getRenewBorrowTable();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(SHOW);
					}
					
				}.start();
				break;
			case SHOW:
				if(table.size()==0){
					Toast.makeText(getActivity(), "列表为空",
							Toast.LENGTH_SHORT).show();
				}
				else{
					boxs = new ArrayList<CheckBox>();
					showTable();
					for(int i = 0;i<boxs.size();i++){
						boxs.get(i).setOnCheckedChangeListener(listener);
					}
				}
				break;
			case RENEW:
				final String ids = msg.getData().getString("ids");
				new Thread(){

					@Override
					public void run() {
						try {
							back = dataService.renewMyBooks(ids);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(BACK);
					}
					
				}.start();
				break;
			case BACK:
				dlg.dismiss();
				if(back==null){
					Toast.makeText(getActivity(), "请求失败，请重试", Toast.LENGTH_SHORT).show();
				}
				else{
					if(back.isEmpty()){
						Toast.makeText(getActivity(), "无结果", Toast.LENGTH_SHORT).show();
					}
					else{
						String msgBack="";
						for(int i= 0;i<back.size();i++){
							msgBack+="第"+i+"本书:"+back.get(i)+"\n";
						}
						new AlertDialog.Builder(getActivity())
						.setTitle("续借结果")
						.setMessage(msgBack)
						.setPositiveButton("确定", null)
						.show();
						break;
					}
				}
			}
			
		}

		
		
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataService = new DataService();
		num = new ArrayList<String>();
		dlg = new ProgressDialog(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_renewborrow,
				container, false);
		infoTable = (TableLayout)rootView.findViewById(R.id.renew_table);
		all= (CheckBox)rootView.findViewById(R.id.renew_checkbox_all);
		btn_renew = (Button)rootView.findViewById(R.id.renew_submit);
		all.setOnCheckedChangeListener(listener);
		if(table!=null){
			handler.sendEmptyMessage(SHOW);
		}
		else{
			handler.sendEmptyMessage(LOAD);
		}
		btn_renew.setOnClickListener(btnListener);
		return rootView;
	}
	OnCheckedChangeListener listener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int id = buttonView.getId();
			if(id == R.id.renew_checkbox_all){
				for(int i =0;i<boxs.size();i++){
					boxs.get(i).setChecked(buttonView.isChecked());
				}
			}
			else{
				if(buttonView.isChecked()){
					num.add(id+"");
				}
				else{
					num.remove(id+"");
				}
				//Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	OnClickListener btnListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(num.isEmpty()){
				Toast.makeText(getActivity(), "请选择要续借的书籍", Toast.LENGTH_SHORT).show();
			}
			else{
				String str="";
				for(int i = 0;i<num.size();i++){
					str+=num.get(i)+";";
				}
				Message msg = new Message();
				msg.what=RENEW;
				Bundle data = new Bundle();
				data.putString("ids", str);
				msg.setData(data);
				dlg.setMessage("正在发送请求...");
				dlg.show();
				dlg.setCancelable(true);
				dlg.setOnCancelListener(cancelListener);
				handler.sendMessage(msg);
//				Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
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
	private void showTable() {
		int col=9;
		ArrayList<TableRow> trs = new ArrayList<TableRow>();
		ArrayList<TextView> txts = new ArrayList<TextView>();
		LayoutParams param = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		for(int r = 0;r<table.size()/col;r++){
			//添加一行
			trs.add(new TableRow(infoTable.getContext()));
			CheckBox box = new CheckBox(trs.get(r).getContext());
			box.setId(Integer.parseInt(table.get(r*col)));
			boxs.add(box);
			trs.get(r).addView(box);
			for(int c = 0;c <col; c++){
				if(c%col==0){
					continue;
				}
				if((c-2)%col==0){
					continue;
				}
				if((c-3)%col==0){
					continue;
				}
				if((c-4)%col==0){
					continue;
				}
				if((c-7)%col==0){
					continue;
				}
				else{
					TextView txt = new TextView(trs.get(r).getContext());
					//设置矛值
					txt.setLayoutParams(param);
					//添加一项
					txts.add(txt);
					//设置信息
					txts.get(txts.size()-1).setText(table.get(r*col+c)+"\n");
					//设置间距
					txts.get(txts.size()-1).setWidth(trs.get(r).getWidth()/4);
					//设置位置
					txts.get(txts.size()-1).setGravity(Gravity.CENTER);
					//加入行中
					trs.get(r).addView(txts.get(txts.size()-1));
				}
			}
			//加入表格中
			infoTable.addView(trs.get(r));
		}
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
