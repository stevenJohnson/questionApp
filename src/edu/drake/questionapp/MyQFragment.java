package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.HashMap;

import database.dbmethods;

import utilities.QListAdapter;
import utilities.Question;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MyQFragment.OnFragmentInteractionListener} interface to handle
 * interaction events.
 * 
 */
public class MyQFragment extends Fragment {

	private static final String TAG = "MyQFragment";
	public MyQFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_my_q, container, false);

		final ListView listview = (ListView) view.findViewById(R.id.topQlist);
		
		QListAdapter adapter = new QListAdapter(view.getContext(), new ArrayList<Question>());
		listview.setAdapter(adapter);


		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg1, View arg2, int pos, long arg3)
			{
				Log.d(TAG, "" + pos);

				// start new activity passing the position of the clicked picture to know what to query
				Intent intent = new Intent(view.getContext(), QuestionActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
}
