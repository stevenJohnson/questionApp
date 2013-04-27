package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.AListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class QuestionActivity extends Activity {

	private static final String TAG = "QuestionActivity";
	Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		final ListView listview = (ListView) findViewById(R.id.AList);
	    String[] names = new String[] {"Ross", "Steven", "McKenzie", "Megan", "Joe"};
	    String[] answers = new String[] { "answer one", "answer two", 
	    		"answer three",	"answer four", "answer five"};
	    
	    final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	    HashMap<String, String> map;
	    for (int i = 0; i < names.length; ++i) {
	    	 map = new HashMap<String, String>();
	    	    map.put("name", names[i]);
	    	    map.put("answer", answers[i]);
	    	    list.add(map);
	    }
	    AListAdapter adapter = new AListAdapter(this, list);
	    listview.setAdapter(adapter);
		
		button = (Button) findViewById(R.id.submit); 
		button.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				//code put here will be executed when button is pressed
				Log.v(TAG, "button pressed");

				//launch the 2nd screen via an Intent
				Intent intent = new Intent(v.getContext(), AnswerActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

}
