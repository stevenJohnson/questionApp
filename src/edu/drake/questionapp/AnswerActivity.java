package edu.drake.questionapp;

import database.dbmethods;
import utilities.Answer;
import utilities.CategorySorter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AnswerActivity extends Activity
{
	int questionID = 0;
	Answer toPost = new Answer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		
		questionID = getIntent().getExtras().getInt("questionID");
		
		TextView textViewQ = (TextView) findViewById(R.id.question);
		ImageView imageView = (ImageView) findViewById(R.id.icon);
		TextView textViewN = (TextView) findViewById(R.id.name);
		TextView textViewAN = (TextView) findViewById(R.id.answerername);
		
		textViewQ.setText(getIntent().getExtras().getString("question"));
		textViewN.setText(getIntent().getExtras().getString("user"));
		textViewAN.setText(getIntent().getExtras().getString("personname"));
		imageView.setImageResource(getIntent().getExtras().getInt("persondrawable"));
		
		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view){
						TextView tv = (TextView)findViewById(R.id.personid);
						String theAnswer = tv.getText().toString();
						toPost = new Answer(theAnswer, questionID);
						PostAnswerTask pqt = new PostAnswerTask();
						pqt.execute();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answer, menu);
		return true;
	}

	public class PostAnswerTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return dbmethods.postAnswer(toPost, getApplicationContext());
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			// show a popup if successful/unsuccessful and other stuffs...
			if(success) finish();
		}
	}
}
