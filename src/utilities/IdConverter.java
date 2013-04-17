package utilities;

public class IdConverter 
{
	public static String intToStringId(int i)
	{
		String s = Integer.toString(i);
		while(s.length() < 6)
		{
			s = "0" + s;
		}
		return s;
	}
}
