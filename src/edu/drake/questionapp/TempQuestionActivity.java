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

import utilities.IdConverter;
import utilities.ThisApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class TempQuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_question);


		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Log.d("TempQ", "in on click");
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

	private boolean postQuestion()
	{
		int personId = Integer.parseInt(((EditText)findViewById(R.id.personid)).getText().toString());
		String question = ((EditText)findViewById(R.id.questionfield)).getText().toString();
		String appUser = ((ThisApplication)getApplicationContext()).getUsername();

		String filepath = "";
		//make text file to be uploaded
		try
		{
			filepath = getApplicationContext().getFilesDir().getPath().toString() + "/tmp.txt";
			File file = new File(filepath);
			file.createNewFile();

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));

			// write the question.txt file locally as tmp.txt
			writer.write(question);
			writer.write(System.getProperty("line.separator"));
			writer.write(Integer.toString(personId));
			writer.write(System.getProperty("line.separator"));
			writer.write("0");
			writer.write(System.getProperty("line.separator"));
			writer.write(appUser);

			writer.flush();
			writer.close();
		}
		catch(Exception ex)
		{
			// note error in local file creation
			Log.d("TempQuestion", ex.getMessage());
		}

		//ssh
		JSch jsch = new JSch();
		String user="asapp";
		String host="artsci.drake.edu";
		String pass="9Gj24!L6c848FG$";
		int port=22;

		try
		{
			Session session=jsch.getSession(user, host, port);
			JSch.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(pass);
			session.connect();
			Channel channel=session.openChannel("sftp");
			channel.connect();
			ChannelSftp c=(ChannelSftp)channel;

			c.cd("WhatWould");

			// obtain vector from ls simply to count the number of directories
			Vector v = c.ls("Questions");

			c.cd("Questions");
			//get question id

			// one for ., one for .., so subtract two since we index from 0
			int i = v.size() - 2;
			String questionId = IdConverter.intToStringId(i);

			// make a new directory for the question
			c.mkdir(questionId);
			c.cd(questionId);
			c.mkdir("Answers");
			c.put(filepath, "question.txt");

			// add this question id to the user's question.txt file
			c.cd("../../Users/");
			c.cd(appUser);

			//make a new file that is a copy of the user's old question.txt
			filepath = getApplicationContext().getFilesDir().getPath().toString() + "/tmp.txt";
			File file = new File(filepath);
			file.createNewFile();

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));			

			InputStream is = c.get("questions.txt");
			Scanner scanny = new Scanner(is);
			String s = "";
			while(scanny.hasNext())
			{
				s = scanny.nextLine();
				writer.write(s);
				writer.write(System.getProperty("line.separator"));
			}
			scanny.close();
			is.close();

			// write new questionid
			writer.write(questionId);

			writer.flush();
			writer.close();

			// put back the new file
			c.rm("questions.txt");
			c.put(filepath, "questions.txt");
		}
		catch(Exception ex)
		{
			Log.d("TMP QUESTION",ex.getMessage());
		}

		return true;
	}

	public class PostQuestionTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return postQuestion();
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			// show a popup if successful/unsuccessful and other stuffs...
		}
	}

}
