package utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoImageAdapter extends BaseAdapter
{
	private Context mContext;

	public  PhotoImageAdapter(Context c)
	{
		mContext = c;
	}

	public int getCount()
	{
		return CategorySorter.getLength();
	}

	public Object getItem(int position)
	{
		return null;
	}

	public long getItemId(int position)
	{
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} 
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(CategorySorter.getDrawable(CategorySorter.getPosition(position)));
		
		return imageView;
	}
}