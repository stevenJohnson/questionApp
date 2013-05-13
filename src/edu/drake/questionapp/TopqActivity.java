package edu.drake.questionapp;

import java.util.ArrayList;

import database.dbmethods;

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
	private View theView;
	private boolean isDBcall = false;
	private String appUser = "";

	public TopqActivity()
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
		appUser = ((ThisApplication) getActivity().getApplication()).getUsername();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_topq, container, false);
		theView = view;
		ListView listview = (ListView) view.findViewById(R.id.Qlist);

		if(myQuestions.size() == 0 && !isDBcall)
		{
			isDBcall = true;
			makeLoadingVisible();
			GetTopQuestionsTask t = new GetTopQuestionsTask();
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
				//stuff to pass down the line.
				intent.putExtra("questionID", myQuestions.get(pos).getQuestionID());
				intent.putExtra("question", myQuestions.get(pos).getQuestion());
				intent.putExtra("likes", myQuestions.get(pos).getUps());
				intent.putExtra("person", myQuestions.get(pos).getAnswerers().ordinal());
				intent.putExtra("user", myQuestions.get(pos).getUser());
				//note: put this next line everywhere it needs to be
				intent.putExtra("hasLiked", myQuestions.get(pos).getHasUserLiked());
				Log.d(TAG, "questionID passed to QuestionActivity ::: " + myQuestions.get(pos).getQuestionID());

				for(Question q: myQuestions)
				{
					Log.d(TAG, "questionID ::: " + q.getQuestionID());
					Log.d(TAG, "question ::: " + q.getQuestion());
					Log.d(TAG, "num answers ::: " + q.getNumAnswers());
				}

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
			myQuestions = dbmethods.getTopQuestions(12, appUser);
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
