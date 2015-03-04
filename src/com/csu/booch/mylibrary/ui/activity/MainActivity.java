package com.csu.booch.mylibrary.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.csu.booch.mylibrary.data.adapters.BookListDataAdapter;
import com.csu.booch.mylibrary.data.adapters.DrawerViewAdapter;
import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.MyAccount;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.service.DataService;
import com.csu.booch.mylibrary.ui.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.Window;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 软件主界面
 * @name MainActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("HandlerLeak")
public class  MainActivity extends Activity {

	
	/**
	 *  修改：将所有属性访问级别变为 ：私有  
	 *  原因：属性私有原则
	 *  修改人：xmt
	 *  时间：20140328 21：45
	 */
	private final int SEARCHCODE = 100;//请求搜索
	private final int SHOWBOOKS = 101;//请求显示书籍数据
	private final int SHOWCOVERS = 102;//请求显示封面
	private final int SHOWERROR = 103;//请求错误信息
	private final int REFLASHLIST = 104;//请求更新
	private final int OKFORNEXT = 105;
	private DrawerLayout drawerLayout = null;
	private ImageButton btn_Search;//搜索按钮
	private EditText edt_Search;//滚剪子输入框
	private RadioGroup type;//高级搜索类型

	//控制面板控件
	private RelativeLayout ctrPanel;//控制面板布局
	private TextView pageTxt;//页数信息
	private Button prePage;//前一页
	private Button nextPage;//后一页
	//xmt add 20140328 21:45
	private ImageButton btn_OpenDrawer; //打开抽屉的按钮
	private ListView bookListView;//显示列表
	private ListView Drawer;//抽屉
	private ProgressDialog dlg;//进度对话框
	//add end
	//add by xmt 20140328 21:45
	private ArrayList<Book> bookList;//书籍数据数组
	private List<File> imagesFileList;//封面文件路径
	private DataService service;//数据服务
	private String keyWord ="";//关键字
	private String errorMsg = "";//错误信息
	private BookListDataAdapter dataAdapter;//书籍数据适配器
	private DrawerViewAdapter viewAdapter;//抽屉面板适配器
	private int currentPage = 1;//当前页码
	private String searchType;//搜索类型
	private int cp;//当前页起始位置下表
	private int np;//下一页起始位置下表
	private boolean okForNext;
	private ArrayList<Book> searchCache;//搜索结果缓存
	//线程控制中心，界面控制中心
	private Handler handler = new Handler(){
		/**
		 * 处理消息
		 * 处理一些线程：例如 联网线程
		 * 原因：android UI线程的保护机制，不允许在主线程内启动其它线程
		 * 		要通过此函数实现启动其它线程
		 * 注意：在其它线程内不允许访问界面控件,即不允许在非ui线程内修改空间的内容，否则报错
		 */
		@Override
		public void handleMessage(Message msg) {
			// 处理消息
			super.handleMessage(msg);
			switch(msg.what){
			case SEARCHCODE:
				if(Connection.isNetworkConnected(getApplication())){
					btn_Search.setEnabled(false);
					//处理其它事情,这里实现联网搜索，获取返回结果,联网线程
					getBookList();
				}
				else{
					errorMsg = "无网络链接，请检查您的网络设置";
					handler.sendEmptyMessage(SHOWERROR);
					dlg.dismiss();
				}
				break;

			case SHOWBOOKS:
				/**
				 * 显示书籍数据
				 */
				if(bookList==null||bookList.isEmpty()){
					int code = 0;
					//软件的网络权限被禁止，无法通过获取手机网络状态判断，回应为空
					if(Connection.getHttpResponse()!=null){
						code = Connection.getHttpResponse().getStatusLine().getStatusCode();
					}
					else{
						code=101;
					}
					if(code==500){
						errorMsg = "服务器出错";
					}
					else if(code==101){
						errorMsg = "无法连接到网络,请检查您的网络设置";
					}
					else{
						errorMsg = "抱歉，没有找到相关的书籍，\n建议更换关键字后重试.";
					}
					handler.sendEmptyMessage(SHOWERROR);
				}
				else{
					if(dataAdapter == null){
						dataAdapter = new BookListDataAdapter(MainActivity.this,bookList);
						bookListView.setAdapter(dataAdapter);
					}
					else{
						dataAdapter.setList(bookList);
					}
					activeCtrPanel();
					//TODO  请求搜索下一页数据
					try{
						//如果下一页数据不为空
						if(searchCache.get(np)!=null){
							//下一页以准备好了
							okForNext = true;
						}
					}
					catch(Exception e){
						//获取下一页数据
						getNextPageData();
					}
					getBookCover();
				}
				btn_Search.setEnabled(true);
				break;

			case SHOWCOVERS:
				/**
				 * 显示封面
				 */
				showBookCovers();
				break;

			case SHOWERROR:
				/**
				 * 显示错误
				 */
				showErrorMsg();
				break;

			case REFLASHLIST:
				/**
				 * 通知列表更新数据
				 */
				dataAdapter.notifyDataSetInvalidated();
				break;
			case OKFORNEXT:
				//TODO 完成了下一页的加载之后
				if(okForNext){
					//如果用户提前请求了下一页，加载对话框还没有消失
					if(dlg.isShowing()){
						//让对话框消失
						dlg.dismiss();
						//进行下一页加载处理
						cp+=10;
						np =searchCache.size();
						bookList.clear();
						for(int i=cp;i<np;i++) {
							bookList.add(searchCache.get(i));
						}
						currentPage++;
						pageTxt.setText("  页码："+currentPage+"/"+service.getPageNum());
						handler.sendEmptyMessage(SHOWBOOKS);
					}
					
				}
			}
		}

	};
	//add end
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//用于去掉窗口最上方的安卓机器人标志
		//理解：向系统请求让这个界面不带标题栏    修改人：xmt
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initActivity();//初始化界面
	}

	/**
	 * 呼出菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * 初始化界面
	 */
	public void initActivity(){
		service = new DataService();//实例化数据服务变量
		//获取控件
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		btn_Search = (ImageButton) findViewById(R.id.btn_Search);
		edt_Search = (EditText) findViewById(R.id.edt_Search);
		type = (RadioGroup)findViewById(R.id.rbtn_group);
		//设置搜索类型监听器
		type.setOnCheckedChangeListener(listener);
		((RadioButton)type.getChildAt(0)).setChecked(true);
		//xmt add 20140328 21:45
		//获取控件
		Drawer = (ListView)findViewById(R.id.left_drawer);//获取抽屉listView
		viewAdapter = new DrawerViewAdapter(MainActivity.this);//实例化抽屉界面适配器
		Drawer.setAdapter(viewAdapter);//设置适配器
		bookListView = (ListView)this.findViewById(R.id.book_list_view);
		btn_OpenDrawer = (ImageButton) findViewById(R.id.btn_openDrawer);
		ctrPanel = (RelativeLayout)findViewById(R.id.ctr_panel);//获取控制面板
		//add end
		btn_Search.setOnClickListener(Listener_Button);//设置搜索按钮监听

		// xmt add 20140328 21:45

		btn_OpenDrawer.setOnClickListener(Listener_Button);//设置按钮监听器
		bookListView.setOnItemClickListener(itemClickListener);//添加对listView的监听
		//Drawer.setOnItemClickListener(itemClickListener);//设置抽屉面板listView监听
		//add end
	}
	//单项选择选项改变按钮监听器
	OnCheckedChangeListener listener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch(checkedId){
			case R.id.rbtn_type_all:
				searchType="text";
				break;
			case R.id.rbtn_type_name:
				searchType="name";
				break;
			case R.id.rbtn_type_author:
				searchType="author";
				break;
			case R.id.rbtn_type_publisher:
				searchType="publish";
				break;
			case R.id.rbtn_type_ibsn:
				searchType="ibsn";
				break;
			default:
				searchType="text";
			}
		}

	};
	/**
	 * 按钮点击监听器
	 */
	private OnClickListener Listener_Button = new OnClickListener(){
		public void onClick(View v)
		{
			int id = v.getId();
			switch(id){
			case R.id.btn_openDrawer:
				// 按钮按下，将抽屉打开
				drawerLayout.openDrawer(Gravity.LEFT);
				break;

			case R.id.btn_Search:
				//点击搜索按钮
				//赋值
				keyWord = edt_Search.getText().toString();
				keyWord = keyWord.trim();//去掉空格
				//判断输入是不是空
				if(keyWord.length()==0){
					Toast.makeText(MainActivity.this,
							"请输入关键字",Toast.LENGTH_SHORT).show();
				}
				else{

					edt_Search.setText("");
					//得到InputMethodManager的实例
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
					//强制关闭软键盘
					imm.hideSoftInputFromWindow(edt_Search.getWindowToken(), 0);
					//初始化搜索
					initSearch();
					//发送请求搜索消息
					handler.sendEmptyMessage(SEARCHCODE);
				}
				break;

			case R.id.btn_previous:
				/**
				 * 请求前一页
				 */
				if(currentPage > 1){
					//TODO
//					dlg.setMessage("加载中...");
//					dlg.show();
					cp-=10;
					np-=10;
					bookList.clear();
					for(int i=cp;i<np;i++) {
						bookList.add(searchCache.get(i));
					}
					currentPage--;
					pageTxt.setText("  页码："+currentPage+"/"+service.getPageNum());
					handler.sendEmptyMessage(SHOWBOOKS);
				}
				else{
					Toast.makeText(MainActivity.this,
							"已经是第一页了",Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.btn_next:
				/**
				 * 请求下一页
				 */
				if(currentPage < service.getPageNum()){
					if(okForNext){
						cp+=10;
						np=searchCache.size();
						bookList.clear();
						for(int i=cp;i<np;i++) {
							bookList.add(searchCache.get(i));
						}
						currentPage++;
						pageTxt.setText("  页码："+currentPage+"/"+service.getPageNum());
						handler.sendEmptyMessage(SHOWBOOKS);
					}
					else{
						dlg.setMessage("加载中...");
						dlg.show();
					}
				}
				else{
					Toast.makeText(MainActivity.this,
							"已经是最后一页了",Toast.LENGTH_SHORT).show();
				}
				break;
			}

		}
	};
	//列表项选择监听器
	OnItemClickListener itemClickListener = new OnItemClickListener(){

		/**
		 * 处理listView监听时事件
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			int id = arg0.getId();
			switch(id){
			case R.id.book_list_view:
				//显示详细信息
				Intent intent = new Intent(MainActivity.this,BookDetailActivity.class);
				Book book = (Book)dataAdapter.getItem(position);
				intent.putExtra("book", book);
				startActivity(intent);
				break;
			}

		}

	};

	/**
	 * 监听按键按下事件
	 * 返回键：首先要关闭抽屉，然后才退出程序
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//判断是不是返回键，抽屉是不是打开了

		if(keyCode == KeyEvent.KEYCODE_BACK
				&&this.drawerLayout.isDrawerVisible(Gravity.LEFT)){
			drawerLayout.closeDrawers();
		}

		else if(keyCode == KeyEvent.KEYCODE_BACK){
			moveTaskToBack(false);
			return true;
		}
		return false;
	}

	/**
	 * 监听菜单选择
	 * @param featureId 菜单id
	 * @param item 菜单项
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		switch(id){
		case R.id.action_exit:
			//得到InputMethodManager的实例
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			//关闭软键盘
			imm.hideSoftInputFromWindow(edt_Search.getWindowToken(), 0);
			finish();
			break;
		case R.id.action_about:
			Intent intent = new Intent(MainActivity.this,AboutActivity.class);
			startActivity(intent);
		}

		return super.onMenuItemSelected(featureId, item);
	}
	/**
	 * 获取显示的书籍的封面
	 */
	private void getBookCover(){
		new Thread(){
			/**
			 *  获取封面
			 */
			@Override
			public void run() {
				super.run();
				if(imagesFileList!=null){
					imagesFileList.clear();//清空当前数据
				}
				ArrayList<String> IbsnList = new ArrayList<String>();
				for(int i = 0;i<bookList.size();i++){
					IbsnList.add(bookList.get(i).getISBN());
				}
				imagesFileList = service.getImageFielList(IbsnList);//获取封面
				if(imagesFileList==null){
					errorMsg = "封面下载失败" +service.getMsg() ;
					handler.sendEmptyMessage(SHOWERROR);
				}
				else{
					handler.sendEmptyMessage(SHOWCOVERS);
				}
			}
		}.start();
	}
	/**
	 *加载封面
	 */
	private  void showBookCovers(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				if(imagesFileList!=null){
					for(int i = 0;i<imagesFileList.size();i++){
						//TODO  加载封面
						if(imagesFileList.get(i).getName().equals(bookList.get(i).getISBN())){
							Bitmap coverImage = BitmapFactory
									.decodeFile(imagesFileList.get(i).getPath());
							bookList.get(i).setCoverImage(coverImage);
						}
						else{
							Bitmap coverImage = BitmapFactory
									.decodeResource(getResources(), R.drawable.cover_default_s);
							bookList.get(i).setCoverImage(coverImage);
						}
					}
					handler.sendEmptyMessage(REFLASHLIST);
				}
				else{
					errorMsg = "未能加载封面";
					handler.sendEmptyMessage(SHOWERROR);
				}
			}
		}.start();
	}
	/**
	 * 显示错误信息
	 */
	public void showErrorMsg(){
		if(!errorMsg.equals("")){
			Toast.makeText(MainActivity.this,
					errorMsg,Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化搜索
	 * 内容：
	 * 1.关闭控制面板
	 * 2.清空书籍数据
	 * 3.清空封面数据
	 */
	private void initSearch() {
		currentPage = 1;
		cp=0;
		np=10;
		if(ctrPanel.getVisibility() == View.VISIBLE){
			ctrPanel.setVisibility(View.GONE);
		}
		if(searchCache!=null){
			bookList.clear();
			searchCache.clear();
			if(dataAdapter != null){
				dataAdapter.notifyDataSetInvalidated();
			}

		}
		else{
			searchCache = new ArrayList<Book>();
			bookList = new ArrayList<Book>();
		}
		if(imagesFileList!=null){
			imagesFileList.clear();
		}
		//进度对话框
		dlg = new ProgressDialog(MainActivity.this);
		dlg.setMessage("正在搜索"+keyWord);
		dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(true);
		//设置对话框取消操作监听
		dlg.setOnCancelListener(dlglistener);
		dlg.show();

	}

	OnCancelListener dlglistener = new OnCancelListener(){

		@Override
		public void onCancel(DialogInterface dialog) {
			btn_Search.setEnabled(true);
			try {
				Connection.disConnection();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	};
	/**
	 * 显示书籍列表
	 */
	private void getBookList(){
		new Thread(){
			/**
			 *  获取书籍列表
			 */
			@Override
			public void run() {

				if(bookList!=null){
					bookList.clear();//清空当前数据
				}
				try {
					//TODO 获取并加入缓存
					//获取书籍数据列表
					bookList = service.getBookList(searchType,keyWord,currentPage);
					//加入搜索缓存
					searchCache.addAll(bookList);
				} catch (Exception e) {
					//错误提示
					errorMsg ="获取数据失败";
					handler.sendEmptyMessage(SHOWERROR);
				}
				dlg.dismiss();
				handler.sendEmptyMessage(SHOWBOOKS);
			}
		}.start();
	}
	/**
	 * 预获取下一页
	 *@author Tomorrow
	 *@since 2014-5-7
	 */
	private void getNextPageData() {
		okForNext = false;
		new Thread(){

			@Override
			public void run() {
				try {
					//TODO
					searchCache.addAll(service.getBookList(searchType,keyWord,(currentPage+1)));
					okForNext = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(OKFORNEXT);
			}

		}.start();
	}

	/**
	 * 激活控制面板
	 */
	private void activeCtrPanel(){
		ctrPanel.setVisibility(View.VISIBLE);
		if(pageTxt==null){
			//初始化控件
			pageTxt = (TextView)findViewById(R.id.result_info);
			prePage = (Button)findViewById(R.id.btn_previous);
			nextPage = (Button)findViewById(R.id.btn_next);
			prePage.setOnClickListener(Listener_Button);
			nextPage.setOnClickListener(Listener_Button);
		}
		pageTxt.setText("  页码："+currentPage+"/"+service.getPageNum());
	}

	/* 
	 * 回调函数，处理返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==200&&resultCode==RESULT_OK){
			//登陆成功后刷新抽屉界面的显示的名字
			viewAdapter.notifyDataSetChanged();
		}

	}
	/*
	 * 重写方法，刷新抽屉
	 */
	@Override
	protected void onStart() {
		super.onStart();
		viewAdapter.notifyDataSetChanged();
	}
	/*
	 * 重写方法，退出处理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//退出后清除用户信息
		MyAccount.setCookies(null);
		MyAccount.setUserName("登录");
		try {
			Connection.disConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
