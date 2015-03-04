package com.csu.booch.mylibrary.ui.fragments;

import java.util.ArrayList;
import com.csu.booch.mylibrary.data.adapters.BookShelfListAdapter;
import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.ShelfNode;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.ui.R;
import com.csu.booch.mylibrary.ui.activity.BookDetailActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 预借查询
 * @name BorrowFragment.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("HandlerLeak")
public class BookShelfFragment extends Fragment{
	private ListView bookShelfList;
	private ProgressBar bar;
	private BookShelfListAdapter adapter;
	private ArrayList<ShelfNode> data;
	private DataService dataService;
	private Button btn_delete;
	private final int LOAD=999;
	private final int SHOW=998;
	private final int JUMP=997;
	private final int DELETE = 996;
	private final int VIEW=995;
	private final int MSG=994;
	private Book book;
	private String error="";
	private ProgressDialog dlg;//进度对话框
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
						try{
							data = dataService.getBookShelfData();
							handler.sendEmptyMessage(SHOW);
						}
						catch(Exception e){

						}
						
					}
				}.start();
				break;
			case SHOW:
				bar.setVisibility(View.GONE);
				adapter = new BookShelfListAdapter(data, getActivity());
				bookShelfList.setAdapter(adapter);
				if(data.size()==0){
					Toast.makeText(getActivity(), "书架为空", Toast.LENGTH_SHORT).show();
				}
				break;
			case JUMP:
				//显示详细信息
				if(error.equals("")){
					dlg.dismiss();
					Intent intent = new Intent(getActivity(),BookDetailActivity.class);
					intent.putExtra("book", book);
					startActivity(intent);
				}
				else{
					dlg.dismiss();
					Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
					error="";
				}
				break;
			case VIEW:
				final String name = msg.getData().getString("bookName");
				new Thread(){

					@Override
					public void run() {
						try {
							book = dataService.getBook(name);
							Bitmap coverImage = dataService.getBookCover(book.getISBN());
							book.setCoverImage(coverImage);
						} catch (Exception e) {
							error = "错误，请重试";
						}
						handler.sendEmptyMessage(JUMP);
					}

				}.start();
				break;
			case DELETE:
				Toast.makeText(getActivity(), "删除ing", Toast.LENGTH_SHORT).show();
				final String id = msg.getData().getString("id");
				p = msg.getData().getInt("position");
				new Thread(){
					@Override
					public void run() {
						try {
							if(dataService.deleteBookStore(id)){
								error ="删除成功";
							}
							else{
								error ="删除失败";
							}
						} catch (Exception e) {
							error ="Exception";
						} 
						handler.sendEmptyMessage(MSG);
					}
				}.start();
				break;
			case MSG:
				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
				if(error.equals("删除成功")){
					data.remove(p);
					adapter.notifyDataSetChanged();
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

		View rootView = inflater.inflate(R.layout.fragment_bookshelf,
				container, false);
		bookShelfList = (ListView)rootView.findViewById(R.id.book_shelf_list);
		bar = (ProgressBar)rootView.findViewById(R.id.proBar);
		bar.setVisibility(View.VISIBLE);
		bookShelfList.setOnItemClickListener(listener);
		bookShelfList.setOnItemLongClickListener(longClickListener);
		bookShelfList.setOnTouchListener(touchListener);
		if(data==null){
			handler.sendEmptyMessage(LOAD);
		}
		else{
			handler.sendEmptyMessage(SHOW);
		}

		return rootView;
	}
	OnItemClickListener listener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String name = ((ShelfNode)adapter.getItem(position)).getName();
			//String str = ((ShelfNode)adapter.getItem(position)).getId();
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			Message msg = new Message();
			msg.what=VIEW;
			Bundle msgData = new Bundle();
			msgData.putString("bookName", name);
			msg.setData(msgData);
			handler.sendMessage(msg);
			dlg = new ProgressDialog(getActivity());
			dlg.setMessage("加载中...");
			dlg.setCanceledOnTouchOutside(false);
			dlg.setCancelable(true);
			dlg.setOnCancelListener(dlglistener);
			dlg.show();

		}

	};
	OnItemLongClickListener longClickListener = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final int p = position;
			btn_delete  = (Button) adapter.getView(position, view, parent)
					.findViewById(R.id.btn_shelf_delete);
			Animation animation = AnimationUtils
					.loadAnimation(btn_delete.getContext(),android.R.anim.fade_in);
			btn_delete.setAnimation(animation);
			btn_delete.setVisibility(View.VISIBLE);
			btn_delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					v.setVisibility(View.GONE);
					Message msg = new Message();
					msg.what=DELETE;
					Bundle msgData = new Bundle();
					msgData.putString("id", data.get(p).getId());
					msgData.putInt("position", p);
					msg.setData(msgData);
					handler.sendMessage(msg);
				}

			});
			return true;
		}

	};
	OnTouchListener touchListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if(btn_delete!=null){
					if(btn_delete.getVisibility()==View.VISIBLE){
						btn_delete.setVisibility(View.GONE);
						return true;
					}
				}
			}
			return false;
		}

	};



	OnCancelListener dlglistener = new OnCancelListener(){

		@Override
		public void onCancel(DialogInterface dialog) {
			try {
				Connection.disConnection();
			} catch (Exception e) {

				e.printStackTrace();
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
