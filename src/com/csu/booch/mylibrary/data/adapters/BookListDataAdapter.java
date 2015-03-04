package com.csu.booch.mylibrary.data.adapters;

import java.util.List;

import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.ui.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 书籍搜索结果列表数据适配器  
 * 作用:将数据与listView连接
 * @author Tomorrow
 *
 */
@SuppressLint("NewApi")
public class BookListDataAdapter extends BaseAdapter{

	private List<Book> list;
	private LayoutInflater inflate;
	public BookListDataAdapter(Context context , List<Book> list){
		this.list = list;
		inflate = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {

		return list.size();
	}

	/**
	 *返回当前的书籍对象
	 */
	@Override
	public Object getItem(int i) {

		return list.get(i);
	}

	@Override
	public long getItemId(int i) {

		return 0;
		
	}

	/**
	 * 获得listView子项的view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = new ViewHolder();;
		if (convertView == null) {
			convertView = inflate.inflate(
					R.layout.item_booklist, null);
			viewHolder.bookCover = (ImageView)convertView.findViewById(R.id.book_cover);
			viewHolder.bookName = (TextView) convertView.findViewById(R.id.book_name);
			viewHolder.anthor = (TextView) convertView.findViewById(R.id.author);
			viewHolder.publisher = (TextView) convertView.findViewById(R.id.publisher);
			viewHolder.searchID = (TextView) convertView.findViewById(R.id.search_id);
			viewHolder.classifyID = (TextView) convertView.findViewById(R.id.classify_id);
			viewHolder.holdNumber = (TextView) convertView.findViewById(R.id.hold_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(convertView != null){
			if(position < list.size()){
				if(list.get(position).getCoverImage() ==null){
					viewHolder.bookCover = (ImageView)convertView.findViewById(R.id.book_cover);
				}
				else{
					viewHolder.bookCover.setImageBitmap(list.get(position).getCoverImage());
				}
				viewHolder.bookName.setText(list.get(position).getBookName());
				viewHolder.anthor.setText("作者:"+list.get(position).getAuthor());
				viewHolder.publisher.setText("出版社:"+list.get(position).getPublisher());
				viewHolder.searchID.setText("索书号:"+list.get(position).getSearchID());
				viewHolder.classifyID.setText("分类号:"+list.get(position).getClassifyID());
				viewHolder.holdNumber.setText("在馆数:"+list.get(position).getHoldNumber());
				convertView.setTag(viewHolder);
			}
		}
		Animation animation = AnimationUtils.loadAnimation(convertView.getContext(), android.R.anim.slide_in_left);
		convertView.setAnimation(animation);
		return convertView;
	}
	/**
	 * @param list the list to set
	 * @author Tomorrow
	 * 
	 */
	public void setList(List<Book> list) {
		this.list = list;
	}
	/**
	 * 
	 * @name BookListDataAdapter.java
	 * @author Tomorrow
	 * @since  2014-4-28
	 */
	public class ViewHolder{
		ImageView bookCover;
		TextView bookName;
		TextView anthor;
		TextView publisher;
		TextView searchID;
		TextView classifyID;
		TextView holdNumber;
	}

}
