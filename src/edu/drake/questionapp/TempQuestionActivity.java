package edu.drake.questionapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import database.dbmethods;

import utilities.IdConverter;
import utilities.Question;
import utilities.ThisApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class TempQuestionActivity extends Activity
{

	private Question toPost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_question);


		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int personId = Integer.parseInt(((EditText)findViewById(R.id.personid)).getText().toString());
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
