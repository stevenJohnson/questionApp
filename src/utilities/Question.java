package utilities;

public class Question
{
	// fields
	private int questionID;
	private String theQuestion;
	private int ups;
	private Answerer answerer;
	private String user;
	
	// constructors
	public Question() // don't use me
	{
		questionID = -1;
	}
	
	public Question(String q, int a, String use)
	{
		// questionID is generated when inserting to the database, will be returned (more to add)
		theQuestion = q;
		ups = 0;
		answerer = Answerer.values()[a]; // todo::: check for bounds !!!!
		user = use;
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
	
	public Answerer getAnswerers()
	{
		return answerer;
	}
	
	public String getUser()
	{
		return user;
	}
}
