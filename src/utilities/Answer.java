package utilities;

public class Answer
{
	// fields
	private int answerID;
	private int questionID;
	private String theAnswer;
	private int ups;
	private boolean hasUserLiked;
	
	// constructors
	public Answer() // don't use me
	{
		answerID = -1;
	}
	
	public Answer(String ans, int q)
	{
		// answer id obtained for insertion
		questionID = q;
		theAnswer = ans;
		ups = 0;
		hasUserLiked = false;
	}
	
	public Answer(String ans, int q, int a, int upss)
	{
		answerID = a;
		questionID = q;
		theAnswer = ans;
		ups = upss;
		hasUserLiked = false;
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
	
	public boolean getHasUserLiked()
	{
		return hasUserLiked;
	}
	
	public void setHasUserLiked(boolean b)
	{
		hasUserLiked = b;
	}
}
