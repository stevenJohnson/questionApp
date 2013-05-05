package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.HashMap;

import database.dbmethods;
import edu.drake.questionapp.TopqActivity.GetTopQuestionsTask;

import utilities.QListAdapter;
import utilities.Question;
import utilities.ThisApplication;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MyQFragment extends Fragment
{
	private static final String TAG = "MyQFragment";
	private ArrayList<Question> myQuestions = new ArrayList<Question>();
	View theView;
	boolean isDBcall = false;

	public MyQFragment()
	{
		// Required empty public constructor
	}

	private void makeLoadingInvisible()
	{
		TextView loading = (TextView) theView.findViewById(R.id.Qloading);
		loading.setVisibility(TextView.INVISIBLE);
	}

	private void makeLoadingVisible()
	{
		TextView loading = (TextView) theView.findViewById(R.id.Qloading);
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
		final View view = inflater.inflate(R.layout.fragment_my_q, container, false);
		theView = view;
		final ListView listview = (ListView) view.findViewById(R.id.Qlist);
		
		if(myQuestions.size() == 0 && !isDBcall)
		{
			makeLoadingVisible();
			GetMyQuestionsTask t = new GetMyQuestionsTask();
			t.execute();
		}
		
		if(!isDBcall)
		{
			listview.setAdapter(new QListAdapter(view.getContext(), myQuestions));
		}
		else
		{
			makeLoadingVisible();
		}

		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg1, View arg2, int pos, long arg3)
			{
				Log.d(TAG, "" + pos);

				// start new activity passing the position of the clicked picture to know what to query
				Intent intent = new Intent(view.getContext(), QuestionActivity.class);
				intent.putExtra("questionID", myQuestions.get(pos).getQuestionID());
				Log.d(TAG, "questionID passed to QuestionActivity ::: " + myQuestions.get(pos).getQuestionID());
				
				for(Question q: myQuestions)
				{
					Log.d(TAG, "questionID ::: " + q.getQuestionID());
					Log.d(TAG, "question ::: " + q.getQuestion());
					Log.d(TAG, "questionID ::: " + q.getNumAnswers());
				}
				
				startActivity(intent);
			}
		});
		return view;
	}
	
	public class GetMyQuestionsTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{			
			myQuestions = dbmethods.getMyQuestions(12, ((ThisApplication) getActivity().getApplication()).getUsername());
			return myQuestions.size() <= 12 && myQuestions.size() > 0;
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			if (success)
			{
				// remove the loading message
				makeLoadingInvisible();

				ListView listview = (ListView) theView.findViewById(R.id.Qlist);
				Log.d("post ex", "about to refresh");
				listview.setAdapter(new QListAdapter(theView.getContext(), myQuestions));
				Log.d("post ex", "refreshed");
			}
			else
			{
				// post an error
				Log.d("post ex", "ERRORRRRRRRRRRRRRRRRRRR");
			}
			isDBcall = false;
		}
	}
}
