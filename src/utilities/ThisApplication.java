package utilities;

import android.app.Application;

public class ThisApplication extends Application
{
	private String username = null;
	private boolean hasUserPostedQ = false;
	
	public boolean getPosted()
	{
		return hasUserPostedQ;
	}
	
	public void setPosted(boolean b)
	{
		hasUserPostedQ = b;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String a)
	{
		username = a;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
}
