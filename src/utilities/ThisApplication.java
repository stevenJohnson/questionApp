package utilities;

import android.app.Application;

public class ThisApplication extends Application
{
	private String username = null;
	
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
