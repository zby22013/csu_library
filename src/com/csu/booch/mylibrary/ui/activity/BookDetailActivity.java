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
 * �鼮��ϸ��Ϣ��ͼ
 * @name BookDetailActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class BookDetailActivity extends Activity{

	//����������ʼ
	private ImageView bookCover;//����
	private TextView bookName;//����
	private TextView author;//����
	private TextView publisher;//������
	private TextView publishDate;//��������
	private TextView IBSN;
	private TextView price;//�۸�
	private TextView classifyID;//�����
	private TextView searchID;//�����
	private TextView pageNum;//ҳ��
	private TextView description;//����
	private TableLayout infoTable;//�ݲ���Ϣ��
	private Button checkCom;//�鿴����
	private Button addToShef;//�������
	private Button addToRemind;//��������
	private Book book;//�鱾
	private ArrayList<String> tableInfo;
	private final int TABLEINFO = 99;
	private final int ERRORMSG = 98;
	private final int SHOWTABLE = 97;
	private final int PUTTOSHELF = 96;
	private final int PUTTOREMIND = 95;
	private String errorMsg = "";
	private DataService dataService;//���ݷ���
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
								errorMsg = "�ɹ������ҵ����";
							}
							else{
								errorMsg = "�����ҵ����ʧ��";
							}

						} catch (Exception e) {
							errorMsg ="�����ҵ����ʧ�ܣ�������";
							//e.printStackTrace();
						}
						handler.sendEmptyMessage(ERRORMSG);
					}

				}.start();
				break;
			case PUTTOREMIND:
				Toast.makeText(BookDetailActivity.this, "���ڼ�������...", Toast.LENGTH_SHORT).show();
				new Thread(){
					@Override
					public void run() {
						try {
							if(postService.putToBookRemindPost(MyAccount.getUserId(),book.getISBN())){
								errorMsg = "�������ѳɹ�";
							}
							else{
								errorMsg = "��������ʧ��";
							}
						} catch (Exception e) {
							errorMsg="��������ʧ��";
						}
						handler.sendEmptyMessage(ERRORMSG);
					}
				}.start();
			}
		}
	};
	//������������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bookdetail);
		this.setTitle("�鼮����");
		Intent intent = this.getIntent();
		book = new Book();
		book = intent.getParcelableExtra("book");
		initActivity();
		handler.sendEmptyMessage(TABLEINFO);
	}

	/**
	 * ��ʼ������
	 */
	private void initActivity() {
		dataService = new DataService();
		postService = new PostService();
		//��ȡ�ؼ�
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
		//���ü���
		checkCom.setOnClickListener(click_listener);
		addToShef.setOnClickListener(click_listener);
		addToRemind.setOnClickListener(click_listener);
		bookCover.setOnClickListener(click_listener);
		//��ֵ
		setValue();
	}
	private void setValue() {
		if(book != null){
			bookCover.setImageBitmap(book.getCoverImage());
			bookName.setText("����:"+book.getBookName());
			author.setText("���ߣ�"+book.getAuthor());
			publisher.setText("�����磺"+book.getPublisher());
			publishDate.setText("�������ڣ�"+book.getPublishDate());
			IBSN.setText("IBSN��"+book.getISBN());
			price.setText("�۸�"+book.getPrice());
			classifyID.setText("����ţ�"+book.getClassifyID());
			searchID.setText("����ţ�"+book.getSearchID());
			pageNum.setText("ҳ����"+book.getPageNumber());
			description.setText("������"+book.getDescription());
			if(book.getHoldNumber()==0){
				addToRemind.setVisibility(View.VISIBLE);
			}
		}
		else{
			Toast.makeText(BookDetailActivity.this, 
					"��ȡ��Ϣʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	OnClickListener click_listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.btn_dtl_buy:
				String[] webs = {"����ѷ","�Ա�","����"};
				new AlertDialog.Builder(BookDetailActivity.this)
				.setTitle("��ѡ��")
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
						.setNegativeButton("ȡ��", null)
						.show();

				break;
			case R.id.btn_dtl_put_to_shef:
				if(MyAccount.getCookies()==null){
					Toast.makeText(BookDetailActivity.this, 
							"��δ��¼�����ȵ�¼", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(BookDetailActivity.this,LoginActivity.class);
					startActivityForResult(intent,200);
				}
				else{
					Toast.makeText(BookDetailActivity.this, 
							"���ڷ������", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(PUTTOSHELF);
				}

				break;
			case R.id.btn_dtl_put_to_remind:
				if(MyAccount.getCookies()==null){
					Toast.makeText(BookDetailActivity.this, 
							"��δ��¼�����ȵ�¼", Toast.LENGTH_SHORT).show();
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
				.setTitle("����")
				.setView(img)
				.setPositiveButton("ȷ��", null)
				.show();

			}
		}
	};

	/**
	 * ��ȡ�ݲ���Ϣ��
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
							errorMsg = "�ػ�ȡ�ݲ���Ϣʧ��:"+Connection.getHttpResponse().getStatusLine();
							handler.sendEmptyMessage(ERRORMSG);
						}
					}
					catch (Exception e) {
						errorMsg = "�ػ�ȡ�ݲ���Ϣʧ��";
						handler.sendEmptyMessage(ERRORMSG);

					}
				}
				else{
					errorMsg = "���粻����";
					handler.sendEmptyMessage(ERRORMSG);
				}
			}
		}.start();
	}


	/**
	 * ��ʾ���
	 *@author Tomorrow
	 *@since 2014-5-2
	 */
	private void showTable() {
		ArrayList<TableRow> trs = new ArrayList<TableRow>();
		ArrayList<TextView> txts = new ArrayList<TextView>();
		LayoutParams param = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		//����
		int col = 8;//�������������վ���ܻ����
		for(int r = 0;r<tableInfo.size()/col;r++){
			//���һ��
			trs.add(new TableRow(infoTable.getContext()));
			//����
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
					//����ìֵ
					txt.setLayoutParams(param);
					//���һ��
					txts.add(txt);
					//������Ϣ
					txts.get(txts.size()-1).setText(tableInfo.get(r*col+c));
					//���ü��
					txts.get(txts.size()-1).setWidth(trs.get(r).getWidth()/5);
					//����λ��
					txts.get(txts.size()-1).setGravity(Gravity.CENTER);
					//��������
					trs.get(r).addView(txts.get(txts.size()-1));
				}
			}
			//��������
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
