package utilities;

public class Answer
{
	// fields
	private int answerID;
	private int questionID;
	private String theAnswer;
	private int ups;
	private int downs;
	private Answerer answeredAs;
	
	// constructors
	public Answer() // don't use me
	{
		answerID = -1;
	}
	
	public Answer(String a, Answerer b, int q)
	{
		// answer id obtained for insertion
		questionID = q;
		theAnswer = a;
		ups = downs = 0;
		answeredAs = b;
	}
	
	public int getAnswerID()
	{
		return answerID;
	}
	
	public int getQuestionID()
	{
		return questionID;
	}
	
	public String getAnswer()
	{
		return theAnswer;
	}
	
	public int getUps()
	{
		return ups;
	}
	
	public int getDowns()
	{
		return downs;
	}
	
	public Answerer getAnswerer()
	{
		return answeredAs;
	}
}
