package fragments;

import utilities.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import edu.drake.questionapp.CategoryActivity;
import edu.drake.questionapp.R;

public class PhotoFragment extends Fragment
{
	private static final String TAG = "PhotoFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		GridView gridView = (GridView) getView().findViewById(R.id.gridview);

		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg1, View arg2, int pos, long arg3)
			{
				Log.d(TAG, "" + pos);
				
				// start new activity passing the position of the clicked picture to know what to query
				Intent intent = new Intent(getView().getContext(), CategoryActivity.class);
				intent.putExtra("person", pos);
				startActivity(intent);
			}
		});
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.gridview,container,false);
		GridView gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setAdapter(new PhotoImageAdapter(view.getContext()));			
		return view;			
	}
}