package fragments;

import utilities.PhotoImageAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import edu.drake.questionapp.R;

public class PhotoFragment extends Fragment
{
	/*@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

		GridView gridView = (GridView) this.getActivity().findViewById(R.id.gridview);
		gridView.setAdapter(new PhotoImageAdapter(this.getActivity()));

	    gridView.setOnItemClickListener(new OnItemClickListener()
	    {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	        {
	            Toast.makeText(new MainActivity(), "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	*/
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		//GridView gridView = (GridView) findViewById(R.id.gridview);
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