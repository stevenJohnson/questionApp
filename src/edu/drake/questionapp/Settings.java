package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends Activity {
	 private ListView mainListView;  
	private ArrayAdapter<String> listAdapter;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		//Get the ListView resource
		 mainListView = (ListView) findViewById(R.id.mainListView);
		 //Create and populate a list
		 String[] list = new String[]{"Edit Profile", "Change Email", "Reset Password","Invite People Via Email", "Log Out"};
		 ArrayList<String> myList = new ArrayList<String>();
		 myList.addAll(Arrays.asList(list));
		 
		 //Create Array Adapter using myList
		 listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow, myList);
		 
		 //Set the Array adapter as the List views adapter
		 mainListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
}
