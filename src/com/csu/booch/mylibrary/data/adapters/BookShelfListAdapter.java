/**
 * 
 */
package com.csu.booch.mylibrary.data.adapters;

import java.util.ArrayList;

import com.csu.booch.mylibrary.data.domain.ShelfNode;
import com.csu.booch.mylibrary.ui.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @name BookShelfListAdapter.java
 * @author Tomorrow
 * @since  2014-5-3
 */
public class BookShelfListAdapter extends BaseAdapter {
	private ArrayList<ShelfNode> bookList;
	private Context context;

	public BookShelfListAdapter(ArrayList<ShelfNode> bookList, Context context) {
		this.bookList = bookList;
		this.context = context;
	}


	@Override
	public int getCount() {

		return bookList.size();
	}


	@Override
	public Object getItem(int position) {

		return bookList.get(position);
	}


	@Override
	public long getItemId(int position) {

		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();
		if(convertView==null){
			convertView = View.inflate(context, 
					R.layout.item_bookshelflist, null);
			
		}
		viewHolder.bookName = (TextView)convertView.findViewById(R.id.shelf_book_name);
		viewHolder.author=(TextView)convertView.findViewById(R.id.shelf_book_author);
		viewHolder.dateCome=(TextView)convertView.findViewById(R.id.shelf_come_date);
		viewHolder.delete=(Button)convertView.findViewById(R.id.btn_shelf_delete);
		convertView.setTag(viewHolder);
		if(bookList.get(position)!=null){
			viewHolder.bookName.setText(bookList.get(position).getName());
			viewHolder.author.setText(bookList.get(position).getAuthor());
			viewHolder.dateCome.setText(bookList.get(position).getDateCome());
		}
		Animation animation = AnimationUtils.loadAnimation(convertView.getContext(), android.R.anim.slide_in_left);
		convertView.setAnimation(animation);
		
		return convertView;
	}
	public class ViewHolder{
		Button delete;
		TextView bookName;
		TextView author;
		TextView dateCome;
	}

}
