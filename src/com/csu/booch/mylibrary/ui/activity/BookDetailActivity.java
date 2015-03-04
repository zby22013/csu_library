package com.csu.booch.mylibrary.ui.activity;

import java.util.ArrayList;

import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.MyAccount;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.persistence.connection.ConnectionURL;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.service.PostService;
import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 书籍详细信息视图
 * @name BookDetailActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class BookDetailActivity extends Activity{

	//变量声明开始
	private ImageView bookCover;//封面
	private TextView bookName;//书名
	private TextView author;//作者
	private TextView publisher;//出版社
	private TextView publishDate;//出版日期
	private TextView IBSN;
	private TextView price;//价格
	private TextView classifyID;//分类号
	private TextView searchID;//索书号
	private TextView pageNum;//页数
	private TextView description;//描述
	private TableLayout infoTable;//馆藏信息表
	private Button checkCom;//查看评论
	private Button addToShef;//加入书架
	private Button addToRemind;//加入提醒
	private Book book;//书本
	private ArrayList<String> tableInfo;
	private final int TABLEINFO = 99;
	private final int ERRORMSG = 98;
	private final int SHOWTABLE = 97;
	private final int PUTTOSHELF = 96;
	private final int PUTTOREMIND = 95;
	private String errorMsg = "";
	private DataService dataService;//数据服务
	private PostService postService; 
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case TABLEINFO:
				getTableInfo();
				break;

			case ERRORMSG:

				Toast.makeText(BookDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
				break;

			case SHOWTABLE:
				showTable();
				break;

			case PUTTOSHELF:
				//Toast.makeText(BookDetailActivity.this, "in", Toast.LENGTH_SHORT).show();
				new Thread(){
					@Override
					public void run() {

						try {
							//errorMsg = postService.putToShelfPost(book.getiD());
							if(postService.putToShelfPost(book.getiD())){
								errorMsg = "成功加入我的书架";
							}
							else{
								errorMsg = "加入我的书架失败";
							}

						} catch (Exception e) {
							errorMsg ="加入我的书架失败，请重试";
							//e.printStackTrace();
						}
						handler.sendEmptyMessage(ERRORMSG);
					}

				}.start();
				break;
			case PUTTOREMIND:
				Toast.makeText(BookDetailActivity.this, "正在加入提醒...", Toast.LENGTH_SHORT).show();
				new Thread(){
					@Override
					public void run() {
						try {
							if(postService.putToBookRemindPost(MyAccount.getUserId(),book.getISBN())){
								errorMsg = "加入提醒成功";
							}
							else{
								errorMsg = "加入提醒失败";
							}
						} catch (Exception e) {
							errorMsg="加入提醒失败";
						}
						handler.sendEmptyMessage(ERRORMSG);
					}
				}.start();
			}
		}
	};
	//变量声明结束
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bookdetail);
		this.setTitle("书籍详情");
		Intent intent = this.getIntent();
		book = new Book();
		book = intent.getParcelableExtra("book");
		initActivity();
		handler.sendEmptyMessage(TABLEINFO);
	}

	/**
	 * 初始化界面
	 */
	private void initActivity() {
		dataService = new DataService();
		postService = new PostService();
		//获取控件
		bookCover = (ImageView)this.findViewById(R.id.dtl_book_cover);
		bookName = (TextView)this.findViewById(R.id.dtl_book_name);
		author = (TextView)this.findViewById(R.id.dtl_author);
		publisher = (TextView)this.findViewById(R.id.dtl_publisher);
		publishDate = (TextView)this.findViewById(R.id.dtl_publish_date);
		IBSN = (TextView)this.findViewById(R.id.dtl_ibsn);
		price = (TextView)this.findViewById(R.id.dtl_price);
		classifyID = (TextView)this.findViewById(R.id.dtl_classify_id);
		searchID = (TextView)this.findViewById(R.id.dtl_search_id);
		pageNum = (TextView)this.findViewById(R.id.dtl_page_num);
		description = (TextView)this.findViewById(R.id.dtl_brif_desc);
		checkCom = (Button)this.findViewById(R.id.btn_dtl_buy);
		addToShef = (Button)this.findViewById(R.id.btn_dtl_put_to_shef);
		addToRemind = (Button)this.findViewById(R.id.btn_dtl_put_to_remind);
		infoTable = (TableLayout)this.findViewById(R.id.dtl_info_table);
		//设置监听
		checkCom.setOnClickListener(click_listener);
		addToShef.setOnClickListener(click_listener);
		addToRemind.setOnClickListener(click_listener);
		bookCover.setOnClickListener(click_listener);
		//赋值
		setValue();
	}
	private void setValue() {
		if(book != null){
			bookCover.setImageBitmap(book.getCoverImage());
			bookName.setText("书名:"+book.getBookName());
			author.setText("作者："+book.getAuthor());
			publisher.setText("出版社："+book.getPublisher());
			publishDate.setText("出版日期："+book.getPublishDate());
			IBSN.setText("IBSN："+book.getISBN());
			price.setText("价格："+book.getPrice());
			classifyID.setText("分类号："+book.getClassifyID());
			searchID.setText("索书号："+book.getSearchID());
			pageNum.setText("页数："+book.getPageNumber());
			description.setText("描述："+book.getDescription());
			if(book.getHoldNumber()==0){
				addToRemind.setVisibility(View.VISIBLE);
			}
		}
		else{
			Toast.makeText(BookDetailActivity.this, 
					"获取信息失败", Toast.LENGTH_SHORT).show();
		}
	}
	OnClickListener click_listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.btn_dtl_buy:
				String[] webs = {"亚马逊","淘宝","京东"};
				new AlertDialog.Builder(BookDetailActivity.this)
				.setTitle("请选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(webs, 0, 
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int position) {
						Intent intentBuy = new Intent();
						Uri uri;
						switch(position){
						case 0:
							intentBuy.setAction("android.intent.action.VIEW");    
							uri = Uri.parse(ConnectionURL.getAmazon(book.getBookName()));
							intentBuy.setData(uri);  
							startActivity(intentBuy);
							break;

						case 1:
							intentBuy.setAction("android.intent.action.VIEW");    
							uri = Uri.parse(ConnectionURL.getTaobao(book.getBookName()));
							intentBuy.setData(uri);  
							startActivity(intentBuy);
							break;

						case 2:
							intentBuy.setAction("android.intent.action.VIEW");    
							uri = Uri.parse(ConnectionURL.getJindong(book.getBookName()));
							intentBuy.setData(uri);  
							startActivity(intentBuy);
						}
						dialog.dismiss();
					}
				}
						)
						.setNegativeButton("取消", null)
						.show();

				break;
			case R.id.btn_dtl_put_to_shef:
				if(MyAccount.getCookies()==null){
					Toast.makeText(BookDetailActivity.this, 
							"您未登录，请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(BookDetailActivity.this,LoginActivity.class);
					startActivityForResult(intent,200);
				}
				else{
					Toast.makeText(BookDetailActivity.this, 
							"正在放入书架", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(PUTTOSHELF);
				}

				break;
			case R.id.btn_dtl_put_to_remind:
				if(MyAccount.getCookies()==null){
					Toast.makeText(BookDetailActivity.this, 
							"您未登录，请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(BookDetailActivity.this,LoginActivity.class);
					startActivityForResult(intent,200);
				}
				else{
					handler.sendEmptyMessage(PUTTOREMIND);
					//MyAccount.getRemindList().add(book);
				}

				break;

			case R.id.dtl_book_cover:
				ImageView img = new ImageView(BookDetailActivity.this);
				img.setImageBitmap(book.getCoverImage());
				new AlertDialog.Builder(BookDetailActivity.this)
				.setTitle("封面")
				.setView(img)
				.setPositiveButton("确定", null)
				.show();

			}
		}
	};

	/**
	 * 获取馆藏信息表
	 */
	private void getTableInfo(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				if(Connection.isNetworkConnected(BookDetailActivity.this)){
					try {
						tableInfo = dataService.getHoldConditionTableInfo(book.getiD());
						if(tableInfo!=null){
							handler.sendEmptyMessage(SHOWTABLE);
						}
						else{
							errorMsg = "回获取馆藏信息失败:"+Connection.getHttpResponse().getStatusLine();
							handler.sendEmptyMessage(ERRORMSG);
						}
					}
					catch (Exception e) {
						errorMsg = "回获取馆藏信息失败";
						handler.sendEmptyMessage(ERRORMSG);

					}
				}
				else{
					errorMsg = "网络不可用";
					handler.sendEmptyMessage(ERRORMSG);
				}
			}
		}.start();
	}


	/**
	 * 显示表格
	 *@author Tomorrow
	 *@since 2014-5-2
	 */
	private void showTable() {
		ArrayList<TableRow> trs = new ArrayList<TableRow>();
		ArrayList<TextView> txts = new ArrayList<TextView>();
		LayoutParams param = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		//行数
		int col = 8;//表格总列数，网站可能会更改
		for(int r = 0;r<tableInfo.size()/col;r++){
			//添加一行
			trs.add(new TableRow(infoTable.getContext()));
			//列数
			for(int c = 0; c < col;c++){
				if((c-1)%col==0){
					continue;
				}
				if((c-6)%col==0){
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
					txts.get(txts.size()-1).setText(tableInfo.get(r*col+c));
					//设置间距
					txts.get(txts.size()-1).setWidth(trs.get(r).getWidth()/5);
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
	protected void onDestroy() {

		super.onDestroy();
		try {
			Connection.disConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
