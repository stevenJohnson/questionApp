package utilities;

import java.util.ArrayList;

import edu.drake.questionapp.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QListAdapter extends ArrayAdapter<Question>
{
	private static final String TAG = "qAdapter";
	private final Context context;
	//private final String[] names;
	//private final String[] questions;
	private final ArrayList<Question> list;
	int likeFlag = 0;

	public QListAdapter(Context context, ArrayList<Question> list)
	{
		super(context, R.layout.rowlayout, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d("ASDF", "getting to getView");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);		

		TextView textViewQ = (TextView) rowView.findViewById(R.id.question);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textViewN = (TextView) rowView.findViewById(R.id.name);
		TextView textViewA = (TextView) rowView.findViewById(R.id.answers);
		TextView textViewL = (TextView) rowView.findViewById(R.id.likes);
		final ImageButton button;
		
		Question q = list.get(position);

		textViewQ.setText(q.getQuestion());
		textViewN.setText(CategorySorter.getCharacterName(q.getAnswerers().ordinal()));
		textViewA.setText(q.getNumAnswers() + "A");
		textViewL.setText(q.getUps() + "Likes");
		
		button = (ImageButton) rowView.findViewById(R.id.likeButton);/*
		button.setOnClickListener(new OnClickListener()
		{
					public void onClick(View v) {
						if (likeFlag == 0)
						{
							button.setImageResource(R.drawable.starchecked);
							likeFlag++;
						}
						else 
						{
							button.setImageResource(R.drawable.starunchecked);
							likeFlag--;
						}
						
					}
				});
*/
		return rowView;
	}
} 