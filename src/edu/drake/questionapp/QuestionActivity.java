package edu.drake.questionapp;

import java.util.ArrayList;

import utilities.AListAdapter;
import utilities.Answer;
import utilities.CategorySorter;
import utilities.ThisApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import database.dbmethods;

public class QuestionActivity extends Activity
{
	private static final String TAG = "QuestionActivity";
	Button button;
	ImageButton likeButton;
	int desiredQuestionID = -1;
	int likeFlag = 0;
	ArrayList<Answer> myAnswers = new ArrayList<Answer>();
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
		GetAnswersTask t = new GetAnswersTask();
		t.execute();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		myContext = getBaseContext();
		
		desiredQuestionID = getIntent().getExtras().getInt("questionID");
		//added new intents to fill these
		TextView textViewQ = (TextView) findViewById(R.id.question);
		ImageView imageView = (ImageView) findViewById(R.id.icon);
		TextView textViewN = (TextView) findViewById(R.id.name);
		TextView textViewL = (TextView) findViewById(R.id.likes);
		TextView textViewAN = (TextView) findViewById(R.id.answerername);
		
		textViewQ.setText(getIntent().getExtras().getString("question"));
		textViewN.setText(getIntent().getExtras().getString("user"));
		textViewL.setText(getIntent().getExtras().getInt("likes") + " likes");
		textViewAN.setText(CategorySorter.getCharacterName(getIntent().getExtras().getInt("person")));
		imageView.setImageResource(CategorySorter.getDrawable(getIntent().getExtras().getInt("person")));

		final ListView listview = (ListView) findViewById(R.id.AList);

		// use an asynch task to fill myAnswers !!
		makeLoadingVisible();
		GetAnswersTask t = new GetAnswersTask();
		t.execute();		

		AListAdapter adapter = new AListAdapter(this, myAnswers);
		listview.setAdapter(adapter);

		button = (Button) findViewById(R.id.submit); 
		button.setOnClickListener(new OnClickListener()
		{ 
			@Override
			public void onClick(View v) {
				//code put here will be executed when button is pressed
				Log.v(TAG, "button pressed");

				//launch the 2nd screen via an Intent
				Intent intent = new Intent(v.getContext(), AnswerActivity.class);
				intent.putExtra("questionID", desiredQuestionID);
				intent.putExtra("question", getIntent().getExtras().getString("question"));
				intent.putExtra("personname", CategorySorter.getCharacterName(getIntent().getExtras().getInt("person")));
				intent.putExtra("persondrawable", CategorySorter.getDrawable(getIntent().getExtras().getInt("person")));
				intent.putExtra("user", getIntent().getExtras().getString("user"));
				startActivity(intent);
			}
		});
		
		likeButton = (ImageButton) findViewById(R.id.likeButton);
		if(getIntent().getExtras().getInt("hasLiked") == 1) likeButton.setImageResource(R.drawable.starchecked); 
		
		likeButton.setOnClickListener(new OnClickListener()
		{
					public void onClick(View v) {
						if (likeFlag == 0)
						{
							likeButton.setImageResource(R.drawable.starchecked);
							likeFlag++;
							LikeTask l = new LikeTask();
							l.execute();
						}
						else 
						{
							likeButton.setImageResource(R.drawable.starunchecked);
							likeFlag--;
						}
						
					}
				});
		
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	public class GetAnswersTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			myAnswers = dbmethods.getAnswers(desiredQuestionID);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			if (success)
			{
				// remove the loading message 
				makeLoadingInvisible();

				ListView listview = (ListView) findViewById(R.id.AList);
				Log.d("post ex", "about to refresh");
				listview.setAdapter(new AListAdapter(myContext, myAnswers));
				Log.d("post ex", "refreshed");				
			}
			else
			{
				// post an error
				Log.d("post ex", "ERRORRRRRRRRRRRRRRRRRRR");
			}
		}
	}
	
	public class LikeTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			//some issue with context here.
			dbmethods.likeQuestion(desiredQuestionID, myContext, ((ThisApplication)getApplicationContext()).getUsername());
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success){
				//reload the like number text view
				TextView textViewL = (TextView) findViewById(R.id.likes);
				textViewL.setText(getIntent().getExtras().getInt("likes") + " likes");		
			}
			else {
				// post an error
				Log.d("LikeTask", "didn't like :(");
			}
		}
	}
}
