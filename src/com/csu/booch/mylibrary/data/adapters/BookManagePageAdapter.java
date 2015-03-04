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
 * ͼ�������������������
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
		//����һ��ҳ��
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
		//��ʾ4������
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		//���ر���
		switch (position) {
		case 0:
			return "ͼ������" ;
		case 1:
			return "������ʷ";
		case 2:
			return "�ƾ���Ϣ";
		case 3:
			return "ϵͳ��Ϣ";
		}
		return null;
	}
}