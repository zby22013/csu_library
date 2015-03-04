
package com.csu.booch.mylibrary.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

/**
 * @name HistoryFragment.java
 * @author Tomorrow
 * @since  2014-5-10
 */
@SuppressLint("HandlerLeak")
public class HistoryFragment extends Fragment{
	
	private EditText startDateEdit = null;
	private EditText endDateEdit = null;
	private ImageButton search = null;
	private DataService dataService = null;
	private PostService postService = null;
	final int LOAD = 0;
	final int SHOW = 1;
	private ArrayList<String> table;
	private TableLayout infoTable;
	private String startDate = "";
	private String endDate = "";
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
							table = dataService.getHistoryData(postService.getHistoryPost(startDate, endDate));
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						handler.sendEmptyMessage(SHOW);
					}
					
				}.start();
				break;
			case SHOW:
				dlg.dismiss();
				if(table.size()==0){
					Toast.makeText(getActivity(), "列表为空，请检查输入日期",
							Toast.LENGTH_SHORT).show();
				}
				else{
					showTable();
				}
				
			}
			
		}
			
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		dataService = new DataService();
		postService = new PostService();
		dlg = new ProgressDialog(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_history,
				container, false);
		startDateEdit = (EditText) rootView.findViewById(R.id.date_start);
		endDateEdit = (EditText) rootView.findViewById(R.id.date_end);
		search = (ImageButton) rootView.findViewById(R.id.his_search);
		search.setOnClickListener(butClickListener);
		infoTable = (TableLayout)rootView.findViewById(R.id.his_info_table);
		return rootView;
	}
	
	private OnClickListener butClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//得到InputMethodManager的实例
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			//关闭软键盘
			imm.hideSoftInputFromWindow(endDateEdit.getWindowToken(), 0);
			if (startDateEdit.getText().equals("")||endDateEdit.getText().equals("")) {
				Toast.makeText(getActivity(), "请输入起始日期和结束日期", Toast.LENGTH_SHORT).show();
				return;
			}
			startDate = startDateEdit.getText().toString();
			endDate = endDateEdit.getText().toString();
			handler.sendEmptyMessage(LOAD);
			dlg.setMessage("正在发送请求...");
			dlg.show();
			dlg.setCancelable(true);
			dlg.setOnCancelListener(cancelListener);
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
		int col=6;
		ArrayList<TableRow> trs = new ArrayList<TableRow>();
		ArrayList<TextView> txts = new ArrayList<TextView>();
		LayoutParams param = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		for(int r = 0;r<table.size()/col;r++){
			//添加一行
			trs.add(new TableRow(infoTable.getContext()));
			for(int c = 0;c <col; c++){
				if(c==0){
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
