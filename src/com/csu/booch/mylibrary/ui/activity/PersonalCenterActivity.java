package com.csu.booch.mylibrary.ui.activity;



import com.csu.booch.mylibrary.data.adapters.PCPagesAdapter;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.ui.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * 个人中心
 * @name PersonalCenterActivity.java
 * @author Tomorrow
 * @since  2014-4-28
 */
@SuppressLint("NewApi")
public class PersonalCenterActivity extends FragmentActivity implements ActionBar.TabListener 
{
	PCPagesAdapter mSectionsPagerAdapter;

	/**
	 * 管理fragment
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		// 声明一个actionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//实例化适配器
		mSectionsPagerAdapter = new PCPagesAdapter(
				getSupportFragmentManager());

		// 获取mViewPager控件
		mViewPager = (ViewPager) findViewById(R.id.personal_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// 设置监听
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.banner));
		actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
		actionBar.setTitle("");
		// 根据界面数量加载tab
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			switch(i){
			case 0:
				actionBar.addTab(actionBar.newTab()
//						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setIcon(R.drawable.clock_on)
						.setTabListener(this));
				break;
			case 1:
				actionBar.addTab(actionBar.newTab()
//						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setIcon(R.drawable.user_on)
						.setTabListener(this));
				break;
			case 2:
				actionBar.addTab(actionBar.newTab()
//						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setIcon(R.drawable.key_on)
						.setTabListener(this));
				break;
			case 3:
				actionBar.addTab(actionBar.newTab()
//						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setIcon(R.drawable.sheild_on)
						.setTabListener(this));
				break;
				
			}
			
		}
	}



	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
			mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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

