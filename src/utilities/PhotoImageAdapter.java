package utilities;

import edu.drake.questionapp.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoImageAdapter extends BaseAdapter
{	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	private Context mContext;

	public  PhotoImageAdapter(Context c)
	{
		mContext = c;
	}

	@Override
	public int getCount()
	{
		return CategorySorter.getLength();
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		//ImageView imageView;
		View grid;
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		int imageDims = displayMetrics.widthPixels * 15 / 48;
		int actualImageDims = imageDims -30;
		
		if (convertView == null)
		{			
			/*imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(imageDims, imageDims));
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setPadding(8, 8, 8, 8);*/
			
			grid = new View(mContext);
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			grid = inflater.inflate(R.layout.categorygrid, parent, false);
		} 
		else
		{
			//imageView = (ImageView) convertView;
			grid = (View)convertView;
		}
		
		//imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), CategorySorter.getDrawable(CategorySorter.getPosition(position)), imageDims, imageDims));
		ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
		
		//imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setPadding(8, 8, 8, 8);
		
		TextView textView = (TextView) grid.findViewById(R.id.textpart);
		imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), CategorySorter.getDrawable(CategorySorter.getPosition(position)), actualImageDims, actualImageDims));
		textView.setText(CategorySorter.getCharacterName(position));
		
		grid.setLayoutParams(new GridView.LayoutParams(imageDims, imageDims + 100));
		grid.setPadding(8, 8, 8, 8);
		return grid;
	}
}