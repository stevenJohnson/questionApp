package edu.drake.questionapp;

import utilities.*;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import fragments.DummySectionFragment;
import fragments.PhotoFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	private static final String TAG = "MainActivity";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	PhotoFragment piFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		setTitle("");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
		{
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// start with top questions selected
		actionBar.setSelectedNavigationItem(1);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
        if (((ThisApplication)getApplicationContext()).getUsername() == null)
        {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
            return;
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override 	
	public boolean onOptionsItemSelected(MenuItem item) 
	{ 	    	
		switch (item.getItemId()) 	   
		{ 	    	
		case R.id.menu_settings: 
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			return true;
		case R.id.search:
			Intent intentSearch = new Intent(this,Search.class);
			startActivity(intentSearch);
			return true;
		case R.id.compose:
			// todo ::: use once new question is actually implemented...
			//Intent newQ = new Intent(this, NewQuestion.class);
			
			Intent newQ = new Intent(this, TempQuestionActivity.class);
			startActivity(newQ);
			return true;
		default: 	 
			return super.onOptionsItemSelected(item); 	 
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			// getItem is called to instantiate the fragment for the given page.

			if(position == 0)
			{
				return new PhotoFragment();
			}
			
			if(position == 1)
			{
				return new TopqActivity();
			}
			
			if(position == 2)
			{
				return new RecqActivity();
			} 
			if(position == 3)
			{
				return new MyQFragment();
			}

			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount()
		{
			// Show 4 total pages: categories, top questions, recent questions, my questions
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position)
			{
			case 0:
				return "categories";
				//return getString(R.string.title_section1).toUpperCase();
			case 1:
				return "top questions";
				//return getString(R.string.title_section2).toUpperCase();
			case 2:
				return "recent questions";
				//return getString(R.string.title_section3).toUpperCase();
			case 3:
				return "my questions";
			}
			return null;
		}
	}
}
