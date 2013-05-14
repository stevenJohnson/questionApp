package utilities;

import java.util.ArrayList;

import database.dbmethods;

import edu.drake.questionapp.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AListAdapter extends ArrayAdapter<Answer>
{
	private final Context context;
	private final ArrayList<Answer> list;
	int likeFlag = 0;
	int qid;
	int aid;
	Context appContext;

	public AListAdapter(Context context, ArrayList<Answer> list)
	{
		super(context, R.layout.anslayout, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.anslayout, parent, false);
		appContext = this.context;

		TextView textViewA = (TextView) rowView.findViewById(R.id.answer);
		TextView textViewL = (TextView) rowView.findViewById(R.id.likes);
		final ImageButton button;

		final Answer ansGet = list.get(position);
		qid = ansGet.getQuestionID();
		aid = ansGet.getAnswerID();
		textViewA.setText(ansGet.getAnswer());
		//textViewN.setText(""); // Without a more robust Answer object, we can't get user, answerer easily
		textViewL.setText(ansGet.getUps() + " likes");
		
		button = (ImageButton) rowView.findViewById(R.id.likeButton);
		if(ansGet.getHasUserLiked()) button.setImageResource(R.drawable.starchecked);
		
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
			dbmethods.likeAnswer(qid, aid, appContext);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success){
				//do nothing, for now
			}
			else {
				// post an error
				Log.d("LikeTask", "didn't like :(");
			}
		}
	}
} 