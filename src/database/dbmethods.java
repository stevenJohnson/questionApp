package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import utilities.*;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import edu.drake.questionapp.R;
import edu.drake.questionapp.QuestionActivity.GetAnswersTask;

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

				// make directory for likes
				c.mkdir("Likes");
				c.cd("Likes");
				c.put(filepath, "likedA.txt");
				c.put(filepath, "likedQ.txt");
				c.cd("..");

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

	public static boolean postAnswer(Answer a, Context context)
	{
		String questionID = utilities.IdConverter.intToStringId(a.getQuestionID());

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
				c.cd("Questions");
				c.cd(questionID);
				Vector v = c.ls("Answers");
				c.cd("Answers");

				// one for ., one for .., so subtract two since we index from 0
				int i = v.size() - 2;
				String answerID = IdConverter.intToStringId(i);

				String filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
				File file = new File(filepath);
				file.createNewFile();

				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));

				writer.write(a.getAnswer());
				writer.write(System.getProperty("line.separator"));
				writer.write(((ThisApplication)context).getUsername());
				writer.write(System.getProperty("line.separator"));
				writer.write("0"); // likes

				writer.flush();
				writer.close();

				c.put(filepath, answerID + ".txt");

				c.cd("../../..");
				c.cd("Users");
				c.cd(((ThisApplication)context).getUsername());

				// add this new questionID:answerID to the user's answer.txt
				file = new File(filepath);
				file.createNewFile();

				writer = new OutputStreamWriter(new FileOutputStream(file));

				InputStream is = c.get("answers.txt");
				Scanner scanny = new Scanner(is);
				String s = "";
				while(scanny.hasNext())
				{
					s = scanny.nextLine();
					writer.write(s);
				}
				writer.write(questionID + ":" + answerID);

				writer.flush();
				writer.close();
				is.close();

				c.put(filepath, "answers.txt");
			}
			catch(Exception ex)
			{
				Log.d("postAnswer", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("postAnswer", ex.getMessage());
		}

		return true;
	}

	public static ArrayList<Question> getQuestions(ArrayList<Integer> questionIDs, String appUser)
	{
		ArrayList<Question> retval = new ArrayList<Question>();
		String theQuestion = "";
		int ups = 0;
		int answerer = 0;
		String username = "";

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
				Log.d("getQ", "top");
				Log.d("getQ", "questionIDs size ::: " + questionIDs.size());

				for(Integer a : questionIDs)
				{
					Log.d("getQ", a + "");
				}

				c.cd("WhatWould");
				c.cd("Questions");
				Log.d("gettopq", "size of questionIDs ::: " + questionIDs.size());

				InputStream is = null;
				Scanner scanny = null;

				for(int i = 0; i < questionIDs.size(); i++)
				{
					Log.d("getQ", utilities.IdConverter.intToStringId(questionIDs.get(i)));
					c.cd(utilities.IdConverter.intToStringId(questionIDs.get(i)));

					try
					{
						Log.d("getQ", "changed to the questionid dir " + questionIDs.get(i));
						is = c.get("question.txt");
						scanny = new Scanner(is);
						theQuestion = scanny.nextLine();
						Log.d("getQ", "theQuestion ::: " + theQuestion);
						answerer = scanny.nextInt();
						Log.d("getQ", "answerer ::: " + answerer);
						ups = scanny.nextInt();
						Log.d("getQ", "ups ::: " + ups);
						username = scanny.nextLine();
						username = scanny.nextLine();
						Log.d("getQ", "username ::: " + username);

						Question q = new Question(questionIDs.get(i), theQuestion, ups, answerer, username);
						Log.d("getQ", "created question object");

						is.close();
						scanny.close();

						// get current number of answers
						try
						{
							Vector v = c.ls("Answers");
							Log.d("getQ", "did the ls");

							// one for ., one for .., so subtract two since we index from 0 
							int answers = v.size() - 2;
							Log.d("getQ", "got the size");
							q.setNumAnswers(answers);
							Log.d("getQ", "set number of answers to ::: " + answers);
						}
						catch(Exception ex)
						{
							q.setNumAnswers(0);
						}						

						retval.add(q);
					}
					catch(Exception ex)
					{
						Log.e("getQ", "had a silly error ::: " + questionIDs.get(i));
					}
					finally
					{
						is.close();
						scanny.close();
					}

					Log.d("getQ", "made it to end of loop");
					c.cd("..");
				}
				
				c.cd("../Users");
				c.cd(appUser);
				c.cd("Likes");
				
				is = c.get("likedQ.txt");
				scanny = new Scanner(is);
				ArrayList<Integer> likedQs = new ArrayList<Integer>();

				while(scanny.hasNext())
				{
					likedQs.add(Integer.parseInt(scanny.nextLine()));
				}
				scanny.close();
				is.close();
				
				for(Question q : retval)
				{
					// check if this q's ID is in likedQs
					for(int i : likedQs)
					{
						if(i == q.getQuestionID())
						{
							q.setHasUserLiked(true);
							break;
						}
					}
				}
			}
			catch(Exception ex)
			{
				Log.d("getQuestions", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("getQuestions", ex.getMessage());
		}

		Log.d("end of getquestions", "size of input ::: " + questionIDs.size());
		Log.d("end of getquestions", "size of output ::: " + retval.size());

		for(Question q : retval)
		{
			Log.d("end of getquestions", "question id ::: " + q.getQuestionID());
			Log.d("end of getquestions", q.getQuestion());
			Log.d("end of getquestions", "answerer ::: " + q.getAnswerers());
			Log.d("end of getquestions", "likes ::: " + q.getUps());
			Log.d("end of getquestions", "does user like ::: " + q.getHasUserLiked());
		}

		Log.d("end of getquestions", "return time");

		return retval;
	}

	public static ArrayList<Question> getTopQuestions(int number, String appUser)
	{
		Log.d("gettopq", "top of top q");
		ArrayList<MiniQuestion> retval = new ArrayList<MiniQuestion>();
		ArrayList<Integer> tmpRetval = new ArrayList<Integer>();
		int minLikes = 0;

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
				Log.d("gettopq", "we has connected");
				c.cd("WhatWould");

				// obtain vector from ls simply to count the number of directories
				Vector v = c.ls("Questions");

				c.cd("Questions");
				// get question id

				// one for ., one for .., so subtract two since we index from 0
				// i is the number of questions total
				int totalQuestions = v.size() - 2;

				Log.d("gettopq", "number of questions total ::: " + totalQuestions);

				if(number > totalQuestions)				
				{
					Log.d("gettopq", "we ain't got many questions");
					// return all questions
					for(int i = 0; i < totalQuestions; i++)
					{
						tmpRetval.add(i);
					}
				}
				else
				{
					Log.d("gettopq", "grabbing first number of questions");
					// grab first number questions
					int thisQuestionsLikes = 0;

					InputStream is = null;
					Scanner scanny = null;
					int trash; String garbage;

					Log.d("gettopq", "number ::: " + number);

					for(int i = 0; i < number; i++)
					{
						

						try
						{
							c.cd(utilities.IdConverter.intToStringId(i));
							Log.d("gettopq", "in first dir");
							
							
							is = c.get("question.txt");
							scanny = new Scanner(is);
							garbage = scanny.nextLine();
							trash = scanny.nextInt();
							thisQuestionsLikes = scanny.nextInt();

							Log.d("gettopq", "read in a q " + i);

							retval.add(new MiniQuestion(i, thisQuestionsLikes));
						}
						catch(Exception ex)
						{
							Log.d("gettopq", "exception while reading in a file... " + i);
						}
						finally
						{
							scanny.close();
							is.close();
							c.cd("..");
						}
						
					}

					for(MiniQuestion m : retval)
					{
						Log.d("gettopq", "MINIQUESTIONS PT A " + m.id + " " + m.likes);
					}

					Log.d("gettopq", "after adding original stuffs");

					// set minLikes for the first time
					minLikes = 9001;
					for(MiniQuestion m : retval)
					{
						if(m.likes < minLikes) minLikes = m.likes;
					}

					// iterate through rest of questions, adding to retval if likes is > minLikes
					// while removing old questionID with minLikes and adding
					for(int i = number; i < totalQuestions; i++)
					{
						c.cd(utilities.IdConverter.intToStringId(i));
						Log.d("gettopq", "in second dir");

						is = c.get("question.txt");
						scanny = new Scanner(is);
						garbage = scanny.nextLine();
						garbage = scanny.nextLine();
						//trash = scanny.nextInt();
						thisQuestionsLikes = scanny.nextInt();
						//
						scanny.close();
						is.close();

						Log.d("gettopq", "read in a q " + i);

						if(thisQuestionsLikes > minLikes)
						{
							Log.d("gettopq", "inside greater than stuff " + i);
							Log.d("gettopq", "size of retval at start ::: " + retval.size());
							Log.d("gettopq", "minlikes at start ::: " + minLikes);
							// remove a question with minLikes number of likes
							for(MiniQuestion m : retval)
							{
								if(m.likes == minLikes)
								{
									retval.remove(m);
									break;
								}
							}

							// add our new question
							retval.add(new MiniQuestion(i, thisQuestionsLikes));

							// recompute minLikes
							minLikes = 9001;
							for(MiniQuestion m : retval)
							{
								if(m.likes < minLikes) minLikes = m.likes;
							}
							Log.d("gettopq", "size of retval at end ::: " + retval.size());
							Log.d("gettopq", "minlikes at end ::: " + minLikes);
						}

						c.cd("..");
					}

					Log.d("gettopq", "about to add questions to tmpretval");

					for(MiniQuestion m : retval)
					{
						Log.d("gettopq", "MINIQUESTIONS PT B " + m.id + " " + m.likes);
					}

					// get actual questions
					for(MiniQuestion m : retval)
					{
						tmpRetval.add(m.id);
					}
				}
			}
			catch(Exception ex)
			{
				Log.d("getTopQuestions", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("getTopQuestions", ex.getMessage());
		}

		return getQuestions(tmpRetval, appUser);
	}

	// untested
	public static void likeQuestion(int questionId, Context context, String username)
	{
		String questionIdString = IdConverter.intToStringId(questionId);
		boolean alreadyLiked = false;
		// check if user has already liked this question
		JSch jsch = new JSch();
		String user="asapp";
		String host="artsci.drake.edu";
		String passy="9Gj24!L6c848FG$";
		int port=22;
		
		Log.d("likeQ", "questionID string ::: " + questionIdString);
		Log.d("likeQ", "questionID ::: " + questionId);

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
				c.cd("Users");
				c.cd(username);
				c.cd("Likes");
				
				Log.d("likeQ", "cd to likes of ::: " + username);

				InputStream is = c.get("likedQ.txt");
				Scanner scanny = new Scanner(is);

				String s = "";
				while(scanny.hasNext())
				{
					s = scanny.nextLine();
					if(s.compareTo(questionIdString) == 0)
					{
						// we have already liked this question!!!
						alreadyLiked = true;
						break;
					}
				}

				scanny.close();
				is.close();

				if(alreadyLiked)
				{
					// we should never get here...
					Log.d("likeQuestion", "trying to like an already liked question.......");
					return;
				}
				else
				{
					Log.d("likeQ", "top");
					// user doesn't already like the question, so we must add it to their likedQ.txt
					is = c.get("likedQ.txt");
					scanny = new Scanner(is);

					// make a new file that is a copy of likedQ.txt, but add our new questionId
					String filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
					File file = new File(filepath);
					file.createNewFile();

					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));

					s = "";
					while(scanny.hasNext())
					{
						s = scanny.nextLine();
						writer.write(s);
						writer.write(System.getProperty("line.separator"));
					}
					scanny.close();
					is.close();

					// write new questionId
					writer.write(questionIdString);
					writer.write(System.getProperty("line.separator"));

					writer.flush();
					writer.close();

					// put back the new file
					c.rm("likedQ.txt");
					c.put(filepath, "likedQ.txt");
					
					Log.d("likeQ", "put back likedQ.txt");

					// must also increment the number of likes in this question's question.txt
					c.cd("../../../Questions");
					c.cd(questionIdString);

					is = c.get("question.txt");
					scanny = new Scanner(is);

					// make a new file that is a copy of question.txt, but increment likes
					filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
					file = new File(filepath);
					file.createNewFile();

					writer = new OutputStreamWriter(new FileOutputStream(file));

					Log.d("likeQ", "test1");
					s = "";
					
					s = scanny.nextLine(); // gets question text
					Log.d("likeQ", "firstline ::: " + s);
					writer.write(s);
					writer.write(System.getProperty("line.separator"));
					
					s = scanny.nextLine(); // gets person id
					Log.d("likeQ", "secondline ::: " + s);
					writer.write(s);
					writer.write(System.getProperty("line.separator"));
					
					s = scanny.nextLine(); // gets number of likes
					Log.d("likeQ", "numlikes ::: " + s);
					int currentLikes = Integer.parseInt(s);
					currentLikes++;
					writer.write("" + currentLikes);
					writer.write(System.getProperty("line.separator"));
					
					s = scanny.nextLine(); // gets username of question writer
					Log.d("likeQ", "fourthline ::: " + s);
					writer.write(s); 
					
					Log.d("likeQ", "test4");

					scanny.close();
					is.close();

					writer.flush();
					writer.close();

					// put back the new file
					c.rm("question.txt");
					c.put(filepath, "question.txt");
					
					Log.d("likeQ", "put back question.txt");
				}
			}
			catch(Exception ex)
			{
				Log.d("likeQuestion", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("likeQuestion", ex.getMessage());
		}
	}

	// untested
	public static void likeAnswer(int questionId, int answerId, Context context, String username)
	{
		String answerIdString = IdConverter.intToStringId(answerId);
		String questionIdString = IdConverter.intToStringId(questionId);
		String combinedIdString = questionIdString + ":" + answerIdString;
		boolean alreadyLiked = false;
		// check if user has already liked this answer
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
				c.cd("Users");
				c.cd(username);
				c.cd("Likes");

				InputStream is = c.get("likedA.txt");
				Scanner scanny = new Scanner(is);

				String s = "";
				while(scanny.hasNext())
				{
					s = scanny.nextLine();
					if(s.compareTo(combinedIdString) == 0)
					{
						// we have already liked this answer!!!
						alreadyLiked = true;
						break;
					}
				}

				scanny.close();
				is.close();

				if(alreadyLiked)
				{
					// we should never get here...
					Log.d("likeAnswer", "trying to like an already liked answer.......");
					return;
				}
				else
				{
					// user doesn't already like the answer, so we must add it to their likedA.txt
					is = c.get("likedA.txt");
					scanny = new Scanner(is);

					// make a new file that is a copy of likedA.txt, but add our new answerId
					String filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
					File file = new File(filepath);
					file.createNewFile();

					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));

					s = "";
					while(scanny.hasNext())
					{
						s = scanny.nextLine();
						writer.write(s);
						writer.write(System.getProperty("line.separator"));
					}
					scanny.close();
					is.close();

					// write new combinedId
					writer.write(combinedIdString);
					writer.write(System.getProperty("line.separator"));

					writer.flush();
					writer.close();

					// put back the new file
					c.rm("likedA.txt");
					c.put(filepath, "likedA.txt");

					// must also increment the number of likes in this answer's answer.txt
					c.cd("../../../Questions");
					c.cd(questionIdString);
					c.cd("Answers");

					is = c.get(answerIdString + ".txt");
					scanny = new Scanner(is);

					// make a new file that is a copy of "answerIdstring".txt, but increment likes
					filepath = context.getFilesDir().getPath().toString() + "/tmp.txt";
					file = new File(filepath);
					file.createNewFile();

					writer = new OutputStreamWriter(new FileOutputStream(file));

					s = "";
					writer.write(scanny.nextLine()); // gets answer text
					writer.write(System.getProperty("line.separator"));
					writer.write(scanny.nextLine()); // gets username of answer writer
					writer.write(System.getProperty("line.separator"));
					int currentLikes = Integer.parseInt(scanny.nextLine()); // gets number of likes
					currentLikes++;
					writer.write("" + currentLikes);
					writer.write(System.getProperty("line.separator"));

					scanny.close();
					is.close();

					writer.flush();
					writer.close();

					// put back the new file
					c.rm(answerIdString + ".txt");
					c.put(filepath, answerIdString + ".txt");
					
					Log.d("likeA", "put back " + answerIdString + ".txt");
				}
			}
			catch(Exception ex)
			{
				Log.d("likeAnswer", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("likeAnswer", ex.getMessage());
		}
	}

	public static ArrayList<Answer> getAnswers(int questionID, String appUser)
	{
		ArrayList<Answer> retval = new ArrayList<Answer>();

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
				c.cd("Questions");
				c.cd(utilities.IdConverter.intToStringId(questionID));

				Vector v = c.ls("Answers");

				// one for ., one for .., so subtract two since we index from 0 
				int answers = v.size() - 2;
				c.cd("Answers");

				InputStream is;
				Scanner scanny;

				String answer = "";
				String poster = "";
				int ups = 0;

				for(int i = 0; i < answers; i++)
				{
					is = c.get(utilities.IdConverter.intToStringId(i) + ".txt");
					scanny = new Scanner(is);

					answer = scanny.nextLine();
					poster = scanny.nextLine();
					ups = scanny.nextInt();

					scanny.close();
					is.close();

					retval.add(new Answer(answer, questionID, i, ups));
				}
				
				// check if user likes these answers
				c.cd("../../../Users");
				c.cd(appUser);
				c.cd("Likes");
				
				is = c.get("likedA.txt");
				scanny = new Scanner(is);
				ArrayList<String> likedAs = new ArrayList<String>();

				while(scanny.hasNext())
				{
					likedAs.add(scanny.nextLine());
				}
				scanny.close();
				is.close();
				
				for(Answer a : retval)
				{
					// check if this q's ID is in likedQs
					for(String s : likedAs)
					{
						String[] tmp = s.split(":");
						if(Integer.parseInt(tmp[0]) == a.getQuestionID() && Integer.parseInt(tmp[1]) == a.getAnswerID())
						{
							a.setHasUserLiked(true);
							break;
						}
					}
				}
				Log.d("getAnswers", "we got past the setting user likes !!!");
			}
			catch(Exception ex)
			{
				Log.d("getAnswers", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.d("getAnswers", ex.getMessage());
		}
		return retval;
	}
	
	//search Results 
	public static ArrayList<Answer> getResults(int number, String appUser)
	{
		return null;
		
	}

	// recent questions
	public static ArrayList<Question> getRecentQuestions(int number, String appUser)
	{
		ArrayList<Integer> tmpRetval = new ArrayList<Integer>();

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
				Vector v = c.ls("Questions");

				// one for ., one for .., so subtract two since we index from 0 
				int numQuestions = v.size() - 2;

				if(number > numQuestions)
				{
					number = numQuestions;
				}

				for(int i = numQuestions - 1; i >= numQuestions - number; i--)
				{
					tmpRetval.add(i);
				}
				/*
				for(int i = numQuestions - number; i < numQuestions; i++)
				{
					tmpRetval.add(i);
				}*/
			}
			catch(Exception ex)
			{
				Log.e("getRecentQuestions", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.e("getRecentQuestions", ex.getMessage());
		}
		return getQuestions(tmpRetval, appUser);
	}

	// my questions
	public static ArrayList<Question> getMyQuestions(int number, String appUser)
	{
		ArrayList<Integer> tmpRetval = new ArrayList<Integer>();
		ArrayList<Integer> revisedTmpRetval = new ArrayList<Integer>();

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
				c.cd("Users");
				c.cd(appUser);

				// read the question ids from questions.txt
				InputStream is = c.get("questions.txt");
				Scanner scanny = new Scanner(is);
				String s = "";
				while(scanny.hasNext())
				{
					s = scanny.nextLine();
					tmpRetval.add(Integer.parseInt(s));
				}
				scanny.close();
				is.close();

				int numQuestions = tmpRetval.size();

				if(numQuestions == 0)
				{
					// this loser hasn't answered any questions!
					return new ArrayList<Question>();
				}

				if(number > numQuestions)
				{
					number = numQuestions;
				}
				/*
				for(int i = numQuestions - number; i < numQuestions; i++)
				{
					revisedTmpRetval.add(tmpRetval.get(i));
				}*/

				for(int i = numQuestions - 1; i >= numQuestions - number; i--)
				{
					revisedTmpRetval.add(tmpRetval.get(i));
				}
			}
			catch(Exception ex)
			{
				Log.e("getMyQuestions", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.e("getMyQuestions", ex.getMessage());
		}
		return getQuestions(revisedTmpRetval, appUser);
	}

	// category questions
	public static ArrayList<Question> getCategoryQuestions(int categoryID, int max, String appUser)
	{
		ArrayList<Integer> tmpRetval = new ArrayList<Integer>();

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
				// get the question ID's of the questions posed for our characterID
				c.cd("WhatWould");
				Vector v = c.ls("Questions");

				c.cd("Questions");

				// one for ., one for .., so subtract two since we index from 0
				int numQuestions = v.size() - 2;

				InputStream is = null;
				Scanner scanny = null;
				String garbage = "";
				int thisAnswerer = -1;

				for(int i = numQuestions -1; i > -1; i--)
				{
					c.cd(utilities.IdConverter.intToStringId(i));

					try
					{
						is = c.get("question.txt");
						scanny = new Scanner(is);
						garbage = scanny.nextLine();
						thisAnswerer = scanny.nextInt();

						is.close();
						scanny.close();

						if(thisAnswerer == categoryID)
						{
							tmpRetval.add(i);
						}
					}
					catch(Exception ex)
					{
						Log.e("getCategoryQuestions", "silly error ::: " + ex.getMessage());
					}
					if(tmpRetval.size() == max) break;
					c.cd("..");
				}
			}
			catch(Exception ex)
			{
				Log.e("getCategoryQuestions", ex.getMessage());
			}
		}
		catch(Exception ex)
		{
			Log.e("getCategoryQuestions", ex.getMessage());
		}
		return tmpRetval.size() == 0 ? new ArrayList<Question>() : getQuestions(tmpRetval, appUser);
	}
}
