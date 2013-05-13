package utilities;

import java.util.ArrayList;

import database.dbmethods;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.drake.questionapp.R;
import edu.drake.questionapp.QuestionActivity.LikeTask;

public class QListAdapter extends ArrayAdapter<Question>
{
	private static final String TAG = "qAdapter";
	private final Context context;
	//private final String[] names;
	//private final String[] questions;
	private final ArrayList<Question> list;
	int likeFlag = 0;
	int qid;
	Context appContext;

	public QListAdapter(Context context, ArrayList<Question> list)
	{
		super(context, R.layout.rowlayout, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		//appContext = ((ThisApplication) getContext());
		Log.d("ASDF", "getting to getView");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);		

		TextView textViewQ = (TextView) rowView.findViewById(R.id.question);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textViewN = (TextView) rowView.findViewById(R.id.name);
		TextView textViewA = (TextView) rowView.findViewById(R.id.answers);
		TextView textViewL = (TextView) rowView.findViewById(R.id.likes);
		TextView textViewAN = (TextView) rowView.findViewById(R.id.answerername);
		final ImageButton button;
		
		final Question q = list.get(position);
		
		qid = q.getQuestionID();

		textViewQ.setText(q.getQuestion());
		textViewN.setText(q.getUser());
		textViewAN.setText(CategorySorter.getCharacterName(q.getAnswerers().ordinal()));
		textViewA.setText(q.getNumAnswers() + " A");
		
		button = (ImageButton) rowView.findViewById(R.id.likeButton);
		// TODO ::: set the status of the button with this thangggg
		if(q.getHasUserLiked()) button.setImageResource(R.drawable.starchecked);
		
		textViewL.setText(q.getUps() + " Likes");
		imageView.setImageResource(CategorySorter.getDrawable(q.getAnswerers().ordinal()));
		
		button.setOnClickListener(new OnClickListener()
		{
					public void onClick(View v) {
						if (likeFlag == 0)
						{
							button.setImageResource(R.drawable.starchecked);
							likeFlag++;
							LikeTask l = new LikeTask();
							l.execute();
						}
						else 
						{
							button.setImageResource(R.drawable.starunchecked);
							likeFlag--;
						}
						
					}
				});

		return rowView;
	}
	
	public class LikeTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			//some issue with context here.
			dbmethods.likeQuestion(qid, context);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success){
				//do nothing, for now
			}
			else {
				// post an error
				Log.d("post ex", "didn't like :(");
			}
		}
	}
} 