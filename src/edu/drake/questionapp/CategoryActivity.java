package edu.drake.questionapp;

import java.util.ArrayList;

import database.dbmethods;

import utilities.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends Activity
{
	private ArrayList<Question> myQuestions = new ArrayList<Question>();
	private boolean isDBcall = false;
	private int personID = 0;
	private View theView;
	private String appUser = "";
	
	private void makeLoadingInvisible()
	{
		TextView loading = (TextView) findViewById(R.id.Qloading);
		loading.setVisibility(TextView.INVISIBLE);
	}

	private void makeLoadingVisible()
	{
		TextView loading = (TextView) findViewById(R.id.Qloading);
		loading.setVisibility(TextView.VISIBLE);
	}
	
	private void changeLoadingText()
	{
		TextView loading = (TextView) theView.findViewById(R.id.Qloading);
		loading.setText("This person doesn't appear to have any questions asked of them!");
		loading.setVisibility(TextView.VISIBLE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		appUser = ((ThisApplication) getApplication()).getUsername();
		
		theView = findViewById(android.R.id.content);

		personID = getIntent().getExtras().getInt("position");
		
		// set the text
		TextView loading = (TextView) findViewById(R.id.Qloading);
		loading.setText("Loading questions for " + utilities.CategorySorter.getCharacterName(personID) + "...");		
		
		ListView listview = (ListView) findViewById(R.id.Qlist);

		if(myQuestions.size() == 0 && !isDBcall)
		{
			isDBcall = true;
			makeLoadingVisible();
			GetCategoryQuestionsTask t = new GetCategoryQuestionsTask();
			t.execute();
		}

		if(!isDBcall)
		{
			listview.setAdapter(new QListAdapter(getApplicationContext(), myQuestions));
		}
		else
		{
			makeLoadingVisible();
		}
		
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg1, View arg2, int pos, long arg3)
			{
				// start new activity passing the position of the clicked picture to know what to query
				Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
				intent.putExtra("questionID", myQuestions.get(pos).getQuestionID());
				intent.putExtra("question", myQuestions.get(pos).getQuestion());
				intent.putExtra("likes", myQuestions.get(pos).getUps());
				intent.putExtra("person", myQuestions.get(pos).getAnswerers().ordinal());
				intent.putExtra("user", myQuestions.get(pos).getUser());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_category, menu);
		return true;
	}
	
	public class GetCategoryQuestionsTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{			
			myQuestions = dbmethods.getCategoryQuestions(personID, 12, appUser);
			return myQuestions.size() <= 12 && myQuestions.size() > 0;
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			if (success)
			{
				// remove the loading message
				makeLoadingInvisible();

				ListView listview = (ListView) theView.findViewById(R.id.Qlist);
				Log.d("post ex", "about to refresh");
				listview.setAdapter(new QListAdapter(theView.getContext(), myQuestions));
				Log.d("post ex", "refreshed");
			}
			else
			{
				// remove the loading message
				if(myQuestions.size() == 0) changeLoadingText();
				
				// post an error
				Log.d("post ex", "ERRORRRRRRRRRRRRRRRRRRR");
			}
			isDBcall = false;
		}
	}
}