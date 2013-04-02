package edu.drake.questionapp;

import utilities.PhotoImageAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class RecqActivity extends Fragment {

	public RecqActivity() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recq, container, false);
		ListView listView = (ListView) view.findViewById(R.id.listView1);
		listView.setAdapter(new PhotoImageAdapter(view.getContext()));			
		return view;
	}

}
