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
 * �������Ľ��������������
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
		//����һ��ҳ��
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
		//��ʾ4������
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		//���ر���
		switch (position) {
		case 0:
			return "�����޸�" ;
		case 1:
			return "���ϸ���";
		case 2:
			return "�Ķ�Ȩ��";
		case 3:
			return "Ԥ��ͼ��";
		}
		return null;
	}
}