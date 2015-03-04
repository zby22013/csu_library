package com.csu.booch.mylibrary.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.csu.booch.mylibrary.data.adapters.BookListDataAdapter;
import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.MyAccount;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;
import android.widget.ImageButton;
/**
 * 
 * @name RemindTimingActivity.java
 * @author ZhouBoYu
 * @since  2014-5-10
 */
@SuppressLint("HandlerLeak")
public class RemindTimingActivity extends Activity {
	private ListView remind_list;//列表
	private final int LOAD =100;//加载指令
	private final int SHOW =101;//显示指令
	private final int DELETE = 102;//删除指令
	private final int MSG = 104;
	private final int LOADCOVER =103;//显示指令
	private ProgressBar bar;//进度条
	private PostService postService;//post服务
	private DataService dataService;//数据服务
	private ArrayList<Book> remindBookList;//提醒书籍数据
	private String massage="";
	private BookListDataAdapter adapter;
	private ImageButton btn_delete;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int p = 0;
			switch(msg.what){
			case LOAD:
				new Thread(){

					@Override
					public void run() {
						//获取提醒列表
						try {
							String html = postService.getRemindListPost(MyAccount.getUserId());
							remindBookList = dataService.getRemindBookList(html);

							massage="ok";
						} catch (IOException e) {
							massage =e.toString();
							e.printStackTrace();
						}
						handler.sendEmptyMessage(SHOW);
					}

				}.start();
				break;
			case SHOW:
				if(massage.equals("ok")){
					if(remindBookList.size()==0){
						Toast.makeText(RemindTimingActivity.this,"提醒列表为空", Toast.LENGTH_SHORT).show();
					}
					else{
						if(adapter==null){
							adapter = new BookListDataAdapter(RemindTimingActivity.this,remindBookList);
							remind_list.setAdapter(adapter);
							handler.sendEmptyMessage(LOADCOVER);
						}
						else{
							adapter.notifyDataSetChanged();
						}
					}
				}
				else{
					Toast.makeText(RemindTimingActivity.this, massage, Toast.LENGTH_SHORT).show();
				}
				bar.setVisibility(View.GONE);
				break;
			case LOADCOVER:
				new Thread(){

					@Override
					public void run() {
						for(int i = 0;i<remindBookList.size();i++ ){
							remindBookList.get(i).setCoverImage(dataService.getBookCover(remindBookList.get(i).getISBN()));
						}
						massage ="ok";
						handler.sendEmptyMessage(SHOW);
					}
				}.start();
				break;
			case DELETE:
				final String isbn = msg.getData().getString("isbn");
				p = msg.getData().getInt("p");
				new Thread(){

					@Override
					public void run() {
						try {
							if(postService.deleteRemindBookPost(MyAccount.getUserId(),isbn)){
								massage = "ok";
							}
							else{
								massage = "no";	
							}
						} catch (IOException e) {
							massage = "no";
							e.printStackTrace();
						}
						handler.sendEmptyMessage(MSG);
					}
				}.start();
				break;
			case MSG:
				if(massage.equals("ok")){
					Toast.makeText(RemindTimingActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					remindBookList.remove(p);
					adapter.notifyDataSetChanged();
				}
				else{
					Toast.makeText(RemindTimingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_remind_timing);
		postService = new PostService();
		dataService = new DataService();
		remind_list = (ListView)findViewById(R.id.book_remind_list);
		bar = (ProgressBar)findViewById(R.id.remind_proBar);
		handler.sendEmptyMessage(LOAD);
		remind_list.setOnItemClickListener(listener);

		remind_list.setOnItemLongClickListener(longListener);
	}
	OnItemClickListener listener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(RemindTimingActivity.this,BookDetailActivity.class);
			intent.putExtra("book", remindBookList.get(position));
			startActivity(intent);
		}
	};

	OnItemLongClickListener longListener = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final int p = position;
			View itemView= adapter.getView(position, view, parent);
			btn_delete = (ImageButton)itemView.findViewById(R.id.btn_booklist_delete);
			if(btn_delete.getVisibility()==View.GONE){
				btn_delete.setVisibility(View.VISIBLE);
				btn_delete.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						int id = v.getId();
						switch(id){
						case R.id.btn_booklist_delete:
							Message msg = new Message();
							msg.what=DELETE;
							Bundle msgData = new Bundle();
							msgData.putString("isbn", remindBookList.get(p).getISBN());
							msgData.putInt("p", p);
							msg.setData(msgData);
							Toast.makeText(RemindTimingActivity.this, "删除中", Toast.LENGTH_SHORT).show();
							handler.sendMessage(msg);
						}

					}

				});
				return true;
			}
			return false;
		}

	};


}
