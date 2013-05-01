package utilities;

import java.util.ArrayList;

import edu.drake.questionapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AListAdapter extends ArrayAdapter<Answer>
{
	private final Context context;
	private final ArrayList<Answer> list;

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

		TextView textViewA = (TextView) rowView.findViewById(R.id.answer);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textViewN = (TextView) rowView.findViewById(R.id.name);
		TextView textViewL = (TextView) rowView.findViewById(R.id.likes);

		Answer ansGet = list.get(position);
		textViewA.setText(ansGet.getAnswer());
		textViewN.setText(""); // TODO ::: set this perhaps with the user's first name ??
		textViewL.setText(ansGet.getUps() + " likes");

		return rowView;
	}
} 