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
 * ���������
 * @name MainActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("HandlerLeak")
public class  MainActivity extends Activity {

	
	/**
	 *  �޸ģ����������Է��ʼ����Ϊ ��˽��  
	 *  ԭ������˽��ԭ��
	 *  �޸��ˣ�xmt
	 *  ʱ�䣺20140328 21��45
	 */
	private final int SEARCHCODE = 100;//��������
	private final int SHOWBOOKS = 101;//������ʾ�鼮����
	private final int SHOWCOVERS = 102;//������ʾ����
	private final int SHOWERROR = 103;//���������Ϣ
	private final int REFLASHLIST = 104;//�������
	private final int OKFORNEXT = 105;
	private DrawerLayout drawerLayout = null;
	private ImageButton btn_Search;//������ť
	private EditText edt_Search;//�����������
	private RadioGroup type;//�߼���������

	//�������ؼ�
	private RelativeLayout ctrPanel;//������岼��
	private TextView pageTxt;//ҳ����Ϣ
	private Button prePage;//ǰһҳ
	private Button nextPage;//��һҳ
	//xmt add 20140328 21:45
	private ImageButton btn_OpenDrawer; //�򿪳���İ�ť
	private ListView bookListView;//��ʾ�б�
	private ListView Drawer;//����
	private ProgressDialog dlg;//���ȶԻ���
	//add end
	//add by xmt 20140328 21:45
	private ArrayList<Book> bookList;//�鼮��������
	private List<File> imagesFileList;//�����ļ�·��
	private DataService service;//���ݷ���
	private String keyWord ="";//�ؼ���
	private String errorMsg = "";//������Ϣ
	private BookListDataAdapter dataAdapter;//�鼮����������
	private DrawerViewAdapter viewAdapter;//�������������
	private int currentPage = 1;//��ǰҳ��
	private String searchType;//��������
	private int cp;//��ǰҳ��ʼλ���±�
	private int np;//��һҳ��ʼλ���±�
	private boolean okForNext;
	private ArrayList<Book> searchCache;//�����������
	//�߳̿������ģ������������
	private Handler handler = new Handler(){
		/**
		 * ������Ϣ
		 * ����һЩ�̣߳����� �����߳�
		 * ԭ��android UI�̵߳ı������ƣ������������߳������������߳�
		 * 		Ҫͨ���˺���ʵ�����������߳�
		 * ע�⣺�������߳��ڲ�������ʽ���ؼ�,���������ڷ�ui�߳����޸Ŀռ�����ݣ����򱨴�
		 */
		@Override
		public void handleMessage(Message msg) {
			// ������Ϣ
			super.handleMessage(msg);
			switch(msg.what){
			case SEARCHCODE:
				if(Connection.isNetworkConnected(getApplication())){
					btn_Search.setEnabled(false);
					//������������,����ʵ��������������ȡ���ؽ��,�����߳�
					getBookList();
				}
				else{
					errorMsg = "���������ӣ�����������������";
					handler.sendEmptyMessage(SHOWERROR);
					dlg.dismiss();
				}
				break;

			case SHOWBOOKS:
				/**
				 * ��ʾ�鼮����
				 */
				if(bookList==null||bookList.isEmpty()){
					int code = 0;
					//���������Ȩ�ޱ���ֹ���޷�ͨ����ȡ�ֻ�����״̬�жϣ���ӦΪ��
					if(Connection.getHttpResponse()!=null){
						code = Connection.getHttpResponse().getStatusLine().getStatusCode();
					}
					else{
						code=101;
					}
					if(code==500){
						errorMsg = "����������";
					}
					else if(code==101){
						errorMsg = "�޷����ӵ�����,����������������";
					}
					else{
						errorMsg = "��Ǹ��û���ҵ���ص��鼮��\n��������ؼ��ֺ�����.";
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
					//TODO  ����������һҳ����
					try{
						//�����һҳ���ݲ�Ϊ��
						if(searchCache.get(np)!=null){
							//��һҳ��׼������
							okForNext = true;
						}
					}
					catch(Exception e){
						//��ȡ��һҳ����
						getNextPageData();
					}
					getBookCover();
				}
				btn_Search.setEnabled(true);
				break;

			case SHOWCOVERS:
				/**
				 * ��ʾ����
				 */
				showBookCovers();
				break;

			case SHOWERROR:
				/**
				 * ��ʾ����
				 */
				showErrorMsg();
				break;

			case REFLASHLIST:
				/**
				 * ֪ͨ�б��������
				 */
				dataAdapter.notifyDataSetInvalidated();
				break;
			case OKFORNEXT:
				//TODO �������һҳ�ļ���֮��
				if(okForNext){
					//����û���ǰ��������һҳ�����ضԻ���û����ʧ
					if(dlg.isShowing()){
						//�öԻ�����ʧ
						dlg.dismiss();
						//������һҳ���ش���
						cp+=10;
						np =searchCache.size();
						bookList.clear();
						for(int i=cp;i<np;i++) {
							bookList.add(searchCache.get(i));
						}
						currentPage++;
						pageTxt.setText("  ҳ�룺"+currentPage+"/"+service.getPageNum());
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
		//����ȥ���������Ϸ��İ�׿�����˱�־
		//��⣺��ϵͳ������������治��������    �޸��ˣ�xmt
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initActivity();//��ʼ������
	}

	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * ��ʼ������
	 */
	public void initActivity(){
		service = new DataService();//ʵ�������ݷ������
		//��ȡ�ؼ�
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		btn_Search = (ImageButton) findViewById(R.id.btn_Search);
		edt_Search = (EditText) findViewById(R.id.edt_Search);
		type = (RadioGroup)findViewById(R.id.rbtn_group);
		//�����������ͼ�����
		type.setOnCheckedChangeListener(listener);
		((RadioButton)type.getChildAt(0)).setChecked(true);
		//xmt add 20140328 21:45
		//��ȡ�ؼ�
		Drawer = (ListView)findViewById(R.id.left_drawer);//��ȡ����listView
		viewAdapter = new DrawerViewAdapter(MainActivity.this);//ʵ�����������������
		Drawer.setAdapter(viewAdapter);//����������
		bookListView = (ListView)this.findViewById(R.id.book_list_view);
		btn_OpenDrawer = (ImageButton) findViewById(R.id.btn_openDrawer);
		ctrPanel = (RelativeLayout)findViewById(R.id.ctr_panel);//��ȡ�������
		//add end
		btn_Search.setOnClickListener(Listener_Button);//����������ť����

		// xmt add 20140328 21:45

		btn_OpenDrawer.setOnClickListener(Listener_Button);//���ð�ť������
		bookListView.setOnItemClickListener(itemClickListener);//��Ӷ�listView�ļ���
		//Drawer.setOnItemClickListener(itemClickListener);//���ó������listView����
		//add end
	}
	//����ѡ��ѡ��ı䰴ť������
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
	 * ��ť���������
	 */
	private OnClickListener Listener_Button = new OnClickListener(){
		public void onClick(View v)
		{
			int id = v.getId();
			switch(id){
			case R.id.btn_openDrawer:
				// ��ť���£��������
				drawerLayout.openDrawer(Gravity.LEFT);
				break;

			case R.id.btn_Search:
				//���������ť
				//��ֵ
				keyWord = edt_Search.getText().toString();
				keyWord = keyWord.trim();//ȥ���ո�
				//�ж������ǲ��ǿ�
				if(keyWord.length()==0){
					Toast.makeText(MainActivity.this,
							"������ؼ���",Toast.LENGTH_SHORT).show();
				}
				else{

					edt_Search.setText("");
					//�õ�InputMethodManager��ʵ��
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
					//ǿ�ƹر������
					imm.hideSoftInputFromWindow(edt_Search.getWindowToken(), 0);
					//��ʼ������
					initSearch();
					//��������������Ϣ
					handler.sendEmptyMessage(SEARCHCODE);
				}
				break;

			case R.id.btn_previous:
				/**
				 * ����ǰһҳ
				 */
				if(currentPage > 1){
					//TODO
//					dlg.setMessage("������...");
//					dlg.show();
					cp-=10;
					np-=10;
					bookList.clear();
					for(int i=cp;i<np;i++) {
						bookList.add(searchCache.get(i));
					}
					currentPage--;
					pageTxt.setText("  ҳ�룺"+currentPage+"/"+service.getPageNum());
					handler.sendEmptyMessage(SHOWBOOKS);
				}
				else{
					Toast.makeText(MainActivity.this,
							"�Ѿ��ǵ�һҳ��",Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.btn_next:
				/**
				 * ������һҳ
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
						pageTxt.setText("  ҳ�룺"+currentPage+"/"+service.getPageNum());
						handler.sendEmptyMessage(SHOWBOOKS);
					}
					else{
						dlg.setMessage("������...");
						dlg.show();
					}
				}
				else{
					Toast.makeText(MainActivity.this,
							"�Ѿ������һҳ��",Toast.LENGTH_SHORT).show();
				}
				break;
			}

		}
	};
	//�б���ѡ�������
	OnItemClickListener itemClickListener = new OnItemClickListener(){

		/**
		 * ����listView����ʱ�¼�
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			int id = arg0.getId();
			switch(id){
			case R.id.book_list_view:
				//��ʾ��ϸ��Ϣ
				Intent intent = new Intent(MainActivity.this,BookDetailActivity.class);
				Book book = (Book)dataAdapter.getItem(position);
				intent.putExtra("book", book);
				startActivity(intent);
				break;
			}

		}

	};

	/**
	 * �������������¼�
	 * ���ؼ�������Ҫ�رճ��룬Ȼ����˳�����
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//�ж��ǲ��Ƿ��ؼ��������ǲ��Ǵ���

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
	 * �����˵�ѡ��
	 * @param featureId �˵�id
	 * @param item �˵���
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		switch(id){
		case R.id.action_exit:
			//�õ�InputMethodManager��ʵ��
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			//�ر������
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
	 * ��ȡ��ʾ���鼮�ķ���
	 */
	private void getBookCover(){
		new Thread(){
			/**
			 *  ��ȡ����
			 */
			@Override
			public void run() {
				super.run();
				if(imagesFileList!=null){
					imagesFileList.clear();//��յ�ǰ����
				}
				ArrayList<String> IbsnList = new ArrayList<String>();
				for(int i = 0;i<bookList.size();i++){
					IbsnList.add(bookList.get(i).getISBN());
				}
				imagesFileList = service.getImageFielList(IbsnList);//��ȡ����
				if(imagesFileList==null){
					errorMsg = "��������ʧ��" +service.getMsg() ;
					handler.sendEmptyMessage(SHOWERROR);
				}
				else{
					handler.sendEmptyMessage(SHOWCOVERS);
				}
			}
		}.start();
	}
	/**
	 *���ط���
	 */
	private  void showBookCovers(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				if(imagesFileList!=null){
					for(int i = 0;i<imagesFileList.size();i++){
						//TODO  ���ط���
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
					errorMsg = "δ�ܼ��ط���";
					handler.sendEmptyMessage(SHOWERROR);
				}
			}
		}.start();
	}
	/**
	 * ��ʾ������Ϣ
	 */
	public void showErrorMsg(){
		if(!errorMsg.equals("")){
			Toast.makeText(MainActivity.this,
					errorMsg,Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ��ʼ������
	 * ���ݣ�
	 * 1.�رտ������
	 * 2.����鼮����
	 * 3.��շ�������
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
		//���ȶԻ���
		dlg = new ProgressDialog(MainActivity.this);
		dlg.setMessage("��������"+keyWord);
		dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(true);
		//���öԻ���ȡ����������
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
	 * ��ʾ�鼮�б�
	 */
	private void getBookList(){
		new Thread(){
			/**
			 *  ��ȡ�鼮�б�
			 */
			@Override
			public void run() {

				if(bookList!=null){
					bookList.clear();//��յ�ǰ����
				}
				try {
					//TODO ��ȡ�����뻺��
					//��ȡ�鼮�����б�
					bookList = service.getBookList(searchType,keyWord,currentPage);
					//������������
					searchCache.addAll(bookList);
				} catch (Exception e) {
					//������ʾ
					errorMsg ="��ȡ����ʧ��";
					handler.sendEmptyMessage(SHOWERROR);
				}
				dlg.dismiss();
				handler.sendEmptyMessage(SHOWBOOKS);
			}
		}.start();
	}
	/**
	 * Ԥ��ȡ��һҳ
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
	 * ����������
	 */
	private void activeCtrPanel(){
		ctrPanel.setVisibility(View.VISIBLE);
		if(pageTxt==null){
			//��ʼ���ؼ�
			pageTxt = (TextView)findViewById(R.id.result_info);
			prePage = (Button)findViewById(R.id.btn_previous);
			nextPage = (Button)findViewById(R.id.btn_next);
			prePage.setOnClickListener(Listener_Button);
			nextPage.setOnClickListener(Listener_Button);
		}
		pageTxt.setText("  ҳ�룺"+currentPage+"/"+service.getPageNum());
	}

	/* 
	 * �ص������������ؽ��
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==200&&resultCode==RESULT_OK){
			//��½�ɹ���ˢ�³���������ʾ������
			viewAdapter.notifyDataSetChanged();
		}

	}
	/*
	 * ��д������ˢ�³���
	 */
	@Override
	protected void onStart() {
		super.onStart();
		viewAdapter.notifyDataSetChanged();
	}
	/*
	 * ��д�������˳�����
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//�˳�������û���Ϣ
		MyAccount.setCookies(null);
		MyAccount.setUserName("��¼");
		try {
			Connection.disConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
