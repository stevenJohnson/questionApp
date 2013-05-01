package edu.drake.questionapp;

import java.util.ArrayList;

import database.dbmethods;

import utilities.QListAdapter;
import utilities.Question;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TopqActivity extends Fragment
{
	private static final String TAG = "TopqActivity";
	private ArrayList<Question> myQuestions = new ArrayList<Question>();
	View theView;

	public TopqActivity()
	{
		// Required empty public constructor
	}

	private void makeLoadingInvisible()
	{
		TextView loading = (TextView) theView.findViewById(R.id.topQloading);
		loading.setVisibility(TextView.INVISIBLE);
	}

	private void makeLoadingVisible()
	{
		TextView loading = (TextView) theView.findViewById(R.id.topQloading);
		loading.setVisibility(TextView.VISIBLE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_topq, container, false);
		theView = view;
		ListView listview = (ListView) view.findViewById(R.id.topQlist);

		if(myQuestions.size() == 0)
		{
			makeLoadingVisible();
			GetTopQuestionsTask t = new GetTopQuestionsTask();
			t.execute();
		}

		listview.setAdapter(new QListAdapter(view.getContext(), myQuestions));

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

	public class GetTopQuestionsTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{			
			myQuestions = dbmethods.getTopQuestions(16);
			return myQuestions.size() <= 16 && myQuestions.size() > 0;
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			if (success)
			{
				// remove the loading message 
				makeLoadingInvisible();

				ListView listview = (ListView) theView.findViewById(R.id.topQlist);
				Log.d("post ex", "about to refresh");
				listview.setAdapter(new QListAdapter(theView.getContext(), myQuestions));
				Log.d("post ex", "refreshed");
			}
			else
			{
				// post an error
				Log.d("post ex", "ERRORRRRRRRRRRRRRRRRRRR");
			}
		}
	}

}
