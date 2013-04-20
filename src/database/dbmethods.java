package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

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

	public static boolean loginPls(String email, String pass)
	{
		// attempt authentication against the drake server !!
		JSch jsch = new JSch();
		String user="asapp";
		String host="artsci.drake.edu";
		String passy="9Gj24!L6c848FG$";
		int port=22;

		try
		{
			Session session=jsch.getSession(user, host, port);
			JSch.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passy);
			session.connect();
			Channel channel=session.openChannel("sftp");
			channel.connect();
			ChannelSftp c=(ChannelSftp)channel;
			try
			{
				c.cd("WhatWould");
				InputStream is = c.get("userinfo.txt");
				Scanner scanny = new Scanner(is);
				String[] creds;
				while(scanny.hasNext())
				{
					creds = scanny.nextLine().split(":");
					if(creds[0].equals(email)) 
					{
						session.disconnect();
						c.disconnect();
						return creds[1].equals(pass);
					}
				}
			}
			catch(SftpException e)
			{
				e.printStackTrace();
			}
		}
		catch(JSchException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean signupPls(String userName, String name, String password, Context context)
	{
		// make sure that user isn't already a user, else sign up
		JSch jsch = new JSch();
		String user="asapp";
		String host="artsci.drake.edu";
		String passy="9Gj24!L6c848FG$";
		int port=22;

		try
		{
			Session session=jsch.getSession(user, host, port);
			JSch.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passy);
			session.connect();
			Channel channel=session.openChannel("sftp");
			channel.connect();
			ChannelSftp c=(ChannelSftp)channel;
			try
			{
				c.cd("WhatWould");
				InputStream is = c.get("userinfo.txt");
				Scanner scanny = new Scanner(is);
				String[] creds;
				while(scanny.hasNext())
				{
					creds = scanny.nextLine().split(":");
					if(creds[0].equals(userName)) 
					{
						// the email already exists, so deny login
						session.disconnect();
						c.disconnect();
						scanny.close();
						return false;
					}
				}
				// if we've made it past the while loop, the email is not already an account so sign em up !
				
				// release old imput stream and scanner
				is.close();
				scanny.close();
				
				is = c.get("userinfo.txt");
				scanny = new Scanner(is);
				
				// make a new file that is a copy of server's userinfo.txt, and add this user
				// yes, this has obvious security flaws, but it is merely for proof of concept
				String filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
				File file = new File(filepath);
				file.createNewFile();

				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
				
				String s = "";
				while(scanny.hasNext())
				{
					s = scanny.nextLine();
					writer.write(s);
					writer.write(System.getProperty("line.separator"));
				}
				scanny.close();
				is.close();

				// write new user
				writer.write(userName + ":" + password);

				writer.flush();
				writer.close();

				// put back the new file
				c.rm("userinfo.txt");
				c.put(filepath, "userinfo.txt");
				
				// create a directory for this user in Users directory
				c.cd("Users");
				c.mkdir(userName);
				
				// create a questions.txt, answers.txt, name.txt for this new user
				c.cd(userName);
				
				File filey = new File(filepath);
				filey.createNewFile();
				
				OutputStreamWriter writery = new OutputStreamWriter(new FileOutputStream(filey));
				
				// blank file for questions and answers
				c.put(filepath, "questions.txt");
				c.put(filepath, "answers.txt");
				
				// add name for name.txt
				writery.write(name);
				writery.flush();
				writery.close();
				
				c.put(filepath, "name.txt");
				
				return true;
			}
			catch(SftpException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch(JSchException e)
		{
			e.printStackTrace();
		}
		finally
		{
			// ensure server's userinfo.txt is deleted locally
			File file = new File(context.getFilesDir().getPath().toString() + "/tmp.txt");
			file.delete();
		}
		return false;
	}
}
