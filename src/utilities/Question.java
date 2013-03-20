package utilities;

import java.util.Set;

public class Question
{
	// fields
	private int questionID;
	private String theQuestion;
	private int ups;
	private int downs;
	private Set<Answerer> answerers;
	
	// constructors
	public Question() // don't use me
	{
		questionID = -1;
	}
	
	public Question(String q, Set<Answerer> a)
	{
		// questionID is generated when inserting to the database, will be returned (more to add)
		theQuestion = q;
		ups = downs = 0;
		answerers = a;
	}
	
	// methods
	public int getQuestionID()
	{
		return questionID;
	}
	
	public String getQuestion()
	{
		return theQuestion;
	}
	
	public int getUps()
	{
		return ups;
	}
	
	public int getDowns()
	{
		return downs;
	}
	
	public Set<Answerer> getAnswerers()
	{
		return answerers;
	}
}
