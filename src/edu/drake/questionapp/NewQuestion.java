package edu.drake.questionapp;

import utilities.PhotoImageAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class NewQuestion extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_question);
		
		ListView listView = (ListView) findViewById(R.id.expandableListView1);
		listView.setAdapter(new PhotoImageAdapter(listView.getContext()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_question, menu);
		return true;
	}

}
