package edu.drake.questionapp;

import utilities.CategorySorter;
import utilities.Question;
import utilities.ThisApplication;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import database.dbmethods;

public class TempQuestionActivity extends Activity
{
	private Question toPost;

	String[] names = new String[CategorySorter.getLength()];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_question);

		//picking the person with a spinner instead of typing ID.
		final Spinner spinner = (Spinner) findViewById(R.id.namespinner);
		for (int i= 0; i < CategorySorter.getLength(); i++){
			names[i] = CategorySorter.getCharacterName(i);
		}
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int personId = spinner.getSelectedItemPosition();
						//int personId = Integer.parseInt(((EditText)findViewById(R.id.personid)).getText().toString());
						String question = ((EditText)findViewById(R.id.questionfield)).getText().toString();
						String appUser = ((ThisApplication)getApplicationContext()).getUsername();
						toPost = new Question(question, personId, appUser);
						PostQuestionTask pqt = new PostQuestionTask();
						pqt.execute();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temp_question, menu);
		return true;
	}

	public class PostQuestionTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return dbmethods.postQuestion(toPost, getApplicationContext());
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			// show a popup if successful/unsuccessful and other stuffs...
			if(success) finish();
		}
	}
}
