package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.PhotoImageAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TopqActivity extends Fragment {
	private static final String TAG = "TopqActivity";

	public TopqActivity() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_topq, container, false);
		
		final ListView listview = (ListView) findViewById(R.id.listView1);
	    String[] names = new String[] { "Bob", "Dan", "Andy",
	        "Ross", "Steven", "McKenzie", "Megan", "Joe",
	        "Lisa", "Big Mclarge Huge" };
	    String[] questions = new String[] { "question one", "question two", 
	    		"question three",	"question four", "question five", "question six", 
	    		"question seven", "question eight", "question nine", "question ten"};
	    //int[] answers = new int[] {3,4,5,1,12,200,64,44,25,9};
	    //int[] likes = new int[] {44,2,0,0,12,9,105,22,50,1};

	    final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	    HashMap<String, String> map;
	    for (int i = 0; i < names.length; ++i) {
	    	 map = new HashMap<String, String>();
	    	    map.put("name", names[i]);
	    	    map.put("question", questions[i]);
	    	    list.add(map);
	    }
	    NewAdapter adapter = new QListAdapter(this, list);
	    listview.setAdapter(adapter);
	  }
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg1, View arg2, int pos, long arg3)
			{
				Log.d(TAG, "" + pos);
				
				// start new activity passing the position of the clicked picture to know what to query
				Intent intent = new Intent(getView().getContext(), QuestionActivity.class);
				startActivity(intent);
			}
		});
		return view;
		
	}

}
