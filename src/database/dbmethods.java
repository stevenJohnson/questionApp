package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Vector;

import utilities.IdConverter;
import utilities.Question;
import android.content.Context;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class dbmethods
{
	public static boolean postQuestion(Question q, Context context)
	{
		int personId = q.getAnswerers().ordinal();
		String question = q.getQuestion();
		String appUser = q.getUser();

		String filepath = "";
		//make text file to be uploaded
		try
		{
			filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
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
			filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
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
}
