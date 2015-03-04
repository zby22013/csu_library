package com.csu.booch.mylibrary.data.adapters;

import com.csu.booch.mylibrary.ui.fragments.ArrearsgeFragment;
import com.csu.booch.mylibrary.ui.fragments.HistoryFragment;
import com.csu.booch.mylibrary.ui.fragments.MessageFragment;
import com.csu.booch.mylibrary.ui.fragments.RenewBorrowFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * 图书管理界面的数据适配器
 * @author Tomorrow
 *
 */
public class BookManagePageAdapter extends FragmentPagerAdapter {
	
	private  ArrearsgeFragment arrFragment;
	private  HistoryFragment hisFragment;
	private  MessageFragment msgFragment;
	private  RenewBorrowFragment rFragment;
	public BookManagePageAdapter(FragmentManager fm) {
		super(fm);
		arrFragment=new ArrearsgeFragment();
		hisFragment=new HistoryFragment();
		msgFragment=new MessageFragment();
		rFragment=new RenewBorrowFragment();
	}

	@Override
	public Fragment getItem(int position) {
		//返回一个页面
		switch(position){
		case 0:
			return rFragment;
		case 1:
			return hisFragment;
		case 2:
			return arrFragment;
		case 3:
			return msgFragment;
		default: 
			return rFragment;
		}
	
	}

	@Override
	public int getCount() {
		//显示4个界面
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		//返回标题
		switch (position) {
		case 0:
			return "图书续借" ;
		case 1:
			return "借阅历史";
		case 2:
			return "财经信息";
		case 3:
			return "系统信息";
		}
		return null;
	}
}