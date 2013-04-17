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
import android.app.Application;
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
	public boolean onCreateOptionsMenu(Menu menu) {
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
			Log.d("TempQ", "a");
			filepath = getApplicationContext().getFilesDir().getPath().toString() + "/tmp.txt";
			File file = new File(filepath);
			Log.d("TempQ", "b");
			file.createNewFile();
			Log.d("TempQ", "c");
			
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
			
			writer.write(question);
			writer.write(System.getProperty("line.separator"));
			writer.write(Integer.toString(personId));
			writer.write(System.getProperty("line.separator"));
			writer.write("0");
			writer.write(System.getProperty("line.separator"));
			writer.write(appUser);
			Log.d("TempQ", "d");
			
			writer.flush();
			writer.close();
		}
		catch(Exception ex)
		{
			// note error in local file creation
			Log.d("TempQ", ex.getMessage());
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
			Log.d("TempQ", "e");
			session.connect();
			Log.d("TempQ", "f");
			Channel channel=session.openChannel("sftp");
			Log.d("TempQ", "g");
			channel.connect();
			Log.d("TempQ", "h");
			ChannelSftp c=(ChannelSftp)channel;
			
			c.cd("WhatWould");
			
			Vector v = c.ls("Questions");
			
			c.cd("Questions");
			Log.d("TempQ", "h");
			//get question id
			
			// one for ., one for ..
			int i = v.size() - 2;
			Log.d("TempQ", "" + i);
			String questionId = IdConverter.intToStringId(i);
			Log.d("TempQ", "qid: " + questionId);
			Log.d("TempQ", "i");
			
			
			/* may not need this file !!!
			InputStream is = chan.get("number.txt");
			Scanner scanny = new Scanner(is);
			is.close();
			
			int qId = Integer.parseInt(scanny.nextLine());
			String questionId = IdConverter.intToStringId(qId);
			*/
			c.mkdir(questionId);
			c.cd(questionId);
			Log.d("TempQ", "in questionid dir");
			c.mkdir("Answers");
			c.put(filepath, "question.txt");
			Log.d("TempQ", "put dat file");
			
			
			
			// add this question id to the user's question.txt file
			c.cd("../../Users/");
			Log.d("asdf", "here");
			c.cd(appUser);
			Log.d("asdf", "hurrr");
			
			//make a new file that is a copy of the user's old question.txt
			filepath = getApplicationContext().getFilesDir().getPath().toString() + "/tmp.txt";
			File file = new File(filepath);
			Log.d("asdf", "about to create new file");
			file.createNewFile();
			
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));			
			
			Log.d("asdf", "before get questions.txt");
			InputStream is = c.get("questions.txt");
			Log.d("asdf", "get questions.txt");
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
			Log.d("asdf", "about to rm");
			c.rm("questions.txt");
			Log.d("asdf", "rm");
			c.put(filepath, "questions.txt");
			Log.d("asdf", "put new user question.txt!!");
		}
		catch(Exception ex)
		{
			Log.d("TMP QUESTION",ex.getMessage());
		}


		return true;
	}
	
	public class PostQuestionTask extends AsyncTask<Void, Void, Boolean> {
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
