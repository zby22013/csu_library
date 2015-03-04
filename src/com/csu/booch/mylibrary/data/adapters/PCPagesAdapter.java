package com.csu.booch.mylibrary.data.adapters;

import com.csu.booch.mylibrary.ui.fragments.BookShelfFragment;
import com.csu.booch.mylibrary.ui.fragments.ChangeInfoFragment;
import com.csu.booch.mylibrary.ui.fragments.ChangePasswordFragment;
import com.csu.booch.mylibrary.ui.fragments.PermissionFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * 个人中心界面的数据适配器
 * @author Tomorrow
 *
 */
public class PCPagesAdapter extends FragmentPagerAdapter {
	private  ChangePasswordFragment cpFragment;
	private  ChangeInfoFragment ciFragment;
	private  PermissionFragment pFragment;
	private  BookShelfFragment bFragment;
	public PCPagesAdapter(FragmentManager fm) {
		super(fm);
		cpFragment=new ChangePasswordFragment();
		ciFragment=new ChangeInfoFragment();
		pFragment=new PermissionFragment();
		bFragment=new BookShelfFragment();
	}

	@Override
	public Fragment getItem(int position) {
		//返回一个页面
		switch(position){
		case 0:
			return bFragment;
		case 1:
			return ciFragment;
		case 2:
			return pFragment;
		case 3:
			return cpFragment;
		default: 
			return bFragment;
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
			return "密码修改" ;
		case 1:
			return "资料更变";
		case 2:
			return "阅读权限";
		case 3:
			return "预借图书";
		}
		return null;
	}
}