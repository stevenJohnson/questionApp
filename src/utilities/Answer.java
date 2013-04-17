package utilities;

public class Answer
{
	// fields
	private int answerID;
	private int questionID;
	private String theAnswer;
	private int ups;
	
	// constructors
	public Answer() // don't use me
	{
		answerID = -1;
	}
	
	public Answer(String a, int q)
	{
		// answer id obtained for insertion
		questionID = q;
		theAnswer = a;
		ups = 0;
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
}
