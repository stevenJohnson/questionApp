package edu.drake.questionapp;

import utilities.*;
import utilities.CategorySorter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class CategoryActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_category);

		int personQuery = getIntent().getExtras().getInt("position");

		TextView tv = (TextView) findViewById(R.id.categoryQueryText);

		tv.setText("questions from " + CategorySorter.getCharacterName(CategorySorter.getPosition(personQuery)) + "\n    here is who is logged in ::: " + ((ThisApplication)getApplicationContext()).getUsername());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_category, menu);
		return true;
	}
}