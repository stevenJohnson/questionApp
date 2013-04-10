package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.Arrays;

import utilities.ThisApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Settings extends Activity {
	private static final String TAG = "Settings";
	 private ListView mainListView;  
	private ArrayAdapter<String> listAdapter;  

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
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
		 //mainListView.setOnClickListener(onListItemClick);
		 mainListView.setOnItemClickListener( new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg1, View v, int position, long id) 
			 {
		          Toast.makeText(getBaseContext(),"Selected item is "+ position, Toast.LENGTH_SHORT).show();   
		          switch (position)
		          {
		          case 0:
		        	  Intent i = new Intent(Settings.this, EditProfile.class);     
			          Settings.this.startActivity(i);
			          return;
		          case 1:
		        	  Intent i2 = new Intent(Settings.this,ChangeEmail.class);     
			          Settings.this.startActivity(i2);
			          return;
		          case 2:
		        	  Intent i3 = new Intent(Settings.this,ChangeProfile.class);     
			          Settings.this.startActivity(i3);
			          return;
		          case 4:
		        	  new AlertDialog.Builder(Settings.this)
		        	  .setTitle("Log Out")
		        	  .setMessage("Are you sure that you want to log out?")
		        	  .setIcon(android.R.drawable.ic_dialog_alert)
		        	  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		        	      public void onClick(DialogInterface dialog, int whichButton)
		        	      {
		        	    	  Intent goLogin = new Intent(Settings.this, MainActivity.class);
		        	    	  ((ThisApplication)getApplicationContext()).setUsername(null);
		        	    	  goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					          Settings.this.startActivity(goLogin);
					          finish();
		        	      }})
		        	   .setNegativeButton(android.R.string.no, null).show();
		        	  return;
		          }
			 }
		 } );
		 
		 
		 
		 
	}
	 public void onListItemClick(AdapterView<?> arg1, View v, int position, long id) 
	 {
		 Log.d(TAG, "This is working in the list clicks for settings ");
		 
	
	          Toast.makeText(getBaseContext(),"Selected item is "+ position, Toast.LENGTH_SHORT).show();    
//	          Intent i = new Intent(Settings.this, EditProfile.class);     
//	          Settings.this.startActivity(i);
			 
	  
//				Intent intent = new Intent(this, Settings.class);
//				startActivity(intent);

	 }
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	
}
