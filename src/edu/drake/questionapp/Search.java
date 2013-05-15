package edu.drake.questionapp;

import java.util.ArrayList;

import database.dbmethods;

import utilities.AListAdapter;
import utilities.Answer;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Search extends Activity {
	
	private static final String TAG = "SearchActivity";
	Button button;
	int desiredQuestionId = -1;
	int likeFlag = 0;
	ArrayList<Answer> myResults = new ArrayList<Answer>();
	Context myContext;

	private void makeLoadingInvisible()
	{
		TextView loading = (TextView) findViewById(R.id.answerLoading);
		loading.setVisibility(TextView.INVISIBLE);
	}

	private void makeLoadingVisible()
	{
		TextView loading = (TextView) findViewById(R.id.answerLoading);
		loading.setVisibility(TextView.VISIBLE);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		((ListView) findViewById(R.id.AList)).setAdapter(new AListAdapter(this, new ArrayList<Answer>()));
		
		makeLoadingVisible();
		GetResultsTask t = new GetResultsTask();
		t.execute();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		myContext = getBaseContext();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public class GetResultsTask extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) {
			//myResults = dbmethods.getResults(desiredQuestionsID, appUser);
			return null;
		}
		
	}

}
