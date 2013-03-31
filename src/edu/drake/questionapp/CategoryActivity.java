package edu.drake.questionapp;

import utilities.Answerer;
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

		int personQuery = getIntent().getExtras().getInt("person");

		TextView tv = (TextView) findViewById(R.id.categoryQueryText);

		if(personQuery > 3) personQuery = 3;

		tv.setText("questions from " + Answerer.values()[personQuery]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_category, menu);
		return true;
	}
}
